package tlu.cse.ht64.apphoctienganh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import tlu.cse.ht64.apphoctienganh.R;
import tlu.cse.ht64.apphoctienganh.database.SQLiteHelper;
import tlu.cse.ht64.apphoctienganh.model.Word;
import tlu.cse.ht64.apphoctienganh.utils.SessionManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TestActivity extends AppCompatActivity {
    private TextView tvQuestionNumber, tvWord;
    private Button btnOption1, btnOption2, btnOption3, btnOption4;
    private SQLiteHelper dbHelper;
    private List<Word> wordList;
    private List<Word> testWords;
    private int currentQuestion = 0;
    private int score = 0;
    private int totalQuestions = 30;
    private boolean answered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        tvQuestionNumber = findViewById(R.id.tvQuestionNumber);
        tvWord = findViewById(R.id.tvWord);
        btnOption1 = findViewById(R.id.btnOption1);
        btnOption2 = findViewById(R.id.btnOption2);
        btnOption3 = findViewById(R.id.btnOption3);
        btnOption4 = findViewById(R.id.btnOption4);
        dbHelper = new SQLiteHelper(this);

        wordList = dbHelper.getAllWords(new SessionManager(this).getUserId());
        testWords = new ArrayList<>();

        // Chọn ngẫu nhiên 30 từ
        if (wordList.size() >= totalQuestions) {
            List<Word> tempList = new ArrayList<>(wordList);
            Collections.shuffle(tempList);
            testWords = tempList.subList(0, totalQuestions);
        } else {
            testWords = new ArrayList<>(wordList);
        }

        loadQuestion();

        View.OnClickListener optionListener = v -> {
            if (answered) return; // Ngăn người dùng chọn lại sau khi đã trả lời

            Button clickedButton = (Button) v;
            answered = true;
            if (clickedButton.getText().equals(testWords.get(currentQuestion).getVietnamese())) {
                score++;
                clickedButton.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
            } else {
                clickedButton.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                // Tìm và tô màu xanh cho đáp án đúng
                if (btnOption1.getText().equals(testWords.get(currentQuestion).getVietnamese())) {
                    btnOption1.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                } else if (btnOption2.getText().equals(testWords.get(currentQuestion).getVietnamese())) {
                    btnOption2.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                } else if (btnOption3.getText().equals(testWords.get(currentQuestion).getVietnamese())) {
                    btnOption3.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                } else if (btnOption4.getText().equals(testWords.get(currentQuestion).getVietnamese())) {
                    btnOption4.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                }
            }

            // Chờ 1 giây trước khi chuyển câu hỏi tiếp theo
            v.postDelayed(() -> {
                currentQuestion++;
                if (currentQuestion < testWords.size()) {
                    loadQuestion();
                } else {
                    Intent intent = new Intent(TestActivity.this, TestResultActivity.class);
                    intent.putExtra("score", score);
                    intent.putExtra("total", testWords.size());
                    startActivity(intent);
                    finish();
                }
            }, 1000);
        };

        btnOption1.setOnClickListener(optionListener);
        btnOption2.setOnClickListener(optionListener);
        btnOption3.setOnClickListener(optionListener);
        btnOption4.setOnClickListener(optionListener);
    }

    private void loadQuestion() {
        answered = false;
        tvQuestionNumber.setText("Question " + (currentQuestion + 1) + "/" + testWords.size());
        tvWord.setText(testWords.get(currentQuestion).getEnglish());

        List<String> options = new ArrayList<>();
        options.add(testWords.get(currentQuestion).getVietnamese());
        while (options.size() < 4) {
            String randomMeaning = wordList.get(new Random().nextInt(wordList.size())).getVietnamese();
            if (!options.contains(randomMeaning)) {
                options.add(randomMeaning);
            }
        }
        Collections.shuffle(options);

        btnOption1.setText(options.get(0));
        btnOption2.setText(options.get(1));
        btnOption3.setText(options.get(2));
        btnOption4.setText(options.get(3));

        // Reset màu của các nút
        btnOption1.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        btnOption2.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        btnOption3.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        btnOption4.setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }
}