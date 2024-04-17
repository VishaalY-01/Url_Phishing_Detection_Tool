package com.example.kavach;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

public class GuestUrlCheck extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_guest_url_check);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
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
                Toast.makeText(GuestUrlCheck.this, "Prediction: " + prediction, Toast.LENGTH_SHORT).show();
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
        ImageButton imgBtnLogin = findViewById(R.id.GutLogin);
        imgBtnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Open UrlCheck activity
                Intent intent = new Intent(GuestUrlCheck.this, Login_page.class);
                startActivity(intent);
            }
        });
    }
}

