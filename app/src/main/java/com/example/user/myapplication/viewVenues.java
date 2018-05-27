package com.example.user.myapplication;

import android.app.ProgressDialog;
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

public class viewVenues extends AppCompatActivity {
    private static ArrayList<venue_details> venue_list;
    private   RecyclerView venue_recycler;

   private static   Adapter adapter;

   private static ProgressDialog pd ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_venues);

        pd = new ProgressDialog(this);
        pd.setTitle("Loading");
        pd.setMessage("Please wait");

        venue_list = new ArrayList<>();

        venue_recycler = findViewById(R.id.venue_recycler);

        adapter = new Adapter(venue_list);

        venue_recycler.setLayoutManager(new LinearLayoutManager(viewVenues.this , LinearLayoutManager.VERTICAL, false));

        venue_recycler.setAdapter(adapter);
    }

    public static void get_venue()
    {

        pd.show();
        FirebaseAuth firebase = FirebaseAuth.getInstance();

        FirebaseDatabase data =FirebaseDatabase.getInstance();
        System.out.println("rrrr");
        data.getReference().child("venue").addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                venue_list.clear();


                pd.hide();
                for (DataSnapshot data : dataSnapshot.getChildren())
                {
                    venue_details details = data.getValue(venue_details.class);
                    System.out.println("rrrrrr");
                    venue_list.add(details);


                }

                adapter.notifyDataSetChanged();
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

    public void open_filter(View view) {

        new FilterDialog(viewVenues.this , R.style.AppTheme).show();
    }

    public class view_holder extends RecyclerView.ViewHolder{

        TextView venue_name,venue_loc ;
        LinearLayout venue_lay;
        public view_holder(View itemView) {
            super(itemView);

            venue_name = itemView.findViewById(R.id.name);
            venue_loc = itemView.findViewById(R.id.loc);
            venue_lay = itemView.findViewById(R.id.venue_lay);
        }
    }

    public class Adapter extends RecyclerView.Adapter<view_holder>
    {
        ArrayList<venue_details> venue_list;

        public Adapter(ArrayList<venue_details> venue_list)
        {
            this.venue_list = venue_list ;
        }

        @Override
        public view_holder onCreateViewHolder(ViewGroup parent, int viewType) {

            view_holder v = new view_holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.venue_cell,parent , false ));

            return v ;
        }

        @Override
        public void onBindViewHolder(view_holder holder, int position) {

            final venue_details data = venue_list.get(position);
            holder.venue_name.setText(data.venuename);
            holder.venue_loc.setText(data.venue_loc);
            holder.venue_lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String venuename=data.venuename;
                    String venueloc=data.venue_loc;
                    int venueprice=data.price;

                    Intent i=new Intent(viewVenues.this,viewVenueDetails.class);
                    i.putExtra("venuename",venuename);
                    i.putExtra("venueloc",venueloc);
                    i.putExtra("venueprice", String.valueOf(venueprice));
                    i.putExtra("service1" , data.service1);
                    i.putExtra("service2" , data.service2);
                    i.putExtra("service3" , data.service3);
                    i.putExtra("capacity" , String.valueOf(data.capacity));
                    i.putExtra("title" , getIntent().getStringExtra("title"));
                    i.putExtra("date" , getIntent().getStringExtra("date"));

                    startActivity(i);

                    finish();
                }
            }); }

        @Override
        public int getItemCount() {
            return venue_list.size();
        }
    }


    public static void get_filter_venue(final String location , final int min_price , final int max_price)
    {
        pd.show();

        FirebaseAuth firebase = FirebaseAuth.getInstance();

        FirebaseDatabase data =FirebaseDatabase.getInstance();
        System.out.println("rrrr");
        data.getReference().child("venue").addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                venue_list.clear();

                pd.hide();

                for (DataSnapshot data : dataSnapshot.getChildren())
                {
                    venue_details details = data.getValue(venue_details.class);

                    if(!location.toLowerCase().equals("any"))
                    {

                        if(details.venue_loc.toLowerCase().contains(location))
                        {

                            if(details.price > min_price && details.price < max_price)

                            {
                                venue_list.add(details);
                            }

                        }


                    }

                    else {

                        venue_list.add(details);
                    }

                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}