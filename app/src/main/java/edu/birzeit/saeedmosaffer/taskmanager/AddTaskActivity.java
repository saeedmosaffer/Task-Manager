package edu.birzeit.saeedmosaffer.taskmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddTaskActivity extends AppCompatActivity {

    private EditText editTextTitle;
    private EditText editTextDetails;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDetails = findViewById(R.id.editTextDetails);
        datePicker = findViewById(R.id.datePicker);
        timePicker = findViewById(R.id.timePicker);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();



        Button btnSaveTask = findViewById(R.id.btnSaveTask);
        btnSaveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTask();
            }
        });
    }

    // Save the task to SharedPreferences
    private void saveTask() {
        String title = editTextTitle.getText().toString();
        String details = editTextDetails.getText().toString();
        String date = getFormattedDate();
        String time = getFormattedTime();
        Switch statusSwitch = findViewById(R.id.statusSwitch);
        boolean isCompleted = statusSwitch.isChecked();

        // If the title is empty, set it to a default value
        if (title.isEmpty()) {
            title = "This is a new task";
        }

        Log.d("Switch State", "isCompleted: " + isCompleted);

        Task task = new Task(title, details, date, time, isCompleted ? Task.STATUS_COMPLETED : Task.STATUS_ON_HOLD);

        // Save the task as a JSON string in SharedPreferences
        SharedPreferences preferences = getSharedPreferences("TaskData", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("task_" + System.currentTimeMillis(), task.toJson());
        editor.apply();

        Intent resultIntent = new Intent();
        resultIntent.putExtra("newTask", task.toString());
        setResult(RESULT_OK, resultIntent);

        finish();
    }

    // Get the date from the DatePicker and format it as yyyy-MM-dd
    private String getFormattedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1; // Month is zero-based
        int year = datePicker.getYear();
        Date date = new Date(year - 1900, month - 1, day); // Date constructor is deprecated, but still works
        return sdf.format(date);
    }

    // Get the time from the TimePicker and format it as HH:mm
    private String getFormattedTime() {
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();
        return String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
    }
}