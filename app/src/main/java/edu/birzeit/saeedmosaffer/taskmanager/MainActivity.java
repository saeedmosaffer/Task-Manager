package edu.birzeit.saeedmosaffer.taskmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onRemindersClick(View view) {
        Intent intent = new Intent(this, TasksActivity.class);
        startActivity(intent);
    }

    public void onAddTaskClick(View view) {
        Intent intent = new Intent(this, AddTaskActivity.class);
        startActivity(intent);
    }

    public void onViewAllClick(View view) {
        Intent intent = new Intent(this, TasksActivity.class);
        startActivity(intent);
    }

    public void onViewTodayTasksClick(View view) {
        Intent intent = new Intent(this, TodayTasksActivity.class);
        startActivity(intent);
    }

    public void onViewOnHoldTasksClick(View view) {
        Intent intent = new Intent(this, OnHoldTasksActivity.class);
        startActivity(intent);
    }

    public void onViewCompletedTasksClick(View view) {
        Intent intent = new Intent(this, CompletedTasksActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateTaskCounts();
    }

    private void updateTaskCounts() {
        // Calculate counts for each category based on your task data
        int allTasksCount = calculateAllTasksCount();
        int todayTasksCount = calculateTodayTasksCount();
        int scheduledTasksCount = calculateScheduledTasksCount();
        int completedTasksCount = calculateCompletedTasksCount();

        // Update TextViews with the calculated counts
        TextView tvAllTasksCount = findViewById(R.id.tvAllTasksCount);
        TextView tvTodayTasksCount = findViewById(R.id.tvTodayTasksCount);
        TextView tvScheduledTasksCount = findViewById(R.id.tvScheduledTasksCount);
        TextView tvCompletedTasksCount = findViewById(R.id.tvCompletedTasksCount);

        tvAllTasksCount.setText(String.valueOf(allTasksCount));
        tvTodayTasksCount.setText(String.valueOf(todayTasksCount));
        tvScheduledTasksCount.setText(String.valueOf(scheduledTasksCount));
        tvCompletedTasksCount.setText(String.valueOf(completedTasksCount));
    }

    private int calculateAllTasksCount() {
        SharedPreferences preferences = getSharedPreferences("TaskData", MODE_PRIVATE);
        Map<String, ?> allEntries = preferences.getAll();

        int allTasksCount = 0;

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if (entry.getKey().startsWith("task_")) {
                allTasksCount++;
            }
        }

        return allTasksCount;
    }

    private int calculateTodayTasksCount() {
        SharedPreferences preferences = getSharedPreferences("TaskData", MODE_PRIVATE);
        Map<String, ?> allEntries = preferences.getAll();

        int todayTasksCount = 0;
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if (entry.getKey().startsWith("task_")) {
                String json = entry.getValue().toString();
                Task task = Task.fromJson(json);

                // Check if the task date is today
                if (task.getDate().equals(getCurrentDate())) {
                    todayTasksCount++;
                }
            }
        }

        return todayTasksCount;

    }

    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(new Date());
    }

    private int calculateScheduledTasksCount() {
        SharedPreferences preferences = getSharedPreferences("TaskData", MODE_PRIVATE);
        Map<String, ?> allEntries = preferences.getAll();

        int scheduledTasksCount = 0;

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if (entry.getKey().startsWith("task_")) {
                String json = entry.getValue().toString();
                Task task = Task.fromJson(json);

                // Check if the task is scheduled
                if (task.getStatus().equals(Task.STATUS_ON_HOLD)) {
                    scheduledTasksCount++;
                }
            }
        }

        return scheduledTasksCount;
    }

    private int calculateCompletedTasksCount() {
        SharedPreferences preferences = getSharedPreferences("TaskData", MODE_PRIVATE);
        Map<String, ?> allEntries = preferences.getAll();

        int completedTasksCount = 0;

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if (entry.getKey().startsWith("task_")) {
                String json = entry.getValue().toString();
                Task task = Task.fromJson(json);

                // Check if the task is completed
                if (task.getStatus().equals(Task.STATUS_COMPLETED)) {
                    completedTasksCount++;
                }
            }
        }

        return completedTasksCount;
    }

}