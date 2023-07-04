package com.example.cookmaster;

import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements NfcAdapter.ReaderCallback {
    private NfcAdapter nfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Récupérer l'instance du NfcAdapter
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter == null) {
            // Le dispositif ne supporte pas NFC
            Toast.makeText(this, "NFC n'est pas supporté sur cet appareil.", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Vérifier si le NFC est activé
        if (nfcAdapter.isEnabled()) {
            // Activer la détection de jetons NFC en priorité
            nfcAdapter.enableReaderMode(this, this, NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK, null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Désactiver la détection de jetons NFC
        nfcAdapter.disableReaderMode(this);
    }

    @Override
    public void onTagDiscovered(Tag tag) {
        // Lire les données du jeton NFC ici
        String url = "https://yourcookmaster.com/android/sananes.png";

        // Afficher l'image dans votre application
        runOnUiThread(() -> {
            ImageView imageView = findViewById(R.id.imageView);
            Picasso.get().load(url).into(imageView); // Utilisez Picasso ou une autre bibliothèque pour charger l'image depuis l'URL
        });
    }
}

