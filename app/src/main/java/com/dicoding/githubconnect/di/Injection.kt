package com.dicoding.githubconnect.di

import android.content.Context
import com.dicoding.githubconnect.data.local.FavoriteUserDatabase
import com.dicoding.githubconnect.repository.UserRepository
import com.dicoding.githubconnect.utils.AppExecutors
import com.dicoding.githubconnect.utils.SettingPreferences
import com.dicoding.githubconnect.utils.dataStore

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val database = FavoriteUserDatabase.getInstance(context)
        val dao = database.favoriteUserDao()
        val appExecutors = AppExecutors()
        val pref = SettingPreferences.getInstance(context.dataStore)
        return UserRepository.getInstance(dao, appExecutors, pref)
    }
}