package com.lkd.utils;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateUtil{

    /**
     * 获取指定日期的开始时间，date 00:00:01
     *
     * @param date
     * @return
     */
    public static LocalDateTime getFirstTimeOfDay(String date) {
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
        return LocalDateTime.of(localDate, LocalTime.MIN);
    }

    /**
     * 获取指定日期的结束时间, date 23:59:59
     *
     * @param date
     * @return
     */
    public static LocalDateTime getLastTimeOfDay(String date) {
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
        return LocalDateTime.of(localDate, LocalTime.MAX);
    }


    /**
     * 获取当前时间的季度信息
     * @param dateTime
     * @return
     */
    public static Season getSeason(LocalDateTime dateTime){
        int firstMonth = dateTime.getMonth().firstMonthOfQuarter().getValue();
        int lastMonth = firstMonth + 2;
        LocalDateTime start = LocalDateTime.of(dateTime.getYear(),firstMonth,1,0,0,0);
        Season s = new Season();
        s.setStartDate(start);
        LocalDateTime end = LocalDateTime.of(dateTime.getYear(),lastMonth,1,0,0,0);
        end = end.plusMonths(1).plusDays(-1);
        s.setEndDate(end);

        return s;
    }

    /**
     * 季节
     */
    @Data
    public static class Season{
        private LocalDateTime startDate;
        private LocalDateTime endDate;
    }
}
