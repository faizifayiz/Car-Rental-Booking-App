package com.faizi_faiz.carrentalbookingapp

import android.app.DatePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import java.util.Calendar

class MainActivity : NavActivity() {

    private lateinit var etPickupLocation: AutoCompleteTextView
    private lateinit var etDropoffLocation: AutoCompleteTextView
    private lateinit var etPickupDate: EditText
    private lateinit var etDropoffDate: EditText
    private lateinit var btnSearch: Button
    private lateinit var switchTheme: SwitchCompat
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var logoImageView: ImageView

    private val AFFILIATE_ID = "awesomecars" // Replace with actual affiliate ID

    override fun onCreate(savedInstanceState: Bundle?) {

        loadTheme()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        switchTheme = findViewById(R.id.switchTheme)
        etPickupLocation = findViewById(R.id.etPickupLocation)
        etDropoffLocation = findViewById(R.id.etDropoffLocation)
        etPickupDate = findViewById(R.id.etPickupDate)
        etDropoffDate = findViewById(R.id.etDropoffDate)
        btnSearch = findViewById(R.id.btnSearch)

        etPickupDate.setOnClickListener { showDatePickerDialog(etPickupDate) }
        etDropoffDate.setOnClickListener { showDatePickerDialog(etDropoffDate) }

        setupStaticAutoComplete(etPickupLocation)
        setupStaticAutoComplete(etDropoffLocation)

        btnSearch.setOnClickListener { searchOnKayak() }


        logoImageView = findViewById(R.id.logoImageView)
        sharedPreferences = getSharedPreferences("AppSettingsPrefs", MODE_PRIVATE)

        // ✅ Set switch state based on current mode
        switchTheme.isChecked = isDarkMode()

        updateLogo()

        // ✅ Toggle between Light & Dark mode
        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            val editor = sharedPreferences.edit()
            editor.putBoolean("isDarkMode", isChecked)
            editor.apply()

            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            )

            // ✅ Update switch icon dynamically
            switchTheme.setThumbResource(if (isChecked) R.drawable.ic_moon else R.drawable.ic_sun)
        }
    }

    private fun showDatePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                editText.setText("$selectedYear-${selectedMonth + 1}-$selectedDay")
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    private fun updateLogo() {
        if (isDarkMode()) {
            logoImageView.setImageResource(R.drawable.logo_dark) // 🌙 Dark Mode Image
        } else {
            logoImageView.setImageResource(R.drawable.logo_light) // ☀️ Light Mode Image
        }
    }

    private fun saveTheme(isDarkMode: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("isDarkMode", isDarkMode)
        editor.apply()
    }

    private fun loadTheme() {
        val sharedPreferences = getSharedPreferences("AppSettingsPrefs", MODE_PRIVATE)
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


    private fun isDarkMode(): Boolean {
        val sharedPreferences = getSharedPreferences("AppSettingsPrefs", MODE_PRIVATE)
        return sharedPreferences.getBoolean("isDarkMode", false)
    }

    private fun setupStaticAutoComplete(autoCompleteTextView: AutoCompleteTextView) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, LocationData.locationList)
        autoCompleteTextView.setAdapter(adapter)
    }


    private fun searchOnKayak() {
        val pickupLocation = etPickupLocation.text.toString().trim()
        val dropoffLocation = etDropoffLocation.text.toString().trim()
        val pickupDate = etPickupDate.text.toString().trim()
        val dropoffDate = etDropoffDate.text.toString().trim()

        if (pickupLocation.isEmpty() || pickupDate.isEmpty()) {
            etPickupLocation.error = "Required"
            etPickupDate.error = "Required"
            return
        }

        val kayakUrl = buildKayakUrl(pickupLocation, dropoffLocation, pickupDate, dropoffDate)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(kayakUrl))
        startActivity(intent)
    }

    fun buildKayakUrl(pickup: String, dropoff: String, pickupDate: String, dropoffDate: String): String {
        val DOMAIN = "www.kayak.com"
        val dropoffPart = if (dropoff.isNotEmpty()) dropoff else "anywhere"

        return "https://$DOMAIN/in?a=$AFFILIATE_ID&url=/cars/$pickup/$dropoffPart/$pickupDate/$dropoffDate"
    }
}
