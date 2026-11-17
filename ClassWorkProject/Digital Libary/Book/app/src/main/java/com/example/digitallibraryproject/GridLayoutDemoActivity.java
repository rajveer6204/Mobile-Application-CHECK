package com.example.digitallibraryproject;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

public class GridLayoutDemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_layout_demo);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        int[] cardIds = {
                R.id.card1, R.id.card2, R.id.card3,
                R.id.card4, R.id.card5, R.id.card6
        };

        for (int i = 0; i < cardIds.length; i++) {
            final int index = i + 1;
            CardView card = findViewById(cardIds[i]);
            if (card != null) {
                card.setOnClickListener(v ->
                        Toast.makeText(GridLayoutDemoActivity.this, "Clicked Grid Item #" + index, Toast.LENGTH_SHORT).show()
                );
            }
        }
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
