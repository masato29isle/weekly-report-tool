package com.github.masato29isle.tool.report.service;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.github.masato29isle.tool.report.factory.SlackApiRequestFactory;
import com.github.masato29isle.tool.report.slack.bean.ChannelHistory;
import com.github.masato29isle.tool.report.slack.bean.Message;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Slack投稿メッセージ取得実装クラス
 */
public class MessageSearchServiceImpl implements MessageSearchService {

    @Inject
    private OkHttpClient client;

    @Inject
    private ObjectMapper objectMapper;

    /**
     * デフォルト取得メッセージ数
     */
    private static final int DEFAULT_MESSAGE_NUM = 20;

    @Override
    public List<Message> execute(String token, String targetChannel ,String searchKeyword) throws IOException {

        Request request = SlackApiRequestFactory.getChannelHistoryRequest(token, targetChannel, DEFAULT_MESSAGE_NUM);
        String responseToJson = Objects.requireNonNull(client.newCall(request).execute().body()).string();

        ChannelHistory channelHistory = objectMapper.readValue(responseToJson, ChannelHistory.class);

        return channelHistory.getMessages().stream()
                .filter(message -> message.getText().contains(searchKeyword))
                .sorted(Comparator.comparing(Message::getUser))
                .peek(message -> {
                    String userId = message.getUser();
                    message.setUser(getParameter(userId).orElse(userId));
                })
                .collect(Collectors.toList());
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
        return Optional.ofNullable(AWSSimpleSystemsManagementClientBuilder.defaultClient()
                .getParameter(request).getParameter().getValue());
    }
}
