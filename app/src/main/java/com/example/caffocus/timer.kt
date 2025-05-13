package com.example.caffocus

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.jvm.java

class timer : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? =null


    private lateinit var timerText: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var startPauseButton: ImageButton
    private lateinit var quoteText: TextView

    private var isRunning = false
    private var isWorkTime = true
    private var timeLeftInMillis: Long = 25 * 60 * 1000
    private var totalTimeInMillis: Long = 25 * 60 * 1000
    private var countDownTimer: CountDownTimer? = null
    private var quoteTimer: CountDownTimer? = null

    private lateinit var prefs: SharedPreferences

    private val quotes = listOf(
        "시작이 반이다.",
        "포기하지 마라. 큰일도 작은 행동에서 시작된다.",
        "공부는 미래의 너에게 보내는 선물이다.",
        "오늘 걷지 않으면 내일은 뛰어야 한다.",
        "성공은 작은 노력이 반복된 결과다."
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_timer)

        prefs = getSharedPreferences("settings", MODE_PRIVATE)

        val focusMinutes = prefs.getInt("focus_minutes", 25)
        val restMinutes = prefs.getInt("rest_minutes", 5)

        totalTimeInMillis = focusMinutes * 60 * 1000L
        timeLeftInMillis = totalTimeInMillis



        val volume = getSharedPreferences("settings", MODE_PRIVATE)
            .getInt("volume", 50) / 100f
        mediaPlayer?.setVolume(volume, volume)

        timerText = findViewById(R.id.timerText)
        progressBar = findViewById(R.id.progressBar)
        startPauseButton = findViewById(R.id.startPauseButton)
        quoteText = findViewById(R.id.quoteText)

        var calendarbtn  = findViewById<ImageButton>(R.id.calendar)
        var userbtn  = findViewById<ImageButton>(R.id.user)

        calendarbtn.setOnClickListener {
            intent = Intent(this , calendaractivity::class.java)
            startActivity(intent)
        }

        userbtn.setOnClickListener {
            intent = Intent(this, useractivity::class.java)
            startActivity(intent)

        }

        updateQuoteVisibility()
        updateTimerText()
        updateProgress()

        startPauseButton.setOnClickListener {
            if (isRunning) pauseTimer() else startTimer()
        }

        startQuoteTimer()

    }


    override fun onResume() {
        super.onResume()
        updateQuoteVisibility()
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateTimerText()
                updateProgress()
            }

            override fun onFinish() {
                isRunning = false
                startPauseButton.setImageResource(R.drawable.play)
                mediaPlayer?.pause()

                val title = if (isWorkTime) "25분 종료" else "휴식 끝"
                val message = if (isWorkTime) "5분 휴식을 시작할까요?" else "다시 집중을 시작할까요?"
                showAlert(title, message) {
                    isWorkTime = !isWorkTime
                    val focusMinutes = prefs.getInt("focus_minutes", 25)
                    val restMinutes = prefs.getInt("rest_minutes", 5)
                    totalTimeInMillis = if (isWorkTime) focusMinutes * 60 * 1000L else restMinutes * 60 * 1000L
                    timeLeftInMillis = totalTimeInMillis
                    updateTimerText()
                    updateProgress()
                    startTimer()
                }
            }
        }.start()


        val selectedMusic = if (isWorkTime) {
            prefs.getString("music", "whitenoise1")
        } else {
            prefs.getString("restmusic", "whitenoise2")
        }
        val volume = prefs.getInt("volume", 50) / 100f
        val resid = resources.getIdentifier(selectedMusic, "raw", packageName)

        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(this, resid)?.apply {
            isLooping = true
            setVolume(volume, volume)
            start()
        }


        isRunning = true
        startPauseButton.setImageResource(R.drawable.pause)
    }

    private fun pauseTimer() {
        countDownTimer?.cancel()
        isRunning = false
        isRunning = false
        mediaPlayer?.pause()
        startPauseButton.setImageResource(R.drawable.play)
    }

    private fun updateTimerText() {
        val minutes = (timeLeftInMillis / 1000) / 60
        val seconds = (timeLeftInMillis / 1000) % 60
        timerText.text = String.format("%02d:%02d", minutes, seconds)
    }

    private fun updateProgress() {
        val progress = ((totalTimeInMillis - timeLeftInMillis) / 1000).toInt()
        progressBar.max = (totalTimeInMillis / 1000).toInt()
        progressBar.progress = progress
    }

    private fun updateQuoteVisibility() {
        val showQuote = prefs.getBoolean("show_quote", true)
        if (showQuote) {
            quoteText.text = quotes.random()
            quoteText.visibility = TextView.VISIBLE
        } else {
            quoteText.visibility = TextView.GONE
        }
    }

    private fun startQuoteTimer() {
        quoteTimer?.cancel()
        if (!prefs.getBoolean("show_quote", true)) return

        quoteTimer = object : CountDownTimer(300000, 300000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                quoteText.text = quotes.random()
                startQuoteTimer()
            }
        }.start()
    }

    private fun showAlert(title: String, message: String, onConfirm: () -> Unit) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton("확인") { _, _ -> onConfirm() }
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
        quoteTimer?.cancel()
        mediaPlayer?.release()
        mediaPlayer = null
    }



}


