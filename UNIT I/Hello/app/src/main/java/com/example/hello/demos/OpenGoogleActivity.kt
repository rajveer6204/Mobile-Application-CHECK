package com.example.hello.demos

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.example.hello.R

class OpenGoogleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_google)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.title = "Open Google Demo"
        toolbar.setNavigationOnClickListener { finish() }

        val btnOpen = findViewById<Button>(R.id.btnOpenGoogle)
        btnOpen.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"))
            startActivity(intent)
        }
    }
}
