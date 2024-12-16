package com.example.worldstory.view.ducactivity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityForgotPasswordBinding
import com.example.worldstory.duc.ducutils.UserLoginStateEnum
import com.example.worldstory.duc.ducutils.hashPassword
import com.example.worldstory.duc.ducutils.toActivity
import com.example.worldstory.view_models.ducviewmodel.DucAccountManagerViewModel
import com.example.worldstory.view_models.ducviewmodelfactory.DucAccountManagerViewModelFactory
import kotlin.getValue

class DucForgotPasswordActivity : AppCompatActivity() {
    private val ducAccountManagerViewModel: DucAccountManagerViewModel by viewModels{
        DucAccountManagerViewModelFactory(this)
    }
    private lateinit var binding : ActivityForgotPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityForgotPasswordBinding.inflate(layoutInflater)
        val view= binding.root
        enableEdgeToEdge()
        setContentView(view)


        setChangePassword()
        setConfigButton()




    }

    private fun setChangePassword() {
        ducAccountManagerViewModel.checkEmailExist.observe(this, Observer{
            check->
            if(check.first== UserLoginStateEnum.EMAIL_DOES_NOT_EXISTS){
                //kiem tra email co ton tai khong
                binding.inputLayoutEmailForgotPassword.error=getString(R.string.emailDoesNotExists)
                binding.inputLayoutComfirmpasswordForgotPassword.error=null
            }else{
                //kiem tra password co giong nhau khong
                var password = binding.etxtPasswordForgotPassword.text.toString().trim()
                var confirmPassword = binding.etxtConfirmPasswordForgotPassword.text.toString().trim()
                if (password != confirmPassword) {
                    // mat khau khac, hien canh bao
                    binding.inputLayoutEmailForgotPassword.error=null
                    binding.inputLayoutComfirmpasswordForgotPassword.error=getString(R.string.comfirmPasswordDoesNotMatchPassword)
                }else{
                    // kiem tra xong
                    binding.inputLayoutEmailForgotPassword.error=null
                    binding.inputLayoutComfirmpasswordForgotPassword.error=null

                    var user=check.second
                    user?.hashedPw= hashPassword(password)
                    user?.let {
                        ducAccountManagerViewModel.updatePassword(it)

                    }

                    toActivity(DucLoginActivity::class.java)
                }
            }
        })
    }

    private fun setConfigButton() {
        binding.btnChangePasswordForgotPassword.setOnClickListener{
            var email=binding.etxtEmailForgotPassword.text.toString().trim()
            ducAccountManagerViewModel.fetchCheckEmailExist(email)
        }
    }

}