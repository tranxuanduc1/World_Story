package com.example.worldstory.view.ducactivity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityDucSignUpBinding
import com.example.worldstory.duc.ducutils.UserLoginStateEnum
import com.example.worldstory.duc.ducutils.toActivity
import com.example.worldstory.view_models.ducviewmodel.DucAccountManagerViewModel
import com.example.worldstory.view_models.ducviewmodelfactory.DucAccountManagerViewModelFactory
import kotlin.getValue


class DucSignUpActivity : AppCompatActivity() {
    private val ducAccountManagerViewModel: DucAccountManagerViewModel by viewModels {
        DucAccountManagerViewModelFactory(this)
    }
    private lateinit var binding: ActivityDucSignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDucSignUpBinding.inflate(layoutInflater)
        val view = binding.root
        enableEdgeToEdge()
        setContentView(view)

        //nguoi dung bam nut
        setConfigButton()

        // sau khi lay duoc du lieu kiem tra ten dang nhap co ton tai hay khong
        ducAccountManagerViewModel.checkAccountExist.observe(this, Observer { check ->
            if (check == UserLoginStateEnum.USERNAME_ALREADY_EXISTS) {
                binding.inputLayoutUsernameSignup.error = getString(R.string.usernameAlreadyExists)
                binding.inputLayoutComfirmpasswordSignup.error = null
                binding.inputLayoutEmailSignup.error =null
            }
            if (check == UserLoginStateEnum.EMAIL_ALREADY_EXISTS) {
                binding.inputLayoutEmailSignup.error = getString(R.string.emailAlreadyExists)
                binding.inputLayoutComfirmpasswordSignup.error = null
                binding.inputLayoutUsernameSignup.error=null
            }
            else if (check == UserLoginStateEnum.CORRECT) {
                // neu tenn dang nhap chua ton tai, tiep tuc kiem tra mat khau bi trung
                var password = binding.etxtPasswordSignup.text.toString().trim()
                var confirmPassword = binding.etxtConfirmPasswordSignup.text.toString().trim()
                var email=binding.etxtEmailSignup.text.toString().trim()
                if (password != confirmPassword) {
                    // mat khau khac, hien canh bao
                    binding.inputLayoutComfirmpasswordSignup.error =
                        getString(R.string.comfirmPasswordDoesNotMatchPassword)
                    binding.inputLayoutUsernameSignup.error = null
                    binding.inputLayoutEmailSignup.error =null
                }else
                if (!isValidEmail(email)) {
                    //kiem tra email dung dinh dang khong
                    binding.inputLayoutEmailSignup.error = getString(R.string.emailFormatIsIncorrect)
                    binding.inputLayoutComfirmpasswordSignup.error = null
                    binding.inputLayoutUsernameSignup.error = null
                }
                else {
                    // Email đúng định dạng, đăng ký thành công
                    binding.inputLayoutEmailSignup.error = null
                    binding.inputLayoutComfirmpasswordSignup.error = null
                    binding.inputLayoutUsernameSignup.error = null


                    // dang ky thanh cong
                    //luu thong tin dang ky
                    saveDataUserSignup()

                    //chuyen huong ve man hinh dang nhap
                    toActivity(DucLoginActivity::class.java)
                    finish()
                }
            }
        })
    }

    private fun setConfigButton() {
        binding.btnSignupSignup.setOnClickListener {
            var username = binding.etxtUsernameSignup.text.toString().trim()
            var email = binding.etxtEmailSignup.text.toString().trim()

            ducAccountManagerViewModel.fetchCheckAccountExist(username,email)
        }
    }

    private fun DucSignUpActivity.saveDataUserSignup() {
        binding.inputLayoutUsernameSignup.error = null
        binding.inputLayoutComfirmpasswordSignup.error = null


        var username = binding.etxtUsernameSignup.text.toString().trim()
        var email = binding.etxtEmailSignup.text.toString().trim()
        var nickname = binding.etxtNicknameSignup.text.toString().trim()
        var password = binding.etxtPasswordSignup.text.toString().trim()

        ducAccountManagerViewModel.SignUpNewAccount(username,password,email,nickname)
    }
    fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
        return email.matches(emailRegex.toRegex())
    }
}

