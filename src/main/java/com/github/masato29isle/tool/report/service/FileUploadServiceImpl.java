package com.github.masato29isle.tool.report.service;

import com.github.masato29isle.tool.report.slack.bean.Message;
import com.github.masato29isle.tool.report.util.KeywordUtil;
import com.google.inject.Inject;
import com.github.masato29isle.tool.report.factory.SlackApiRequestFactory;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.commons.io.FileUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 * Slack作成ファイルアップロード実装クラス
 */
public class FileUploadServiceImpl implements FileUploadService {

    @Inject
    private TemplateEngine templateEngine;

    @Inject
    private OkHttpClient client;

    @Override
    public void execute(String token, String targetChannel, List<Message> messageList) throws IOException {
        final Context ctx = new Context(Locale.getDefault());
        ctx.setVariable("reportTerm", KeywordUtil.getTargetTerm(DateTimeFormatter.ofPattern("MM/dd")));
        ctx.setVariable("reportList", messageList);

        String reportText = templateEngine.process("report-template.txt", ctx)
                .replaceAll("\\r\\n|\\r|\\n", "\r\n")
                .replaceAll(StringEscapeUtils.escapeXSI(KeywordUtil.getSearchKeyword()), "");

        File reportFile = new File("/tmp/" + KeywordUtil.getUploadFileName());

        FileUtils.writeStringToFile(reportFile, reportText, "UTF-8");

        String uploadComment = "!!週報フォーマットファイル生成!!\r\n取りまとめ人数：" + messageList.size();

        Request request = SlackApiRequestFactory.getFileUploadRequest(reportFile, token, targetChannel, uploadComment);
        client.newCall(request).execute();
    }
}
