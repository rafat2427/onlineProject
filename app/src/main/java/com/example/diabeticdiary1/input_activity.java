package com.example.diabeticdiary1;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class input_activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    FirebaseDatabase database;
    DatabaseReference myRef;

    EditText d_reading;
    Spinner d_type;
    TextView d_date;
    TextView d_time;
    Button submit;

    ImageView clock,calendar;

    Diabetes patient;

    String typef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_data);

        final String date,time;

        d_reading=findViewById(R.id.g_level);

        d_date=findViewById(R.id.date_p);
        d_time=findViewById(R.id.time_p);

        clock=findViewById(R.id.clock_icon);
        calendar=findViewById(R.id.calendar_icon);

        d_type=findViewById(R.id.spinner);

        submit=findViewById(R.id.submit);

        patient=new Diabetes();

        database = FirebaseDatabase.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference("Data");


        //Setting the current time and date in textView
        Calendar c=Calendar.getInstance();

        date = DateFormat.getDateInstance().format(c.getTime());
        time = DateFormat.getTimeInstance().format(c.getTime());


        d_date.setText(date);
        d_time.setText(time);

        //Spinner values are set in an ArrayAdapter
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.r_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        d_type.setAdapter(adapter);
        d_type.setOnItemSelectedListener(this);


        //Clicking on calendar icon calls class DatePickerFragment to show DatePicker
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datepicker= new DatePickerFragment();
                datepicker.show(getSupportFragmentManager(),"Pick a Date");
            }
        });

        //Clicking on clock icon calls class TimePickerFragment to show TimePicker
        clock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timepicker= new TimePickerFragment();
                timepicker.show(getSupportFragmentManager(),"Pick a Time");
            }
        });


        //Actions on Clicking Submit Button
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String dr;
                dr=d_reading.getText().toString();
                if (dr.isEmpty()){
                    Toast.makeText(input_activity.this, "Enter Glucose Reading", Toast.LENGTH_SHORT).show();
                    return;
                }
                double val;
                val=Double.parseDouble(dr);

                if (val<0 || val>60){
                    Toast.makeText(input_activity.this, "Glucose reading must be within 0-60", Toast.LENGTH_SHORT).show();
                    d_reading.setTextColor(Color.RED);
                    return;
                }
                else{
                    patient.setDate(date);
                    patient.setTime(time);
                    patient.setLevel(dr);
                    patient.setType(typef);

                    String key=myRef.push().getKey();


                    myRef.child(key).setValue(patient);
                }

                //Prompting Alert Dialog to ask for leaving from or staying in the page
                AlertDialog.Builder alert= new AlertDialog.Builder(input_activity.this);

                alert.setTitle("Data Inserted");
                alert.setMessage("Do you want to Enter any more Data?");

                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {


                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        goToMain();
                    }
                });

                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {


                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

                alert.show();
            }
        });
    }

    public void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    //Set the type as the item selected in spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        typef=parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Toast.makeText(input_activity.this, "Select a Type", Toast.LENGTH_SHORT).show();
    }

    //Works when user selects a date in the prompt DatePicker(it passes the values)
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar c=Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,day);
        String dd= DateFormat.getDateInstance().format(c.getTime());

        TextView dada;
        dada=findViewById(R.id.date_p);
        dada.setText(dd);

    }

    //Works when user selects a time in the prompt DatePicker(it passes the values)
    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        Calendar c=Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,hour);
        c.set(Calendar.MINUTE,minute);
        String dd= DateFormat.getTimeInstance().format(c.getTime());

        TextView dada;
        dada=findViewById(R.id.time_p);
        dada.setText(dd);
    }
}
