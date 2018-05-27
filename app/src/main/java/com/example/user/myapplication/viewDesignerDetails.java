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

public class viewDesignerDetails extends AppCompatActivity {

    String designername, designerloc, designerservice = "Not Available";
    int price = 0;
    TextView tv_designername, tv_designerloc, tv_designerprice,tv_designerservice ;

    private Boolean date_available = true ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_designer_details);
        tv_designername = findViewById(R.id.tv_designername);
        tv_designerloc = findViewById(R.id.tv_designerloc);

        tv_designerprice = findViewById(R.id.tv_designerprice);

        tv_designerservice = findViewById(R.id.tv_designerservices);

        tv_designername.setText(getIntent().getStringExtra("designername"));
        tv_designerloc.setText(getIntent().getStringExtra("designerloc"));
        tv_designerservice.setText(getIntent().getStringExtra("designerservice"));
        tv_designerprice.setText(getIntent().getStringExtra("designerprice"));
    }


    public void book_designer(View view) {
        if(!date_available)
        {
            Toast.makeText(viewDesignerDetails.this , "date not available" , Toast.LENGTH_SHORT).show();

            return;
        }

        book_service();
    }

    private void check_date_available()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        database.getReference().child("booked_events").child("designer_bookings").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {

                    for(DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren())
                    {
                        SingleServiceBooking data = dataSnapshot2.getValue(SingleServiceBooking.class);

                        if(data.vendor_key.equals(getIntent().getStringExtra("designername")+getIntent().getStringExtra("designerloc"))
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
                "designer" , getIntent().getStringExtra("designername")+getIntent().getStringExtra("designerloc") );

        database.getReference().child("booked_events").child("designer_bookings").child(email).child(String.valueOf(System.currentTimeMillis())).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(viewDesignerDetails.this , "Booking done" , Toast.LENGTH_SHORT).show();

                finish();
            }
        });


    }
}
