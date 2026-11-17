package com.example.hello.demos

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.example.hello.R

class FilePickerActivity : AppCompatActivity() {

    private lateinit var tvFileDetails: TextView

    private val filePickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            tvFileDetails.text = "Selected File:\n$uri"
        } else {
            tvFileDetails.text = "No file selected."
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_picker)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.title = "File Picker Demo"
        toolbar.setNavigationOnClickListener { finish() }

        tvFileDetails = findViewById(R.id.tvSelectedFileDetails)
        val btnPick = findViewById<Button>(R.id.btnPickFile)

        btnPick.setOnClickListener {
            filePickerLauncher.launch("*/*")
        }
    }
}
