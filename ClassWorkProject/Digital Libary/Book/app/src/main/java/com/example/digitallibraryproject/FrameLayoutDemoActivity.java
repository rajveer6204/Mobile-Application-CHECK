package com.example.digitallibraryproject;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class FrameLayoutDemoActivity extends AppCompatActivity {

    private TextView textOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame_layout_demo);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        textOverlay = findViewById(R.id.textOverlay);
        Button btnToggle = findViewById(R.id.btnToggleOverlay);

        btnToggle.setOnClickListener(v -> {
            if (textOverlay.getVisibility() == View.VISIBLE) {
                textOverlay.setVisibility(View.GONE);
            } else {
                textOverlay.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
