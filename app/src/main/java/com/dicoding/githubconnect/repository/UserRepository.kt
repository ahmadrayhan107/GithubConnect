package com.dicoding.githubconnect.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.dicoding.githubconnect.data.local.FavoriteUser
import com.dicoding.githubconnect.data.local.FavoriteUserDao
import com.dicoding.githubconnect.utils.AppExecutors
import com.dicoding.githubconnect.utils.SettingPreferences

class UserRepository(
    private val favoriteUserDao: FavoriteUserDao,
    private val appExecutors: AppExecutors,
    private val pref: SettingPreferences
) {
    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            favoriteUserDao: FavoriteUserDao,
            appExecutors: AppExecutors,
            pref: SettingPreferences
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(favoriteUserDao, appExecutors, pref)
            }.also { instance = it }
    }

    fun setFavorite(username: String, avatarUrl: String) {
        appExecutors.diskIO.execute {
            val favoriteUser = FavoriteUser(username, avatarUrl)
            favoriteUserDao.insert(favoriteUser)
        }
    }

    fun getFavorite(username: String) = favoriteUserDao.getFavoriteUserByUsername(username)

    fun unsetFavorite(username: String, avatarUrl: String) {
        appExecutors.diskIO.execute {
            val favoriteUser = FavoriteUser(username, avatarUrl)
            favoriteUserDao.delete(favoriteUser)
        }
    }

    fun getFavoriteUser(): LiveData<List<FavoriteUser>> {
        return favoriteUserDao.getFavoriteUser()
    }

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        pref.saveThemeSetting(isDarkModeActive)
    }
}