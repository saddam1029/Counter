package com.example.counter

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import android.content.SharedPreferences
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.example.counter.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var counter = 0
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        // Initialize SharedPreferences before any use
        sharedPreferences = getSharedPreferences("CounterPrefs", MODE_PRIVATE)

        // Set the theme before calling setContentView
        val theme = sharedPreferences.getInt("theme", AppCompatDelegate.MODE_NIGHT_NO)
        setThemeBasedOnPreference(theme)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set system bar colors
        setSystemBarColors(theme)

        counter = sharedPreferences.getInt("counter", 0)

        // Set the initial counter value
        binding.tvCount.text = counter.toString()

        binding.ivAdd.setOnClickListener {
            add()
        }

        binding.ivMinus.setOnClickListener {
            minus()
        }

        binding.rlAdd.setOnClickListener {
            add()
        }

        binding.rlMinus.setOnClickListener {
            minus()
        }

        binding.ivSetting.setOnClickListener {
            showThemeDialog()
        }

        binding.ivRestart.setOnClickListener {
            restart()
        }

        // Apply image tint based on theme
        val tintColor = if (theme == AppCompatDelegate.MODE_NIGHT_YES) {
            ContextCompat.getColor(this, R.color.white)
        } else {
            ContextCompat.getColor(this, R.color.dark)
        }

        // Apply text color based on theme
        val textColor = if (theme == AppCompatDelegate.MODE_NIGHT_YES) {
            ContextCompat.getColor(this, R.color.white)
        } else {
            ContextCompat.getColor(this, R.color.dark)
        }

        binding.tvCount.setTextColor(textColor)
        binding.ivAdd.setColorFilter(tintColor)
        binding.ivMinus.setColorFilter(tintColor)
        binding.ivSetting.setColorFilter(tintColor)
        binding.ivHelp.setColorFilter(tintColor)
        binding.ivRestart.setColorFilter(tintColor)
    }

    private fun add() {
        counter++
        updateCounterDisplay()
        saveCounter()
    }

    private fun minus(){
        if (counter > 0) {
            counter--
            binding.tvCount.text = counter.toString()
            saveCounter()
        }
    }

    private fun restart(){
        counter = 0
        updateCounterDisplay()
        saveCounter()
    }

    private fun updateCounterDisplay() {
        // Adjust text size based on counter value
        when {
            counter >= 100000 -> binding.tvCount.textSize = 90f
            counter >= 10000 -> binding.tvCount.textSize = 110f // Shrink further when counter >= 10000
            counter >= 1000 -> binding.tvCount.textSize = 130f  // Shrink when counter >= 1000
            else -> binding.tvCount.textSize = 150f  // Default size for smaller numbers
        }
        binding.tvCount.text = counter.toString()
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
            window.statusBarColor = ContextCompat.getColor(this, R.color.white)
            window.navigationBarColor = ContextCompat.getColor(this, R.color.white)
            insetsController.isAppearanceLightStatusBars = true
            insetsController.isAppearanceLightNavigationBars = true
        }
    }

    private fun showThemeDialog() {
        val builder = AlertDialog.Builder(this, getDialogTheme())
        builder.setTitle("Select Theme")
        val themes = arrayOf("Light", "Dark")
        builder.setItems(themes) { _, which ->
            when (which) {
                0 -> setAppTheme(AppCompatDelegate.MODE_NIGHT_NO)
                1 -> setAppTheme(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
        builder.show()
    }

    private fun getDialogTheme(): Int {
        return when (sharedPreferences.getInt("theme", AppCompatDelegate.MODE_NIGHT_NO)) {
            AppCompatDelegate.MODE_NIGHT_YES -> R.style.DialogTheme_Dark
            else -> R.style.DialogTheme_Light
        }
    }

    private fun setAppTheme(mode: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt("theme", mode)
        editor.apply()

        setThemeBasedOnPreference(mode)
        recreate() // Restart activity to apply theme
    }

    private fun setThemeBasedOnPreference(mode: Int) {
        when (mode) {
            AppCompatDelegate.MODE_NIGHT_NO -> setTheme(R.style.Theme_Counter_Light)
            AppCompatDelegate.MODE_NIGHT_YES -> setTheme(R.style.Theme_Counter_Dark)
            else -> setTheme(R.style.Theme_Counter)
        }
    }

    private fun saveCounter() {
        val editor = sharedPreferences.edit()
        editor.putInt("counter", counter)
        editor.apply()
    }
}
