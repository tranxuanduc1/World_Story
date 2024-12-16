package com.example.worldstory.view.ducactivity

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityDucInfoUserBinding
import com.example.worldstory.duc.ducadapter.Duc_StoryPostedByUser_Adapter
import com.example.worldstory.view.ducdialog.DucEditInfoUserDialogFragment
import com.example.worldstory.duc.ducutils.getKeyUserInfo
import com.example.worldstory.duc.ducutils.getUserIdSession
import com.example.worldstory.duc.ducutils.loadImgURL
import com.example.worldstory.duc.ducutils.numDef
import com.example.worldstory.view_models.ducviewmodel.DucAccountManagerViewModel
import com.example.worldstory.view_models.ducviewmodel.DucStoryViewModel
import com.example.worldstory.view_models.ducviewmodelfactory.DucAccountManagerViewModelFactory
import com.example.worldstory.view_models.ducviewmodelfactory.DucStoryViewModelFactory
import com.example.worldstory.data.model.User

class DucInfoUserActivity : AppCompatActivity() {
    private val ducStoryViewModel: DucStoryViewModel by viewModels {
        DucStoryViewModelFactory(this)
    }
    private val ducAccountManagerViewModel: DucAccountManagerViewModel by viewModels {
        DucAccountManagerViewModelFactory(this)
    }
    private lateinit var binding: ActivityDucInfoUserBinding
    private var userInfo: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDucInfoUserBinding.inflate(layoutInflater)
        val view = binding.root
        enableEdgeToEdge()
        setContentView(view)


        ducAccountManagerViewModel.userByUserId.observe(this, Observer{
            user->
            userInfo=user
            binding.swipeRefreshInfoUser.isRefreshing=false

            setData()
            userInfo?.let { user->
                user.userID?.let {
                    ducStoryViewModel.fetchStoriesByUser(it)


                }
            }


        })
        if (checkloadInfoUser()) {
            loadData()
            setData()
            loadUserPostStory()
        }
        setConfigButton()

    }


    private fun checkloadInfoUser(): Boolean {
        return (intent.hasExtra(getKeyUserInfo(this)))
    }


    private fun loadData() {
        userInfo = intent.getParcelableExtra(getKeyUserInfo(this))
        userInfo?.let { user->
            user.userID?.let {
                ducAccountManagerViewModel.fetchUserByUserId(it)

            }
        }
    }

    private fun setData() {
        userInfo?.let { user ->
            binding.txtNicknameInfoUser.text = user.nickName
            binding.imgAvatarInfoUser.loadImgURL(this, user.imgAvatar)
            if(user.userID==getUserIdSession()){
                binding.imgAvatarInfoUser.setOnClickListener{
                    DucEditInfoUserDialogFragment().show(supportFragmentManager,"")

                }
            }
        }
    }

    private fun loadUserPostStory() {
        // dat giao dien recycler
        binding.rvUserPostStoryInfoUser.apply {
            layoutManager= LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)

        }
        userInfo?.let { user ->
            //kiem stories ma user nay da post
            ducStoryViewModel.fetchStoriesByUser(user.userID ?: numDef)
            ducStoryViewModel.storiesByUser.observe(this, Observer { stories ->
                if(stories.isEmpty()){
                    binding.txtDataNotFoundInfoUser.visibility= View.VISIBLE
                }else{
                    binding.txtDataNotFoundInfoUser.visibility= View.GONE

                }
                // xoa dapter cu
                binding.rvUserPostStoryInfoUser.adapter=null
                var adapterStoryPost= Duc_StoryPostedByUser_Adapter(this,stories,user)
                binding.rvUserPostStoryInfoUser.apply {
                    adapter=adapterStoryPost
                }
            })
        }

    }

    private fun setConfigButton() {
        binding.btnBackInfoUser.setOnClickListener {
            finish()
        }
        binding.swipeRefreshInfoUser.setOnRefreshListener{
            userInfo?.let { user->
                user.userID?.let {
                    ducAccountManagerViewModel.fetchUserByUserId(it)

                }
            }
        }
    }
}