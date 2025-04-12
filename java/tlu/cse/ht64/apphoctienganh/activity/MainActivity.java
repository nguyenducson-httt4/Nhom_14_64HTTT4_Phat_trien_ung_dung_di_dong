package tlu.cse.ht64.apphoctienganh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import tlu.cse.ht64.apphoctienganh.R;
import tlu.cse.ht64.apphoctienganh.utils.SessionManager;

public class MainActivity extends AppCompatActivity {
    private Button btnLessons, btnSettings, btnLogout;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLessons = findViewById(R.id.btnLessons);
        btnSettings = findViewById(R.id.btnSettings);
        btnLogout = findViewById(R.id.btnLogout);
        session = new SessionManager(this);

        if (!session.isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        btnLessons.setOnClickListener(v -> startActivity(new Intent(this, LessonsActivity.class)));
        btnSettings.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));
        btnLogout.setOnClickListener(v -> {
            session.logout();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}