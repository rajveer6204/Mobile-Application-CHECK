package com.example.hello.demos

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.hello.R

class FabDemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fab_demo)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.title = "FAB Demo"
        toolbar.setNavigationOnClickListener { finish() }

        val fab = findViewById<FloatingActionButton>(R.id.fabAction)
        fab.setOnClickListener {
            Toast.makeText(this, "Floating Action Button Clicked! 🎉", Toast.LENGTH_SHORT).show()
        }
    }
}
