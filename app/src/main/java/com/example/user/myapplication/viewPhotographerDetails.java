package com.example.user.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

public class viewPhotographerDetails extends AppCompatActivity {

    String photographername, photographerloc, photographerservice = "Not Available";
    int price = 0;
    TextView tv_photographername, tv_photographerloc, tv_photographerprice,tv_photographerservice ;

    private Boolean date_available = true ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photographer_details);
        tv_photographername = findViewById(R.id.tv_photographername);
        tv_photographerloc = findViewById(R.id.tv_photographerloc);

        tv_photographerprice = findViewById(R.id.tv_photographerprice);

        tv_photographerservice = findViewById(R.id.tv_photographerservices);

        tv_photographername.setText(getIntent().getStringExtra("photographername"));
        tv_photographerloc.setText(getIntent().getStringExtra("photographerloc"));
        tv_photographerservice.setText(getIntent().getStringExtra("photographerservice"));
        tv_photographerprice.setText(getIntent().getStringExtra("photographerprice"));

        check_date_available();
    }




    private void check_date_available()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        database.getReference().child("booked_events").child("photographer_bookings").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {

                    for(DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren())
                    {
                        SingleServiceBooking data = dataSnapshot2.getValue(SingleServiceBooking.class);

                        if(data.vendor_key.equals(getIntent().getStringExtra("photographername")+getIntent().getStringExtra("photographerloc"))
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


    public void book_photographer(View view) {
        if(!date_available)
        {
            Toast.makeText(viewPhotographerDetails.this , "date not available" , Toast.LENGTH_SHORT).show();

            return;
        }

        book_service();
    }


    private void book_service()
    {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        String email = auth.getCurrentUser().getEmail().replace("." , "");

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        SingleServiceBooking data = new SingleServiceBooking(getIntent().getStringExtra("title") , getIntent().getStringExtra("date") ,
                "photographer" , getIntent().getStringExtra("photographername")+getIntent().getStringExtra("photographerloc") );

        database.getReference().child("booked_events").child("photographer_bookings").child(email).child(String.valueOf(System.currentTimeMillis())).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(viewPhotographerDetails.this , "Booking done" , Toast.LENGTH_SHORT).show();

                finish();
            }
        });


    }
}
