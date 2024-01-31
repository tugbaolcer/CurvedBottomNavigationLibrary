package com.tugbaolcer.curvedbottomnavigationlibrary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tugbaolcer.curvedbottomnavigationlibrary.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            bottomNavigation.apply {
                setOnNavigationItemSelectedListener (this@MainActivity)
                inflateMenu(R.menu.bottom_nav_menu)
            }
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        binding.apply {
            bottomNavigation.changedFabIcon(bottomNavigation, item)
        }
        return true
    }

}