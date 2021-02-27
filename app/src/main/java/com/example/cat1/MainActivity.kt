package com.example.cat1


import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.get
import com.example.cat1.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.navView.setNavigationItemSelectedListener(this)

        setSupportActionBar(binding.toolbar)
        var toggle: ActionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, HomeFragment()).commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, HomeFragment()).commit()
            }
            R.id.nav_history -> {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, HistoryFragment()).commit()
            }
            R.id.nav_my_profile -> {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, MyProfileFragment()).commit()
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true;
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)){
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        var inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_settings -> {
                Toast.makeText(applicationContext, "You clicked on Settings", Toast.LENGTH_SHORT)
                    .show()
                return true
            }
            R.id.menu_about -> {
                Toast.makeText(applicationContext, "You clicked on About", Toast.LENGTH_SHORT)
                    .show()
                return true
            }
            R.id.menu_Quit -> {
                Toast.makeText(applicationContext, "You clicked on Quit", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}