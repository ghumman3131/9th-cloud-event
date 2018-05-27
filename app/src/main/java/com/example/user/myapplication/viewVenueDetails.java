package com.example.user.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.myapplication.data_model.SingleServiceBooking;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class viewVenueDetails extends AppCompatActivity {

    String venuename, venue_loc, service1 = "Not Available", service2 = "Not Available", service3 = "Not Available";
    CheckBox cb_service1, cb_Service3, cb_Service2;
    int price = 0, capacity = 0;
    TextView tv_venuename, tv_venue_loc, tv_price, tv_capacity;

    private Boolean date_available = true ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_venue_details);
        tv_venuename = findViewById(R.id.tv_venuename);
        tv_venue_loc = findViewById(R.id.tv_venue_loc);
        tv_venue_loc = findViewById(R.id.tv_venue_loc);
        tv_price = findViewById(R.id.tv_price);
        tv_capacity = findViewById(R.id.tv_capacity);
        cb_service1 = findViewById(R.id.rb_service1);
        cb_Service2 = findViewById(R.id.rb_service2);
        cb_Service3 = findViewById(R.id.rb_service3);
        tv_venuename.setText(getIntent().getStringExtra("venuename"));
        tv_venue_loc.setText(getIntent().getStringExtra("venueloc"));
        tv_price.setText(getIntent().getStringExtra("venueprice"));

        tv_price.setText(getIntent().getStringExtra("venueprice"));

        if(!getIntent().getStringExtra("service1").equals("Not Available"))
        {
            cb_service1.setChecked(true);
        }

        if(!getIntent().getStringExtra("service2").equals("Not Available"))
        {
            cb_Service2.setChecked(true);
        }

        if(!getIntent().getStringExtra("service3").equals("Not Available"))
        {
            cb_Service3.setChecked(true);
        }


        tv_capacity.setText(getIntent().getStringExtra("capacity"));

        check_date_available();

    }

    public void book_venue(View view) {


        if(!date_available)
        {
            Toast.makeText(viewVenueDetails.this , "date not available" , Toast.LENGTH_SHORT).show();

            return;
        }

        book_service();
    }

    private void check_date_available()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        database.getReference().child("booked_events").child("venue_bookings").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {

                    for(DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren())
                    {
                        SingleServiceBooking data = dataSnapshot2.getValue(SingleServiceBooking.class);

                        if(data.vendor_key.equals(getIntent().getStringExtra("venuename")+getIntent().getStringExtra("venueloc"))
                                && data.date.equals(getIntent().getStringExtra("date")))
                        {
                            date_available = false ;
                        }
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void book_service()
    {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        String email = auth.getCurrentUser().getEmail().replace("." , "");

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        SingleServiceBooking data = new SingleServiceBooking(getIntent().getStringExtra("title") , getIntent().getStringExtra("date") ,
                "venue" , getIntent().getStringExtra("venuename")+getIntent().getStringExtra("venueloc") );

        database.getReference().child("booked_events").child("venue_bookings").child(email).child(String.valueOf(System.currentTimeMillis())).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(viewVenueDetails.this , "Booking done" , Toast.LENGTH_SHORT).show();

                finish();
            }
        });


    }
}
