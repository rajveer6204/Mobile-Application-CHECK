package com.example.hello.demos

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import com.example.hello.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.title = "Login Page Demo"
        toolbar.setNavigationOnClickListener { finish() }

        val etUser = findViewById<TextInputEditText>(R.id.etUsername)
        val etPass = findViewById<TextInputEditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLoginSubmit)

        btnLogin.setOnClickListener {
            val user = etUser.text.toString().trim()
            val pass = etPass.text.toString().trim()

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Welcome, $user! Login Successful. 🎉", Toast.LENGTH_LONG).show()
            }
        }
    }
}
