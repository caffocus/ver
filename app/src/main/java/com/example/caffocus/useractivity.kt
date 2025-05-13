package com.example.caffocus

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ToggleButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.caffocus.databinding.ActivityCalendaractivityBinding
import com.example.caffocus.databinding.ActivityTimerBinding
import com.example.caffocus.databinding.ActivityUseractivityBinding
import kotlin.jvm.java

class useractivity : AppCompatActivity() {
    lateinit var binding: ActivityUseractivityBinding
    private lateinit var prefs: SharedPreferences
    private lateinit var quoteToggle: ToggleButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        prefs = getSharedPreferences("settings", MODE_PRIVATE)

        binding = ActivityUseractivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        quoteToggle = findViewById(R.id.toggle)


        binding.toggle.isChecked = !prefs.getBoolean("show_quote", true)
        binding.toggle.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("show_quote", !isChecked).apply()
        }
        binding.timer3.setOnClickListener {
            var timerintent = Intent(this, timer::class.java)
            startActivity(timerintent)
        }
        binding.calendar3.setOnClickListener {
            var calendarintent = Intent(this, calendaractivity::class.java)
            startActivity(calendarintent)
        }


        binding.soundsetting.setOnClickListener {
            var soundsetintent = Intent(this , whitenoisesetting::class.java)
            startActivity(soundsetintent)
        }
        binding.restsoun.setOnClickListener {
            var resetintent = Intent(this, resttimesound::class.java)
            startActivity(resetintent)
        }
        binding.setting.setOnClickListener {
            var focusintent = Intent(this, focussetting::class.java)
            startActivity(focusintent)
        }


    }
}