package com.patika.homework4.service

import com.patika.homework4.model.LoginResponse
import com.patika.homework4.ui.recyclerview.model.ResponseTask
import com.patika.homework4.ui.recyclerview.model.ResponseTaskList
import com.patika.homework4.ui.recyclerview.model.TaskModel
import retrofit2.Call
import retrofit2.http.*

interface TodoAPI {

    @GET("user/me")
    fun getMe() : Call<LoginResponse>

    @POST("user/login")
    fun login(@Body params : MutableMap<String, Any>) : Call<LoginResponse>

    @GET("task")
    fun getAllTasks() : Call<ResponseTaskList>

    @GET("task")
    fun getTasksWithPagination(@QueryMap params : MutableMap<String, Int>) : Call<ResponseTaskList>

    @POST("task")
    fun addTask(@Body params : MutableMap<String, Any>) : Call<ResponseTask>

    @PUT("task/{path_id}")
    fun completeTask(@Path("path_id") id:String,@Body params : MutableMap<String, Any>) : Call<ResponseTask>

    @DELETE("task/{path_id}")
    fun deleteTask(@Path("path_id") id:String) : Call<ResponseTask>
}