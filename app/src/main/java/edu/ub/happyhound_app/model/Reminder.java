package edu.ub.happyhound_app.model;

public class Reminder {

    private String name;
    private String type;
    private String description;
    private String date;
    private String time;
    private long timeInMillis;

    public Reminder() {
    }

    public Reminder(String name, String type, String description, String date, String time) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.date = date;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public long getTimeInMillis() {
        return timeInMillis;
    }

    public void setTimeInMillis(long timeInMillis) {
        this.timeInMillis = timeInMillis;
    }
}
