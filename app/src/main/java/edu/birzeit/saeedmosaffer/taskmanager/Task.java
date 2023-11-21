package edu.birzeit.saeedmosaffer.taskmanager;

import com.google.gson.Gson;
import java.io.Serializable;

public class Task implements Serializable {
    private String title;
    private String details;
    private String date;
    private String time;
    private String status;
    private boolean isCompleted;

    public static final String STATUS_COMPLETED = "Completed";
    public static final String STATUS_ON_HOLD = "On Hold";

    public Task(){}

    public Task(String title){
        this.title = title;
    }
    public Task(String title, String details, String date, String time, String status){
        this.title = title;
        this.details = details;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

     public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
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

     public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // toJson method to convert Task object to JSON string
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    // fromJson method to convert JSON string to Task object
    public static Task fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Task.class);
    }

    @Override
    public String toString() {
        return title;
    }

    private boolean isValidTask(String title) {
        return !title.isEmpty();
    }

}
