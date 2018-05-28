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

import com.example.user.myapplication.data_model.designer_detail;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class viewDesigners extends AppCompatActivity {
    private static ArrayList<designer_detail> designers_list;
    RecyclerView designer_recycler;

    private static Adapter adapter;

    private static ProgressDialog pd ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_designers);
        designers_list = new ArrayList<>();

        pd = new ProgressDialog(this);
        pd.setTitle("Loading");
        pd.setMessage("Please wait");

        adapter = new Adapter(designers_list);

        designer_recycler = findViewById(R.id.designer_recycler);

        designer_recycler.setLayoutManager(new LinearLayoutManager(viewDesigners.this , LinearLayoutManager.VERTICAL, false));

        designer_recycler.setAdapter(adapter);

    }


    public void get_designers()
    {
        pd.show();

        FirebaseAuth firebase = FirebaseAuth.getInstance();
        String email=firebase.getCurrentUser().getEmail();
        FirebaseDatabase data =FirebaseDatabase.getInstance();
        System.out.println("rrrr");
        data.getReference().child("designer").addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                designers_list.clear();

                pd.hide();

                for (DataSnapshot data : dataSnapshot.getChildren())
                {
                    designer_detail details = data.getValue(designer_detail.class);
                    System.out.println("rrrrrr");
                    designers_list.add(details);


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

        get_designers();
    }

    public class view_holder extends RecyclerView.ViewHolder{

        TextView designers_name,designers_loc ;
        LinearLayout designer_lay;
        public view_holder(View itemView) {
            super(itemView);

            designers_name = itemView.findViewById(R.id.name);
            designers_loc = itemView.findViewById(R.id.loc);
            designer_lay =itemView.findViewById(R.id.designer_lay);
        }
    }

    public class Adapter extends RecyclerView.Adapter<view_holder>
    {

        ArrayList<designer_detail> designer_list;

        public Adapter(ArrayList<designer_detail> designer_list)
        {
            this.designer_list = designer_list ;
        }

        @Override
        public view_holder onCreateViewHolder(ViewGroup parent, int viewType) {

            view_holder v = new view_holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.designer_cell,parent , false ));

            return v ;
        }

        @Override
        public void onBindViewHolder(view_holder holder, int position) {


            final designer_detail data=designers_list.get(position);
            holder.designers_name.setText(data.designername);
            holder.designers_loc.setText(data.designerloc);
            holder.designer_lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String designername = data.designername;
                    String designerloc = data.designerloc;
                    String designerservice = data.designerservice;
                    int designerprice = data.designerprice;

                    Intent i = new Intent(viewDesigners.this, viewDesignerDetails.class);
                    i.putExtra("designername", designername);
                    i.putExtra("designerloc", designerloc);
                    i.putExtra("designerservice", designerservice);
                    i.putExtra("designerprice", String.valueOf(designerprice));

                    i.putExtra("title" , getIntent().getStringExtra("title"));
                    i.putExtra("date" , getIntent().getStringExtra("date"));


                    startActivity(i);
                }
            });
        }

        @Override
        public int getItemCount() {
            return designers_list.size();
        }
    }

    public static void get_filter_caterer(final String location , final int min_price , final int max_price)
    {
        pd.show();

        FirebaseAuth firebase = FirebaseAuth.getInstance();

        FirebaseDatabase data =FirebaseDatabase.getInstance();
        System.out.println("rrrr");
        data.getReference().child("designer").addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                designers_list.clear();

                pd.hide();

                for (DataSnapshot data : dataSnapshot.getChildren())
                {
                    designer_detail details = data.getValue(designer_detail.class);

                    if(!location.toLowerCase().equals("any"))
                    {
                        Log.i("selected location is : " , location);
                        Log.i("decorater location is: " , details.designerloc);

                        if(details.designerloc.toLowerCase().contains(location.toLowerCase()))
                        {

                            if(details.designerprice > min_price && details.designerprice < max_price)

                            {

                                designers_list.add(details);

                            }

                        }


                    }

                    else {

                        designers_list.add(details);
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

        new FilterDialog(viewDesigners.this , R.style.AppTheme).show();
    }
}