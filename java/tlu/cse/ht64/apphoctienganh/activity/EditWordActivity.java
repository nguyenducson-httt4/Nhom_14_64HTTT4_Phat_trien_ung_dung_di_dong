package tlu.cse.ht64.apphoctienganh.activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import tlu.cse.ht64.apphoctienganh.R;
import tlu.cse.ht64.apphoctienganh.database.SQLiteHelper;
import tlu.cse.ht64.apphoctienganh.model.Word;

public class EditWordActivity extends AppCompatActivity {
    private EditText etEnglish, etVietnamese, etPhonetic;
    private Spinner spinnerCategory;
    private Button btnSave;
    private SQLiteHelper dbHelper;
    private Word word;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_word);

        etEnglish = findViewById(R.id.etEnglish);
        etVietnamese = findViewById(R.id.etVietnamese);
        etPhonetic = findViewById(R.id.etPhonetic);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnSave = findViewById(R.id.btnSave);
        dbHelper = new SQLiteHelper(this);

        // Thiết lập Spinner cho các chủ đề
        String[] categories = {"Daily", "Work", "Travel", "Education"};
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);

        word = (Word) getIntent().getSerializableExtra("word");
        if (word != null) {
            etEnglish.setText(word.getEnglish());
            etVietnamese.setText(word.getVietnamese());
            etPhonetic.setText(word.getPhonetic());
            spinnerCategory.setSelection(categoryAdapter.getPosition(word.getCategory()));
        }

        btnSave.setOnClickListener(v -> {
            String english = etEnglish.getText().toString().trim();
            String vietnamese = etVietnamese.getText().toString().trim();
            String phonetic = etPhonetic.getText().toString().trim();
            String category = spinnerCategory.getSelectedItem().toString();

            if (english.isEmpty() || vietnamese.isEmpty()) {
                Toast.makeText(this, "English and Vietnamese fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            word.setEnglish(english);
            word.setVietnamese(vietnamese);
            word.setPhonetic(phonetic);
            word.setCategory(category);

            dbHelper.updateWord(word);
            Toast.makeText(this, "Đã sửa từ", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        });
    }
}