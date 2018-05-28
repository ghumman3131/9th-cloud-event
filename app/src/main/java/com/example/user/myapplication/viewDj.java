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

import com.example.user.myapplication.data_model.dj_detail;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class viewDj extends AppCompatActivity {
    private static ArrayList<dj_detail> dj_list;
    RecyclerView dj_recycler;

    private static Adapter adapter;

    private static ProgressDialog pd ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_dj);
        dj_list = new ArrayList<>();

        pd = new ProgressDialog(this);
        pd.setTitle("Loading");
        pd.setMessage("Please wait");

        adapter = new Adapter(dj_list);

        dj_recycler = findViewById(R.id.dj_recycler);

        dj_recycler.setLayoutManager(new LinearLayoutManager(viewDj.this , LinearLayoutManager.VERTICAL, false));

        dj_recycler.setAdapter(adapter);


    }


    public void get_dj()
    {
        pd.show();

        FirebaseAuth firebase = FirebaseAuth.getInstance();

        FirebaseDatabase data =FirebaseDatabase.getInstance();
        System.out.println("rrrr");
        data.getReference().child("dj").addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dj_list.clear();

                pd.hide();

                for (DataSnapshot data : dataSnapshot.getChildren())
                {
                    dj_detail details = data.getValue(dj_detail.class);
                    System.out.println("rrrrrr");
                    dj_list.add(details);


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

        get_dj();
    }

    public class view_holder extends RecyclerView.ViewHolder{

        TextView dj_name,dj_loc ;
        LinearLayout dj_lay;
        public view_holder(View itemView) {
            super(itemView);

            dj_name = itemView.findViewById(R.id.name);
            dj_lay=itemView.findViewById(R.id.dj_lay);
            dj_loc = itemView.findViewById(R.id.loc);
        }
    }

    public class Adapter extends RecyclerView.Adapter<view_holder>
    {

        ArrayList<dj_detail> dj_list;

        public Adapter(ArrayList<dj_detail> dj_list)
        {
            this.dj_list = dj_list ;
        }

        @Override
        public view_holder onCreateViewHolder(ViewGroup parent, int viewType) {

            view_holder v = new view_holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.dj_cell,parent , false ));

            return v ;
        }

        @Override
        public void onBindViewHolder(view_holder holder, int position) {


            final dj_detail data=dj_list.get(position);
            holder.dj_name.setText(data.djname);
            holder.dj_loc.setText(data.djloc);
            holder.dj_lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String djname=data.djname;
                    String djloc=data.djloc;
                    String djservice=data.djservice;
                    int djprice=data.djprice;

                    Intent i=new Intent(viewDj.this,viewDjDetails.class);
                    i.putExtra("djname",djname);
                    i.putExtra("djloc",djloc);
                    i.putExtra("djservice",djservice);
                    i.putExtra("djprice",String.valueOf(djprice));

                    i.putExtra("title" , getIntent().getStringExtra("title"));
                    i.putExtra("date" , getIntent().getStringExtra("date"));



                    startActivity(i);
                }
            });
        }

        @Override
        public int getItemCount() {
            return dj_list.size();
        }
    }

    public static void get_filter_caterer(final String location , final int min_price , final int max_price)
    {
        pd.show();

        FirebaseAuth firebase = FirebaseAuth.getInstance();

        FirebaseDatabase data =FirebaseDatabase.getInstance();
        System.out.println("rrrr");
        data.getReference().child("dj").addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dj_list.clear();

                pd.hide();

                for (DataSnapshot data : dataSnapshot.getChildren())
                {
                    dj_detail details = data.getValue(dj_detail.class);

                    if(!location.toLowerCase().equals("any"))
                    {
                        Log.i("selected location is : " , location);
                        Log.i("decorater location is: " , details.djloc);

                        if(details.djloc.toLowerCase().contains(location.toLowerCase()))
                        {

                            if(details.djprice > min_price && details.djprice < max_price)

                            {

                                dj_list.add(details);

                            }

                        }


                    }

                    else {

                        dj_list.add(details);
                    }

                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void open_filter(View view) {

        new FilterDialog(viewDj.this , R.style.AppTheme).show();
    }
}