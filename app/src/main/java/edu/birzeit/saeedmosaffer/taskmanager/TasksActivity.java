package edu.birzeit.saeedmosaffer.taskmanager;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class TasksActivity extends AppCompatActivity {

    private ArrayList<String> taskList;
    private ArrayAdapter<String> adapter; // Adapter for the ListView that displays the tasks list in the UI (activity_tasks.xml)
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        taskList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, taskList);

        ListView taskListView = findViewById(R.id.tasks_list);

        taskListView.setAdapter(adapter);

        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task selectedTask = getTask(position);
                Intent intent = new Intent(TasksActivity.this, TaskDetails.class);
                intent.putExtra("selectedTask", (Serializable) selectedTask);
                // send the id of the task to TaskDetails activity
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        loadTasks();
    }

    private void loadTasks() {
        SharedPreferences preferences = getSharedPreferences("TaskData", MODE_PRIVATE);
        taskList.clear();

        // Get all keys from SharedPreferences
        Map<String, ?> allEntries = preferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if (entry.getKey().startsWith("task_")) {
                // Retrieve JSON string and convert it to a Task object
                String json = entry.getValue().toString();
                Task task = Task.fromJson(json);
                taskList.add(task.toString());
            }
        }

        adapter.notifyDataSetChanged();
    }

    public void onAddTaskClick(View view) {
        Intent intent = new Intent(this, AddTaskActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Get the task name from the result and add it to the list
            String newTask = data.getStringExtra("newTask");
            addTask(newTask);
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            // If a task was deleted in TaskDetails, refresh the list
            loadTasks();
        }
    }

    private void addTask(String taskName) {
        Task newTask = new Task(taskName);

        // Save the task as a JSON string in SharedPreferences with a unique key
        SharedPreferences preferences = getSharedPreferences("TaskData", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("task_" + System.currentTimeMillis(), newTask.toJson());
        editor.apply();

        loadTasks(); // Reload tasks after adding a new one
    }

    private Task getTask(int position) {
        SharedPreferences preferences = getSharedPreferences("TaskData", MODE_PRIVATE);
        Map<String, ?> allEntries = preferences.getAll();

        int index = 0;
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if (entry.getKey().startsWith("task_")) {
                if (index == position) {
                    // Retrieve JSON string and convert it to a Task object
                    String json = entry.getValue().toString();
                    return Task.fromJson(json);
                }
                index++;
            }
        }

        return null; // Return null if the task is not found
    }

    // Reload tasks after resuming the activity (after returning from TaskDetails) to reflect any changes made to tasks
    @Override
    protected void onResume() {
        super.onResume();
        loadTasks();
    }

    // Go back to the main activity
    public void onBackToMainClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}