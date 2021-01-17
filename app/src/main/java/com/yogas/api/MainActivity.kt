package com.yogas.api

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.yogas.api.databinding.ActivityMainBinding
import com.yogas.api.model.ListUsers
import com.yogas.api.mv.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: ListUsers
    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ListUsers()
        adapter.notifyDataSetChanged()

        binding.rvMain.layoutManager = LinearLayoutManager(this)
        binding.rvMain.adapter = adapter

        mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MainViewModel::class.java)

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            val mHandler = Handler(Looper.getMainLooper())
            override fun onQueryTextChange(newText: String?): Boolean {
                mHandler.removeCallbacksAndMessages(null)
                mHandler.postDelayed({
                    if (!newText.isNullOrBlank()) {
                        showLoading(true)
                        mainViewModel.setListUsers(newText)
                    } else {
                        adapter.clearData()
                    }
                }, 850)
                return true
            }
        })

        mainViewModel.getListUsers().observe(this, { listUsers ->
            if (listUsers != null) {
                adapter.setData(listUsers)
            }
            showLoading(false)
        })

        mainViewModel.statusError.observe(this, { status ->
            if (status != null) {
                Toast.makeText(this, status, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.change_ln -> startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBarMain.visibility = View.VISIBLE
        } else {
            binding.progressBarMain.visibility = View.GONE
        }
    }

    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        binding.searchView.clearFocus()
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, R.string.double_back_pressed_to_exit, Toast.LENGTH_SHORT).show()
        Handler(Looper.getMainLooper()).postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

    override fun onResume() {
        super.onResume()
        binding.searchView.clearFocus()
    }

}
