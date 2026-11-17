package com.example.hello.demos

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.example.hello.R

class HelloPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hello_page)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.title = "Hello Page Demo"
        toolbar.setNavigationOnClickListener { finish() }

        val btnClickMe = findViewById<Button>(R.id.btnClickMe)
        val tvMessage = findViewById<TextView>(R.id.tvMessage)

        btnClickMe.setOnClickListener {
            val msg = getString(R.string.greeting_message)
            tvMessage.text = msg
            tvMessage.visibility = View.VISIBLE
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
        }
    }
}
