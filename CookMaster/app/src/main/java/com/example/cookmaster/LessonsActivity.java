package com.example.cookmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.vishnusivadas.advanced_httpurlconnection.FetchData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LessonsActivity extends AppCompatActivity {

    private AdView mAdView;
    private Button btn_1;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessons);
        this.btn_1 = findViewById(R.id.button_1);
        this.btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LessonsActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        verifySubscription();
        getLessons();
    }

    public void call() {
        new androidx.appcompat.app.AlertDialog.Builder(LessonsActivity.this)
                .setTitle(getResources().getString(R.string.popup_titre))
                .setMessage(getResources().getString(R.string.popup_message))
                .setPositiveButton(getResources().getString(R.string.popup_oui), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.popup_non), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setCancelable(false)
                .show();
    }

    @Override
    public void onBackPressed() {
        call();
    }

    public void verifySubscription() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Intent intent = getIntent();
                String userEmail = intent.getStringExtra("email");

                FetchData fetchData = new FetchData("https://yourcookmaster.com/android/get_subscription.php?email=" + userEmail);
                if (fetchData.startFetch()) {
                    if (fetchData.onComplete()) {
                        String result = fetchData.getResult();

                        if (result.equals("1")) {
                            MobileAds.initialize(LessonsActivity.this, new OnInitializationCompleteListener() {
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

    public void getLessons() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                FetchData fetchData = new FetchData("https://yourcookmaster.com/android/get_lessons.php");
                if (fetchData.startFetch()) {
                    if (fetchData.onComplete()) {
                        String result = fetchData.getResult();

                        try {
                            JSONArray lessonsArray = new JSONArray(result);
                            StringBuilder stringBuilder = new StringBuilder();

                            for (int i = 0; i < lessonsArray.length(); i++) {
                                JSONObject lessonObject = lessonsArray.getJSONObject(i);
                                int lessonId = lessonObject.getInt("id");
                                String lessonTitle = lessonObject.getString("name");

                                stringBuilder.append(i).append("-  Lesson ID: ").append(lessonId);
                                stringBuilder.append("  Title: ").append(lessonTitle).append("\n");
                            }

                            TextView textViewLessons = findViewById(R.id.textViewLessons);
                            textViewLessons.setText(stringBuilder.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }
}
