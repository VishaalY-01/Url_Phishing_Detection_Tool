package com.example.kavach;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ProfileNew extends AppCompatActivity {
    private static final String PREFS_NAME = "LoginPrefs";
    private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static final String URL = "jdbc:oracle:thin:@192.168.0.4:1521:XE";
    private static final String USERNAME = "c##vishaal";
    private static final String PASSWORD = "shiva123";
    private static final String TAG = "ProfileNew";
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView profileImageView;
    private ImageButton editProfilePictureButton;
    private Uri imageUri;
    private TextView userNameTextView;
    private TextView phoneTextView;
    private TextView nameTextView;

    private Connection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_new);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userNameTextView = findViewById(R.id.Email);
        phoneTextView = findViewById(R.id.Phone);
        nameTextView = findViewById(R.id.Name);

        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String username = preferences.getString("username", null);

        if (username != null) {
            fetchUserData(username);
        } else {
            // Handle the case where username is not available
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
        profileImageView = findViewById(R.id.ProfilePic);
        editProfilePictureButton = findViewById(R.id.EditBtt);

        editProfilePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        ImageButton signOutButton = findViewById(R.id.SignOut);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reset SharedPreferences data
                SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("isLoggedIn", false);
                editor.apply();

                // Navigate back to the login page
                Intent intent = new Intent(ProfileNew.this, Login_page.class);
                startActivity(intent);
                finish(); // Optional: finish the current activity to prevent going back to it on back press
            }
        });

        ImageButton back = findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            profileImageView.setImageURI(imageUri);

            // Update the profile picture in the database
            uploadImageToDatabase();
        }
    }

    private void uploadImageToDatabase() {
        new Thread(() -> {
            try {
                String username = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).getString("username", "");
                if (username == null) {
                    Log.e(TAG, "Username not found in SharedPreferences");
                    return;
                }

                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                if (inputStream == null) {
                    Log.e(TAG, "Failed to open input stream from URI");
                    return;
                }

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                }
                byte[] imageBytes = byteArrayOutputStream.toByteArray();

                // Database update code
                Class.forName(DRIVER);
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

                String sql = "UPDATE credentials SET profile_picture = ? WHERE Id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setBytes(1, imageBytes);
                preparedStatement.setString(2, username);
                preparedStatement.executeUpdate();
                connection.close();

                Log.d(TAG, "Profile picture updated successfully for user: " + username);
            } catch (Exception e) {
                Log.e(TAG, "Error uploading image to database", e);
            }
        }).start();
    }

    private void fetchUserData(String username) {
        new Thread(() -> {
            try {
                Class.forName(DRIVER);
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

                String sql = "SELECT * FROM credentials WHERE Id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, username);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    String name = resultSet.getString("name");
                    String phone = resultSet.getString("phone");
                    byte[] profilePictureBytes = resultSet.getBytes("profile_picture");

                    // Log data for debugging
                    Log.d(TAG, "Fetched user data: " + username + ", " + name + ", " + phone);

                    // Use a Handler to post the UI update
                    new Handler(Looper.getMainLooper()).post(() -> updateUI(username, name, phone, profilePictureBytes));
                } else {
                    Log.e(TAG, "No user data found for username: " + username);
                }

            } catch (Exception e) {
                Log.e(TAG, "Error fetching user data", e); // Log the exception
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error closing connection", e); // Log the exception
                }
            }
        }).start();
    }

    private void updateUI(String username, String name, String phone, byte[] profilePictureBytes) {
        userNameTextView.setText(username);
        nameTextView.setText(name);
        phoneTextView.setText(phone);

        if (profilePictureBytes != null) {
            profileImageView.setImageBitmap(BitmapFactory.decodeByteArray(profilePictureBytes, 0, profilePictureBytes.length));
        }

        // Log UI update for debugging
        Log.d(TAG, "UI updated with user data");
    }
}
