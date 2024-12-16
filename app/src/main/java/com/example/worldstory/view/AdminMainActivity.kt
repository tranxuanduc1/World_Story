package com.example.worldstory.view

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.R
import com.example.worldstory.view_models.admin_viewmodels.SharedViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.activity.viewModels

import androidx.fragment.app.Fragment
import com.example.worldstory.view.admin_view_navs.GenreFragment
import com.example.worldstory.view.admin_view_navs.StoryFragment
import com.example.worldstory.view.admin_view_navs.UserFragment
import com.example.worldstory.view_models.admin_viewmodels.GenreViewModel
import com.example.worldstory.view_models.admin_viewmodels.GenreViewModelFactory
import com.example.worldstory.view_models.admin_viewmodels.RoleViewModel
import com.example.worldstory.view_models.admin_viewmodels.RoleViewModelFactory
import com.example.worldstory.view_models.admin_viewmodels.UserViewModel
import com.example.worldstory.view_models.admin_viewmodels.UserViewModelFactory
import com.example.worldstory.data.dbhelper.DatabaseHelper
import com.example.worldstory.duc.ducutils.isUserCurrentAdmin
import com.example.worldstory.duc.ducutils.isUserCurrentAuthor
import com.google.android.material.appbar.MaterialToolbar


class AdminMainActivity : AppCompatActivity() {
    private val sharedViewModel: SharedViewModel by viewModels()
    private var backPressedTime: Long = 0
    private lateinit var toast: Toast
    private val roleViewModel: RoleViewModel by viewModels {
        RoleViewModelFactory(DatabaseHelper(this))
    }

    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory(DatabaseHelper(this))
    }
    private val genreViewModel: GenreViewModel by viewModels {
        GenreViewModelFactory(DatabaseHelper(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }



        //Thiết lập sự kiện thông báo nhấn nút add
        val topAppBar: MaterialToolbar = findViewById(R.id.topAppBar)
        topAppBar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        topAppBar.setOnMenuItemClickListener() { menuitem ->
            when (menuitem.itemId) {
                R.id.add_button -> {
                    sharedViewModel.onAddButtonClicked()
                    true
                }

                R.id.search_button -> {
                    sharedViewModel.onSearch()
                    true
                }

                else -> false
            }
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        if (savedInstanceState == null) {
            if (this.isUserCurrentAdmin())
                loadFragment(UserFragment(), "user")
            else if (this.isUserCurrentAuthor()) {
                bottomNavigationView.menu.findItem(R.id.userFragment).setVisible(false)
                loadFragment(GenreFragment(), "category")
            }
        }


        val bundle = Bundle()
        val currentRole = "ADMIN"
        bundle.putString("currentRole", currentRole)
        bottomNavigationView.setOnItemSelectedListener { item ->

            when (item.itemId) {
                R.id.userFragment -> {
                    if (this.isUserCurrentAdmin()) {
                        val userFragment = UserFragment()
                        userFragment.arguments = bundle
                        loadFragment(userFragment, "user")
                        true
                    } else {
                        Toast.makeText(this, "Bạn không có quyền truy cập", Toast.LENGTH_SHORT)
                            .show()
                        false
                    }

                }

                R.id.categoryFragment -> {
                    val genreFragment = GenreFragment()
                    genreFragment.arguments = bundle
                    loadFragment(genreFragment, "category")
                    true
                }

                R.id.storyFragment -> {
                    val storyFragment = StoryFragment()
                    storyFragment.arguments = bundle
                    loadFragment(storyFragment, "story")
                    true
                }

                else -> false
            }
        }
        supportFragmentManager.addOnBackStackChangedListener {
            if (this.isUserCurrentAdmin()) {
                when (supportFragmentManager.findFragmentById(R.id.host_fragment)?.tag) {
                    "user" -> bottomNavigationView.selectedItemId = R.id.userFragment
                    "category" -> bottomNavigationView.selectedItemId = R.id.categoryFragment
                    "story" -> bottomNavigationView.selectedItemId = R.id.storyFragment
                }
            } else if (this.isUserCurrentAuthor()) {
                when (supportFragmentManager.findFragmentById(R.id.host_fragment)?.tag) {
                    "category" -> bottomNavigationView.selectedItemId = R.id.categoryFragment
                    "story" -> bottomNavigationView.selectedItemId = R.id.storyFragment
                }
            }

        }
        onBackPressedDispatcher.addCallback(this) {
            val currentTime = System.currentTimeMillis()
            if (supportFragmentManager.backStackEntryCount > 1) {
                supportFragmentManager.popBackStack()
            } else if (currentTime - backPressedTime < 2000) {
                toast.cancel()
                finish()
            } else {
                backPressedTime = currentTime
                toast =
                    Toast.makeText(
                        this@AdminMainActivity,
                        "Bấm lần nữa để thoát",
                        Toast.LENGTH_SHORT
                    )
                toast.show()
            }
        }

    }

    private fun loadFragment(fragment: Fragment, tag: String) {
        val fragmentManager = supportFragmentManager
        val existingFragment = fragmentManager.findFragmentByTag(tag)

        if (existingFragment == null) {
            // Chỉ thêm nếu chưa tồn tại
            fragmentManager.beginTransaction().apply {
                replace(R.id.host_fragment, fragment, tag)
                addToBackStack(tag)
                commit()
            }
        } else {
            // Nếu đã tồn tại, pop BackStack về fragment đó
            fragmentManager.popBackStack(tag, 0)
        }
    }

    override fun onDestroy() {
        DatabaseHelper(this).close()
        super.onDestroy()
    }

    private fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}