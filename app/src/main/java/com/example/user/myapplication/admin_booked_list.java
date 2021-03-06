package com.example.user.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.myapplication.data_model.SingleServiceBooking;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class admin_booked_list extends AppCompatActivity {
    ArrayList<SingleServiceBooking> book_list;
    RecyclerView book_recycle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked_list);
        book_list = new ArrayList<>();

        book_recycle = findViewById(R.id.book_recycle);

        book_recycle.setLayoutManager(new LinearLayoutManager(admin_booked_list.this, LinearLayoutManager.VERTICAL, false));
    }

    public void get_booked_list()
    {
        FirebaseAuth firebase = FirebaseAuth.getInstance();
        final String email = firebase.getCurrentUser().getEmail().replace(".", "");
        FirebaseDatabase data = FirebaseDatabase.getInstance();

        data.getReference().child("booked_events").addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                book_list.clear();

                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    for(DataSnapshot dataSnapshot1 : data.getChildren())
                    {

                            for ( DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren())
                            {
                                SingleServiceBooking booking_data = dataSnapshot2.getValue(SingleServiceBooking.class);

                                booking_data.booked_by = dataSnapshot1.getKey();

                                book_list.add(booking_data);

                            }

                    }
                }

                Adapter adapter = new Adapter();
                book_recycle.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        get_booked_list();
    }

    public class view_holder extends RecyclerView.ViewHolder {

        TextView book_date , title , service , booked_by;
        LinearLayout book_lay;



        public view_holder(View itemView) {
            super(itemView);

            book_lay = itemView.findViewById(R.id.book_lay);
            book_date = itemView.findViewById(R.id.event_date);

            title = itemView.findViewById(R.id.event_title);

            book_date = itemView.findViewById(R.id.event_date);

            service = itemView.findViewById(R.id.service);

            booked_by = itemView.findViewById(R.id.booked_by);
        }
    }

    public class Adapter extends RecyclerView.Adapter<view_holder> {

        @Override
        public view_holder onCreateViewHolder(ViewGroup parent, int viewType) {

            view_holder v = new view_holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_booked_cell, parent, false));

            return v;
        }

        @Override
        public void onBindViewHolder(view_holder holder, int position) {


            final SingleServiceBooking data = book_list.get(position);
            holder.book_date.setText(String.valueOf(data.date));

            holder.service.setText(String.valueOf(data.service));

            holder.title.setText(String.valueOf(data.title));


            holder.booked_by.setText("User email: "+data.booked_by);

            holder.book_lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(data.service.equals("venue"))
                    {
                        Intent i = new Intent(admin_booked_list.this , viewbookedvenueDetails.class);

                        i.putExtra("vendor_key" , data.vendor_key);

                        startActivity(i);
                    }

                    if(data.service.equals("caterer"))
                    {
                        Intent i = new Intent(admin_booked_list.this , viewbookedCaterersDetails.class);

                        i.putExtra("vendor_key" , data.vendor_key);

                        startActivity(i);
                    }

                    if(data.service.equals("decorator"))
                    {
                        Intent i = new Intent(admin_booked_list.this , viewbookedDecorDetails.class);

                        i.putExtra("vendor_key" , data.vendor_key);

                        startActivity(i);
                    }

                    if(data.service.equals("designer"))
                    {
                        Intent i = new Intent(admin_booked_list.this , viewbookedDesignerDetails.class);

                        i.putExtra("vendor_key" , data.vendor_key);

                        startActivity(i);
                    }

                    if(data.service.equals("photographer"))
                    {
                        Intent i = new Intent(admin_booked_list.this , viewbookedPhotographerDetails.class);

                        i.putExtra("vendor_key" , data.vendor_key);

                        startActivity(i);
                    }

                    if(data.service.equals("salon"))
                    {
                        Intent i = new Intent(admin_booked_list.this , viewbookedSalonDetails.class);

                        i.putExtra("vendor_key" , data.vendor_key);

                        startActivity(i);
                    }

                    if(data.service.equals("dj"))
                    {
                        Intent i = new Intent(admin_booked_list.this , viewbookedDjDetails.class);

                        i.putExtra("vendor_key" , data.vendor_key);

                        startActivity(i);
                    }

                  /*  String date = String.valueOf(data.time);
                    String dj_name = data.dj_name;
                    String venue_loc = data.venue_loc;
                    String venue_name = data.venue_name;
                    String dj_loc = data.dj_loc;
                    String decorater_name = data.decorater_name;
                    String decorater_loc = data.decorater_loc;
                    String designer_name = data.designer_name;
                    String designer_loc = data.designer_loc;
                    String photographer_name = data.photographer_name;
                    String photographer_loc = data.photographer_loc;
                    String salon_name = data.salon_name;
                    String salon_loc = data.salon_loc;
                    String caterer_name = data.caterer_name;
                    String caterer_loc = data.caterer_loc;
                    Intent i = new Intent(booked_list.this, view_book_event.class);
                    i.putExtra("venue", venue_name + venue_loc);
                    i.putExtra("date", date);
                    i.putExtra("dj", dj_name + dj_loc);
                    i.putExtra("designer", designer_name + designer_loc);
                    i.putExtra("decorater", decorater_name + decorater_loc);
                    i.putExtra("salon", salon_name + salon_loc);
                    i.putExtra("caterer", caterer_name + caterer_loc);
                    i.putExtra("photographer", photographer_name + photographer_loc);
                    startActivity(i);*/

                }
            });
        }

        @Override
        public int getItemCount() {
            return book_list.size();
        }
    }
}