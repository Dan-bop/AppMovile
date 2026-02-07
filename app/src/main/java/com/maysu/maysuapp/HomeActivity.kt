package com.maysu.maysuapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.maysu.maysuapp.Fragment.CatalogFragment
import com.maysu.maysuapp.Fragment.HomeFragment
import com.maysu.maysuapp.Fragment.OrdersFragment
import com.maysu.maysuapp.Fragment.SettingsFragment
import com.maysu.maysuapp.R

class HomeActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        bottomNav = findViewById(R.id.bottomNav)

        // Fragment inicial
        replaceFragment(HomeFragment())

        bottomNav.setOnItemSelectedListener {

            when(it.itemId) {

                R.id.nav_home -> replaceFragment(HomeFragment())

                R.id.nav_catalog -> replaceFragment(CatalogFragment())

                R.id.nav_orders -> replaceFragment(OrdersFragment())

                R.id.nav_settings -> replaceFragment(SettingsFragment())
            }

            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
