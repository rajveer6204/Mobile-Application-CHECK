package com.example.hello.demos

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.example.hello.R

class FrameLayoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_frame_layout)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.title = "Frame Layout Demo"
        toolbar.setNavigationOnClickListener { finish() }
    }
}
