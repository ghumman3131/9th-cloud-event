package com.example.user.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.myapplication.data_model.RatingModel;
import com.example.user.myapplication.data_model.caterer_detail;
import com.example.user.myapplication.data_model.dj_detail;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class viewbookedDjDetails extends AppCompatActivity {


    TextView tv_djname, tv_djloc, tv_djprice, tv_djservice;

    RatingBar rating ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booked_view_dj_details);
        tv_djname = findViewById(R.id.tv_djname);
        tv_djloc = findViewById(R.id.tv_djloc);

        tv_djprice = findViewById(R.id.tv_djprice);

        tv_djservice = findViewById(R.id.tv_djservices);


        get_service_details();
    }





    public void get_service_details()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        database.getReference().child("dj").child(getIntent().getStringExtra("vendor_key")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                dj_detail details = dataSnapshot.getValue(dj_detail.class);

                tv_djname.setText(details.djname);
                tv_djservice.setText(String.valueOf(details.djservice));
                tv_djprice.setText(String.valueOf(details.djprice));

                tv_djloc.setText(String.valueOf(details.djloc));


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
                    Toast.makeText(viewbookedDjDetails.this, "rating applied", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


}