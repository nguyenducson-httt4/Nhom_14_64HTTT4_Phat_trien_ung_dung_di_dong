package tlu.cse.ht64.apphoctienganh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import tlu.cse.ht64.apphoctienganh.R;
import tlu.cse.ht64.apphoctienganh.database.SQLiteHelper;
import tlu.cse.ht64.apphoctienganh.model.User;
import tlu.cse.ht64.apphoctienganh.utils.SessionManager;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private SQLiteHelper dbHelper;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        dbHelper = new SQLiteHelper(this);
        session = new SessionManager(this);

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            User user = dbHelper.authenticateUser(email, password);
            if (user != null) {
                session.createLoginSession(user.getId());
                Log.d("LoginActivity", "User logged in with ID: " + user.getId());
                if (user.getRole().equals("admin")) {
                    startActivity(new Intent(this, AdminActivity.class));
                } else {
                    startActivity(new Intent(this, MainActivity.class));
                }
                finish();
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        });

        tvRegister.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
    }
}