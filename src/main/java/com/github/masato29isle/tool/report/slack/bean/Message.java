package com.github.masato29isle.tool.report.slack.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * Slackメッセージ情報
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class Message {

    private String type;

    private String text;

    private String user;

}
