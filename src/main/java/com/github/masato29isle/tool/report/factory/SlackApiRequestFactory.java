package com.github.masato29isle.tool.report.factory;

import java.io.File;

import com.github.masato29isle.tool.report.slack.constants.ApiUrl;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * SlackApiリクエスト用オブジェクト生成クラス
 */
public class SlackApiRequestFactory {

    private SlackApiRequestFactory() {
    }

    /**
     * Slackチャンネル履歴データ取得リクエストを取得する
     *
     * @param token SlackAPIトークン
     * @param channelId チャンネルID
     * @param messageCnt 取得メッセージ数
     * @return SlackAPIで使用するRequestオブジェクト
     */
    public static Request getChannelHistoryRequest(String token, String channelId, int messageCnt) {

        HttpUrl.Builder urlBuilder = HttpUrl.parse(ApiUrl.historyUrl).newBuilder()
                .addQueryParameter("token", token)
                .addQueryParameter("channel", channelId)
                .addQueryParameter("count", String.valueOf(messageCnt));

        return new Request.Builder().url(urlBuilder.build()).build();

    }

    /**
     * Slackファイルアップロードリクエストを取得する
     *
     * @param file アップロードファイル(プレインテキストのみ)
     * @param token SlackAPIトークン
     * @param channelId チャンネルID
     * @param uploadComment アップロード時の投稿コメント
     * @return SlackAPIで使用するRequestオブジェクト
     */
    public static Request getFileUploadRequest(File file, String token, String channelId, String uploadComment) {

        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(),
                        RequestBody.create(file, MediaType.parse("text/plain")))
                .addFormDataPart("token", token)
                .addFormDataPart("channels", channelId)
                .addFormDataPart("initial_comment", uploadComment)
                .build();

        return new Request.Builder().url(ApiUrl.uploadUrl).post(requestBody).build();
    }

}
