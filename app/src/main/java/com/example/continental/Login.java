package com.example.continental;

import static android.app.PendingIntent.getActivity;

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

public class Login extends AppCompatActivity {
SQLiteDatabase db;
    EditText editTextTextEmailAddress;
    EditText editTextTextPassword;
    Button button3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextTextEmailAddress = findViewById(R.id.editTextTextEmailAddress);
        editTextTextPassword = findViewById(R.id.editTextTextPassword);
        button3 = findViewById(R.id.button3);

        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
        db = dbHelper.getWritableDatabase();

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email= editTextTextEmailAddress.getText().toString();
                String password = editTextTextPassword.getText().toString();
                if(TextUtils.isEmpty(email)){
                    editTextTextEmailAddress.setError("Please enter email to login");
                    Toast.makeText(getApplicationContext(), "Please enter email to login.", Toast.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(password)){
                    editTextTextPassword.setError("Please enter password to login");
                    Toast.makeText(getApplicationContext(), "Please enter password to login.", Toast.LENGTH_LONG).show();
                }
                else{
                    Intent intent = new Intent(Login.this, Home_page.class);
                    Cursor c = db.rawQuery("SELECT * FROM user WHERE email='"+email+"'", null);
                    if(c.moveToFirst()){
                        String pass = c.getString(2);
                        if(password.equals(pass)){
                            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("loggedInUser", email); // Save the username of the logged-in user
                            editor.apply();
                            Toast.makeText(getApplicationContext(), "Successfully logged in", Toast.LENGTH_LONG).show();
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Password wrong.", Toast.LENGTH_LONG).show();
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "User account not found.", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });
    }
}