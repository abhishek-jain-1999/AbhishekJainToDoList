package com.example.abhishek.mytodolist;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityForDisplay extends AppCompatActivity {


    String nameS, dataS, dateS, timeset;
    public static final String type = "Edit";
    long id;
    TextView name, data, date, time;
    int position;

    Intent intent;

    boolean editoption = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_for_display);
        intent = getIntent();
        if (intent.getStringExtra("Action") == null) {

            Bundle bundle = intent.getExtras();
            nameS = bundle.getString("title");
            position = bundle.getInt("position");
            dataS = bundle.getString("text");
            dateS = bundle.getString("date");
            timeset = bundle.getString("time");
            id = bundle.getLong("id");

        } else {
            if (intent.getStringExtra("Action").equals("CustomAction")) {
                dataS = intent.getStringExtra("data");
                nameS = intent.getStringExtra("name");
                dateS = intent.getStringExtra("date");
                timeset = intent.getStringExtra("time");
                position = intent.getIntExtra("position", -1);
                id = intent.getIntExtra("idRow", 0);
            }

        }

        name = findViewById(R.id.text1);
        data = findViewById(R.id.text2);
        date = findViewById(R.id.datetextdisplay);
        time = findViewById(R.id.timetextdisplay);


        name.setText("" + nameS);
        data.setText("" + dataS);
        date.setText("" + dateS);
        time.setText("" + timeset);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.add);
        menuItem.setTitle("EDIT");
        menuItem.setIcon(R.drawable.editicon);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        editoption = true;
        Intent intent1 = new Intent(this, OpenAnotherActivty.class);

        intent1.putExtra("title", nameS);
        intent1.putExtra("type", "Edit");
        intent1.putExtra("data", dataS);
        intent1.putExtra("date", dateS);
        intent1.putExtra("time", timeset);
        startActivityForResult(intent1, 1);


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent dataIntent) {


        if (dataIntent != null) {
            nameS = dataIntent.getStringExtra("title");
            dataS = dataIntent.getStringExtra("text");
            dateS = dataIntent.getStringExtra("date");
            timeset = dataIntent.getStringExtra("time");


            name.setText("" + nameS);
            data.setText("" + dataS);
            time.setText("" + timeset);
            date.setText("" + dateS);
        }
        super.onActivityResult(requestCode, resultCode, dataIntent);
    }

    @Override
    public void onBackPressed() {

        if (editoption) {
            if (intent.getStringExtra("Action") != null) {

                if (intent.getStringExtra("Action").equals("CustomAction")) {
                    MainActivity.updateTask(nameS, dataS, dateS, timeset, this,
                            intent.getLongExtra("idRow", 0),
                            intent.getIntExtra("position", -1));

                }

            } else {
                Intent intent = new Intent();
                intent.putExtra("title", nameS);
                intent.putExtra("data", dataS);
                intent.putExtra("date", dateS);
                intent.putExtra("time", timeset);
                intent.putExtra("id", id);
                intent.putExtra("position", position);
                setResult(1, intent);
            }


        } else {
            setResult(3);
        }
        finish();

        super.onBackPressed();


    }
}
