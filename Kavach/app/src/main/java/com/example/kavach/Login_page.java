package com.example.kavach;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.os.Handler;
import android.os.Looper;

import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Login_page extends AppCompatActivity {

    private EditText UserId, Password;
    private ImageButton Login;

    private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static final String URL = "jdbc:oracle:thin:@192.168.0.10:1521:XE";
    private static final String USERNAME = "c##vishaal";
    private static final String PASSWORD = "shiva123";
    private static final String PREFS_NAME = "LoginPrefs";

    private Connection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        UserId = findViewById(R.id.UserId);
        Password = findViewById(R.id.Password);
        Login = findViewById(R.id.Login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        TextView Register = findViewById(R.id.Guest);
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGuest();
            }

            // Remove 'private' here
            void openGuest() {
                Intent intent = new Intent(Login_page.this, GuestCont.class);
                startActivity(intent);
            }
        });
    }

    private void handleLoginResult(boolean loginSuccess, String username) {
        if (loginSuccess) {
            SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isLoggedIn", true);
            editor.putString("username", username); // Store username in shared preferences
            editor.apply();

            // Login successful
            Toast.makeText(Login_page.this, "Login Successful", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Login_page.this, ContentPage.class);
            startActivity(intent);
            finish();
        } else {
            // Login failed
            Toast.makeText(Login_page.this, "Invalid login/ User not registered", Toast.LENGTH_LONG).show();
        }
    }

    private void loginUser() {
        String username = UserId.getText().toString();
        String password = Password.getText().toString();

        new Thread(() -> {
            try {
                Class.forName(DRIVER);
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM credentials WHERE Id='" + username + "' AND Password='" + password + "'");
                boolean loginSuccess = resultSet.next(); // True if login successful, false otherwise

                // Use a Handler to post the UI update
                new Handler(Looper.getMainLooper()).post(() -> handleLoginResult(loginSuccess, username));

            } catch (Exception e) {
                e.printStackTrace(); // Log the exception
                // Use a Handler to post the UI update in case of an exception
                new Handler(Looper.getMainLooper()).post(() -> handleLoginResult(false, null));
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace(); // Log the exception
                }
            }
        }).start();
    }
}