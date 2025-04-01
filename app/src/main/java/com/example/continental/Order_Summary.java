package com.example.continental;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import continental.R;

public class Order_Summary extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

        // Retrieve data from Intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String username = extras.getString("username");
            String address = extras.getString("address");
            String phone = extras.getString("phone");
            String bill = extras.getString("bill");
            String order = extras.getString("order");

            // Display data in EditText views
            EditText editTextTextPersonName2 = findViewById(R.id.editTextTextPersonName2);
            EditText editTextTextPostalAddress = findViewById(R.id.editTextTextPostalAddress);
            EditText editTextPhone = findViewById(R.id.editTextPhone);
            EditText editTextBill = findViewById(R.id.editTextBill);
            EditText editTextOrder = findViewById(R.id.editTextOrder);

            editTextTextPersonName2.setText(username);
            editTextTextPostalAddress.setText(address);
            assert phone != null;
            if (phone.length() >= 4) {
                String maskedPhone = phone.substring(phone.length() - 4) + "XXXX";
                editTextPhone.setText(maskedPhone);
            }
            editTextBill.setText(bill);
            editTextOrder.setText(order);
        }
    }
}