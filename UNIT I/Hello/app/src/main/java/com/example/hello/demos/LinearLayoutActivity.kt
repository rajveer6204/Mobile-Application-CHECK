package com.example.hello.demos

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.example.hello.R

class LinearLayoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_linear_layout)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.title = "Linear Layout Demo"
        toolbar.setNavigationOnClickListener { finish() }
    }
}
