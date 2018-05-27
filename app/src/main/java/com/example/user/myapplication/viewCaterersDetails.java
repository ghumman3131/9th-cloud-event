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

public class viewCaterersDetails extends AppCompatActivity {

    String caterername, catererloc, catererservice = "Not Available";
    int price = 0;
    TextView tv_caterername, tv_catererloc, tv_catererprice,tv_catererservice ;

    private Boolean date_available = true ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_caterers_details);
        tv_caterername = findViewById(R.id.tv_caterername);
        tv_catererloc = findViewById(R.id.tv_catererloc);

        tv_catererprice = findViewById(R.id.tv_catererprice);

        tv_catererservice = findViewById(R.id.tv_catererservices);

        tv_caterername.setText(getIntent().getStringExtra("caterername"));
        tv_catererloc.setText(getIntent().getStringExtra("catererloc"));
        tv_catererservice.setText(getIntent().getStringExtra("catererservice"));
        tv_catererprice.setText(getIntent().getStringExtra("catererprice"));

        check_date_available();
    }

    private void check_date_available()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        database.getReference().child("booked_events").child("caterer_bookings").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {

                    for(DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren())
                    {
                        SingleServiceBooking data = dataSnapshot2.getValue(SingleServiceBooking.class);

                        if(data.vendor_key.equals(getIntent().getStringExtra("caterername")+getIntent().getStringExtra("catererloc"))
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

    }


    private void book_service()
    {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        String email = auth.getCurrentUser().getEmail().replace("." , "");

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        SingleServiceBooking data = new SingleServiceBooking(getIntent().getStringExtra("title") , getIntent().getStringExtra("date") ,
                "caterer" , getIntent().getStringExtra("caterername")+getIntent().getStringExtra("catererloc") );

        database.getReference().child("booked_events").child("caterer_bookings").child(email).child(String.valueOf(System.currentTimeMillis())).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(viewCaterersDetails.this , "Booking done" , Toast.LENGTH_SHORT).show();

                finish();
            }
        });


    }

    public void book_caterer(View view) {
        if(!date_available)
        {
            Toast.makeText(viewCaterersDetails.this , "date not available" , Toast.LENGTH_SHORT).show();

            return;
        }

        book_service();

    }
}
