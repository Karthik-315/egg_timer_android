package com.skdev.eggtimer

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.slider.Slider
import com.skdev.quizapp.common.switchTheme
import com.skdev.quizapp.common.toggleView
import kotlinx.android.synthetic.main.activity_main.*
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {
    var TIMER_SLIDER_VIEW: View? = null
    val MIN_TIMER_VALUE = 0F
    val MAX_TIMER_VALUE = 20F
    val INITIAL_TIMER_VALUE = 7F
    val MIN_TO_MILLIS_FACTOR = 60000
    val TIMER_STEP_SIZE = 5000F //5 Second steps
    val decimalFormatter = DecimalFormat("00")
    var timerValue: Long = 0L
    var interval = 1000L
    var mediaPlayer: MediaPlayer = MediaPlayer()
    var isTimerRunning: Boolean = false
    lateinit var countDownTimer: CountDownTimer
    var offset = 100L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set initial values.
        TIMER_SLIDER_VIEW = setTimerSlider
        setTimerSlider.valueFrom = MIN_TIMER_VALUE * MIN_TO_MILLIS_FACTOR
        setTimerSlider.valueTo = MAX_TIMER_VALUE * MIN_TO_MILLIS_FACTOR
        setTimerSlider.value = INITIAL_TIMER_VALUE * MIN_TO_MILLIS_FACTOR
        setTimerSlider.stepSize = TIMER_STEP_SIZE

        eggTimerTextView.text = millisToMinutes(setTimerSlider.value.toLong())
        timerValue = setTimerSlider.value.toLong()
        toggleView(stopEndTimerAlertButton, "disable")

        setTimerSlider.addOnChangeListener { slider, value, fromUser ->
            timerValue = value.toLong()
            eggTimerTextView.text = millisToMinutes(timerValue)
        }
    }

    fun switchAppTheme(view: View){
        switchTheme() // Defined in CommonUtils.kt
    }

    fun startTimer(view: View){

        // Prevents start of another timer if one is already running.
        if(!isTimerRunning) {
            mediaPlayer = MediaPlayer.create(this@MainActivity, R.raw.timer_on)
            mediaPlayer.start()
            mediaPlayer.stop()
            mediaPlayer.release()
            startCountdown()
            Log.d("MAIN", "Timer started!")
        }
        else {
            Log.d("MAIN", "Timer is already running.")
        }

        toggleView(TIMER_SLIDER_VIEW!!, "disable")
    }

    private fun startCountdown(){
        countDownTimer = object: CountDownTimer(timerValue + offset, interval){
            override fun onFinish() {
                // Code to play audio
                mediaPlayer = MediaPlayer.create(this@MainActivity, R.raw.timer_complete)
                mediaPlayer.start()
                isTimerRunning = false

                toggleView(TIMER_SLIDER_VIEW!!, "enable")
                toggleView(stopEndTimerAlertButton, "enable")
            }

            override fun onTick(millisUntilFinished: Long) {
                eggTimerTextView.text = millisToMinutes(millisUntilFinished)
                isTimerRunning = true
            }
        }.start()
        toggleView(stopEndTimerAlertButton, "enable")
    }

    fun stopAlert(view: View){

        // Dont attempt to stop end alert sound unless it is playing.
        if(!isTimerRunning) {
            mediaPlayer.stop()
            mediaPlayer.release()
        }

        countDownTimer.cancel()
        isTimerRunning = false

        toggleView(stopEndTimerAlertButton, "disable")
        toggleView(TIMER_SLIDER_VIEW!!, "enable")
        setTimerSlider.value = INITIAL_TIMER_VALUE * MIN_TO_MILLIS_FACTOR
        eggTimerTextView.text = millisToMinutes(setTimerSlider.value.toLong())
        timerValue = setTimerSlider.value.toLong()

    }

    fun millisToMinutes(_millis: Long): String{

        var minutes = decimalFormatter.format((_millis / 1000 / 60).toInt())
        var seconds = decimalFormatter.format((_millis / 1000 % 60).toInt())
        var timer = "$minutes:$seconds"

        return timer
    }
}