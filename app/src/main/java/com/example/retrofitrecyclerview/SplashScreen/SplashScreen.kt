package com.example.retrofitrecyclerview.SplashScreen

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.example.retrofitrecyclerview.Activity.MainActivity
import com.example.retrofitrecyclerview.R

class SplashScreen : AppCompatActivity() {
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val m = MediaPlayer.create(this, R.raw.tom)
        m.start()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        supportActionBar?.hide()
        val backgroundImage: ImageView = findViewById(R.id.driver)
        val slideAnimation = AnimationUtils.loadAnimation(this, R.xml.side_slide)
        backgroundImage.startAnimation(slideAnimation)


        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
            m.stop()

        }, 6000)


    }
}
