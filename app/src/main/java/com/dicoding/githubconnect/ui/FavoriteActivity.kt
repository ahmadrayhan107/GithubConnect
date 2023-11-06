package com.dicoding.githubconnect.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubconnect.adapter.UserAdapter
import com.dicoding.githubconnect.data.remote.response.ItemsItem
import com.dicoding.githubconnect.databinding.ActivityFavoriteBinding
import com.dicoding.githubconnect.vm.FavoriteViewModel
import com.dicoding.githubconnect.vm.ViewModelFactory

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private val favoriteViewModel by viewModels<FavoriteViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        favoriteViewModel.getFavoriteUser().observe(this) { users ->
            val items = arrayListOf<ItemsItem>()
            users.map {
                val item = ItemsItem(login = it.username, avatarUrl = it.avatarUrl)
                items.add(item)
            }
            setFavoritesData(items)
        }

        showRecyclerList()
    }

    private fun showRecyclerList() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvFavorites.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvFavorites.addItemDecoration(itemDecoration)
    }

    private fun setFavoritesData(favorite: List<ItemsItem>) {
        val adapter = UserAdapter()
        adapter.submitList(favorite)
        binding.rvFavorites.adapter = adapter
    }
}