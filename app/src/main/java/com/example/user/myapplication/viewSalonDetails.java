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

public class viewSalonDetails extends AppCompatActivity {

    String salonname, salonloc, salonservice = "Not Available";
    int price = 0;
    TextView tv_salonname, tv_salonloc, tv_salonprice,tv_salonservice ;

    private Boolean date_available = true ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_salon_details);
        tv_salonname = findViewById(R.id.tv_salonname);
        tv_salonloc = findViewById(R.id.tv_salonlocation);

        tv_salonprice = findViewById(R.id.tv_salonprice);

        tv_salonservice = findViewById(R.id.tv_salonservices);

        tv_salonname.setText(getIntent().getStringExtra("salonname"));
        tv_salonloc.setText(getIntent().getStringExtra("salonloc"));
        tv_salonservice.setText(getIntent().getStringExtra("salonservice"));
        tv_salonprice.setText(getIntent().getStringExtra("salonprice"));
        
        check_date_available();
    }

   
  
    private void check_date_available()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        database.getReference().child("booked_events").child("salon_bookings").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {

                    for(DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren())
                    {
                        SingleServiceBooking data = dataSnapshot2.getValue(SingleServiceBooking.class);

                        if(data.vendor_key.equals(getIntent().getStringExtra("salonname")+getIntent().getStringExtra("salonloc"))
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


    public void book_salon(View view) {
        if(!date_available)
        {
            Toast.makeText(viewSalonDetails.this , "date not available" , Toast.LENGTH_SHORT).show();

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
                "salon" , getIntent().getStringExtra("salonname")+getIntent().getStringExtra("salonloc") );

        database.getReference().child("booked_events").child("salon_bookings").child(email).child(String.valueOf(System.currentTimeMillis())).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(viewSalonDetails.this , "Booking done" , Toast.LENGTH_SHORT).show();

                finish();
            }
        });


    }
}
