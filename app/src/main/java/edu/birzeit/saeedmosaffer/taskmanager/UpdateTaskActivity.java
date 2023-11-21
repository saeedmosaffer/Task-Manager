package edu.birzeit.saeedmosaffer.taskmanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;

public class UpdateTaskActivity extends AppCompatActivity {

    private EditText editTextTitle;
    private EditText editTextDetails;
    private Switch statusSwitch;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private Button btnUpdateTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_task);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDetails = findViewById(R.id.editTextDetails);
        statusSwitch = findViewById(R.id.statusSwitch);
        datePicker = findViewById(R.id.datePicker);
        timePicker = findViewById(R.id.timePicker);
        btnUpdateTask = findViewById(R.id.btnUpdateTask);

        // Get the selected task from the intent
        Task selectedTask = (Task) getIntent().getSerializableExtra("selectedTask");

        // Display old data in the form
        displayOldData(selectedTask);

        setUpDateTimePickers();

        setUpStatusSwitch(selectedTask.getStatus());

        btnUpdateTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTask(selectedTask);
                Intent intent = new Intent(UpdateTaskActivity.this, TasksActivity.class);
                startActivity(intent);
            }
        });
    }

    private void displayOldData(Task task) {
        if (task != null) {
            editTextTitle.setText(task.getTitle());
            editTextDetails.setText(task.getDetails());
        }
    }

    private void setUpDateTimePickers() {
        datePicker.setMinDate(System.currentTimeMillis() - 1000);

        timePicker.setIs24HourView(true);
    }

    private void setUpStatusSwitch(String status) {
        if (status != null && status.equals(Task.STATUS_COMPLETED)) {
            statusSwitch.setChecked(true);
        } else {
            statusSwitch.setChecked(false);
        }
    }

    private void updateTask(Task task) {
        String newTitle = editTextTitle.getText().toString();
        String newDetails = editTextDetails.getText().toString();
        String newStatus = statusSwitch.isChecked() ? Task.STATUS_COMPLETED : Task.STATUS_ON_HOLD;

        task.setTitle(newTitle);
        task.setDetails(newDetails);
        task.setStatus(newStatus);

        SharedPreferences preferences = getSharedPreferences("TaskData", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        // Loop through all the tasks in SharedPreferences and update the task with the same title as the selected task
        Map<String, ?> allEntries = preferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if (entry.getKey().startsWith("task_")) {
                String json = entry.getValue().toString();
                Task existingTask = Task.fromJson(json);

                // if the task title matches the title of the task to be updated
                if (existingTask.getTitle().equals(task.getTitle())) {
                    // Update the existing task with the new values
                    editor.putString(entry.getKey(), task.toJson());
                    editor.apply();
                    break;
                }
            }
        }
    }
}