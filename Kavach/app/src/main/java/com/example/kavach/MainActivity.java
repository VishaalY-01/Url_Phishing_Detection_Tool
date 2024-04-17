package com.example.kavach;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> tipsList;
    private ArrayList<String> displayedTipsList;
    private SharedPreferences preferences;
    private static final String PREFS_NAME = "LoginPrefs";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ImageView next_b;
        ImageView sheildImg;
        TextView text;
        TextView text2;
        TextView tip;
        TextView Tips;

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });
        tip=findViewById(R.id.tip);
        Tips=findViewById(R.id.Tips);
        next_b= findViewById(R.id.next_b);
        sheildImg=findViewById(R.id.sheildImg);
        text=findViewById(R.id.text1);
        text2=findViewById(R.id.text2);

        Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        sheildImg.startAnimation(fadeInAnimation);
        Animation fadeInAnimation1 = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        text.startAnimation(fadeInAnimation);
        Animation fadeInAnimation2 = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        text2.startAnimation(fadeInAnimation);
        Animation fadeInAnimation3 = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        next_b.startAnimation(fadeInAnimation);
        Animation fadeInAnimation4 = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        tip.startAnimation(fadeInAnimation);
        Animation fadeInAnimation5 = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Tips.startAnimation(fadeInAnimation);

        // Initialize lists
        tipsList = new ArrayList<>();
        displayedTipsList = new ArrayList<>();

        // Initialize SharedPreferences
        preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Check if the user is already logged in
        boolean isLoggedIn = preferences.getBoolean("isLoggedIn", false);
        if (isLoggedIn) {
            // User is already logged in, navigate to the main activity
            Intent intent = new Intent(this, ContentPage.class);
            startActivity(intent);
            finish(); // Optional: finish the current activity to prevent going back to it on back press
        } else {
            // User is not logged in, continue with the rest of the initialization
            // Read tips from file
            readTipsFromFile();

            // Display a random tip
            TextView tipsTextView = findViewById(R.id.tip);
            displayRandomTip(tipsTextView);
        }
    }

    private void readTipsFromFile() {
        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("cybersecurity_tips.txt");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                tipsList.add(line);
            }
            bufferedReader.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayRandomTip(TextView tipsTextView) {
        Random random = new Random();
        int index = random.nextInt(tipsList.size());
        String randomTip = tipsList.get(index);

        // Check if the tip has already been displayed
        if (!displayedTipsList.contains(randomTip)) {
            // Display the random tip
            tipsTextView.setText(randomTip);
            // Add the tip to the displayed list
            displayedTipsList.add(randomTip);
        } else {
            // If the tip has been displayed, choose another random tip recursively
            displayRandomTip(tipsTextView);
        }

        // Check if all tips have been displayed
        if (displayedTipsList.size() == tipsList.size()) {
            // Reset the displayed list to start again
            displayedTipsList.clear();
        }
    }

    public void  openActivity(View v){
        Intent intent=new Intent(this, Login_page.class);
        startActivity(intent);
    }
}
