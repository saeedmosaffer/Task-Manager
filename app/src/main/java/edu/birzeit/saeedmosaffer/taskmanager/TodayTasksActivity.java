package edu.birzeit.saeedmosaffer.taskmanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class TodayTasksActivity extends AppCompatActivity {

    private ArrayList<String> todayTaskList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_tasks);

        todayTaskList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, todayTaskList);

        ListView todayTaskListView = findViewById(R.id.today_tasks_list);

        todayTaskListView.setAdapter(adapter);

        todayTaskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task selectedTask = getTodayTask(position);
                displayTaskDetails(selectedTask);
            }
        });

        loadTodayTasks();
    }

    private void loadTodayTasks() {
        SharedPreferences preferences = getSharedPreferences("TaskData", MODE_PRIVATE);
        todayTaskList.clear();

        // Get the current device date in the format yyyy-MM-dd
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());

        // Get all keys from SharedPreferences
        Map<String, ?> allEntries = preferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if (entry.getKey().startsWith("task_")) {
                // Retrieve JSON string and convert it to a Task object
                String json = entry.getValue().toString();
                Task task = Task.fromJson(json);

                // Check if the task date is today
                if (task.getDate().equals(currentDate)) {
                    todayTaskList.add(task.toString());
                }
            }
        }

        adapter.notifyDataSetChanged();
    }

    private Task getTodayTask(int position) {
        SharedPreferences preferences = getSharedPreferences("TaskData", MODE_PRIVATE);
        Map<String, ?> allEntries = preferences.getAll();

        int index = 0;
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if (entry.getKey().startsWith("task_")) {
                // Retrieve JSON string and convert it to a Task object
                String json = entry.getValue().toString();
                Task task = Task.fromJson(json);

                // Check if the task date is today
                if (task.getDate().equals(getCurrentDate())) {
                    if (index == position) {
                        return task;
                    }
                    index++;
                }
            }
        }

        return null; // Return null if the today's task is not found
    }

    private void displayTaskDetails(Task selectedTask) {
        Intent intent = new Intent(this, TaskDetails.class);
        intent.putExtra("selectedTask", selectedTask);
        startActivity(intent);
    }

    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(new Date());
    }
}