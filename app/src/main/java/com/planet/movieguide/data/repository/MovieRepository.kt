package com.planet.movieguide.data.repository

import DBHelper
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.planet.movieguide.core.constant.Config.Companion.API_KEY
import com.planet.movieguide.data.api.RetrofitInstance
import com.planet.movieguide.data.model.Movie
import com.planet.movieguide.data.model.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieRepository(var db: DBHelper) {

     var allUpComingList: MutableLiveData<List<Result>>
     var allPopularList: MutableLiveData<List<Result>>
     var allFavouriteList: MutableLiveData<List<Movie>>

    init {
        allUpComingList= MutableLiveData<List<Result>>()
        allPopularList= MutableLiveData<List<Result>>()
        allFavouriteList= MutableLiveData<List<Movie>>()

    }
    fun getPopularMovie(page: Int = 1,progressDialog: ProgressDialog) {
        if(page!=1){
            progressDialog.show()
        }
        RetrofitInstance.api.getPopularMovies(api_key = API_KEY, page = page)
            .enqueue(object : Callback<Result> {
                override fun onResponse(call: Call<Result>, response: Response<Result>) {
                    if (response.body() != null) {
                        var arrayList = response.body()!!
                        addPopularResult(arrayList)
                        getAllResult()
                        progressDialog.dismiss()
                        Log.d("CHECKPOPULARARRAYLIST", response.body().toString())
                    }

                }

                override fun onFailure(call: Call<Result>, t: Throwable) {
                    Log.d("CHECKPOPULARARRAYLIST", t.message.toString())
                    getAllResult()
                    progressDialog.dismiss()
                }
            })
    }



    fun getUpComingMovie(page: Int = 1,progressDialog: ProgressDialog) {
        if(page!=1){
            progressDialog.show()
        }
        RetrofitInstance.api.getUpComingMovies(api_key = API_KEY, page = page)
            .enqueue(object : Callback<Result> {
                override fun onResponse(call: Call<Result>, response: Response<Result>) {
                    if (response.body() != null) {
                        var arrayList = response.body()!!
                        addUpComingResult(arrayList)
                        getAllResult()
                        progressDialog.dismiss()
                        Log.d("CHECKUPCOMINGARRAYLIST", response.body().toString())
                    }

                }

                override fun onFailure(call: Call<Result>, t: Throwable) {
                    Log.d("CHECKUPCOMINGARRAYLIST", t.message.toString())
                    getAllResult()
                    progressDialog.dismiss()
                }
            })
    }


    fun getAllResult() {

        allUpComingList.postValue(db.getAllUpComingList())
        allPopularList.postValue(db.getAllPopularList())

    }

    fun clearDataIfOnline(){
        db.clearDataIfOnline()
    }

    fun getAllFavouriteList(){
        allFavouriteList.postValue(db.getFavouriteList())
    }

    fun addFavouriteResult(movie: Movie) {
        db.addFavourite( movie)
    }

    fun removeFromFavourite(movie: Movie,context: Context){
        val result=db.deleteFromFavourite(movie.id)
        if(result){
            getAllFavouriteList()
        }
    }


    fun addUpComingResult(result: Result) {
        db.addUpComing( result)
    }

    fun addPopularResult(result: Result) {
        db.addPopular( result)
    }


}
