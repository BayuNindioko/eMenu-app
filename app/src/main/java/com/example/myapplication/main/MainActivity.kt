package com.example.myapplication.main


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.example.myapplication.R
import com.example.myapplication.api.ApiConfig
import com.example.myapplication.data.LogoutResponse
import com.example.myapplication.setting.SettingActivity
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.login.LoginActivity
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs

        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        if (!isLoggedIn) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            tabs.setupWithViewPager(viewPager)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.setting -> {
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
            }

            R.id.signOut -> {
                val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                val token = sharedPreferences.getString("TOKEN", "")
                Log.d("bayo", "Token: $token")

                if (!token.isNullOrEmpty()) {
                    ApiConfig().getApiService().logout("Bearer $token").enqueue(object : Callback<LogoutResponse> {
                        override fun onResponse(call: Call<LogoutResponse>, response: Response<LogoutResponse>) {
                            if (response.isSuccessful) {

                                val editor = sharedPreferences.edit()
                                editor.clear()
                                editor.apply()

                                Toast.makeText(this@MainActivity, "Logout Successfully!", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                                finish()
                            } else {
                                Toast.makeText(this@MainActivity, "Logout Successfully!", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                                finish()
                                Log.d("bayo", "${response.code()}")
                                Toast.makeText(this@MainActivity, "Failed to logout. Please try again.", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<LogoutResponse>, t: Throwable) {
                            Log.e("bayo", "Logout onFailure: ${t.message}")
                            Toast.makeText(this@MainActivity, "Failed to logout. Please try again.", Toast.LENGTH_SHORT).show()
                        }
                    })
                }

//                val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
//                val editor = sharedPreferences.edit()
//                editor.clear()
//                editor.apply()
//
//
//                Toast.makeText(this, "Logout Successfully!", Toast.LENGTH_SHORT).show()
//                startActivity(Intent(this, LoginActivity::class.java))
//                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}