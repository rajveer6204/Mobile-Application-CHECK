package com.example.hello.demos

import android.graphics.Color
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.appbar.MaterialToolbar
import com.example.hello.R

class SelectColorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_color)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.title = "Select Color Demo"
        toolbar.setNavigationOnClickListener { finish() }

        val container = findViewById<LinearLayout>(R.id.llColorContainer)
        val rgColors = findViewById<RadioGroup>(R.id.rgColors)

        rgColors.setOnCheckedChangeListener { _, checkedId ->
            val color = when (checkedId) {
                R.id.rbRed -> ContextCompat.getColor(this, R.color.demo_red)
                R.id.rbGreen -> ContextCompat.getColor(this, R.color.demo_green)
                R.id.rbBlue -> ContextCompat.getColor(this, R.color.demo_blue)
                R.id.rbYellow -> ContextCompat.getColor(this, R.color.demo_yellow)
                R.id.rbPurple -> ContextCompat.getColor(this, R.color.demo_purple)
                else -> ContextCompat.getColor(this, R.color.bg_light)
            }
            container.setBackgroundColor(color)
        }
    }
}
