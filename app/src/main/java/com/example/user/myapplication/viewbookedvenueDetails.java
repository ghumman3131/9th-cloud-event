package com.example.user.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.myapplication.data_model.RatingModel;
import com.example.user.myapplication.data_model.venue_details;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class viewbookedvenueDetails extends AppCompatActivity {


    CheckBox cb_service1, cb_Service3, cb_Service2;
    TextView tv_venuename, tv_venue_loc, tv_price, tv_capacity;

    RatingBar rating ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booked_view_venue_details);
        tv_venuename = findViewById(R.id.tv_venuename);
        tv_venue_loc = findViewById(R.id.tv_venue_loc);
        tv_venue_loc = findViewById(R.id.tv_venue_loc);
        tv_price = findViewById(R.id.tv_price);
        tv_capacity = findViewById(R.id.tv_capacity);
        cb_service1 = findViewById(R.id.rb_service1);
        cb_Service2 = findViewById(R.id.rb_service2);
        cb_Service3 = findViewById(R.id.rb_service3);

        rating = findViewById(R.id.rating_bar);


        get_service_details();
    }

    public void get_service_details()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        database.getReference().child("venue").child(getIntent().getStringExtra("vendor_key")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                venue_details details = dataSnapshot.getValue(venue_details.class);

                tv_venuename.setText(details.venuename);
                tv_capacity.setText(String.valueOf(details.capacity));
                tv_price.setText(String.valueOf(details.price));

                if(!details.service1.equals("Not Available"))
                {
                    cb_service1.setChecked(true);
                }

                if(!details.service2.equals("Not Available"))
                {
                    cb_Service2.setChecked(true);
                }

                if(!details.service2.equals("Not Available"))
                {
                    cb_Service2.setChecked(true);
                }

                tv_venue_loc.setText(details.venue_loc);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public void rate(View view)
    {

        FirebaseAuth auth = FirebaseAuth.getInstance();

        String email = auth.getCurrentUser().getEmail().replace(".","");

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        RatingModel model = new RatingModel(rating.getRating());

        database.getReference().child("ratings").child(email).child(getIntent().getStringExtra("vendor_key")).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()) {
                    Toast.makeText(viewbookedvenueDetails.this, "rating applied", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
