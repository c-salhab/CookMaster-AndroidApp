package com.example.cookmaster;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements NfcAdapter.ReaderCallback {
    private NfcAdapter nfcAdapter;

    private Button btn_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter == null) {

            Toast.makeText(this, "NFC n'est pas supporté sur cet appareil.", Toast.LENGTH_SHORT).show();
            return;
        }

        this.btn_2 = findViewById(R.id.button_2);
        this.btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, LessonsActivity.class);
                startActivity(i);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (nfcAdapter.isEnabled()) {

            nfcAdapter.enableReaderMode(this, this, NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK, null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        nfcAdapter.disableReaderMode(this);
    }

    @Override
    public void onTagDiscovered(Tag tag) {
        String url = "https://yourcookmaster.com/android/sananes.png";

        runOnUiThread(() -> {
            ImageView imageView = findViewById(R.id.imageView);
            Picasso.get().load(url).into(imageView);
        });
    }

}

