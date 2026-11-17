package com.example.hello.demos

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import com.example.hello.R

class PhoneCallerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_caller)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.title = "Phone Caller Demo"
        toolbar.setNavigationOnClickListener { finish() }

        val etPhone = findViewById<TextInputEditText>(R.id.etPhoneNumber)
        val btnDial = findViewById<Button>(R.id.btnDialPhone)

        btnDial.setOnClickListener {
            val number = etPhone.text.toString().trim()
            if (number.isNotEmpty()) {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number"))
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please enter a phone number!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
