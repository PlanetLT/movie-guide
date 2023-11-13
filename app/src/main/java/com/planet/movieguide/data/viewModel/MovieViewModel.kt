package com.planet.movieguide.data.viewModel

import DBHelper
import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.planet.movieguide.data.model.Movie
import com.planet.movieguide.data.model.Result
import com.planet.movieguide.data.repository.MovieRepository

class MovieViewModel(
    application: Application
) : AndroidViewModel(application) {

    var popularMovieList: LiveData<List<Result>>
    var upComingMovieList: LiveData<List<Result>>
    var favouriteMovieList: LiveData<List<Movie>>
    private val repository: MovieRepository

    init {
        val db = DBHelper(application.applicationContext, null)
        repository = MovieRepository(db)
        repository.getAllResult()

        popularMovieList = repository.allPopularList
        upComingMovieList = repository.allPopularList
        favouriteMovieList=repository.allFavouriteList
    }

    fun fetchAllMovieList() {
        repository.getPopularMovie()
        repository.getUpComingMovie()
        getAllResult()
    }
    fun fetchAllUpComingMovieListAccordingWithPageNumber(pageNumber:Int){
        repository.getUpComingMovie(pageNumber)
    }

    fun fetchAllPopularMovieListAccordingWithPageNumber(pageNumber:Int){
        repository.getPopularMovie(pageNumber)
    }


    fun addFavouriteMovie(movie: Movie,context: Context){
        repository.addFavouriteResult(movie)
        Toast.makeText(context,"This movie is added to Favourite List",Toast.LENGTH_SHORT).show()
        getAllResult()
    }


    fun observePopularMovieListLiveData(): LiveData<List<Result>> {
        return popularMovieList
    }

    fun observeUpComingMovieListLiveData(): LiveData<List<Result>> {
        return upComingMovieList
    }

    fun observeFavouriteMovieListLiveData(): LiveData<List<Movie>> {
        return favouriteMovieList
    }




    fun getAllResult(){
        repository.getAllResult()
        getAllFavourite()
    }

    fun removeFromFavourite(movie: Movie,context: Context){
        repository.removeFromFavourite(movie,context)
    }

    fun getAllFavourite(){
        repository.getAllFavouriteList()
    }

    fun clearDataIfOnline(){
        repository.clearDataIfOnline()
    }

}