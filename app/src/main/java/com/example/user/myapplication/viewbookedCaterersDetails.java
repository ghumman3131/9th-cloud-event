package com.example.user.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.myapplication.data_model.RatingModel;
import com.example.user.myapplication.data_model.caterer_detail;
import com.example.user.myapplication.data_model.venue_details;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class viewbookedCaterersDetails extends AppCompatActivity {


    TextView tv_caterername, tv_catererloc, tv_catererprice,tv_catererservice ;

    RatingBar rating ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booked_view_caterers_details);
        tv_caterername = findViewById(R.id.tv_caterername);
        tv_catererloc = findViewById(R.id.tv_catererloc);

        tv_catererprice = findViewById(R.id.tv_catererprice);

        tv_catererservice = findViewById(R.id.tv_catererservices);

        rating = findViewById(R.id.rating_bar);

        get_service_details();


    }



    public void get_service_details()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        database.getReference().child("caterer").child(getIntent().getStringExtra("vendor_key")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                caterer_detail details = dataSnapshot.getValue(caterer_detail.class);

                tv_caterername.setText(details.caterername);
                tv_catererservice.setText(String.valueOf(details.catererservice));
                tv_catererprice.setText(String.valueOf(details.catererprice));

                tv_catererloc.setText(String.valueOf(details.catererloc));


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
                    Toast.makeText(viewbookedCaterersDetails.this, "rating applied", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
