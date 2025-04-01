package com.example.continental;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import continental.R;

public class Bill_Payment_Deal_One extends AppCompatActivity {
    EditText editTextTextPersonName2;
    EditText editTextTextPostalAddress;
    EditText editTextPhone;
    Button button12;
    String cv, mail;
    SQLiteDatabase db;
    SharedPreferences sharedPreferences;
    String username, address, phone, userPhoneNumber;
    private static final String CHANNEL_ID = "order_confirmation";
    private static final int NOTIFICATION_ID = 123;
    private static final int REQUEST_NOTIFICATION_PERMISSION = 1;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =1;
    private static final String NOTIFICATION_POLICY_PERMISSION = Manifest.permission.ACCESS_NOTIFICATION_POLICY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_payment_deal_one);
        editTextTextPersonName2 = findViewById(R.id.editTextTextPersonName2);
        editTextTextPostalAddress =findViewById(R.id.editTextTextPostalAddress);
        editTextPhone = findViewById(R.id.editTextPhone);
        button12 = findViewById(R.id.button12);

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        mail = sharedPreferences.getString("loggedInUser","");

        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
        db = dbHelper.getWritableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM user WHERE email='"+mail+"'", null);
        if(c.moveToFirst()){
            cv = c.getString(4);}
        else{
            Toast.makeText(Bill_Payment_Deal_One.this, "Previous orders not retrievable. ", Toast.LENGTH_LONG).show();
        }

        button12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = editTextTextPersonName2.getText().toString();
                address = editTextTextPostalAddress.getText().toString();
                phone = editTextPhone.getText().toString();
                if (TextUtils.isEmpty(username)) {
                    editTextTextPersonName2.setError("Please enter your username");
                } else if (TextUtils.isEmpty(address)) {
                    editTextTextPostalAddress.setError("Please enter your address");
                } else if (TextUtils.isEmpty(phone)) {
                    editTextPhone.setError("Please enter your phone number");
                } else {
                    ContentValues values = new ContentValues();
                    values.put("orders",  cv+"\nSingle Patty Burger");
                    String whereClause = "email = ?";
                    String[] selectionArgs = { mail };
                    int rowsAffected = db.update("user", values, whereClause, selectionArgs);

                    userPhoneNumber = getUserPhoneNumberFromDB(mail);

                    if (userPhoneNumber != null) {
                        String confirmationMessage = "Dear customer, your order is confirmed. It will be delivered to " +
                                address + ". Thank you!";
                        sendNotification(userPhoneNumber, confirmationMessage, username);
                    } else {
                        Toast.makeText(Bill_Payment_Deal_One.this, "Error: Phone number not found.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private String getUserPhoneNumberFromDB(String email) {
        Cursor cursor = db.rawQuery("SELECT phone FROM user WHERE email='" + email + "'", null);
        if (cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex("phone"));
        }
        return null; // Phone number not found
    }

    private void sendNotification(String phoneNumber, String message, String username) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (checkSelfPermission(NOTIFICATION_POLICY_PERMISSION) != PackageManager.PERMISSION_GRANTED) {
                // Permission not granted, request it
                requestPermissions(new String[]{NOTIFICATION_POLICY_PERMISSION}, REQUEST_NOTIFICATION_PERMISSION);
                return;
            }
        }
        Toast.makeText(Bill_Payment_Deal_One.this, "Your order is confirmed "+username, Toast.LENGTH_LONG).show();
        createNotificationChannel();
        showNotification("Order Confirmed", "Your order is confirmed " + username);
    }

    private void showNotification(String title, String message) {

        Intent intent = new Intent(Bill_Payment_Deal_One.this, Order_Summary.class);
        intent.putExtra("username", username);
        intent.putExtra("address", address);
        intent.putExtra("phone", phone);
        intent.putExtra("bill", "Amount: 400 INR");
        intent.putExtra("order", "Deal Five");
        PendingIntent pendingIntent = PendingIntent.getActivity(Bill_Payment_Deal_One.this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notilogo)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
        checkForSmsPermission();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Order Confirmation";
            String description = "Notification for order confirmation";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    private void checkForSmsPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
        } else {
            sendSmsMessage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_SEND_SMS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendSmsMessage();
            } else {
                Toast.makeText(this, "SMS permission required to send an SMS.", Toast.LENGTH_LONG).show();
            }
        }
    }
    private void sendSmsMessage() {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(userPhoneNumber, null, "Your order has been booked, we will deliver it at "+ address+" in 30 minutes from now.", null, null);
        Toast.makeText(getApplicationContext(), "SMS sent.", Toast.LENGTH_LONG).show();
    }
}
