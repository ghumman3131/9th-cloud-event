package com.example.user.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.myapplication.data_model.RatingModel;
import com.example.user.myapplication.data_model.caterer_detail;
import com.example.user.myapplication.data_model.decorator_detail;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class viewbookedDecorDetails extends AppCompatActivity {


    TextView tv_decoratername, tv_decoraterloc, tv_decoraterprice,tv_decoraterservice ;

    RatingBar rating ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booked_view_decor_details);
        tv_decoratername = findViewById(R.id.tv_decoratername);
        tv_decoraterloc = findViewById(R.id.tv_decoraterloc);

        tv_decoraterprice = findViewById(R.id.tv_decoraterprice);

        tv_decoraterservice = findViewById(R.id.tv_decoraterservices);

        get_service_details();

    }







    public void get_service_details()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        database.getReference().child("decorator").child(getIntent().getStringExtra("vendor_key")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                decorator_detail details = dataSnapshot.getValue(decorator_detail.class);

                tv_decoratername.setText(details.decoratorname);
                tv_decoraterservice.setText(String.valueOf(details.decoratorservice));
                tv_decoraterprice.setText(String.valueOf(details.decoratorprice));

                tv_decoraterloc.setText(String.valueOf(details.decoratorloc));


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
                    Toast.makeText(viewbookedDecorDetails.this, "rating applied", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    }
