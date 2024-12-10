package com.example.worldstory.duc.ducactivity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityDucInfoUserBinding

class DucInfoUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDucInfoUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDucInfoUserBinding.inflate(layoutInflater)
        val view =binding.root
        enableEdgeToEdge()
        setContentView(view)

        binding.btnBackInfoUser.setOnClickListener{
            finish()
        }

    }
}