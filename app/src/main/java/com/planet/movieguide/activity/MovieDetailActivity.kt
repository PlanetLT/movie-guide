package com.planet.movieguide.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.planet.movieguide.R
import com.planet.movieguide.core.constant.Config
import com.planet.movieguide.data.model.Movie
import com.planet.movieguide.data.viewModel.MovieViewModel
import com.planet.movieguide.data.viewModel.MovieViewModelProviderFactory
import com.squareup.picasso.Picasso

class MovieDetailActivity : AppCompatActivity() {
    lateinit var imgThumbnail: ImageView
    lateinit var imgFavourite: ImageView
    lateinit var tvTitle: TextView
    lateinit var tvDesc: TextView
    lateinit var tvOther: TextView
    private lateinit var movieViewModel: MovieViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        initView()
        initEvent()
    }

    fun initView() {
        imgThumbnail = findViewById(R.id.img_thumbnail)
        tvTitle = findViewById(R.id.tv_title)
        tvDesc = findViewById(R.id.tv_desc)
        tvOther = findViewById(R.id.tv_other)
        imgFavourite = findViewById(R.id.img_favourite)
        val provider = MovieViewModelProviderFactory(application)
        movieViewModel = ViewModelProvider(this, provider).get(MovieViewModel::class.java)
    }

    fun initEvent() {
        movieViewModel.getAllFavourite()
        val movie = intent.getSerializableExtra("movie_detail") as Movie
        movieViewModel.observeFavouriteMovieListLiveData().observe(this, Observer { resultList ->
            if (resultList.contains(movie)) {
                imgFavourite.setBackgroundResource(R.drawable.favorite)
            } else {
                imgFavourite.setBackgroundResource(R.drawable.un_favourite)
            }

            imgFavourite.setOnClickListener {
                if (resultList.contains(movie)) {
                    Toast.makeText(this, "already added.", Toast.LENGTH_SHORT).show()
                } else {
                    movieViewModel.addFavouriteMovie(movie, this)
                }
            }
        })


        Picasso.with(this).load(Config.IMG_PREFIX + movie.backdropPath).fit().centerCrop()
            .placeholder(R.drawable.movie_place_holder)
            .error(R.drawable.movie_place_holder)
            .into(imgThumbnail)
        tvTitle.text = movie.title
        tvDesc.text = movie.overview
        tvOther.text =
            "popularity:" + movie.popularity + "\nrelease_date:" + movie.releaseDate + "\nvote_average:" + movie.voteAverage + "\nvote_count" + movie.voteCount


    }
}