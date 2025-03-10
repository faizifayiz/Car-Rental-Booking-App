package com.faizi_faiz.carrentalbookingapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class SplashActivity : AppCompatActivity() {
    private lateinit var splashImageView: ImageView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        // ✅ Apply saved theme before setting content view
        sharedPreferences = getSharedPreferences("AppSettingsPrefs", MODE_PRIVATE)
        loadTheme()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        splashImageView = findViewById(R.id.splashImageView)

        // ✅ Set correct splash screen logo based on theme
        updateSplashLogo()

        // ✅ Delay for 2 seconds and start MainActivity
        window.decorView.postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 2000) // 2-second delay
    }

    private fun loadTheme() {
        if (!sharedPreferences.contains("isDarkMode")) {
            // ✅ If the user has not manually selected a theme, follow system setting
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        } else {
            // ✅ If the user has selected a theme manually, use the saved preference
            val isDarkMode = sharedPreferences.getBoolean("isDarkMode", false)
            AppCompatDelegate.setDefaultNightMode(
                if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            )
        }
    }


    private fun updateSplashLogo() {
        if (sharedPreferences.getBoolean("isDarkMode", false)) {
            splashImageView.setImageResource(R.drawable.logo_dark) // 🌙 Dark Mode Image
        } else {
            splashImageView.setImageResource(R.drawable.logo_light) // ☀️ Light Mode Image
        }
    }
}
