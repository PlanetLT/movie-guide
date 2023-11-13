package com.planet.movieguide.data.api
import com.planet.movieguide.data.model.Result
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {

    @GET("3/movie/popular")
    fun getPopularMovies(
        @Query("api_key") api_key: String,
        @Query("page") page: Int
    ):  Call<Result>

    @GET("3/movie/upcoming")
    fun getUpComingMovies(@Query("api_key") api_key: String, @Query("page") page: Int) : Call<Result>

}