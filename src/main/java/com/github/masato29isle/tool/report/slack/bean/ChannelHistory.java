package com.github.masato29isle.tool.report.slack.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * Slackチャンネル履歴情報
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class ChannelHistory {

    private boolean ok;

    private List<Message> messages;

}
