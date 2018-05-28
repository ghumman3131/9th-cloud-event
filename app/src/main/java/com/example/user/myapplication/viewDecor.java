package com.example.user.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.myapplication.data_model.decorator_detail;
import com.example.user.myapplication.data_model.venue_details;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class viewDecor extends AppCompatActivity {
    private static ArrayList<decorator_detail> decor_list;
    RecyclerView decorater_recycler;

    private static   Adapter adapter;

    private static ProgressDialog pd ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_decor);

        pd = new ProgressDialog(this);
        pd.setTitle("Loading");
        pd.setMessage("Please wait");

        decor_list = new ArrayList<>();

        decorater_recycler = findViewById(R.id.decor_recycler);

        adapter = new Adapter(decor_list);

        decorater_recycler.setLayoutManager(new LinearLayoutManager(viewDecor.this, LinearLayoutManager.VERTICAL, false));

        decorater_recycler.setAdapter(adapter);
    }


    public void get_decorater() {

        pd.show();
        FirebaseAuth firebase = FirebaseAuth.getInstance();
        String email = firebase.getCurrentUser().getEmail();
        FirebaseDatabase data = FirebaseDatabase.getInstance();
        System.out.println("rrrr");
        data.getReference().child("decorater").addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                pd.hide();

                decor_list.clear();


                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    decorator_detail details = data.getValue(decorator_detail.class);
                    System.out.println("rrrrrr");
                    decor_list.add(details);


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

        get_decorater();
    }

    public void open_filter(View view) {

        new FilterDialog(viewDecor.this , R.style.AppTheme).show();
    }

    public class view_holder extends RecyclerView.ViewHolder {

        TextView decorater_name, decorater_loc;
        LinearLayout decorater_lay;

        public view_holder(View itemView) {
            super(itemView);

            decorater_name = itemView.findViewById(R.id.name);
            decorater_loc = itemView.findViewById(R.id.loc);
            decorater_lay = itemView.findViewById(R.id.decorater_lay);
        }
    }

    public class Adapter extends RecyclerView.Adapter<view_holder> {

        ArrayList<decorator_detail> decor_list;

        public Adapter(ArrayList<decorator_detail> decor_details)
        {
            this.decor_list = decor_details ;
        }
        @Override
        public view_holder onCreateViewHolder(ViewGroup parent, int viewType) {

            view_holder v = new view_holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.decoraters_cell, parent, false));

            return v;
        }

        @Override
        public void onBindViewHolder(view_holder holder, int position) {

            final decorator_detail data = decor_list.get(position);
            holder.decorater_name.setText(data.decoratorname);
            holder.decorater_loc.setText(data.decoratorloc);
            holder.decorater_lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String decoratername = data.decoratorname;
                    String decoraterloc = data.decoratorloc;
                    String decoraterservice = data.decoratorservice;
                    int decoraterprice = data.decoratorprice;
                    System.out.println(decoraterservice+"$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
                    Intent i = new Intent(viewDecor.this, viewDecorDetails.class);
                    i.putExtra("decoratername", decoratername);
                    i.putExtra("decoraterloc", decoraterloc);
                    i.putExtra("decoraterservice", decoraterservice);
                    i.putExtra("decoraterprice", String.valueOf(decoraterprice));
                    i.putExtra("service1" , data.decoratorservice);
                    i.putExtra("title" , getIntent().getStringExtra("title"));
                    i.putExtra("date" , getIntent().getStringExtra("date"));




                    startActivity(i);
                    finish();
                }
            });
        }
        @Override
        public int getItemCount() {
            return decor_list.size();
        }


    }

    public static void get_filter_decorator(final String location , final int min_price , final int max_price)
    {
        pd.show();

        FirebaseAuth firebase = FirebaseAuth.getInstance();

        FirebaseDatabase data =FirebaseDatabase.getInstance();
        System.out.println("rrrr");
        data.getReference().child("decorater").addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                decor_list.clear();

                pd.hide();

                for (DataSnapshot data : dataSnapshot.getChildren())
                {
                    decorator_detail details = data.getValue(decorator_detail.class);

                    if(!location.toLowerCase().equals("any"))
                    {
                        Log.i("selected location is : " , location);
                        Log.i("decorater location is: " , details.decoratorloc);

                        if(details.decoratorloc.toLowerCase().contains(location.toLowerCase()))
                        {

                            if(details.decoratorprice > min_price && details.decoratorprice < max_price)

                            {

                                decor_list.add(details);

                            }

                        }


                    }

                    else {

                        decor_list.add(details);
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

