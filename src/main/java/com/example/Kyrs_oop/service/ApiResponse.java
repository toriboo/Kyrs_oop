package com.example.Kyrs_oop.service;


import java.util.List;
import java.util.Map;

public class ApiResponse {
    private Map<String, GroupData> groups;

    public Map<String, GroupData> getGroups() {
        return groups;
    }

    public void setGroups(Map<String, GroupData> groups) {
        this.groups = groups;
    }

    public static class GroupData {
        private String group;
        private Map<String, Day> days;

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public Map<String, Day> getDays() {
            return days;
        }

        public void setDays(Map<String, Day> days) {
            this.days = days;
        }
    }

    public static class Day {
        private String name;
        private List<Lesson> lessons;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Lesson> getLessons() {
            return lessons;
        }

        public void setLessons(List<Lesson> lessons) {
            this.lessons = lessons;
        }
    }

    public static class Lesson {
        private String teacher;
        private String secondTeacher;
        private String subjectType;
        private String week;
        private String name;
        private String startTime;
        private String endTime;
        private int startTimeSeconds;
        private int endTimeSeconds;
        private String room;
        private String comment;
        private boolean isDistant;
        private List<String> tempChanges;
        private String url;

        public String getTeacher() {
            return teacher;
        }

        public void setTeacher(String teacher) {
            this.teacher = teacher;
        }

    }
}
