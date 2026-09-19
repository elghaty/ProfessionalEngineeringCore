package com.electricalengineeringpro.app.core

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val scrollView = ScrollView(this)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 40, 32, 40)
            setBackgroundColor(Color.rgb(8, 16, 24))
        }

        val title = TextView(this).apply {
            text = "Professional Engineering"
            textSize = 26f
            setTextColor(Color.WHITE)
            setTypeface(null, Typeface.BOLD)
            gravity = Gravity.CENTER
        }

        root.addView(
            title,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )

        val subtitle = TextView(this).apply {
            text = "Electrical Design & Calculation System"
            textSize = 15f
            setTextColor(Color.LTGRAY)
            gravity = Gravity.CENTER
            setPadding(0, 12, 0, 30)
        }

        root.addView(
            subtitle,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )

        addButton(root, "Power Calculator")
        addButton(root, "Load Calculator")
        addButton(root, "Cable Calculator")
        addButton(root, "Voltage Drop")
        addButton(root, "Short Circuit")
        addButton(root, "Breaker Selection")
        addButton(root, "Transformer")
        addButton(root, "Generator")
        addButton(root, "Motor")
        addButton(root, "Pump")
        addButton(root, "MDB")
        addButton(root, "Protection")
        addButton(root, "Network Design")
        addButton(root, "Complete Design")
        addButton(root, "Single Line Diagram")

        val footer = TextView(this).apply {
            text = "\nProfessional Engineering Core\nVersion 1.0"
            textSize = 13f
            setTextColor(Color.GRAY)
            gravity = Gravity.CENTER
            setPadding(0, 30, 0, 10)
        }

        root.addView(
            footer,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )

        scrollView.addView(root)

        setContentView(scrollView)
    }

    private fun addButton(
        parent: LinearLayout,
        text: String
    ) {

        val button = Button(this).apply {
            this.text = text
            textSize = 15f
            isAllCaps = false
            setOnClickListener {
                // Screens will be connected later.
            }
        }

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        params.setMargins(0, 6, 0, 6)

        parent.addView(button, params)
    }
}
