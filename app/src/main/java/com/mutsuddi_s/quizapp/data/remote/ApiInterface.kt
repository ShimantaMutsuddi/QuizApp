package com.mutsuddi_s.quizapp.data.remote

import com.mutsuddi_s.quizapp.data.model.Quiz
import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {

    @GET("quiz.json")
    suspend fun getAllQues(): Response<Quiz>
}