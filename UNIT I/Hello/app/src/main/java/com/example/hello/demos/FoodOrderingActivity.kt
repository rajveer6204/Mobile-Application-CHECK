package com.example.hello.demos

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.example.hello.R

class FoodOrderingActivity : AppCompatActivity() {

    private var qtyPizza = 0
    private var qtyBurger = 0
    private var qtyCoffee = 0

    private val pricePizza = 250
    private val priceBurger = 120
    private val priceCoffee = 80

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_ordering)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.title = "Food Ordering Demo"
        toolbar.setNavigationOnClickListener { finish() }

        val tvQtyPizza = findViewById<TextView>(R.id.tvQtyPizza)
        val tvQtyBurger = findViewById<TextView>(R.id.tvQtyBurger)
        val tvQtyCoffee = findViewById<TextView>(R.id.tvQtyCoffee)
        val tvTotalBill = findViewById<TextView>(R.id.tvTotalBill)
        val btnOrder = findViewById<Button>(R.id.btnPlaceOrder)

        fun updateSummary() {
            tvQtyPizza.text = qtyPizza.toString()
            tvQtyBurger.text = qtyBurger.toString()
            tvQtyCoffee.text = qtyCoffee.toString()
            val total = (qtyPizza * pricePizza) + (qtyBurger * priceBurger) + (qtyCoffee * priceCoffee)
            tvTotalBill.text = "Total Bill: ₹$total"
        }

        findViewById<Button>(R.id.btnPlusPizza).setOnClickListener { qtyPizza++; updateSummary() }
        findViewById<Button>(R.id.btnMinusPizza).setOnClickListener { if (qtyPizza > 0) qtyPizza--; updateSummary() }

        findViewById<Button>(R.id.btnPlusBurger).setOnClickListener { qtyBurger++; updateSummary() }
        findViewById<Button>(R.id.btnMinusBurger).setOnClickListener { if (qtyBurger > 0) qtyBurger--; updateSummary() }

        findViewById<Button>(R.id.btnPlusCoffee).setOnClickListener { qtyCoffee++; updateSummary() }
        findViewById<Button>(R.id.btnMinusCoffee).setOnClickListener { if (qtyCoffee > 0) qtyCoffee--; updateSummary() }

        btnOrder.setOnClickListener {
            val total = (qtyPizza * pricePizza) + (qtyBurger * priceBurger) + (qtyCoffee * priceCoffee)
            if (total == 0) {
                Toast.makeText(this, "Please select at least one item!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Order Placed Successfully! Total: ₹$total", Toast.LENGTH_LONG).show()
            }
        }
    }
}
