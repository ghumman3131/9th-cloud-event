package com.example.user.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.myapplication.data_model.designer_detail;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

public class designerDetails extends AppCompatActivity {

    String designername, designerloc, designerservice = "Not Available";
    int price = 0;
    EditText et_designername, et_designerloc, et_designerprice,et_designerservice ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_designer_details);
        et_designername = findViewById(R.id.et_designername);
        et_designerloc = findViewById(R.id.et_designerloc);

        et_designerprice = findViewById(R.id.et_designerprice);

        et_designerservice = findViewById(R.id.et_designerservices);


    }


    public void save_designer(View view) {

        designername = et_designername.getText().toString();
        designerloc = et_designerloc.getText().toString();
        price = Integer.parseInt(et_designerprice.getText().toString());
        designerservice=et_designerservice.getText().toString();
        if (designername.length() < 2 ||designerservice.length() < 2 || designerloc.length()  <2 || price == 0 ) {
            Toast.makeText(designerDetails.this, "Enter values", Toast.LENGTH_SHORT).show();

        } else {
            designer_detail data = new designer_detail(designername, designerloc, designerservice, price);


            FirebaseDatabase database = FirebaseDatabase.getInstance();


            database.getReference().child("designer").child(designername+designerloc).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {

                public void onComplete(@NonNull Task<Void> task) {

                    if(task.isSuccessful())
                    {

                        Toast.makeText(designerDetails.this , "Data added" , Toast.LENGTH_SHORT).show();
                        startActivity( new Intent(designerDetails.this , stp2admin.class));
                    }
                    else {

                        Toast.makeText(designerDetails.this , "Error" , Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    public void pick_location(View view) {

        startActivityForResult(new Intent(designerDetails.this , PlacePickerActivity.class) , 300);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 300 && resultCode == 200)
        {
            et_designerloc.setText(data.getStringExtra("place"));
        }
    }

}
