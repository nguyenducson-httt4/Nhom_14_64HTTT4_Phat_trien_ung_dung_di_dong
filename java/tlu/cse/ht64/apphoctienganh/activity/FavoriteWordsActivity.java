package tlu.cse.ht64.apphoctienganh.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import tlu.cse.ht64.apphoctienganh.R;
import tlu.cse.ht64.apphoctienganh.database.SQLiteHelper;
import tlu.cse.ht64.apphoctienganh.model.Word;
import tlu.cse.ht64.apphoctienganh.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class FavoriteWordsActivity extends AppCompatActivity {
    private ListView lvFavoriteWords;
    private SQLiteHelper dbHelper;
    private SessionManager session;
    private List<Word> favoriteWords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_words);

        lvFavoriteWords = findViewById(R.id.lvFavoriteWords);
        dbHelper = new SQLiteHelper(this);
        session = new SessionManager(this);

        if (!session.isLoggedIn()) {
            finish();
            return;
        }

        int userId = session.getUserId();
        favoriteWords = dbHelper.getFavoriteWords(userId);

        ArrayAdapter<Word> adapter = new ArrayAdapter<Word>(this, R.layout.item_word_simple, favoriteWords) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_word_simple, parent, false);
                }

                Word word = getItem(position);
                TextView tvEnglish = convertView.findViewById(R.id.tvEnglish);
                TextView tvVietnamese = convertView.findViewById(R.id.tvVietnamese);

                tvEnglish.setText(word.getEnglish() + " (" + word.getPhonetic() + ")");
                tvVietnamese.setText(word.getVietnamese());

                return convertView;
            }
        };
        lvFavoriteWords.setAdapter(adapter);
    }
}