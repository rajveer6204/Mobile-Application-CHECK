package com.example.hello.demos

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.example.hello.R

class ListViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_view)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.title = "ListView Demo"
        toolbar.setNavigationOnClickListener { finish() }

        val studentList = listOf(
            "Rajveer Singh - Roll #101",
            "Aarav Sharma - Roll #102",
            "Ananya Verma - Roll #103",
            "Devansh Patel - Roll #104",
            "Ishita Gupta - Roll #105",
            "Kavya Joshi - Roll #106",
            "Rohan Mehta - Roll #107",
            "Siddharth Kumar - Roll #108",
            "Tanvi Rao - Roll #109",
            "Yash Vardhan - Roll #110"
        )

        val listView = findViewById<ListView>(R.id.lvStudents)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, studentList)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedStudent = studentList[position]
            Toast.makeText(this, "Clicked: $selectedStudent", Toast.LENGTH_SHORT).show()
        }
    }
}
