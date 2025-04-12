package tlu.cse.ht64.apphoctienganh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import tlu.cse.ht64.apphoctienganh.R;
import tlu.cse.ht64.apphoctienganh.utils.SessionManager;

public class LessonsActivity extends AppCompatActivity {
    private Button btnVocabulary, btnTenses, btnSearch, btnTest, btnLearnedWords, btnFavoriteWords;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessons);

        btnVocabulary = findViewById(R.id.btnVocabulary);
        btnTenses = findViewById(R.id.btnTenses);
        btnSearch = findViewById(R.id.btnSearch);
        btnTest = findViewById(R.id.btnTest);
        btnLearnedWords = findViewById(R.id.btnLearnedWords);
        btnFavoriteWords = findViewById(R.id.btnFavoriteWords);
        session = new SessionManager(this);

        if (!session.isLoggedIn()) {
            finish();
            return;
        }

        btnVocabulary.setOnClickListener(v -> startActivity(new Intent(this, VocabularyActivity.class)));
        btnTenses.setOnClickListener(v -> startActivity(new Intent(this, TensesActivity.class)));
        btnSearch.setOnClickListener(v -> startActivity(new Intent(this, SearchActivity.class)));
        btnTest.setOnClickListener(v -> startActivity(new Intent(this, TestActivity.class)));
        btnLearnedWords.setOnClickListener(v -> startActivity(new Intent(this, LearnedWordsActivity.class)));
        btnFavoriteWords.setOnClickListener(v -> startActivity(new Intent(this, FavoriteWordsActivity.class)));
    }
}