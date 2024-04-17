package com.example.kavach;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UrlCheck extends AppCompatActivity {
    private static final String PREFS_NAME = "LoginPrefs";
    private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static final String URL = "jdbc:oracle:thin:@192.168.0.4:1521:XE";
    private static final String USERNAME = "c##vishaal";
    private static final String PASSWORD = "shiva123";
    private ImageView profileImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_url_check);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        profileImageView = findViewById(R.id.ProfilePic);
        fetchAndDisplayProfileImage();
    }
    public void  OpenProfile(View v) {
        Intent intent = new Intent(UrlCheck.this, ProfileNew.class);
        startActivity(intent);
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
            if (!Python.isStarted()) {
                Python.start(new AndroidPlatform(this));
            }

            // Create Python instance
            Python py = Python.getInstance();
            PyObject pyObject = py.getModule("phishing_detect"); // Replace with your Python module name

            EditText urlInput = findViewById(R.id.Url_inp);
            ImageButton checkButton = findViewById(R.id.Check_button);

            checkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get URL input from EditText
                    String url = urlInput.getText().toString();

                    // Call UrlPhishingDetector function with URL input
                    PyObject result = pyObject.callAttr("UrlPhishingDetector", url);

                    // Convert Python result to Java string
                    String prediction = result.toJava(String.class);

                    // Show the prediction result in a Toast message
                    Toast.makeText(UrlCheck.this, "Prediction: " + prediction, Toast.LENGTH_SHORT).show();
                }
            });

        ImageButton back;
        back =findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        }
    }

