package com.example.hello.demos

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.example.hello.R

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second_first)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.title = "Explicit Intent Demo"
        toolbar.setNavigationOnClickListener { finish() }

        val btnGo = findViewById<Button>(R.id.btnGoToSecond)
        btnGo.setOnClickListener {
            val intent = Intent(this, SecondTargetActivity::class.java)
            intent.putExtra("EXTRA_MESSAGE", "Hello from First Activity! 🚀")
            startActivity(intent)
        }
    }
}
