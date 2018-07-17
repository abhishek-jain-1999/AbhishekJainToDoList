package com.example.abhishek.mytodolist;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MyArrayAdapter extends ArrayAdapter{
    List<TaskData> items;
    LayoutInflater inflater;
    Context c;

    public MyArrayAdapter(@NonNull Context context, @NonNull List<TaskData> items) {
        super(context,0,  items);
        inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.items=items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        final int fakePosition=position;
        View output=convertView;
        Calendar cal=Calendar.getInstance();
        if (output==null){
            output=inflater.inflate(R.layout.task_row,parent,false);
            TextView name=output.findViewById(R.id.t1);
            TextView taskContain=output.findViewById(R.id.t2);
            final TextView dateset=output.findViewById(R.id.datetext);
            final TextView timeset=output.findViewById(R.id.timetext);

            dateset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar calendar = Calendar.getInstance();


                    DatePickerDialog datePickerDialog = new DatePickerDialog
                            (getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                            int tempMonth = month + 1;
                            String datasetS = dayOfMonth + "/" + tempMonth + "/" + year;
                            TaskData taskData= items.get(fakePosition);
                            taskData.dateSet=datasetS;

                            dateset.setText(datasetS);

                            MainActivity.updateTask(taskData.name,taskData.taskString,taskData.dateSet,taskData.timeset
                                    ,null,taskData.id,fakePosition);


                        }
                    },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));

                    datePickerDialog.show();
                }
            });
            timeset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Calendar calendar = Calendar.getInstance();
                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    int min = calendar.get(Calendar.MINUTE);

                    TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                            String time = hourOfDay + ":" + minute;
                            TaskData taskData= items.get(fakePosition);
                            taskData.timeset=time;
                            timeset.setText(time);

                            MainActivity.updateTask(taskData.name,taskData.taskString,taskData.dateSet,taskData.timeset
                                    ,null,taskData.id,fakePosition);

                        }
                    },hour,min,false);

                    timePickerDialog.show();
                }
            });


            ToDoClassDataHolder dataHolder=new ToDoClassDataHolder();
            dataHolder.title=name;
            dataHolder.taskData=taskContain;
            dataHolder.dateSet=dateset;
            dataHolder.timeSet=timeset;
            output.setTag(dataHolder);
        }

        ToDoClassDataHolder dataHolder=(ToDoClassDataHolder) output.getTag();

        TaskData taskData=items.get(position);
        dataHolder.title.setText(""+taskData.name);
        dataHolder.taskData.setText(""+taskData.taskString);

        dataHolder.dateSet.setText(""+taskData.dateSet);
        dataHolder.timeSet.setText(""+taskData.timeset );



        return output;
    }
}
