package com.github.masato29isle.tool.report.service;

import com.github.masato29isle.tool.report.slack.bean.Message;

import java.io.IOException;
import java.util.List;

/**
 * Slack作成ファイルアップロードインターフェース
 */
public interface FileUploadService {
    /**
     * Slackに週次報告テキストファイルをアップロードする
     *
     * @param token SlackAPIトークン
     * @param targetChannel 対象チャンネル
     * @param messageList 週次報告メッセージリスト
     * @throws IOException ファイル作成に失敗
     */
    void execute(String token, String targetChannel, List<Message> messageList) throws IOException;
}
