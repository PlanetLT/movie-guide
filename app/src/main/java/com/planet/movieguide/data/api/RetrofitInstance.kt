package com.planet.movieguide.data.api

import com.planet.movieguide.core.constant.Config.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    companion object {
        val retrofit by lazy {


            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        }
        val api by lazy {
            retrofit.create(MovieApi::class.java)
        }
    }
}