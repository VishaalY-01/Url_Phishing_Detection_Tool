package com.example.kavach;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class GuestCont extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_guest_cont);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ImageButton btnUrlAnalysis = findViewById(R.id.UrlAnalysis);
        btnUrlAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open UrlAnalysis activity
                Toast.makeText(GuestCont.this,"Kindly do the REGISTRATION in the Kavach Extenison to use this feature ",Toast.LENGTH_LONG).show();
            }
        });
        ImageButton imgBtnUrlCheck = findViewById(R.id.UrlCheck);
        imgBtnUrlCheck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Open UrlCheck activity
                Intent intent = new Intent(GuestCont.this, GuestUrlCheck.class);
                startActivity(intent);
            }
        });
        ImageButton imgBtnLogin = findViewById(R.id.GutLogin);
        imgBtnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Open UrlCheck activity
                Intent intent = new Intent(GuestCont.this, Login_page.class);
                startActivity(intent);
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