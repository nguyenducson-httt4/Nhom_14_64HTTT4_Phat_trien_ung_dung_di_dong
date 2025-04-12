package tlu.cse.ht64.apphoctienganh.activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import tlu.cse.ht64.apphoctienganh.R;
import tlu.cse.ht64.apphoctienganh.database.SQLiteHelper;
import tlu.cse.ht64.apphoctienganh.model.Word;
import tlu.cse.ht64.apphoctienganh.utils.SessionManager;

import java.util.List;

public class WordListActivity extends AppCompatActivity {
    private TextView tvTitle;
    private ListView lvWords;
    private SQLiteHelper dbHelper;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list);

        tvTitle = findViewById(R.id.tvTitle);
        lvWords = findViewById(R.id.lvWords);
        dbHelper = new SQLiteHelper(this);
        session = new SessionManager(this);

        String type = getIntent().getStringExtra("type");
        List<Word> wordList;
        if ("learned".equals(type)) {
            tvTitle.setText("Learned Words");
            wordList = dbHelper.getLearnedWords(session.getUserId());
        } else {
            tvTitle.setText("Favorite Words");
            wordList = dbHelper.getFavoriteWords(session.getUserId());
        }

        ArrayAdapter<Word> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, wordList);
        lvWords.setAdapter(adapter);
    }
}