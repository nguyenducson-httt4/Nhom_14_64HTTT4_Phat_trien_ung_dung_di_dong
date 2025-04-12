package tlu.cse.ht64.apphoctienganh.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import tlu.cse.ht64.apphoctienganh.R;
import tlu.cse.ht64.apphoctienganh.database.SQLiteHelper;
import tlu.cse.ht64.apphoctienganh.model.Word;
import tlu.cse.ht64.apphoctienganh.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private EditText etSearch;
    private ListView lvSearchResults;
    private TextView tvWordDetails;
    private SQLiteHelper dbHelper;
    private SessionManager session;
    private List<Word> wordList;
    private List<Word> filteredList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        etSearch = findViewById(R.id.etSearch);
        lvSearchResults = findViewById(R.id.lvSearchResults);
        tvWordDetails = findViewById(R.id.tvWordDetails);
        dbHelper = new SQLiteHelper(this);
        session = new SessionManager(this);

        if (!session.isLoggedIn()) {
            finish();
            return;
        }

        int userId = session.getUserId();
        wordList = dbHelper.getAllWords(userId);
        filteredList = new ArrayList<>();

        ArrayAdapter<Word> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, filteredList);
        lvSearchResults.setAdapter(adapter);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim().toLowerCase();
                filteredList.clear();
                if (!query.isEmpty()) {
                    for (Word word : wordList) {
                        if (word.getEnglish().toLowerCase().contains(query)) {
                            filteredList.add(word);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        lvSearchResults.setOnItemClickListener((parent, view, position, id) -> {
            Word selectedWord = filteredList.get(position);
            String details = "English: " + selectedWord.getEnglish() + "\n" +
                    "Phonetic: " + selectedWord.getPhonetic() + "\n" +
                    "Vietnamese: " + selectedWord.getVietnamese() + "\n" +
                    "Learned: " + (selectedWord.isLearned() ? "Yes" : "No") + "\n" +
                    "Favorite: " + (selectedWord.isFavorite() ? "Yes" : "No");
            tvWordDetails.setText(details);
            tvWordDetails.setVisibility(View.VISIBLE);
        });
    }
}