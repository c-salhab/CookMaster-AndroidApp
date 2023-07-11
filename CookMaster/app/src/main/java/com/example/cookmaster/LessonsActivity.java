package com.example.cookmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.vishnusivadas.advanced_httpurlconnection.FetchData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LessonsActivity extends AppCompatActivity {

    private AdView mAdView;
    private ImageButton btn_1;
    private ImageButton btn_2;
    private ListView listView;
    private ArrayAdapter<String> adapter;

    private Button btnWithDate;
    private Button btnWithoutDate;
    private Button btnAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessons);
        this.btn_1 = findViewById(R.id.button_1);
        this.btn_2 = findViewById(R.id.button_2);

        this.btnWithDate = findViewById(R.id.btn_with_date);
        this.btnWithoutDate = findViewById(R.id.btn_with_no_date);
        this.btnAll = findViewById(R.id.btn_all);

        this.btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LessonsActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        this.btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LessonsActivity.this, ProfileActivity.class);
                startActivity(i);
                finish();
            }
        });


        btnWithDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterLessonsWithDate();
            }

        });

        btnWithoutDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterLessonsWithoutDate();
            }
        });

        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAllLessons();
            }
        });
        verifySubscription();
        listView = findViewById(R.id.list_view);
        getLessons();
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

                SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                String userEmail = preferences.getString("email", "");

                FetchData fetchData = new FetchData("https://yourcookmaster.com/android/get_lessons.php?email=" + userEmail);
                if (fetchData.startFetch()) {
                    if (fetchData.onComplete()) {
                        String result = fetchData.getResult();
                        try {
                            JSONArray lessonsArray = new JSONArray(result);
                            String[] lessons = new String[lessonsArray.length()];

                            for (int i = 0; i < lessonsArray.length(); i++) {
                                JSONObject lessonObject = lessonsArray.getJSONObject(i);
                                String lessonTitle = lessonObject.getString("title");
                                lessons[i] = lessonTitle;
                            }

                            adapter = new ArrayAdapter<>(LessonsActivity.this, android.R.layout.simple_list_item_1, lessons);
                            listView.setAdapter(adapter);

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                    String selectedLesson = (String) adapterView.getItemAtPosition(position);
                                    Intent intent = new Intent(LessonsActivity.this, LessonDetailActivity.class);
                                    intent.putExtra("lessonTitle", selectedLesson);
                                    startActivity(intent);
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        call();
    }

    private void call() {
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

    public void showAllLessons(){
        getLessons();
    }

    public void filterLessonsWithoutDate() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                String userEmail = preferences.getString("email", "");

                FetchData fetchData = new FetchData("https://yourcookmaster.com/android/get_lessons.php?email=" + userEmail);
                if (fetchData.startFetch()) {
                    if (fetchData.onComplete()) {
                        String result = fetchData.getResult();
                        try {
                            JSONArray lessonsArray = new JSONArray(result);
                            List<String> filteredLessons = new ArrayList<>();

                            for (int i = 0; i < lessonsArray.length(); i++) {
                                JSONObject lessonObject = lessonsArray.getJSONObject(i);
                                String lessonTitle = lessonObject.getString("title");
                                String lessonDate = lessonObject.getString("date_of_exam");

                                if (lessonDate.equals("null")) {
                                    filteredLessons.add(lessonTitle);
                                }
                            }

                            adapter = new ArrayAdapter<>(LessonsActivity.this, android.R.layout.simple_list_item_1, filteredLessons);
                            listView.setAdapter(adapter);

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                    String selectedLesson = (String) adapterView.getItemAtPosition(position);
                                    Intent intent = new Intent(LessonsActivity.this, LessonDetailActivity.class);
                                    intent.putExtra("lessonTitle", selectedLesson);
                                    startActivity(intent);
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    public void filterLessonsWithDate(){
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                String userEmail = preferences.getString("email", "");

                FetchData fetchData = new FetchData("https://yourcookmaster.com/android/get_lessons.php?email=" + userEmail);
                if (fetchData.startFetch()) {
                    if (fetchData.onComplete()) {
                        String result = fetchData.getResult();
                        try {
                            JSONArray lessonsArray = new JSONArray(result);
                            List<String> filteredLessons = new ArrayList<>();

                            for (int i = 0; i < lessonsArray.length(); i++) {
                                JSONObject lessonObject = lessonsArray.getJSONObject(i);
                                String lessonTitle = lessonObject.getString("title");
                                String lessonDate = lessonObject.getString("date_of_exam");

                                if (!lessonDate.equals("null")) {
                                    filteredLessons.add(lessonTitle);
                                }
                            }

                            adapter = new ArrayAdapter<>(LessonsActivity.this, android.R.layout.simple_list_item_1, filteredLessons);
                            listView.setAdapter(adapter);

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                    String selectedLesson = (String) adapterView.getItemAtPosition(position);
                                    Intent intent = new Intent(LessonsActivity.this, LessonDetailActivity.class);
                                    intent.putExtra("lessonTitle", selectedLesson);
                                    startActivity(intent);
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }
}
