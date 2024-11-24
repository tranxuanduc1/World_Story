package com.example.worldstory

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityStartBinding
import android.os.Looper
import android.os.Handler
import android.window.SplashScreen
import com.example.worldstory.duc.ducactivity.DucUserHomeActivity
import com.example.worldstory.duc.ducutils.toActivity

class StartActivity : AppCompatActivity() {


    private lateinit var binding: ActivityStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivityStartBinding.inflate(layoutInflater)
        val view =binding.root
        setContentView(view)

        val handler= Handler(Looper.getMainLooper())
        handler.postDelayed({
            toActivity(DucUserHomeActivity::class.java)
        },2000)


    }
}