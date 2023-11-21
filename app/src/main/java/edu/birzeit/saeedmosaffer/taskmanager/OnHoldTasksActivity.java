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

public class OnHoldTasksActivity extends AppCompatActivity {

    private ArrayList<String> onHoldTaskList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_hold_tasks);

        onHoldTaskList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, onHoldTaskList);

        ListView onHoldTaskListView = findViewById(R.id.on_hold_tasks_list);

        onHoldTaskListView.setAdapter(adapter);

        onHoldTaskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task selectedTask = getOnHoldTask(position);
                displayTaskDetails(selectedTask);
            }
        });

        // Load on hold tasks from SharedPreferences and update the ListView
        loadOnHoldTasks();
    }

    private void loadOnHoldTasks() {
        SharedPreferences preferences = getSharedPreferences("TaskData", MODE_PRIVATE);
        onHoldTaskList.clear();

        // Get all keys from SharedPreferences
        Map<String, ?> allEntries = preferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if (entry.getKey().startsWith("task_")) {
                // Retrieve JSON string and convert it to a Task object
                String json = entry.getValue().toString();
                Task task = Task.fromJson(json);

                // Check if the task is on hold
                if (task.getStatus().equals(Task.STATUS_ON_HOLD)) {
                    onHoldTaskList.add(task.toString());
                }
            }
        }

        adapter.notifyDataSetChanged();
    }

    private Task getOnHoldTask(int position) {
        SharedPreferences preferences = getSharedPreferences("TaskData", MODE_PRIVATE);
        Map<String, ?> allEntries = preferences.getAll();

        int index = 0;
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if (entry.getKey().startsWith("task_")) {
                // Retrieve JSON string and convert it to a Task object
                String json = entry.getValue().toString();
                Task task = Task.fromJson(json);

                // Check if the task is on hold
                if (task.getStatus().equals(Task.STATUS_ON_HOLD)) {
                    if (index == position) {
                        return task;
                    }
                    index++;
                }
            }
        }

        return null; // Return null if the on hold task is not found
    }

    private void displayTaskDetails(Task selectedTask) {
        Intent intent = new Intent(this, TaskDetails.class);
        intent.putExtra("selectedTask", selectedTask);
        startActivity(intent);
    }
}
