package com.example.worldstory.ducactivity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityDucSignUpBinding

class DucSignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDucSignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDucSignUpBinding.inflate(layoutInflater)
        val view=binding.root
        enableEdgeToEdge()
        setContentView(view)



    }
}