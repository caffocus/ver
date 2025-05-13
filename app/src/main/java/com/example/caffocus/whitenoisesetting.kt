package com.example.caffocus

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.caffocus.databinding.ActivityWhitenoisesettingBinding

class whitenoisesetting : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer? = null
    private var currentVolume = 50
    lateinit var binding: ActivityWhitenoisesettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityWhitenoisesettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prefs = getSharedPreferences("settings", MODE_PRIVATE)

        binding.seekBar.max = 100
        val savedVolume = prefs.getInt("volume", 50)
        currentVolume = savedVolume
        binding.seekBar.progress = savedVolume
        mediaPlayer?.setVolume(savedVolume / 100f, savedVolume / 100f)

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                applyVolume(progress)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        binding.plus.setOnClickListener {
            applyVolume(currentVolume + 5)
        }
        binding.minus.setOnClickListener {
            applyVolume(currentVolume - 5)
        }
        binding.reset.setOnClickListener {
            applyVolume(50)
        }
        binding.nosound.setOnClickListener {
            applyVolume(0)
        }

        binding.backbtn1.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
            finish()
        }

        when (prefs.getString("music", "")) {
            "whitenoise1" -> showOnlySelectedCheck(0)
            "whitenoise2" -> showOnlySelectedCheck(1)
            "rain" -> showOnlySelectedCheck(2)
            "nature" -> showOnlySelectedCheck(3)
            "cafe" -> showOnlySelectedCheck(4)
            "fire" -> showOnlySelectedCheck(5)
        }

        binding.whitenoise1.setOnClickListener {
            prefs.edit().putString("music", "whitenoise1").apply()
            playSound("whitenoise1")
            showOnlySelectedCheck(0)
        }
        binding.whitenoise2.setOnClickListener {
            prefs.edit().putString("music", "whitenoise2").apply()
            playSound("whitenoise2")
            showOnlySelectedCheck(1)
        }
        binding.rainbtn.setOnClickListener {
            prefs.edit().putString("music", "rain").apply()
            playSound("rain")
            showOnlySelectedCheck(2)
        }
        binding.naturebtn.setOnClickListener {
            prefs.edit().putString("music", "nature").apply()
            playSound("nature")
            showOnlySelectedCheck(3)
        }
        binding.cafebtn.setOnClickListener {
            prefs.edit().putString("music", "cafe").apply()
            playSound("cafe")
            showOnlySelectedCheck(4)
        }
        binding.fire.setOnClickListener {
            prefs.edit().putString("music", "fire").apply()
            playSound("fire")
            showOnlySelectedCheck(5)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun playSound(name: String) {
        val resId = resources.getIdentifier(name, "raw", packageName)
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(this, resId)?.apply {
            isLooping = false
            start()
        }
    }

    private fun applyVolume(vol: Int) {
        currentVolume = vol.coerceIn(0, 100)
        binding.seekBar.progress = currentVolume
        mediaPlayer?.setVolume(currentVolume / 100f, currentVolume / 100f)
        getSharedPreferences("settings", MODE_PRIVATE)
            .edit().putInt("volume", currentVolume).apply()
    }

    private fun showOnlySelectedCheck(index: Int) {
        val checkList = listOf(
            binding.check1,
            binding.check2,
            binding.check3,
            binding.check4,
            binding.check5,
            binding.check6
        )
        checkList.forEachIndexed { i, checkView ->
            checkView.visibility = if (i == index) View.VISIBLE else View.GONE
        }
    }
}
