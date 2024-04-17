package com.example.kavach;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UrlAnalysis extends AppCompatActivity {
    private ExecutorService executorService;
    private static final String PREFS_NAME = "LoginPrefs";
    private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static final String URL = "jdbc:oracle:thin:@192.168.0.4:1521:XE";
    private static final String USERNAME = "c##vishaal";
    private static final String PASSWORD = "shiva123";
    private ImageView profileImageView;
    private TextView PhishC;
    private TextView NotPhishC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_url_analysis);

        // Handle window insets for proper layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        executorService = Executors.newSingleThreadExecutor();

        profileImageView = findViewById(R.id.ProfilePic);

        // Initialize TextViews
        PhishC = findViewById(R.id.PhishingC);
        NotPhishC = findViewById(R.id.NotPhishingC);

        // Fetch and display profile image in background
        fetchAndDisplayProfileImageInBackground();

        // Fetch and display phishing and non-phishing count in background
        fetchAndDisplayCountsInBackground();

        // Handle back button click event
        ImageButton back = findViewById(R.id.back_button);
        back.setOnClickListener(v -> finish());
    }

    private void fetchAndDisplayProfileImageInBackground() {
        executorService.execute(() -> {
            String username = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).getString("username", "");
            if (username != null) {
                try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                     PreparedStatement preparedStatement = connection.prepareStatement("SELECT profile_picture FROM credentials WHERE Id = ?")) {

                    Class.forName(DRIVER);
                    preparedStatement.setString(1, username);
                    ResultSet resultSet = preparedStatement.executeQuery();

                    if (resultSet.next()) {
                        byte[] imageBytes = resultSet.getBytes("profile_picture");
                        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                        runOnUiThread(() -> profileImageView.setImageBitmap(bitmap));
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error fetching profile image from database", e);
                }
            }
        });
    }

    private void fetchAndDisplayCountsInBackground() {
        executorService.execute(() -> {
            int phishingCount = getPhishingCount();
            int nonPhishingCount = getNotPhishingCount();
            runOnUiThread(() -> {
                displayOverallChart(phishingCount, nonPhishingCount);
                PhishC.setText(String.valueOf(phishingCount));
                NotPhishC.setText(String.valueOf(nonPhishingCount));
            });
        });
    }

    private int getPhishingCount() {
        int phishingCount = 0;
        String username = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).getString("username", "");
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM extension_data WHERE Prediction = 'Phishing' AND Id = ?")) {

            Class.forName(DRIVER);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                phishingCount = resultSet.getInt(1);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error retrieving phishing count from database", e);
        }
        return phishingCount;
    }

    private int getNotPhishingCount() {
        int NotPhishingCount = 0;
        String username = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).getString("username", "");
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM extension_data WHERE Prediction = 'No Phishing' AND Id = ?")) {

            Class.forName(DRIVER);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                NotPhishingCount = resultSet.getInt(1);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error retrieving total count from database", e);
        }
        return NotPhishingCount;
    }

    public void  OpenProfile(View v) {
        Intent intent = new Intent(UrlAnalysis.this, ProfileNew.class);
        startActivity(intent);
    }

    private void displayOverallChart(int phishingCount, int nonPhishingCount) {
        BarChart barChart = findViewById(R.id.Chart);
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, phishingCount));
        entries.add(new BarEntry(1, nonPhishingCount));

        BarDataSet dataSet = new BarDataSet(entries, "Phishing Data");
        dataSet.setColors(Color.RED, Color.GREEN);
        BarData barData = new BarData(dataSet);

        // Set the X-axis labels
        XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularity(1f); // Set granularity to 1
        xAxis.setGranularityEnabled(true); // Enable granularity
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                if (value == 0) {
                    return "Phishing";
                } else if (value == 1) {
                    return "Not Phishing";
                }
                return super.getAxisLabel(value, axis);
            }
        });

        // Configure legend entries
        Legend legend = barChart.getLegend();
        LegendEntry[] legendEntries = new LegendEntry[2];

        LegendEntry phishingEntry = new LegendEntry();
        phishingEntry.label = "Phishing";
        phishingEntry.formColor = Color.RED;
        legendEntries[0] = phishingEntry;

        LegendEntry nonPhishingEntry = new LegendEntry();
        nonPhishingEntry.label = "Not Phishing";
        nonPhishingEntry.formColor = Color.GREEN;
        legendEntries[1] = nonPhishingEntry;

        legend.setCustom(legendEntries);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP); // Set legend position to top
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER); // Center the legend horizontally
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL); // Set legend orientation to horizontal
        legend.setDrawInside(false); // Ensure legend is drawn outside the chart area
        legend.setTextSize(16f);

        barChart.setData(barData);
        barChart.invalidate(); // Refresh chart
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}
