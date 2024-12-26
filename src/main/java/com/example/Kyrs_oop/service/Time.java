package com.example.Kyrs_oop.service;

import org.springframework.stereotype.Component;

import java.time.*;
@Component
public class Time {
    private Clock clock = Clock.system(ZoneId.of("Europe/Moscow"));
    public String getDayOfWeek(){
        ZonedDateTime moscowTime = ZonedDateTime.now(clock);
        return moscowTime.getDayOfWeek().toString();
    }
    public String getTime(){
        ZonedDateTime moscowTime = ZonedDateTime.now(clock);
        return moscowTime.toLocalTime().toString();
    }
    public String getYear(){
        // Получаем текущее московское время
        ZonedDateTime moscowTime = ZonedDateTime.now(clock);
        // Возвращаем текущий год
        return String.valueOf(moscowTime.getYear());
    }
    public String getNextDayOfWeek() {
        // Получаем текущее московское время
        ZonedDateTime moscowTime = ZonedDateTime.now(clock);
        return moscowTime.plusDays(1).getDayOfWeek().toString();
    }

}
