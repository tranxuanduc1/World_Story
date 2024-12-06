package com.example.worldstory.duc.ducactivity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityDucLoginBinding
import com.example.worldstory.duc.ducutils.UserLoginStateEnum
import com.example.worldstory.duc.ducutils.numDef
import com.example.worldstory.duc.ducutils.toActivity
import com.example.worldstory.duc.ducviewmodel.DucAccountManagerViewModel
import com.example.worldstory.duc.ducviewmodelfactory.DucAccountManagerViewModelFactory
import kotlin.getValue

class DucLoginActivity : AppCompatActivity() {
    private val ducAccountManagerViewModel: DucAccountManagerViewModel by viewModels {
        DucAccountManagerViewModelFactory(this)
    }
    private lateinit var bindinng: ActivityDucLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindinng = ActivityDucLoginBinding.inflate(layoutInflater)
        val view = bindinng.root
        enableEdgeToEdge()
        setContentView(view)

        //nguoi dung bam nut
        setConfigButton()

        // sau khi co du lieu de kiem tra tai khoan
        setCheckAccount()

        // sau khi co duoc user va role tu database
        setDataUserAndRoleAfterLogin()

    }


    private fun setCheckAccount() {
        ducAccountManagerViewModel.checkAccount.observe(this, Observer { state ->
            if (state == UserLoginStateEnum.CORRECT) {
                // tai khoan juan, tiep tuc truy cap database lay user va role
                bindinng.inputLayoutPasswordLogin.error = null
                bindinng.inputLayoutUsernameLogin.error = null

                var username = bindinng.etxtUsernameLogin.text.toString()
                ducAccountManagerViewModel.fetchUserSessionAndRoleByUsername(username)
            } else if (state == UserLoginStateEnum.INCORRECT_USERNAME_OR_PASSWORD) {

                //tai khoan sai ,thong bao toi nguoi dung
                bindinng.inputLayoutPasswordLogin.error =
                    getString(R.string.incorrectUsernameOrPassword)
                bindinng.inputLayoutUsernameLogin.error = null


            } else if (state == UserLoginStateEnum.ACCOUNT_DOES_NOT_EXIST) {
                //tai khoan khong ton tai ,thong bao toi nguoi dung

                bindinng.inputLayoutUsernameLogin.error = getString(R.string.accountDoesNotExist)
                bindinng.inputLayoutPasswordLogin.error = null

            }
        })
    }

    private fun setDataUserAndRoleAfterLogin() {
        ducAccountManagerViewModel.userSessionAndRole.observe(this, Observer { userAndRole ->
            var sharedPreferences =
                getSharedPreferences(getString(R.string.key_user_session), Context.MODE_PRIVATE)
            with(sharedPreferences.edit()) {
                putInt(
                    getString(R.string.key_user_id_session),
                    userAndRole.first.userID ?: numDef
                )//kiem ko dc thi la guest
                putString(getString(R.string.key_user_role_session), userAndRole.second.roleName)
                apply()
            }
            // di chuyen toi trang chu
            var intent = Intent(this, DucUserHomeActivity::class.java)
            //xoa het activity cu
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK )
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)

            this.startActivity(intent)
            finish()
        })
    }

    private fun setConfigButton() {
        // nguoi dung bam dang ky
        bindinng.btnSignUpLogin.setOnClickListener {
            var intent = Intent(this, DucSignUpActivity::class.java)
            startActivity(intent)
        }

        // nguoi dung bam dang nhap
        bindinng.btnLoginLogin.setOnClickListener {
            var username = bindinng.etxtUsernameLogin.text.toString()
            var password = bindinng.etxtPasswordLogin.text.toString()

            //truy cap database de kiem tra tai khoan
            ducAccountManagerViewModel.fetchCheckAccountLogin(username, password)
        }

    }
}