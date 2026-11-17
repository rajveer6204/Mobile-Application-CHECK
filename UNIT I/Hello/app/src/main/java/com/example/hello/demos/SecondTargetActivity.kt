package com.example.hello.demos

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.example.hello.R

class SecondTargetActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second_target)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.title = "Second Activity"
        toolbar.setNavigationOnClickListener { finish() }

        val tvMsg = findViewById<TextView>(R.id.tvPassedMessage)
        val extraText = intent.getStringExtra("EXTRA_MESSAGE")
        if (!extraText.isNullOrEmpty()) {
            tvMsg.text = extraText
        }
    }
}
