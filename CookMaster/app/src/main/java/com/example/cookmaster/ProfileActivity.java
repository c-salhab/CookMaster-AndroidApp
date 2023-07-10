package com.example.cookmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.vishnusivadas.advanced_httpurlconnection.FetchData;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {

    private Button logoutButton;
    private ImageButton backButton;
    private AdView mAdView;

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


        getInfoUser();
        verifySubscription();

    }

    public void verifySubscription() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {

                SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                String userEmail = preferences.getString("email", "");

                FetchData fetchData = new FetchData("https://yourcookmaster.com/android/get_subscription.php?email=" + userEmail);
                if (fetchData.startFetch()) {
                    if (fetchData.onComplete()) {
                        String result = fetchData.getResult();

                        if (result.equals("1")) {
                            MobileAds.initialize(ProfileActivity.this, new OnInitializationCompleteListener() {
                                @Override
                                public void onInitializationComplete(InitializationStatus initializationStatus) {
                                    mAdView = findViewById(R.id.adView);
                                    AdRequest adRequest = new AdRequest.Builder().build();
                                    mAdView.loadAd(adRequest);
                                }
                            });
                        }
                    }
                }
            }
        });
    }

    public void getInfoUser(){
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {

                SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                String userEmail = preferences.getString("email", "");

                FetchData fetchData = new FetchData("https://yourcookmaster.com/android/get_info_users.php?email=" + userEmail);
                if (fetchData.startFetch()) {
                    if (fetchData.onComplete()) {
                        String result = fetchData.getResult();

                        try {
                            JSONObject userInfoObject = new JSONObject(result);
                            String firstName = userInfoObject.getString("first_name");
                            String lastName = userInfoObject.getString("last_name");
                            String subscription = userInfoObject.getString("subscription_name");

                            TextView fnameTextView = findViewById(R.id.fname_text_view);
                            TextView lnameTextView = findViewById(R.id.lname_text_view);
                            TextView emailTextView = findViewById(R.id.email_text_view);
                            TextView subscriptionTextView = findViewById(R.id.subscription_text_view);

                            fnameTextView.setText(firstName);
                            lnameTextView.setText(lastName);
                            emailTextView.setText(userEmail);
                            subscriptionTextView.setText(subscription);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

}
