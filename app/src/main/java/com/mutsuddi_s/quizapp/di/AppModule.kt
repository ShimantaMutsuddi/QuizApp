package com.mutsuddi_s.quizapp.di

import android.content.Context
import android.content.SharedPreferences
import com.mutsuddi_s.quizapp.data.local.SharedPreferencesManager
import com.mutsuddi_s.quizapp.data.remote.ApiInterface
import com.mutsuddi_s.quizapp.utils.NetworkChecker
import com.mutsuddi_s.quizapp.utils.constant.BASE_URL
import com.mutsuddi_s.quizapp.utils.constant.PREFERENCE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun getRetrofitServiceInstance(retrofit: Retrofit): ApiInterface{
        return retrofit.create(ApiInterface::class.java)
    }

    @Singleton
    @Provides
    fun getRetrofitInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideSharedPreferencesManager(sharedPreferences: SharedPreferences): SharedPreferencesManager {
        return SharedPreferencesManager(sharedPreferences)
    }

    @Provides
    fun provideNetworkChecker(@ApplicationContext context: Context): NetworkChecker {
        return NetworkChecker(context)
    }
}