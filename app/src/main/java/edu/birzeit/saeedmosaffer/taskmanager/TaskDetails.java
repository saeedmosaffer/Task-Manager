package edu.birzeit.saeedmosaffer.taskmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.io.Serializable;
import java.util.Map;

public class TaskDetails extends AppCompatActivity {

    private TextView textViewTitle;
    private TextView textViewDetails;
    private TextView textViewDate;
    private TextView textViewTime;
    private TextView textViewStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);
        setContentView(R.layout.activity_task_details);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewDetails = findViewById(R.id.textViewDetails);
        textViewDate = findViewById(R.id.textViewDate);
        textViewTime = findViewById(R.id.textViewTime);
        textViewStatus = findViewById(R.id.textViewStatus);
        Button btnUpdateTask = findViewById(R.id.btnUpdateTask);
        Button btnDeleteTask = findViewById(R.id.btnDeleteTask);

        // Get the selected task from the intent
        Task selectedTask = (Task) getIntent().getSerializableExtra("selectedTask");
        String status = selectedTask.getStatus();

        // Display task details in the TextViews
        if (selectedTask != null) {
            textViewTitle.setText(selectedTask.getTitle());
            textViewDetails.setText(selectedTask.getDetails());
            textViewDate.setText(selectedTask.getDate());
            textViewTime.setText(selectedTask.getTime());
            textViewStatus.setText(status);
        }

        btnUpdateTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTask(selectedTask);
            }
        });

        btnDeleteTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTask(selectedTask);
                finish();
            }
        });
    }

    private void updateTask(Task task) {
        Intent intent = new Intent(TaskDetails.this, UpdateTaskActivity.class);
        intent.putExtra("selectedTask", (Serializable) task);
        startActivity(intent);
    }

    private void deleteTask(Task task) {
        SharedPreferences preferences = getSharedPreferences("TaskData", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        // Loop through all the tasks in SharedPreferences and delete the task with the same title as the selected task
        Map<String, ?> allEntries = preferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if (entry.getKey().startsWith("task_")) {
                String json = entry.getValue().toString();
                Task existingTask = Task.fromJson(json);

                // if the task title matches the title of the task to be deleted
                if (existingTask.getTitle().equals(task.getTitle())) {
                    editor.remove(entry.getKey());
                    editor.apply();
                    break;
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}