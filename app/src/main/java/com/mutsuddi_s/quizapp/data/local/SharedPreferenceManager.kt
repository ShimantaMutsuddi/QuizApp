package com.mutsuddi_s.quizapp.data.local

import android.content.SharedPreferences
import com.mutsuddi_s.quizapp.utils.constant.HIGHEST_SCORE_KEY
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesManager @Inject constructor(private val sharedPreferences: SharedPreferences) {





    fun saveHighestScore(highestScore: Int) {
        sharedPreferences.edit().putInt(HIGHEST_SCORE_KEY, highestScore).apply()
    }

    fun getHighestScore(): Int {
        return sharedPreferences.getInt(HIGHEST_SCORE_KEY, 0)
    }
}