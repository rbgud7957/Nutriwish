package com.example.nutriwish;

public class CalendarTaskItem {
    private String id;
    private String taskName;
    private String date;
    private String time;
    private String memo;

    public CalendarTaskItem() {}

    public CalendarTaskItem(String id, String taskName, String selectedDate, String selectedTime, String taskMemo) {
        this.id = id;
        this.taskName = taskName;
        this.date = selectedDate;
        this.time = selectedTime;
        this.memo = taskMemo;
    }

    // Getter 및 Setter 메서드
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
