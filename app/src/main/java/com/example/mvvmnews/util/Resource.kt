package com.example.mvvmnews.util

sealed class Resource<T>( //type of abstract class we can define which classes are allowed to inherit from that class
        val data : T?=null,
        val message:String?=null
) {
    class Success<T>(data:T):Resource<T>(data)
    class Error<T>(message: String, data: T?=null) : Resource<T>(data,message)
    class Loading<T>:Resource<T>()
}