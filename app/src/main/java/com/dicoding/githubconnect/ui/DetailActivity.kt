package com.dicoding.githubconnect.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.githubconnect.R
import com.dicoding.githubconnect.adapter.SectionPagerAdapter
import com.dicoding.githubconnect.databinding.ActivityDetailBinding
import com.dicoding.githubconnect.vm.DetailViewModel
import com.dicoding.githubconnect.vm.ViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(application)
    }

    companion object {
        const val USERNAME = "username"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val usernamePath = intent.getStringExtra(USERNAME)

        detailViewModel.findDetailUser(usernamePath!!)

        detailViewModel.avatar.observe(this) { avatar ->
            Glide.with(this)
                .load(avatar)
                .into(binding.imgAvatar)
        }

        detailViewModel.username.observe(this) { username ->
            binding.tvUsername.text = username
        }

        detailViewModel.name.observe(this) { name ->
            binding.tvName.text = name
        }

        detailViewModel.followers.observe(this) { followers ->
            binding.tvFollowers.text = "$followers Followers"
        }

        detailViewModel.following.observe(this) { following ->
            binding.tvFollowing.text = "$following Following"
        }

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        detailViewModel.getFavorite(usernamePath).observe(this) { favorite ->
            val fabFavorite = binding.fabFavorite
            if (favorite == null) {
                fabFavorite.setImageDrawable(
                    ContextCompat.getDrawable(
                        fabFavorite.context,
                        R.drawable.favorite_border
                    )
                )
            } else {
                fabFavorite.setImageDrawable(
                    ContextCompat.getDrawable(
                        fabFavorite.context,
                        R.drawable.favorite
                    )
                )
            }

            binding.fabFavorite.setOnClickListener {
                if (favorite == null) {
                    detailViewModel.saveFavorite(
                        detailViewModel.username.value.toString(),
                        detailViewModel.avatar.value.toString()
                    )
                } else {
                    detailViewModel.deleteFavorite(
                        detailViewModel.username.value.toString(),
                        detailViewModel.avatar.value.toString()
                    )
                }
            }
        }

        val sectionPagerAdapter = SectionPagerAdapter(this)
        sectionPagerAdapter.username = usernamePath!!
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tabs, position ->
            tabs.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}