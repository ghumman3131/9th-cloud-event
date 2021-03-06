package com.example.user.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.myapplication.data_model.venue_details;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class addVenue extends AppCompatActivity {
    ArrayList<venue_details> venue_list;
    RecyclerView venue_recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_venue);
        venue_list = new ArrayList<>();

        venue_recycler = findViewById(R.id.venue_recycler);

        venue_recycler.setLayoutManager(new LinearLayoutManager(addVenue.this, LinearLayoutManager.VERTICAL, false));
    }

    public void add_venue_details(View view) {

        startActivity(new Intent(addVenue.this, VenueDetails.class));
    }

    public void get_venue() {
        FirebaseAuth firebase = FirebaseAuth.getInstance();

        FirebaseDatabase data = FirebaseDatabase.getInstance();
        System.out.println("rrrr");
        data.getReference().child("venue").addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                venue_list.clear();


                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    venue_details details = data.getValue(venue_details.class);
                    System.out.println("rrrrrr");
                    venue_list.add(details);

                    Adapter adapter = new Adapter();

                    venue_recycler.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        get_venue();
    }

    public class view_holder extends RecyclerView.ViewHolder {

        TextView venue_name, venue_loc;
        LinearLayout venue_lay;

        public view_holder(View itemView) {
            super(itemView);

            venue_name = itemView.findViewById(R.id.name);
            venue_loc = itemView.findViewById(R.id.loc);
            venue_lay = itemView.findViewById(R.id.venue_lay);
        }
    }

    public class Adapter extends RecyclerView.Adapter<view_holder> {

        @Override
        public view_holder onCreateViewHolder(ViewGroup parent, int viewType) {

            view_holder v = new view_holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.venue_cell, parent, false));

            return v;
        }

        @Override
        public void onBindViewHolder(view_holder holder, int position) {

            final venue_details data = venue_list.get(position);
            holder.venue_name.setText(data.venuename);
            holder.venue_loc.setText(data.venue_loc);
            holder.venue_lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String venuename = data.venuename;
                    String venueloc = data.venue_loc;
                    int venueprice = data.price;

                    Intent i = new Intent(addVenue.this, updatevenue.class);
                    i.putExtra("venuename", venuename);
                    i.putExtra("venueloc", venueloc);
                    i.putExtra("venueprice", String.valueOf(venueprice));
                    i.putExtra("service1" , data.service1);
                    i.putExtra("service2" , data.service2);
                    i.putExtra("service3" , data.service3);
                    i.putExtra("capacity" , String.valueOf(data.capacity));

                    startActivity(i);
                }
            });
        }

        @Override
        public int getItemCount() {
            return venue_list.size();
        }
    }
}