package tlu.cse.ht64.apphoctienganh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import tlu.cse.ht64.apphoctienganh.R;

public class TestResultActivity extends AppCompatActivity {
    private TextView tvScore;
    private Button btnBackToHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_result);

        tvScore = findViewById(R.id.tvScore);
        btnBackToHome = findViewById(R.id.btnBackToHome);

        int score = getIntent().getIntExtra("score", 0);
        int total = getIntent().getIntExtra("total", 0);
        tvScore.setText("Score: " + score + "/" + total);

        btnBackToHome.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }
}