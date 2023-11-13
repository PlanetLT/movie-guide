package com.planet.movieguide.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.planet.movieguide.R
import com.planet.movieguide.adapter.MovieAdapter
import com.planet.movieguide.adapter.PageNumberAdapter
import com.planet.movieguide.core.helper.CheckInternetConnection
import com.planet.movieguide.data.model.Movie
import com.planet.movieguide.data.viewModel.MovieViewModel
import com.planet.movieguide.data.viewModel.MovieViewModelProviderFactory

class MovieActivity() : AppCompatActivity() {
    var totalPageNumber: Int = 0
    lateinit var label: String
    var type: Int = 0
    lateinit var recPageNumber: RecyclerView
    lateinit var recMovieItem: RecyclerView
    lateinit var tvLabel: TextView
    private lateinit var movieViewModel: MovieViewModel
    var selectedPageNumber = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)
        initView()
        initEvent()

    }

    fun initView() {
        totalPageNumber = intent.getIntExtra("total_page_number", 0)
        label = intent.getStringExtra("label").toString()
        type = intent.getIntExtra("type", 0)

        tvLabel = findViewById(R.id.tv_label)
        recPageNumber = findViewById(R.id.rec_page_number)
        recMovieItem = findViewById(R.id.rec_movie_item)
        val provider = MovieViewModelProviderFactory(application)
        movieViewModel = ViewModelProvider(this, provider).get(MovieViewModel::class.java)
    }

    fun initEvent() {
        movieViewModel.getAllResult()

        tvLabel.text = label

        prepareRecPageNumber(totalPageNumber)

        movieViewModel.observeUpComingMovieListLiveData().observe(this, Observer { resultList ->
            if (type == 1) {
                for (item in resultList) {
                    if (selectedPageNumber == item.page) {
                        recPageNumber.visibility = View.VISIBLE
                        prepareRecMovieItemAccordingWithPageNumber(item.results)
                        break
                    }
                }
            }
        })

        movieViewModel.observePopularMovieListLiveData().observe(this, Observer { resultList ->

            if (type == 2) {
                for (item in resultList) {
                    if (selectedPageNumber == item.page) {
                        recPageNumber.visibility = View.VISIBLE
                        prepareRecMovieItemAccordingWithPageNumber(item.results)
                        break
                    }
                }

            }

        })


        movieViewModel.observeFavouriteMovieListLiveData().observe(this, Observer { resultList ->
            if (type == 3) {
                recPageNumber.visibility = View.GONE

                prepareRecMovieItemAccordingWithPageNumber(resultList)
            }
        })

    }

    fun prepareRecMovieItemAccordingWithPageNumber(movieList: List<Movie>) {
        recMovieItem.layoutManager = GridLayoutManager(this, 3)
        movieViewModel.observeFavouriteMovieListLiveData().observe(this, Observer { resultList ->
            val adapter = MovieAdapter(resultList, movieList, this)
            recMovieItem.adapter = adapter
            adapter.onFavouriteClick = {
                if (type == 3) {
                    movieViewModel.removeFromFavourite(it, this)
                } else {
                    if (resultList.contains(it)) {
                        Toast.makeText(this, "already added.", Toast.LENGTH_SHORT).show()
                    } else {
                        movieViewModel.addFavouriteMovie(it, this)
                    }
                }

            }

            adapter.onItemClick = {
                val intent = Intent(this, MovieDetailActivity::class.java)
                intent.putExtra("movie_detail", it)
                startActivity(intent)

            }
        })


    }

    fun prepareRecPageNumber(totalPageNumber: Int) {
        val pageNumberList = ArrayList<Int>()
        for (i in 1..totalPageNumber) {
            pageNumberList.add(i)
        }
        recPageNumber.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true)
        val adapter = PageNumberAdapter(pageNumberList)
        recPageNumber.adapter = adapter

        adapter.onItemClick = {
            selectedPageNumber = it
            val check= CheckInternetConnection.isOnline(this)
            if(check){
                if (type == 1) {
                    movieViewModel.fetchAllUpComingMovieListAccordingWithPageNumber(it)
                } else if (type == 2) {
                    movieViewModel.fetchAllPopularMovieListAccordingWithPageNumber(it)
                }
            }else{
                Toast.makeText(this,"Please check your internet connection",Toast.LENGTH_SHORT).show()
            }

        }
    }


}