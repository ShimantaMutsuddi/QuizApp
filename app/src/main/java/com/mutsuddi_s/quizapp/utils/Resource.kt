package com.mutsuddi_s.quizapp.utils

sealed class Resource<T>(val  data: T?=null, val errorMessage :String?=null) {
    class Loading<T> : Resource<T>()
    class Success<T>(  data: T?=null): Resource<T>(data=data)
    class Error<T>( errorMessage :String) : Resource<T>(errorMessage=errorMessage)
}