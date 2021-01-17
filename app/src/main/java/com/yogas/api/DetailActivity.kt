package com.yogas.api

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.yogas.api.adapter.DetailUserAdapter
import com.yogas.api.databinding.ActivityDetailBinding
import com.yogas.api.model.Users
import com.yogas.api.mv.DetailViewModel

class DetailActivity : AppCompatActivity() {

    private lateinit var detailViewModel: DetailViewModel
    private lateinit var binding: ActivityDetailBinding

    companion object {
        const val DETAIL_USER = "detail_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        detailViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(DetailViewModel::class.java)

        val user = intent.getParcelableExtra<Users>(DETAIL_USER) as Users
        supportActionBar?.title = resources.getString(R.string.app_name_detail) + " @" + user.login

        Glide.with(this)
            .load(user.avatar_url)
            .apply(
                RequestOptions()
                    .error(R.drawable.ic_launcher_foreground)
                    .placeholder(R.drawable.ic_launcher_background)
            )
            .into(binding.detailAvatar)
        binding.detailLogin.text = user.login

        val detailUserPagerAdapter = DetailUserAdapter(this, supportFragmentManager)
        detailUserPagerAdapter.username = user.login
        binding.detailPager.adapter = detailUserPagerAdapter
        binding.detailTab.setupWithViewPager(binding.detailPager)

        getDetailUserData(user.login)
    }

    private fun getDetailUserData(login: String) {
        showLoading(true)
        detailViewModel.setDetailUser(login)

        detailViewModel.getDetailUser().observe(this, { detailUser ->
            if (detailUser != null) {
                binding.detailLogin.text = checkNullOrEmptyString(detailUser.name)
                binding.detailUrl.text = checkNullOrEmptyString(detailUser.html_url)

                binding.detailTab.getTabAt(0)?.text =
                    detailUser.followers.toString() + "\n" + getString(R.string.follower)
                binding.detailTab.getTabAt(1)?.text =
                    detailUser.following.toString() + "\n" + getString(R.string.following)
            }
            showLoading(false)
        })

        detailViewModel.statusError.observe(this, { status ->
            if (status != null) {
                Toast.makeText(this, status, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun checkNullOrEmptyString(text: String?): String {
        return when {
            text.isNullOrBlank() -> {
                " - "
            }
            text == "null" -> {
                " - "
            }
            else -> {
                text
            }
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBarDetail.visibility = View.VISIBLE
        } else {
            binding.progressBarDetail.visibility = View.GONE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }
}