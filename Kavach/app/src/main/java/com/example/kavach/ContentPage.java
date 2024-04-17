package com.example.kavach;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ContentPage extends AppCompatActivity {
    private boolean doubleBackToExitPressedOnce = false;
    private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static final String URL = "jdbc:oracle:thin:@192.168.0.4:1521:XE";
    private static final String USERNAME = "c##vishaal";
    private static final String PASSWORD = "shiva123";
    private static final String PREFS_NAME = "LoginPrefs";
    private ImageView profileImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_content_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        profileImageView = findViewById(R.id.ProfilePic);
        fetchAndDisplayProfileImage();
    }

    private void fetchAndDisplayProfileImage() {
        String username = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).getString("username", "");
        if (username != null) {
            new Thread(() -> {
                try {
                    Class.forName(DRIVER);
                    Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                    String sql = "SELECT profile_picture FROM credentials WHERE Id = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, username);
                    ResultSet resultSet = preparedStatement.executeQuery();

                    if (resultSet.next()) {
                        // Retrieve the image bytes from the result set
                        byte[] imageBytes = resultSet.getBytes("profile_picture");

                        // Decode the image bytes into a bitmap
                        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                        // Set the bitmap to the profile image view on the UI thread
                        runOnUiThread(() -> profileImageView.setImageBitmap(bitmap));
                    }

                    connection.close();
                } catch (Exception e) {
                    Log.e(TAG, "Error fetching profile image from database", e);
                }
            }).start();
        }


        ImageButton btnUrlAnalysis = findViewById(R.id.UrlAnalysis);
        btnUrlAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open UrlAnalysis activity
                Intent intent = new Intent(ContentPage.this, UrlAnalysis.class);
                startActivity(intent);
            }
        });
        ImageButton imgBtnUrlCheck = findViewById(R.id.UrlCheck);
        imgBtnUrlCheck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Open UrlCheck activity
                Intent intent = new Intent(ContentPage.this, UrlCheck.class);
                startActivity(intent);
            }
        });
    }

    public void  OpenProfile(View v){
        Intent intent=new Intent(ContentPage.this, ProfileNew.class);
        startActivity(intent);


        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (doubleBackToExitPressedOnce) {
                    // If already pressed once, exit the app
                    finishAffinity();
                    System.exit(0);
                } else {
                    doubleBackToExitPressedOnce = true;
                    Toast.makeText(ContentPage.this, "Press back again to exit", Toast.LENGTH_SHORT).show();

                    // Reset the flag after a delay (2 seconds)
                    new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
                }
            }
        };

        // Add the callback to the onBackPressedDispatcher
        getOnBackPressedDispatcher().addCallback(this, callback);
    }
}

