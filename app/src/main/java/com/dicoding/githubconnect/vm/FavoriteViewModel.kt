package com.dicoding.githubconnect.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubconnect.data.local.FavoriteUser
import com.dicoding.githubconnect.repository.UserRepository

class FavoriteViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getFavoriteUser(): LiveData<List<FavoriteUser>> = userRepository.getFavoriteUser()
}