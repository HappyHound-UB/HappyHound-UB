package edu.ub.happyhound_app.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import edu.ub.happyhound_app.R;
import edu.ub.happyhound_app.model.DynamicLayout;
import edu.ub.happyhound_app.model.NotificationManager;
import edu.ub.happyhound_app.model.Reminder;
import edu.ub.happyhound_app.model.SearchDatabase;
import edu.ub.happyhound_app.model.ToastMessage;

public class SetReminder extends AppCompatActivity {

    String selectedPet;
    private Button selDate, selTime, save;
    private ImageView returnBack;
    private Spinner spinner;
    private TextInputLayout notes;
    private MaterialTextView showDate, showTime;
    private Calendar calendar;
    private Reminder reminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_reminder);

        spinner = findViewById(R.id.spinner);
        notes = findViewById(R.id.description);
        selDate = findViewById(R.id.selectDate);
        selTime = findViewById(R.id.selectTime);
        showDate = findViewById(R.id.dateText);
        showTime = findViewById(R.id.timeText);
        save = findViewById(R.id.saveButton);
        returnBack = findViewById(R.id.return_back);

        addSpinnerContent(spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) spinner.setBackgroundColor(com.google.type.Color.GREEN_FIELD_NUMBER);
                else
                    spinner.setBackgroundColor(getResources().getColor(R.color.md_theme_light_inverseOnSurface));
                selectedPet = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        calendar = Calendar.getInstance();
        reminder = new Reminder();

        returnBack.setOnClickListener(view -> finish());
        selDate.setOnClickListener(view -> selectDate());
        selTime.setOnClickListener(view -> selectTime());

        NotificationManager manager = new NotificationManager(this);
        save.setOnClickListener(view -> {
            if (!checkDateTime() && spinner.getSelectedItemPosition() != 0) {
                manager.setReminder(reminder, calendar.getTimeInMillis(), selectedPet);

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("targetFragment", 2);
                startActivity(intent);
                finish();

            } else {
                if (spinner.getSelectedItemPosition() == 0)
                    ToastMessage.displayToast(getApplicationContext(),
                            "Elige un perro");
                else ToastMessage.displayToast(getApplicationContext(),
                        "Elige una fecha y tiempo para el recordatorio");
            }
        });
    }

    private void addSpinnerContent(Spinner spinner) {
        SearchDatabase database = new SearchDatabase();
        List<String> allPetsList = database.getAllPets();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, allPetsList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        spinner.setBackground(DynamicLayout.changeShape());
    }

    private boolean checkDateTime() {
        String dateView = showDate.getText().toString();
        String timeView = showTime.getText().toString();

        if (dateView.isEmpty() || timeView.isEmpty())
            return true;
        else {
            reminder.setTitle(spinner.getSelectedItem().toString());
            reminder.setDescription(Objects.requireNonNull(notes.getEditText()).getText().toString());
            reminder.setDate(dateView);
            reminder.setTime(timeView);
            reminder.setTimeInMillis(calendar.getTimeInMillis());

            return false;
        }
    }

    private void selectDate() {
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder
                .datePicker()
                .setTitleText("Elegir Fecha")
                .setSelection(System.currentTimeMillis());

        MaterialDatePicker<Long> datePicker = builder.build();

        datePicker.addOnPositiveButtonClickListener(selectedDate -> {
            Date date = new Date(selectedDate);
            calendar.setTime(date);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            showDate.setText(sdf.format(date));

        });

        datePicker.show(getSupportFragmentManager(), "datePicker");

    }

    private void selectTime() {
        Calendar instance = Calendar.getInstance();
        int hour = instance.get(Calendar.HOUR_OF_DAY);
        int minute = instance.get(Calendar.MINUTE);

        MaterialTimePicker.Builder builder = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setTitleText("Elegir Tiempo")
                .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
                .setHour(hour)
                .setMinute(minute);

        MaterialTimePicker timePicker = builder.build();
        timePicker.addOnPositiveButtonClickListener(v -> {
            int selectedHour = timePicker.getHour();
            int selectedMinute = timePicker.getMinute();

            String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute);
            showTime.setText(formattedTime);

            calendar.set(Calendar.HOUR, selectedHour);
            calendar.set(Calendar.MINUTE, selectedMinute);
        });

        timePicker.show(getSupportFragmentManager(), "timePicker");
    }


}