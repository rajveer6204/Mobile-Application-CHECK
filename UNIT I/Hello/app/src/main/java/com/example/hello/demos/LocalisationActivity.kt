package com.example.hello.demos

import android.content.res.Configuration
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.example.hello.R
import java.util.Locale

class LocalisationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_localisation)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.title = getString(R.string.dashboard_title)
        toolbar.setNavigationOnClickListener { finish() }

        val btnEnglish = findViewById<Button>(R.id.btnLangEnglish)
        val btnHindi = findViewById<Button>(R.id.btnLangHindi)

        btnEnglish.setOnClickListener {
            setAppLocale("en")
        }

        btnHindi.setOnClickListener {
            setAppLocale("hi")
        }
    }

    private fun setAppLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration(resources.configuration)
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
        recreate()
    }
}
