package com.example.cookmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {

    private Button logoutButton;
    private ImageButton backButton;
    private TextView emailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        this.backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(ProfileActivity.this, LessonsActivity.class);
                startActivity(i);
                finish();
            }
        });

        this.logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Logged Out Successfully !", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(ProfileActivity.this, LogIn.class);
                startActivity(i);
                finish();
            }
        });


        SharedPreferences sharedPrefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userEmail = sharedPrefs.getString("e" +
                "mail", "");

        emailTextView = findViewById(R.id.email_text_view);
        if (!userEmail.isEmpty()) {
            emailTextView.setText(userEmail);
        } else {
            emailTextView.setText("No email found");
        }

        TextView textActivityTextView = findViewById(R.id.text_activity_text_view);
        textActivityTextView.setText(R.string.hi);
        textActivityTextView.setTypeface(null, Typeface.ITALIC);

    }
}
