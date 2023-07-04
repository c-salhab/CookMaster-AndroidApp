package com.example.cookmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LessonsActivity extends AppCompatActivity {

    private AdView mAdView;
    private Button btn_1;

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
            }
        });

        fetchDataFromAPI();


        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                FetchData fetchData = new FetchData("https://yourcookmaster.com/android/get_lessons.php");
                // Effectuer la requête GET pour récupérer les leçons
                if (fetchData.startFetch()) {
                    if (fetchData.onComplete()) {
                        String result = fetchData.getResult();

                        // Traitez les données récupérées comme vous le souhaitez
                        Log.i("FetchData", result);

                        // Ici, vous pouvez analyser la réponse JSON et afficher les leçons dans votre activité
                        try {
                            JSONArray lessonsArray = new JSONArray(result);
                            StringBuilder stringBuilder = new StringBuilder();

                            for (int i = 0; i < lessonsArray.length(); i++) {
                                JSONObject lessonObject = lessonsArray.getJSONObject(i);
                                int lessonId = lessonObject.getInt("id");
                                String lessonTitle = lessonObject.getString("name");
                                // String lessonDescription = lessonObject.getString("description");

                                stringBuilder.append(i).append("-  Lesson ID: ").append(lessonId);
                                stringBuilder.append("  Title: ").append(lessonTitle).append("\n");
                                // stringBuilder.append("Description: ").append(lessonDescription).append("\n\n");
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

    public void call(){
        new AlertDialog.Builder(LessonsActivity.this)
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

    public void fetchDataFromAPI() {
        String apiUrl = "https://yourcookmaster.com/android/login.php";
        try {

            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Lire la réponse JSON
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Analyser la réponse JSON
            JSONObject jsonResponse = new JSONObject(response.toString());
            boolean login = jsonResponse.getBoolean("login");
            boolean showAds = jsonResponse.getBoolean("showAds");

            // Maintenant, tu as les valeurs de login et showAds dans ton code Java
            // Fais ce que tu souhaites en fonction de ces valeurs
            if (login) {
                if (showAds) {
                    // Afficher les publicités
                    MobileAds.initialize(this, new OnInitializationCompleteListener() {
                        @Override
                        public void onInitializationComplete(InitializationStatus initializationStatus) {
                            AdRequest adRequest = new AdRequest.Builder().build();
                            mAdView.loadAd(adRequest);
                        }
                    });

                    mAdView = findViewById(R.id.adView);
                } else {
                    // Ne pas afficher les publicités
                }
            } else {
                // Login échoué, prendre une autre action appropriée
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
