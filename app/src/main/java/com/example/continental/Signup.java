package com.example.continental;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import continental.R;

public class Signup extends AppCompatActivity {
SQLiteDatabase db;
    EditText editTextTextPersonName,editTextTextEmailAddress2,editTextTextPassword2,editTextTextPassword3;
    Button button4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        editTextTextPersonName = findViewById(R.id.editTextTextPersonName);
        editTextTextEmailAddress2 = findViewById(R.id.editTextTextEmailAddress2);
        editTextTextPassword2 = findViewById(R.id.editTextTextPassword2);
        editTextTextPassword3 = findViewById(R.id.editTextTextPassword3);
        button4 = findViewById(R.id.button4);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
        db = dbHelper.getWritableDatabase();

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username=  editTextTextPersonName.getText().toString();
                String email=  editTextTextEmailAddress2.getText().toString();
                String password = editTextTextPassword2.getText().toString();
                String phone=  editTextTextPassword3.getText().toString();
                if(TextUtils.isEmpty(username)){
                    editTextTextPersonName.setError("Please enter username to signup");
                }
                else if (TextUtils.isEmpty(email)){
                    editTextTextEmailAddress2.setError("Please enter email to signup");
                }
                else if (TextUtils.isEmpty(password)){
                    editTextTextPassword2.setError("Please enter password to signup");
                }
                else if (TextUtils.isEmpty(phone)){
                    editTextTextPassword3.setError("Please enter phone number to signup");
                }
                else{
                    Cursor c = db.rawQuery("SELECT * FROM user WHERE email='"+email+"'", null);
                    if(c.moveToFirst()){
                        Toast.makeText(getApplicationContext(), "User account already exists. Please login", Toast.LENGTH_LONG).show();
                    }
                    else{
                    Intent intent = new Intent(Signup.this, Home_page.class);
                    db.execSQL("INSERT INTO user VALUES('"+username+"','"+email+
                            "','"+password+"','"+phone+"','"+" "+"');");
                    startActivity(intent);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("loggedInUser", email);
                    editor.apply();
                        Toast.makeText(getApplicationContext(), "User account created.😊", Toast.LENGTH_LONG).show();}
                }
            }
        });
    }
}