package com.example.continental;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import continental.R;

public class Set_Profile extends AppCompatActivity {

    EditText editTextTextPersonName3,editTextPhone2;
    Button button15;
    SharedPreferences sharedPreferences;
    String mail;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_profile);
        editTextTextPersonName3 = findViewById(R.id.editTextTextPersonName3);
        editTextPhone2 = findViewById(R.id.editTextPhone2);
        button15 = findViewById(R.id.button15);
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        mail = sharedPreferences.getString("loggedInUser","");

        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
        db = dbHelper.getWritableDatabase();

        button15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String n = editTextTextPersonName3.getText().toString();
                String p = editTextPhone2.getText().toString();
                if(!(n.isEmpty() || p.isEmpty())){
                ContentValues values = new ContentValues();
                values.put("name",n);
                values.put("phone",p);
                String whereClause = "email = ?";
                String[] selectionArgs = { mail };
                int rowsAffected = db.update("user", values, whereClause, selectionArgs);
                Intent intent = new Intent(Set_Profile.this,Profile.class);
                startActivity(intent);
                Toast.makeText(Set_Profile.this, "Changes applied successfully", Toast.LENGTH_SHORT).show();}
                else{
                    Toast.makeText(Set_Profile.this, "Fill all details", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}