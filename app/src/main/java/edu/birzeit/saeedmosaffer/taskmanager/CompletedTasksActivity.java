package edu.birzeit.saeedmosaffer.taskmanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Map;

public class CompletedTasksActivity extends AppCompatActivity {

    private ArrayList<String> completedTaskList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_tasks);

        completedTaskList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, completedTaskList);

        ListView completedTaskListView = findViewById(R.id.completed_tasks_list);

        completedTaskListView.setAdapter(adapter);

        completedTaskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task selectedTask = getCompletedTask(position);
                displayTaskDetails(selectedTask);
            }
        });

        // Load completed tasks from SharedPreferences and update the ListView
        loadCompletedTasks();
    }

    private void loadCompletedTasks() {
        SharedPreferences preferences = getSharedPreferences("TaskData", MODE_PRIVATE);
        completedTaskList.clear();

        // Get all keys from SharedPreferences
        Map<String, ?> allEntries = preferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if (entry.getKey().startsWith("task_")) {
                String json = entry.getValue().toString();
                Task task = Task.fromJson(json);

                // Check if the task is completed
                if (task.getStatus().equals(Task.STATUS_COMPLETED)) {
                    completedTaskList.add(task.toString());
                }
            }
        }

        adapter.notifyDataSetChanged();
    }

    private Task getCompletedTask(int position) {
        SharedPreferences preferences = getSharedPreferences("TaskData", MODE_PRIVATE);
        Map<String, ?> allEntries = preferences.getAll();

        int index = 0;
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if (entry.getKey().startsWith("task_")) {
                String json = entry.getValue().toString();
                Task task = Task.fromJson(json);

                // Check if the task is completed
                if (task.getStatus().equals(Task.STATUS_COMPLETED)) {
                    if (index == position) {
                        return task;
                    }
                    index++;
                }
            }
        }

        return null; // Return null if the completed task is not found
    }

    private void displayTaskDetails(Task selectedTask) {
        Intent intent = new Intent(this, TaskDetails.class);
        intent.putExtra("selectedTask", selectedTask);
        startActivity(intent);
    }
}