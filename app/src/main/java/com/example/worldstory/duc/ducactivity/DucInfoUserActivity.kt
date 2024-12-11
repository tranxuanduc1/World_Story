package com.example.worldstory.duc.ducactivity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityDucInfoUserBinding
import com.example.worldstory.duc.ducutils.getKeyUserInfo
import com.example.worldstory.duc.ducutils.loadImgURL
import com.example.worldstory.model.User

class DucInfoUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDucInfoUserBinding
    private var userInfo: User?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDucInfoUserBinding.inflate(layoutInflater)
        val view =binding.root
        enableEdgeToEdge()
        setContentView(view)



        if(checkloadInfoUser()){
            loadData()
            setData()
        }
        setConfigButton()

    }
    private fun checkloadInfoUser(): Boolean {
        return (intent.hasExtra(getKeyUserInfo(this)))
    }


    private fun loadData() {
        userInfo=intent.getParcelableExtra(getKeyUserInfo(this))
    }

    private fun setData() {
        userInfo?.let { user->
            binding.txtNicknameInfoUser.text=user.nickName
            binding.imgAvatarInfoUser.loadImgURL(this,user.imgAvatar)

        }
    }


    private fun setConfigButton() {
        binding.btnBackInfoUser.setOnClickListener{
            finish()
        }
    }
}