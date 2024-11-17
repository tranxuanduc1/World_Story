package com.example.worldstory.ducactivity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityDucLoginBinding

class DucLoginActivity : AppCompatActivity() {
    private lateinit var bindinng: ActivityDucLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindinng= ActivityDucLoginBinding.inflate(layoutInflater)
        val view=bindinng.root
        enableEdgeToEdge()
        setContentView(view)


        bindinng.btnSignUpLogin.setOnClickListener{
            var intent= Intent(this, DucSignUpActivity::class.java)
            startActivity(intent)
        }




    }
}