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

public class viewDjDetails extends AppCompatActivity {

    String djname, djloc, djservice = "Not Available";
    int price = 0;
    TextView tv_djname, tv_djloc, tv_djprice, tv_djservice;

    private Boolean date_available = true ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_dj_details);
        tv_djname = findViewById(R.id.tv_djname);
        tv_djloc = findViewById(R.id.tv_djloc);

        tv_djprice = findViewById(R.id.tv_djprice);

        tv_djservice = findViewById(R.id.tv_djservices);

        tv_djname.setText(getIntent().getStringExtra("djname"));
        tv_djloc.setText(getIntent().getStringExtra("djloc"));
        tv_djservice.setText(getIntent().getStringExtra("djservice"));
        tv_djprice.setText(getIntent().getStringExtra("djprice"));
        
        check_date_available();
    }

   

  
    private void check_date_available()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        database.getReference().child("booked_events").child("dj_bookings").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {

                    for(DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren())
                    {
                        SingleServiceBooking data = dataSnapshot2.getValue(SingleServiceBooking.class);

                        if(data.vendor_key.equals(getIntent().getStringExtra("djname")+getIntent().getStringExtra("djloc"))
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


    public void book_dj(View view) {
        if(!date_available)
        {
            Toast.makeText(viewDjDetails.this , "date not available" , Toast.LENGTH_SHORT).show();

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
                "dj" , getIntent().getStringExtra("djname")+getIntent().getStringExtra("djloc") );

        database.getReference().child("booked_events").child("dj_bookings").child(email).child(String.valueOf(System.currentTimeMillis())).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(viewDjDetails.this , "Booking done" , Toast.LENGTH_SHORT).show();

                finish();
            }
        });


    }
}