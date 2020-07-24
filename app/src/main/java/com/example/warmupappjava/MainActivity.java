package com.example.warmupappjava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button rvButton = findViewById(R.id.recycler_btn);
        rvButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoArticles();
            }
        });
    }

    public void gotoArticles() {
        Intent intent = new Intent(this, ArticleListPage.class);
        startActivity(intent);
    }
}