package com.planet.movieguide

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.planet.movieguide.activity.MovieActivity
import com.planet.movieguide.data.viewModel.MovieViewModel
import com.planet.movieguide.data.viewModel.MovieViewModelProviderFactory


class MainActivity : AppCompatActivity() {
    private lateinit var movieViewModel: MovieViewModel
    private lateinit var btnPopular:Button
    private lateinit var btnUpcoming:Button
    private lateinit var btnFavourite:Button
    private var totalUpcomingPageNumber:Int=0
    private var totalPopularPageNumber:Int=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initEvent()
    }

    fun initView(){
        btnPopular=findViewById(R.id.btn_popular)
        btnUpcoming=findViewById(R.id.btn_upcoming)
        btnFavourite=findViewById(R.id.btn_favourite)

        val provider = MovieViewModelProviderFactory(application)
        movieViewModel = ViewModelProvider(this,provider).get(MovieViewModel::class.java)
        movieViewModel.fetchAllMovieList()

    }

    fun initEvent(){
        movieViewModel.observeUpComingMovieListLiveData().observe(this, Observer { movieList ->

            if(movieList.isNotEmpty())
            totalUpcomingPageNumber=movieList.get(0).total_pages
        })

        movieViewModel.observePopularMovieListLiveData().observe(this, Observer { movieList ->

            if(movieList.isNotEmpty())
            totalPopularPageNumber=movieList.get(0).total_pages
        })

        btnPopular.setOnClickListener {
            val intent = Intent(this, MovieActivity::class.java)
            intent.putExtra("total_page_number", totalPopularPageNumber)
            intent.putExtra("label", "Popular Movies")
            intent.putExtra("type", 2)
            startActivity(intent)
        }

        btnUpcoming.setOnClickListener {
            val intent = Intent(this, MovieActivity::class.java)
            intent.putExtra("total_page_number", totalUpcomingPageNumber)
            intent.putExtra("label", "UpComing Movies")
            intent.putExtra("type", 1)
            startActivity(intent)
        }

        btnFavourite.setOnClickListener {
            val intent = Intent(this, MovieActivity::class.java)
            intent.putExtra("total_page_number", totalPopularPageNumber)
            intent.putExtra("label", "Favourite Movies")
            intent.putExtra("type", 3)
            startActivity(intent)
        }
    }
}