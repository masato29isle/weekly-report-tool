package com.github.masato29isle.tool.report;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest;
import com.github.masato29isle.tool.report.service.FileUploadService;
import com.github.masato29isle.tool.report.service.MessageSearchService;
import com.github.masato29isle.tool.report.slack.bean.Message;
import com.github.masato29isle.tool.report.util.KeywordUtil;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.github.masato29isle.tool.report.module.WeeklyToolModule;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Slackに投稿された週次報告を取りまとめてテキストファイルをアップロードする
 */
public class WeeklyToolHandler implements RequestHandler<WeeklyToolHandler.RequestData, WeeklyToolHandler.ResponseData> {

    private final String tokenKey = "slackApiToken";

    private MessageSearchService messageSearchService;

    private FileUploadService fileUploadService;

    @Override
    public ResponseData handleRequest(RequestData requestData, Context context) {
        executeInjection();
        String slackApiToken = getParameter(tokenKey)
                .orElseThrow(() -> new NoSuchElementException("AccessTokenの取得に失敗"));
        try {
            // 週次報告メッセージを取得
            List<Message> messageList = messageSearchService.execute(slackApiToken, requestData.targetChannel,
                    KeywordUtil.getSearchKeyword());
            // 取得したメッセージをテキストファイルにまとめてアップロード
            fileUploadService.execute(slackApiToken, requestData.targetChannel, messageList);
        } catch (Exception e) {
            return new ResponseData(Result.ERROR, e.getMessage());
        }
        return new ResponseData(Result.SUCCESS, "upload weekly-report-file!!");
    }

    /**
     * リクエストデータ
     */
    public static class RequestData {
        public String targetChannel;
    }

    /**
     * レスポンスデータ
     */
    public static class ResponseData {
        public ResponseData(Result result, String detail) {
            this.result = result;
            this.detail = detail;
        }
        public Result result;
        public String detail;
    }

    /**
     * 実行結果
     */
    public enum Result {
        SUCCESS,
        ERROR
    }

    /**
     * Injectionを実行する
     */
    private void executeInjection() {
        Injector injector = Guice.createInjector(new WeeklyToolModule());
        messageSearchService = injector.getInstance(MessageSearchService.class);
        fileUploadService = injector.getInstance(FileUploadService.class);
    }

    /**
     * パラメータストアから取得する
     *
     * @param parameterName パラメータ名
     * @return パラメータ
     */
    private Optional<String> getParameter(String parameterName) {
        GetParameterRequest request = new GetParameterRequest();
        request.setName(parameterName);
        request.setWithDecryption(true);
        return Optional.ofNullable(AWSSimpleSystemsManagementClientBuilder.defaultClient()
                .getParameter(request).getParameter().getValue());
    }
}
