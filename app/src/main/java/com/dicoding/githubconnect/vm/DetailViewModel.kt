package com.dicoding.githubconnect.vm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubconnect.data.remote.response.DetailUserResponse
import com.dicoding.githubconnect.data.remote.retrofit.ApiConfig
import com.dicoding.githubconnect.repository.UserRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _avatar = MutableLiveData<String>()
    val avatar: LiveData<String> = _avatar

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _following = MutableLiveData<Int>()
    val following: LiveData<Int> = _following

    private val _followers = MutableLiveData<Int>()
    val followers: LiveData<Int> = _followers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        private const val TAG = "DetailViewModel"
    }

    fun findDetailUser(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _avatar.value = response.body()?.avatarUrl
                    _username.value = response.body()?.login
                    _name.value = response.body()?.name
                    _followers.value = response.body()?.followers
                    _following.value = response.body()?.following
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun saveFavorite(username: String, avatarUrl: String) {
        userRepository.setFavorite(username, avatarUrl)
    }

    fun getFavorite(username: String) = userRepository.getFavorite(username)

    fun deleteFavorite(username: String, avatarUrl: String) {
        userRepository.unsetFavorite(username, avatarUrl)
    }
}