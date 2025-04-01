package com.example.continental;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import continental.R;

public class Profile extends AppCompatActivity {
SQLiteDatabase db;
    TextView textView7,textView8,textView9;
    Button button14, btn;
    SharedPreferences sharedPreferences;
    String name, phn, order;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        textView7 = findViewById(R.id.textView7);
        textView8 = findViewById(R.id.textView8);
        textView9 = findViewById(R.id.textView9);
        button14 = findViewById(R.id.button14);
        btn = findViewById(R.id.ord);

        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
        db = dbHelper.getWritableDatabase();

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String mail = sharedPreferences.getString("loggedInUser","");

        Cursor c = db.rawQuery("SELECT * FROM user WHERE email='"+mail+"'", null);
        if(c.moveToFirst()){
            name = c.getString(0);
            phn = c.getString(3);
            order = c.getString(4);
            textView7.setText(name);
            textView8.setText(mail);
            textView9.setText(phn);
        }
else{
            Toast.makeText(getApplicationContext(), mail, Toast.LENGTH_SHORT).show();
        }
        button14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this,Set_Profile.class);
                startActivity(intent);

                Toast.makeText(Profile.this, "Editing profile", Toast.LENGTH_SHORT).show();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(order.equals(" ")){
                showMessage("Your Orders", "No orders booked.");}
                else{
                    showMessage("Your Orders", order);
                }
            }
        });
    }

    public void showMessage(String title,String message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(Profile.this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}