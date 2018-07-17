package com.example.abhishek.mytodolist;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ListView.OnItemLongClickListener, ListView.OnItemClickListener {


    ListView listView;
    static List<TaskData> task;
    static MyArrayAdapter myArrayAdapter;
    static boolean updateWithMessage = false;
    static TodoDAO todoDAO;
    SharedPreferences pref;

    static long ii;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.list1);

        task = new ArrayList<>();


        pref = this.getSharedPreferences("MyPref", 0);
        updateWithMessage = pref.getBoolean("updateWithMessage", false);

        TodoDataBase dataBase = TodoDataBase.getDb(getApplicationContext());
        todoDAO = dataBase.getTodoDao();


        /*ToDoOpenHelper toDoOpenHelper = ToDoOpenHelper.getOpenHelper(this);

        SQLiteDatabase db = toDoOpenHelper.getWritableDatabase();
        Cursor cursor = db.query("todolistable", null, null, null, null, null, null);
        int h = 1;
        while (cursor.moveToNext()) {
            String nameS = cursor.getString(cursor.getColumnIndex("title"));
            String dataS = cursor.getString(cursor.getColumnIndex("data"));
            String dateS = cursor.getString(cursor.getColumnIndex("date"));
            long id = cursor.getLong(cursor.getColumnIndex("id"));


            Intent intent = new Intent(this, MyReceiver.class);
            intent.putExtra("Action", "CustomAction");
            intent.putExtra("name", nameS);
            intent.putExtra("date", dateS);
            intent.putExtra("data", dataS);
            intent.putExtra("id", h);
            intent.putExtra("idRow", id);
            intent.putExtra("position", (h - 1));


            int[] dateArray = extractDate(dateS);
            Calendar cal = Calendar.getInstance();

            cal.set(dateArray[2], dateArray[1] - 1, dateArray[0], 0, 15);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, h, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            h++;


            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);


            TaskData taskData = new TaskData(nameS, dataS);

            taskData.dateSet = dateS;
            taskData.id = id;

            task.add(taskData);
        }
        cursor.close();*/


       /* for (int i = 1; i <= 1; i++) {
            String ns = "task " + i;
            String ts = "taskDdata " + i;
            TaskData taskData = new TaskData(ns, ts);
            Calendar cal = Calendar.getInstance();
            taskData.setDateSet(cal.get(Calendar.DAY_OF_MONTH), (cal.get(Calendar.MONTH) + 1), cal.get(Calendar.YEAR));
            task.add(taskData);

        }*/

        List<TaskData> dataList = todoDAO.getWholeData();
        int h = 1;
        for (TaskData t : dataList) {


            int[] dateArray = extractDate(t.dateSet);
            int[] timArray = extractTime(t.timeset);

            Calendar cal = Calendar.getInstance();

            cal.set(dateArray[2], dateArray[1] - 1, dateArray[0], timArray[0], timArray[1]);


            if (System.currentTimeMillis() <= cal.getTimeInMillis()) {
                Intent intent = new Intent(this, MyReceiver.class);
                intent.putExtra("Action", "CustomAction");
                intent.putExtra("name", t.name);
                intent.putExtra("date", t.dateSet);
                intent.putExtra("time", t.timeset);
                intent.putExtra("data", t.taskString);
                intent.putExtra("id", h);
                intent.putExtra("idRow", t.id);
                intent.putExtra("position", (h - 1));


                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, h, intent, PendingIntent.FLAG_UPDATE_CURRENT);



                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
            }
            h++;


        }


        task.addAll(dataList);


        //ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.task_row,R.id.t1, task);
        myArrayAdapter = new MyArrayAdapter(this, task);
        listView.setDivider(null);
        listView.setOnItemLongClickListener(this);
        listView.setOnItemClickListener(this);

        listView.setAdapter(myArrayAdapter);
    }

    private int[] extractTime(String timeset) {

        int[] result = new int[2];
        if (timeset != null) {
            String small = "";

            int l = 0;
            for (char c : timeset.toCharArray()) {
                if (c == ':') {
                    result[l] = Integer.valueOf(small);
                    l++;
                    small = "";

                } else {
                    small = small + c;
                }

            }
            result[l] = Integer.valueOf(small);
        } else {
            result[1] = 0;
            result[0] = 0;
        }


        return result;


    }

    public void notifyAllNotification() {
        int h = 1;
        List<TaskData> dataList = todoDAO.getWholeData();
        for (TaskData t : dataList) {

            int[] dateArray = extractDate(t.dateSet);
            int[] timArray = extractTime(t.timeset);

            Calendar cal = Calendar.getInstance();
            cal.set(dateArray[2], dateArray[1] - 1, dateArray[0], timArray[0], timArray[1], 0);

            if (System.currentTimeMillis() <= cal.getTimeInMillis()) {
                Intent intent = new Intent(this, MyReceiver.class);
                intent.putExtra("Action", "CustomAction");
                intent.putExtra("name", t.name);
                intent.putExtra("date", t.dateSet);
                intent.putExtra("time", t.timeset);
                intent.putExtra("data", t.taskString);
                intent.putExtra("id", h);
                intent.putExtra("idRow", t.id);
                intent.putExtra("position", (h - 1));


                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, h, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                h++;


                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
            }


        }

    }

    private int[] extractDate(String dateS) {

        int[] result = new int[3];
        if (dateS != null) {
            String small = "";
            int l = 0;
            for (int i = 0; i < dateS.length(); i++) {
                if (dateS.charAt(i) == '/') {
                    result[l] = Integer.valueOf(small);
                    l++;
                    small = "";
                } else {
                    small = small + dateS.charAt(i);
                }

            }
            result[l] = Integer.valueOf(small);
        } else {
            result[0] = 0;
            result[1] = 1;
            result[2] = 2018;
        }


        return result;
    }


    public static void addTask(String nameS, String dataS, String dateS, String timeset, Context c) {

        TaskData t = new TaskData(nameS, dataS);
        t.dateSet = dateS;
        t.timeset = timeset;

        /*ToDoOpenHelper toDoOpenHelper = ToDoOpenHelper.getOpenHelper(c);

        SQLiteDatabase db = toDoOpenHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("title", nameS);
        contentValues.put("data", dataS);
        contentValues.put("date", t.dateSet);


        long id = db.insert("todolistable", null, contentValues);
        */


        Long id = todoDAO.insertData(t);

        if (id > -1L) {
            t.id = id;
            task.add(0, t);
            myArrayAdapter.notifyDataSetChanged();


        }

    }

    public static void updateTask(String nameS, String dataS, String dateS, String timeset, Context c, long id, int position) {

        /*ToDoOpenHelper openHelper = ToDoOpenHelper.getOpenHelper(c);
        SQLiteDatabase db = openHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", nameS);
        contentValues.put("data", dataS);
        contentValues.put("date", dateS);
        db.update("todolistable", contentValues, "id = " + id, null);*/

        TaskData t = task.get(position);
        t.name = nameS;
        t.taskString = dataS;
        t.dateSet = dateS;
        t.timeset = timeset;


        todoDAO.updateData(t);

        task.set(position, t);
        myArrayAdapter.notifyDataSetChanged();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.add);
        menuItem.setIcon(R.drawable.addicon);

        getMenuInflater().inflate(R.menu.main_messagetab, menu);
        MenuItem menuItem1=menu.findItem(R.id.update);
        menuItem1.setChecked(updateWithMessage);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            String nameS = data.getStringExtra("title"), dataS = data.getStringExtra("text");
            TaskData t = new TaskData(nameS, dataS);
            t.dateSet = data.getStringExtra("date");
            t.timeset = data.getStringExtra("time");
            /*ToDoOpenHelper toDoOpenHelper = ToDoOpenHelper.getOpenHelper(this);*/

            /*SQLiteDatabase db = toDoOpenHelper.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put("title", nameS);
            contentValues.put("data", dataS);
            contentValues.put("date", t.dateSet);*/
            /*long id = db.insert("todolistable", null, contentValues);
            if (id > -1L) {
                t.id = id;
                task.add(0, t);
                myArrayAdapter.notifyDataSetChanged();
            }*/
            Long id = todoDAO.insertData(t);
            if (id > -1L) {
                t.id = id;
                task.add(0, t);
                myArrayAdapter.notifyDataSetChanged();
                notifyAllNotification();
            }


        }
        if (requestCode == 2 && resultCode == 1) {
            int i = data.getIntExtra("position", 0);
            String nameS = data.getStringExtra("title"), dataS = data.getStringExtra("data");
            TaskData t = new TaskData(nameS, dataS);
            t.id = data.getLongExtra("id", -1);
            t.dateSet = data.getStringExtra("date");
            t.timeset = data.getStringExtra("time");

           /* ToDoOpenHelper openHelper = ToDoOpenHelper.getOpenHelper(MainActivity.this);
            SQLiteDatabase db = openHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("title", nameS);
            contentValues.put("data", dataS);
            contentValues.put("date", t.dateSet);


            db.update("todolistable", contentValues, "id = " + t.id, null);
*/

            todoDAO.updateData(t);

            task.set(i, t);
            myArrayAdapter.notifyDataSetChanged();
            notifyAllNotification();
        }


        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId() == R.id.add) {
            Intent intent = new Intent(this, OpenAnotherActivty.class);
            intent.putExtra("type", "main");
            startActivityForResult(intent, 1);
        } else {
            if (item.isChecked()) {

                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("updateWithMessage", false);
                editor.commit();
                item.setChecked(false);
                updateWithMessage = false;
            } else {

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {

                    item.setChecked(true);
                    item.expandActionView();
                    updateWithMessage = true;
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean("updateWithMessage", true);
                    editor.commit();
                } else {
                    String[] d = {Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS};
                    ActivityCompat.requestPermissions(this, d, 1);
                }
            }
        }


        // input in dialog box


        /*AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Add");
        LayoutInflater inflater=(LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View give=inflater.inflate(R.layout.add_interface,null);

        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText editText1=give.findViewById(R.id.edit1);
                EditText editText2=give.findViewById(R.id.edit2);
                String name=editText1.getText().toString();
                String data=editText2.getText().toString();
                task.add(0,new TaskData(name,data));
                myArrayAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setView(give);
        AlertDialog dialog=builder.create();
        dialog.show();*/

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, ActivityForDisplay.class);
        TaskData taskData = task.get(i);
        Bundle bundle = new Bundle();
        bundle.putInt("position", i);
        bundle.putString("title", taskData.name);
        bundle.putString("text", taskData.taskString);
        bundle.putString("date", taskData.dateSet);
        bundle.putString("time", taskData.timeset);
        bundle.putLong("id", taskData.id);
        intent.putExtras(bundle);
        startActivityForResult(intent, 2);


    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int i, long id) {
        TaskData s = task.get(i);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Delete");
        builder.setMessage("Do you want this delete Task : " + s.name);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /*ToDoOpenHelper openHelper = ToDoOpenHelper.getOpenHelper(MainActivity.this);
                SQLiteDatabase db = openHelper.getWritableDatabase();
                Long id = task.get(i).id;
                db.delete("todolistable", "id = " + id, null);*/
                todoDAO.delData(task.get(i));
                task.remove(i);
                myArrayAdapter.notifyDataSetChanged();
                notifyAllNotification();
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });


        AlertDialog dialog = builder.create();
        dialog.show();
        return true;
    }


}
