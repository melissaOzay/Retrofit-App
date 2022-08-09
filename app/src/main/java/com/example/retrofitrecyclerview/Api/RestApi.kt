package com.example.retrofitrecyclerview.Api

import com.example.retrofitrecyclerview.DataClass.UserInfo
import retrofit2.Call
import retrofit2.http.*

interface RestApi {
    @Headers("Content-Type: application/json")
    @POST("post")
    fun addUser(@Body userData: UserInfo): Call<UserInfo>


    @GET("get")
    fun getUser():Call<List<UserInfo>>

    @DELETE("delete/{userId}")
    fun deleteUser(@Path("userId") id: Int): Call<List<UserInfo>>

    @GET("search")
    fun getMovies(@Query("name") name: String): Call<List<UserInfo>>
}