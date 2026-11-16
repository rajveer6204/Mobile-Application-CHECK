package com.example.hello.demos

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.example.hello.R

class CheckboxDemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkbox_demo)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.title = "Checkbox Demo"
        toolbar.setNavigationOnClickListener { finish() }

        val cbAndroid = findViewById<CheckBox>(R.id.cbAndroid)
        val cbWeb = findViewById<CheckBox>(R.id.cbWeb)
        val cbAI = findViewById<CheckBox>(R.id.cbAI)
        val cbDesign = findViewById<CheckBox>(R.id.cbDesign)
        val btnSubmit = findViewById<Button>(R.id.btnSubmitCheckboxes)
        val tvResult = findViewById<TextView>(R.id.tvSelectedOptions)

        btnSubmit.setOnClickListener {
            val selected = mutableListOf<String>()
            if (cbAndroid.isChecked) selected.add("Android App Development")
            if (cbWeb.isChecked) selected.add("Web Development")
            if (cbAI.isChecked) selected.add("Artificial Intelligence & ML")
            if (cbDesign.isChecked) selected.add("UI/UX Designing")

            if (selected.isEmpty()) {
                tvResult.text = "Selected Options:\nNone"
            } else {
                tvResult.text = "Selected Options:\n• " + selected.joinToString("\n• ")
            }
        }
    }
}
