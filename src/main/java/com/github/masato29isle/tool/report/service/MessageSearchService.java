package com.github.masato29isle.tool.report.service;

import com.github.masato29isle.tool.report.slack.bean.Message;

import java.io.IOException;
import java.util.List;

/**
 * Slack投稿メッセージ取得インターフェース
 */
public interface MessageSearchService {

    /**
     * 対象となる投稿メッセージを取得する
     *
     * @param token SlackAPIトークン
     * @param targetChannel 対象チャンネル
     * @param searchKeyword 検索キーワード
     * @return 取得メッセージ一覧
     * @throws IOException 取得メッセージの解析に失敗
     */
    List<Message> execute(String token, String targetChannel, String searchKeyword) throws IOException;
}
