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
import com.example.user.myapplication.data_model.designer_detail;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class viewbookedDesignerDetails extends AppCompatActivity {


    TextView tv_designername, tv_designerloc, tv_designerprice,tv_designerservice ;

    RatingBar rating ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booked_view_designer_details);
        tv_designername = findViewById(R.id.tv_designername);
        tv_designerloc = findViewById(R.id.tv_designerloc);

        tv_designerprice = findViewById(R.id.tv_designerprice);

        tv_designerservice = findViewById(R.id.tv_designerservices);

        get_service_details();

    }




    public void get_service_details()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        database.getReference().child("designer").child(getIntent().getStringExtra("vendor_key")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                designer_detail details = dataSnapshot.getValue(designer_detail.class);

                tv_designername.setText(details.designername);
                tv_designerservice.setText(String.valueOf(details.designerservice));
                tv_designerprice.setText(String.valueOf(details.designerprice));

                tv_designerloc.setText(String.valueOf(details.designerloc));


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
                    Toast.makeText(viewbookedDesignerDetails.this, "rating applied", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }



}
