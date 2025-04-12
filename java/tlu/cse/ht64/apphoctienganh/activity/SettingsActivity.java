package tlu.cse.ht64.apphoctienganh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import tlu.cse.ht64.apphoctienganh.R;

public class SettingsActivity extends AppCompatActivity {
    private Button btnNotifications, btnLearnedWords, btnFavoriteWords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btnNotifications = findViewById(R.id.btnNotifications);
        btnLearnedWords = findViewById(R.id.btnLearnedWords);
        btnFavoriteWords = findViewById(R.id.btnFavoriteWords);

        btnNotifications.setOnClickListener(v -> startActivity(new Intent(this, NotificationsActivity.class)));
        btnLearnedWords.setOnClickListener(v -> startActivity(new Intent(this, LearnedWordsActivity.class)));
        btnFavoriteWords.setOnClickListener(v -> startActivity(new Intent(this, FavoriteWordsActivity.class)));
    }
}