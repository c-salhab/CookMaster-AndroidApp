package com.example.cookmaster;

import android.app.Activity;
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
import android.widget.ImageButton;
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

public class LessonDetailActivity extends Activity {
    private TextView lessonTitleTextView;
    private TextView lessonContentTextView;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_detail);

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LessonDetailActivity.this, LessonsActivity.class);
                startActivity(i);
                finish();
            }
        });

        getLessons();
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
                            MobileAds.initialize(LessonDetailActivity.this, new OnInitializationCompleteListener() {
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
                String title = getIntent().getStringExtra("lessonTitle");
                if (fetchData.startFetch()) {
                    if (fetchData.onComplete()) {
                        String result = fetchData.getResult();
                        try {
                            JSONArray lessonsArray = new JSONArray(result);

                            for (int i = 0; i < lessonsArray.length(); i++) {
                                JSONObject lessonObject = lessonsArray.getJSONObject(i);
                                String lessonTitle = lessonObject.getString("title");
                                String lessonLink = lessonObject.getString("url_to_exam");
                                String lessonDate = lessonObject.getString("date_of_exam");

                                if (lessonTitle.equals(title)) {
                                    TextView lessonTitleTextView = findViewById(R.id.lesson_title_text_view);
                                    lessonTitleTextView.setText(lessonTitle);

                                    TextView lessonLinkTextView = findViewById(R.id.link_content_text_view);
                                    if (!lessonLink.equals("null")) {
                                        SpannableString spannableString = new SpannableString("Click here");
                                        ClickableSpan clickableSpan = new ClickableSpan() {
                                            @Override
                                            public void onClick(View view) {
                                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(lessonLink));
                                                startActivity(intent);
                                            }
                                        };
                                        spannableString.setSpan(clickableSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        lessonLinkTextView.setText(spannableString);
                                        lessonLinkTextView.setMovementMethod(LinkMovementMethod.getInstance());
                                    } else {
                                        lessonLinkTextView.setText("No URL chosen");
                                    }


                                    TextView lessonDateTextView = findViewById(R.id.date_content_text_view);
                                    if (!lessonDate.equals("null")) {
                                        lessonDateTextView.setText(lessonDate);
                                    } else {
                                        lessonDateTextView.setText("No date chosen");
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

}
