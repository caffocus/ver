package com.example.caffocus

import android.os.Bundle
import android.widget.SeekBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.caffocus.databinding.ActivityFocussettingBinding

class focussetting : AppCompatActivity() {
    lateinit var binding: ActivityFocussettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFocussettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prefs = getSharedPreferences("settings", MODE_PRIVATE)



        binding.seekBar.max = 60
        binding.seekBar2.max = 15
        val focusMinutes = prefs.getInt("focus_minutes", 25)
        val restMinutes = prefs.getInt("rest_minutes", 5)

        binding.seekBar.progress = focusMinutes
        binding.seekBar2.progress = restMinutes

        binding.studytimestatus.text = "${focusMinutes}분"
        binding.resttimestatus.text = "${restMinutes}분"

        binding.backbtn2.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        var currentRestTime = prefs.getInt("rest_minutes", 5)


        val minRest = 1
        val maxRest = 15

        binding.restplus.setOnClickListener {
            if (currentRestTime < maxRest) {
                currentRestTime++
                prefs.edit().putInt("rest_minutes", currentRestTime).apply()
                binding.seekBar2.progress = currentRestTime
                binding.resttimestatus.text = "${currentRestTime}분"
            }
        }


        binding.restminus.setOnClickListener {
            if (currentRestTime > minRest) {
                currentRestTime--
                prefs.edit().putInt("rest_minutes", currentRestTime).apply()
                binding.seekBar2.progress = currentRestTime
                binding.resttimestatus.text = "${currentRestTime}분"
            }
        }


        binding.restreset.setOnClickListener {
            currentRestTime = 5
            prefs.edit().putInt("rest_minutes", currentRestTime).apply()
            binding.seekBar2.progress = currentRestTime
            binding.resttimestatus.text = "${currentRestTime}분"
        }


        var currentFocusTime = prefs.getInt("focus_minutes", 25)


        val minFocus = 1
        val maxFocus = 60


        binding.plus.setOnClickListener {
            if (currentFocusTime < maxFocus) {
                currentFocusTime++
                prefs.edit().putInt("focus_minutes", currentFocusTime).apply()
                binding.seekBar.progress = currentFocusTime
                binding.studytimestatus.text = "${currentFocusTime}분"
            }
        }

        binding.minus.setOnClickListener {
            if (currentFocusTime > minFocus) {
                currentFocusTime--
                prefs.edit().putInt("focus_minutes", currentFocusTime).apply()
                binding.seekBar.progress = currentFocusTime
                binding.studytimestatus.text = "${currentFocusTime}분"
            }
        }


        binding.reset.setOnClickListener {
            currentFocusTime = 25
            prefs.edit().putInt("focus_minutes", currentFocusTime).apply()
            binding.seekBar.progress = currentFocusTime
            binding.studytimestatus.text = "${currentFocusTime}분"
        }

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                prefs.edit().putInt("focus_minutes", progress).apply()
                binding.studytimestatus.text = "${progress}분"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        binding.seekBar2.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                prefs.edit().putInt("rest_minutes", progress).apply()
                binding.resttimestatus.text = "${progress}분"



            }



            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}