package com.patika.homework4.service

interface BaseResponseHandlerInterface<T> {
    fun onSuccess(data :  T)
    fun onFailure()
}