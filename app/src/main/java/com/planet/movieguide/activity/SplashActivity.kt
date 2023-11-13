package com.planet.movieguide.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.planet.movieguide.MainActivity
import com.planet.movieguide.R
import com.planet.movieguide.core.helper.CheckInternetConnection.Companion.isOnline
import com.planet.movieguide.data.viewModel.MovieViewModel
import com.planet.movieguide.data.viewModel.MovieViewModelProviderFactory

class SplashActivity : AppCompatActivity() {
    private lateinit var movieViewModel: MovieViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        initView()
        initEvent()
    }

    fun initView() {
        val provider = MovieViewModelProviderFactory(application)
        movieViewModel = ViewModelProvider(this, provider).get(MovieViewModel::class.java)
    }

    fun initEvent() {
        goToMainActivity()

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

    }

    fun goToMainActivity() {
        Handler(Looper.getMainLooper()).postDelayed({
            movieViewModel.observeUpComingMovieListLiveData()
                .observe(this, Observer { movieList ->
                    if (movieList.isNotEmpty()) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()

                    } else {
                        val check = isOnline(this)
                        if(check){
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }else{
                            Toast.makeText(this,"Please open your internet connection for first time",Toast.LENGTH_SHORT).show()
                            initEvent()
                        }

                    }

                })
        }, 3000)

    }
}