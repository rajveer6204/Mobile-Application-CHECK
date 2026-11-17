package com.example.hello.demos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.hello.R

class DemoFragment : Fragment() {

    private var clickCount = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_demo, container, false)

        val tvCounter = view.findViewById<TextView>(R.id.tvFragmentCounter)
        val btnClick = view.findViewById<Button>(R.id.btnFragmentClick)

        btnClick.setOnClickListener {
            clickCount++
            tvCounter.text = "Button Click Count: $clickCount"
        }

        return view
    }
}
