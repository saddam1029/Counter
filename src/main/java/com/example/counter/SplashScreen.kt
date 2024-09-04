package com.example.counter

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.content.ContextCompat

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set system bar colors
        val theme = getSharedPreferences("CounterPrefs", MODE_PRIVATE).getInt("theme", AppCompatDelegate.MODE_NIGHT_NO)
        setSystemBarColors(theme)

        // Delay to show the splash screen for a while
        Handler(Looper.getMainLooper()).postDelayed({
            // Start MainActivity
            startActivity(Intent(this, MainActivity::class.java))
            // Finish SplashScreen so it's removed from the back stack
            finish()
        }, 2000) // 2000 milliseconds delay (2 seconds)
    }

    private fun setSystemBarColors(theme: Int) {
        val window = window
        val insetsController = WindowInsetsControllerCompat(window, window.decorView)
        if (theme == AppCompatDelegate.MODE_NIGHT_YES) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.dark)
            window.navigationBarColor = ContextCompat.getColor(this, R.color.dark)
            insetsController.isAppearanceLightStatusBars = false
            insetsController.isAppearanceLightNavigationBars = false
        } else {
            window.statusBarColor = ContextCompat.getColor(this, R.color.dark)
            window.navigationBarColor = ContextCompat.getColor(this, R.color.dark)
            insetsController.isAppearanceLightStatusBars = true
            insetsController.isAppearanceLightNavigationBars = true
        }
    }
}
