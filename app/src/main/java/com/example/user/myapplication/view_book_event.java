package com.example.user.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.user.myapplication.data_model.caterer_detail;
import com.example.user.myapplication.data_model.decorator_detail;
import com.example.user.myapplication.data_model.designer_detail;
import com.example.user.myapplication.data_model.dj_detail;
import com.example.user.myapplication.data_model.salon_details;
import com.example.user.myapplication.data_model.venue_details;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class view_book_event extends AppCompatActivity {

    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_book_event);
        date = getIntent().getStringExtra("date");


    }


}