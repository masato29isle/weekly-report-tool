package com.github.masato29isle.tool.report.util;

import org.jetbrains.annotations.NotNull;

import java.time.DayOfWeek;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class KeywordUtil {

    private static final DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("MM/dd");
    private static final DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("MMdd");

    private static final ZonedDateTime last2WeekSunday = ZonedDateTime.now().minusWeeks(2).with(DayOfWeek.SUNDAY);
    private static final ZonedDateTime lastWeekSaturday = ZonedDateTime.now().minusWeeks(1).with(DayOfWeek.SATURDAY);

    /**
     * <p>週次報告の対象週文字列を取得する</p>
     * @param formatter  日付フォーマッター
     * @return 対象週文字列
     */
    @NotNull
    public static String getTargetTerm(DateTimeFormatter formatter) {
        return last2WeekSunday.format(formatter) + "〜" + lastWeekSaturday.format(formatter);
    }

    /**
     * <p>Slackから取得するための検索ワードを取得する</p>
     * @return 検索キーワード
     */
    @NotNull
    public static String getSearchKeyword() {
        return "週次報告(" + getTargetTerm(formatter1) + ")";
    }

    /**
     * <p>Slackにアップロードするファイル名を取得する</p>
     * @return アップロードファイル名
     */
    @NotNull
    public static String getUploadFileName() {
        return "【週報】" + getTargetTerm(formatter2) + "(SS部3課).txt";
    }
}
