package com.example.user.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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

public class viewDecorDetails extends AppCompatActivity {

    String decoratername, decoraterloc, decoraterservice = "Not Available";
    int price = 0;
    TextView tv_decoratername, tv_decoraterloc, tv_decoraterprice,tv_decoraterservice ;

    private Boolean date_available = true ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_decor_details);
        tv_decoratername = findViewById(R.id.tv_decoratername);
        tv_decoraterloc = findViewById(R.id.tv_decoraterloc);

        tv_decoraterprice = findViewById(R.id.tv_decoraterprice);

        tv_decoraterservice = findViewById(R.id.tv_decoraterservices);

        tv_decoratername.setText(getIntent().getStringExtra("decoratername"));
        tv_decoraterloc.setText(getIntent().getStringExtra("decoraterloc"));
        tv_decoraterservice.setText(getIntent().getStringExtra("service1"));
        tv_decoraterprice.setText(getIntent().getStringExtra("decoraterprice"));

        check_date_available();
    }

    private void check_date_available()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        database.getReference().child("booked_events").child("decorator_bookings").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {

                    for(DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren())
                    {
                        SingleServiceBooking data = dataSnapshot2.getValue(SingleServiceBooking.class);

                        if(data.vendor_key.equals(getIntent().getStringExtra("decoratername")+getIntent().getStringExtra("decoraterloc"))
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


    public void book_decor(View view) {
        if(!date_available)
        {
            Toast.makeText(viewDecorDetails.this , "date not available" , Toast.LENGTH_SHORT).show();

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
                "decorator" , getIntent().getStringExtra("decoratername")+getIntent().getStringExtra("decoraterloc") );

        database.getReference().child("booked_events").child("decorator_bookings").child(email).child(String.valueOf(System.currentTimeMillis())).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(viewDecorDetails.this , "Booking done" , Toast.LENGTH_SHORT).show();

                finish();
            }
        });


    }
}
