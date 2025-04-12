package tlu.cse.ht64.apphoctienganh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import tlu.cse.ht64.apphoctienganh.R;
import tlu.cse.ht64.apphoctienganh.database.SQLiteHelper;
import tlu.cse.ht64.apphoctienganh.model.User;
import tlu.cse.ht64.apphoctienganh.utils.SessionManager;

public class AdminActivity extends AppCompatActivity {
    private Button btnAddWord, btnEditWord, btnDeleteWord, btnLogout;
    private SQLiteHelper dbHelper;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        btnAddWord = findViewById(R.id.btnAddWord);
        btnEditWord = findViewById(R.id.btnEditWord);
        btnDeleteWord = findViewById(R.id.btnDeleteWord);
        btnLogout = findViewById(R.id.btnLogout);
        dbHelper = new SQLiteHelper(this);
        session = new SessionManager(this);

        if (!session.isLoggedIn()) {
            Toast.makeText(this, "Please log in to continue", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        int userId = session.getUserId();
        User user = dbHelper.getUserById(userId);
        if (user == null || !user.getRole().equals("admin")) {
            Toast.makeText(this, "You do not have admin privileges", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        btnAddWord.setOnClickListener(v -> startActivity(new Intent(this, AddWordActivity.class)));

        btnEditWord.setOnClickListener(v -> {
            Intent intent = new Intent(this, VocabularyActivity.class);
            intent.putExtra("isAdmin", true);
            intent.putExtra("deleteMode", false);
            startActivity(intent);
        });

        btnDeleteWord.setOnClickListener(v -> {
            Intent intent = new Intent(this, VocabularyActivity.class);
            intent.putExtra("isAdmin", true);
            intent.putExtra("deleteMode", true);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            session.logout();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}