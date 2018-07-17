package com.example.abhishek.mytodolist;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class OpenAnotherActivty extends AppCompatActivity {


    String dateSet = "0/0/2018", timeset = "00:00";
    long id;
    boolean Stext = false;
    TextView time, t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_another_activty);
        Intent intent = getIntent();


        if (intent.getAction() != null) {
            if (intent.getAction().equals(Intent.ACTION_SEND)) {
                Stext = true;
                EditText data = findViewById(R.id.etext2);
                data.setText("" + intent.getStringExtra(Intent.EXTRA_TEXT));


            }
        } else {

            String type = intent.getStringExtra("type");

            if (type.equals("Edit")) {
                EditText name = findViewById(R.id.etext1), data = findViewById(R.id.etext2);

                t = findViewById(R.id.datetextedit);
                time = findViewById(R.id.timetextedit);

                name.setText(intent.getStringExtra("title"));
                data.setText(intent.getStringExtra("data"));

                dateSet = intent.getStringExtra("date");
                timeset = intent.getStringExtra("time");

                id = intent.getLongExtra("id", -1);
                t.setText("" + dateSet);
                time.setText("" + timeset);
            } else {
                EditText name = findViewById(R.id.etext1), data = findViewById(R.id.etext2);

                t = findViewById(R.id.datetextedit);
                time = findViewById(R.id.timetextedit);
            }


        }
    }

    public void f1(View v) {
        EditText name = findViewById(R.id.etext1), data = findViewById(R.id.etext2);

        String nameS = name.getText().toString();
        String dataS = data.getText().toString();
        timeset = time.getText().toString();
        dateSet = t.getText().toString();

        if (getform(name.getText().toString())) {
            Toast.makeText(this, "Please Fill Title", Toast.LENGTH_LONG).show();
            return;
        }
        if (getform(data.getText().toString())) {
            Toast.makeText(this, "Please Fill Task", Toast.LENGTH_LONG).show();
            return;
        }
        if (getform(time.getText().toString())) {
            Toast.makeText(this, "Please Fill Time", Toast.LENGTH_LONG).show();
            return;
        }
        if (getform(t.getText().toString())) {
            Toast.makeText(this, "Please Fill Date", Toast.LENGTH_LONG).show();
            return;
        }
        if (checkDateTime(0)) {
            Toast.makeText(this, "Invalid Date", Toast.LENGTH_LONG).show();
            return;
        }
        if (checkDateTime(1)) {
            Toast.makeText(this, "Invalid Time", Toast.LENGTH_LONG).show();
            return;

        }

        Intent intent = new Intent();
        intent.putExtra("title", nameS);
        intent.putExtra("text", dataS);
        intent.putExtra("date", dateSet);
        intent.putExtra("time", timeset);
        intent.putExtra("id", id);
        setResult(2, intent);
        finish();


        if (Stext) {
            Stext = false;


            TodoDataBase dataBase = TodoDataBase.getDb(OpenAnotherActivty.this);
            TodoDAO todoDAO = dataBase.getTodoDao();

            TaskData taskData = new TaskData(nameS, dataS);
            taskData.dateSet = dateSet;
            taskData.timeset = timeset;
            todoDAO.insertData(taskData);

            /*ToDoOpenHelper toDoOpenHelper = ToDoOpenHelper.getOpenHelper(this);

            SQLiteDatabase db = toDoOpenHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put("title", nameS);
            contentValues.put("data", dataS);

            contentValues.put("time", timeset);
            contentValues.put("date", dateSet);

            db.insert("todolistable", null, contentValues);
            */
        }


    }

    private boolean checkDateTime(int i) {
        boolean resultboolean = false;
        if (i == 1) {
            int[] result = new int[2];
            String small = "";

            int l = 0;
            for (char c : timeset.toCharArray()) {
                if (c == ':') {

                    try {
                        result[l] = Integer.valueOf(small);
                    } catch (NumberFormatException y) {
                        return true;
                    }
                    l++;
                    small = "";

                } else {
                    small = small + c;
                }

            }
            try {
                result[l] = Integer.valueOf(small);
            } catch (NumberFormatException y) {
                return true;
            }

        }
        if (i == 0) {
            int[] result = new int[3];
            String small = "";
            int l = 0;
            for (int j = 0; j < dateSet.length(); j++) {
                if (dateSet.charAt(j) == '/') {
                    try {
                        result[l] = Integer.valueOf(small);
                    } catch (NumberFormatException y) {
                        return true;
                    }
                    l++;
                    small = "";
                } else {
                    small = small + dateSet.charAt(j);
                }

            }
            try {
                result[l] = Integer.valueOf(small);
            } catch (NumberFormatException y) {
                return true;
            }
        }

        return resultboolean;
    }

    private boolean getform(String string) {

        boolean f = string.length() == 0;
        return string == null || f;
    }

    public void funcDateset(View v) {
        final ImageView textView = (ImageView) v;

        final Calendar cal = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog
                (OpenAnotherActivty.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        t.setText("" + dayOfMonth + "/" + (month + 1) + "/" + year);
                        dateSet = "" + dayOfMonth + "/" + (month + 1) + "/" + year;

                    }
                }, cal.get(Calendar.YEAR), (cal.get(Calendar.MONTH)), cal.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public void funcTimeset(View view) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(OpenAnotherActivty.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                timeset = hourOfDay + ":" + minute;
                time.setText(timeset);

            }
        }, hour, min, false);

        timePickerDialog.show();

    }


    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }
}

