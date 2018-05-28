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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class viewbookedPhotographerDetails extends AppCompatActivity {


    TextView tv_photographername, tv_photographerloc, tv_photographerprice,tv_photographerservice ;

    RatingBar rating ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booked_view_photographer_details);
        tv_photographername = findViewById(R.id.tv_photographername);
        tv_photographerloc = findViewById(R.id.tv_photographerloc);

        tv_photographerprice = findViewById(R.id.tv_photographerprice);

        tv_photographerservice = findViewById(R.id.tv_photographerservices);


        get_service_details();
    }



    public void get_service_details()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        database.getReference().child("photographer").child(getIntent().getStringExtra("vendor_key")).addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {

                photographer_details details = dataSnapshot.getValue(photographer_details.class);

                tv_photographername.setText(details.photographername);
                tv_photographerservice.setText(String.valueOf(details.photographerservice));
                tv_photographerprice.setText(String.valueOf(details.photographerprice));

                tv_photographerloc.setText(String.valueOf(details.photographerloc));


            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

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
                    Toast.makeText(viewbookedPhotographerDetails.this, "rating applied", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


}
