package com.example.hello.demos

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.example.hello.R

class AboutAppVersionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_app_version)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.title = "About App Version"
        toolbar.setNavigationOnClickListener { finish() }

        val tvAppName = findViewById<TextView>(R.id.tvAppName)
        val tvVersion = findViewById<TextView>(R.id.tvVersion)
        val tvPackageName = findViewById<TextView>(R.id.tvPackageName)

        val appName = getString(R.string.app_name)
        val packageName = packageName

        var versionName = "1.0"
        try {
            val pInfo = packageManager.getPackageInfo(packageName, 0)
            versionName = pInfo.versionName ?: "1.0"
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        tvAppName.text = appName
        tvVersion.text = "Version: $versionName"
        tvPackageName.text = "Package: $packageName"
    }
}
