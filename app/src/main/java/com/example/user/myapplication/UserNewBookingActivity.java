package com.example.user.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

public class UserNewBookingActivity extends AppCompatActivity {

    private Spinner services_spinner ;

    private EditText title_et , date_et ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_new_booking);

        services_spinner = findViewById(R.id.services_spinner);

        title_et = findViewById(R.id.title_et);

        date_et = findViewById(R.id.date_et);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.services, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        services_spinner.setAdapter(adapter);

    }

    public void book_service(View view) {

        if(title_et.getText().toString().equals(""))
        {
            Toast.makeText(UserNewBookingActivity.this , "please enter title for event" , Toast.LENGTH_SHORT).show();
            return;
        }

        if(date_et.getText().toString().equals(""))
        {
            Toast.makeText(UserNewBookingActivity.this , "please select date" , Toast.LENGTH_SHORT).show();
            return;
        }

        if(services_spinner.getSelectedItem().toString().equals("venue"))
        {
            Intent i = new Intent(UserNewBookingActivity.this , viewVenues.class);


            i.putExtra("date", date_et.getText().toString());
            i.putExtra("title" , title_et.getText().toString());

            startActivity(i);

            finish();

        }

        if(services_spinner.getSelectedItem().toString().equals("decorater"))
        {
            Intent i = new Intent(UserNewBookingActivity.this , viewDecor.class);


            i.putExtra("date", date_et.getText().toString());
            i.putExtra("title" , title_et.getText().toString());

            startActivity(i);

            finish();

        }

        if(services_spinner.getSelectedItem().toString().equals("caterer"))
        {
            Intent i = new Intent(UserNewBookingActivity.this , viewCaterer.class);


            i.putExtra("date", date_et.getText().toString());
            i.putExtra("title" , title_et.getText().toString());

            startActivity(i);

            finish();

        }


        if(services_spinner.getSelectedItem().toString().equals("Dj"))
        {
            Intent i = new Intent(UserNewBookingActivity.this , viewDj.class);


            i.putExtra("date", date_et.getText().toString());
            i.putExtra("title" , title_et.getText().toString());

            startActivity(i);

            finish();

        }

        if(services_spinner.getSelectedItem().toString().equals("photographer"))
        {
            Intent i = new Intent(UserNewBookingActivity.this , viewPhotographers.class);


            i.putExtra("date", date_et.getText().toString());
            i.putExtra("title" , title_et.getText().toString());

            startActivity(i);

            finish();

        }

        if(services_spinner.getSelectedItem().toString().equals("designer"))
        {
            Intent i = new Intent(UserNewBookingActivity.this , viewDesigners.class);


            i.putExtra("date", date_et.getText().toString());
            i.putExtra("title" , title_et.getText().toString());

            startActivity(i);

            finish();

        }

        if(services_spinner.getSelectedItem().toString().equals("salon"))
        {
            Intent i = new Intent(UserNewBookingActivity.this , viewSalons.class);


            i.putExtra("date", date_et.getText().toString());
            i.putExtra("title" , title_et.getText().toString());

            startActivity(i);

            finish();

        }



    }

    public void select_date(View view) {

        Calendar calendar = Calendar.getInstance();

        int day = calendar.get(Calendar.DAY_OF_WEEK);
        int month  = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);


        DatePickerDialog dp = new DatePickerDialog(UserNewBookingActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                date_et.setText(String.valueOf(dayOfMonth)+"/"+String.valueOf(month+1)+"/"+String.valueOf(year));
            }
        } , year , month , day);

        dp.getDatePicker().setMinDate(System.currentTimeMillis());

        dp.show();
    }
}
