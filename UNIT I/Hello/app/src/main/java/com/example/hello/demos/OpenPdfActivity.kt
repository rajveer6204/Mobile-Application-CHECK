package com.example.hello.demos

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.example.hello.R

class OpenPdfActivity : AppCompatActivity() {

    private lateinit var tvPdfDetails: TextView

    private val pdfPickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            tvPdfDetails.text = "Selected PDF:\n$uri"
        } else {
            tvPdfDetails.text = "No PDF file selected."
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_pdf)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.title = "Open PDF Demo"
        toolbar.setNavigationOnClickListener { finish() }

        tvPdfDetails = findViewById(R.id.tvPdfDetails)
        val btnSelect = findViewById<Button>(R.id.btnSelectPdf)

        btnSelect.setOnClickListener {
            pdfPickerLauncher.launch("application/pdf")
        }
    }
}
