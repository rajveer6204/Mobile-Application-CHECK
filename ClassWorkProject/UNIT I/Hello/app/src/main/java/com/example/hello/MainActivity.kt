package com.example.hello

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hello.demos.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rvPracticals = findViewById<RecyclerView>(R.id.rvPracticals)
        rvPracticals.layoutManager = GridLayoutManager(this, 2)

        val practicalsList = listOf(
            PracticalItem(1, "About App Version", R.drawable.ic_info, AboutAppVersionActivity::class.java),
            PracticalItem(2, "Calculator", R.drawable.ic_calculator, CalculatorActivity::class.java),
            PracticalItem(3, "Checkbox Demo", R.drawable.ic_checkbox, CheckboxDemoActivity::class.java),
            PracticalItem(4, "Choose Files from File Manager", R.drawable.ic_folder, FilePickerActivity::class.java),
            PracticalItem(5, "FAB Demo", R.drawable.ic_fab, FabDemoActivity::class.java),
            PracticalItem(6, "Food Ordering", R.drawable.ic_food, FoodOrderingActivity::class.java),
            PracticalItem(7, "Fragment Demo", R.drawable.ic_fragment, FragmentDemoActivity::class.java),
            PracticalItem(8, "Frame Layout Demo", R.drawable.ic_frame, FrameLayoutActivity::class.java),
            PracticalItem(9, "Grid Layout Demo", R.drawable.ic_grid, GridLayoutActivity::class.java),
            PracticalItem(10, "Hello Page Demo", R.drawable.ic_hello, HelloPageActivity::class.java),
            PracticalItem(11, "Linear Layout Demo", R.drawable.ic_linear, LinearLayoutActivity::class.java),
            PracticalItem(12, "ListView Demo", R.drawable.ic_list, ListViewActivity::class.java),
            PracticalItem(13, "Localisation Demo", R.drawable.ic_language, LocalisationActivity::class.java),
            PracticalItem(14, "Location Page", R.drawable.ic_location, LocationActivity::class.java),
            PracticalItem(15, "Login Page Demo", R.drawable.ic_login, LoginActivity::class.java),
            PracticalItem(16, "Open Google Demo", R.drawable.ic_web, OpenGoogleActivity::class.java),
            PracticalItem(17, "Open PDF", R.drawable.ic_pdf, OpenPdfActivity::class.java),
            PracticalItem(18, "Phone Caller", R.drawable.ic_phone, PhoneCallerActivity::class.java),
            PracticalItem(19, "Second Activity", R.drawable.ic_navigate, SecondActivity::class.java),
            PracticalItem(20, "Select Color Demo", R.drawable.ic_color, SelectColorActivity::class.java),
            PracticalItem(21, "Splash Screen Demo", R.drawable.ic_splash, SplashActivity::class.java)
        )

        val adapter = PracticalAdapter(practicalsList)
        rvPracticals.adapter = adapter
    }
}
