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
import com.example.user.myapplication.data_model.salon_details;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class viewbookedSalonDetails extends AppCompatActivity {


    TextView tv_salonname, tv_salonloc, tv_salonprice,tv_salonservice ;

    RatingBar rating ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booked_view_salon_details);
        tv_salonname = findViewById(R.id.tv_salonname);
        tv_salonloc = findViewById(R.id.tv_salonlocation);

        tv_salonprice = findViewById(R.id.tv_salonprice);

        tv_salonservice = findViewById(R.id.tv_salonservices);

        get_service_details();

    }




    public void get_service_details()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        database.getReference().child("salon").child(getIntent().getStringExtra("vendor_key")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                salon_details details = dataSnapshot.getValue(salon_details.class);

                tv_salonname.setText(details.salonname);
                tv_salonservice.setText(String.valueOf(details.salonservice));
                tv_salonprice.setText(String.valueOf(details.salonprice));

                tv_salonloc.setText(String.valueOf(details.salonloc));


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
                    Toast.makeText(viewbookedSalonDetails.this, "rating applied", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


}
