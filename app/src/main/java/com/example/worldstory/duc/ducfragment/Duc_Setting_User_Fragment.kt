package com.example.worldstory.duc.ducfragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.app.Dialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.myapplication.databinding.DialogComfirmLogoutBinding
import com.example.myapplication.databinding.FragmentDucSettingUserBinding
import com.example.worldstory.AdminMainActivity
import com.example.worldstory.StartActivity
import com.example.worldstory.duc.ducactivity.DucChapterMarkedActivity
import com.example.worldstory.duc.ducactivity.DucInfoUserActivity
import com.example.worldstory.duc.ducactivity.DucLoginActivity
import com.example.worldstory.duc.ducutils.callLog
import com.example.worldstory.duc.ducutils.clearUserSession
import com.example.worldstory.duc.ducutils.getKeyUserInfo
import com.example.worldstory.duc.ducutils.isUserCurrentAdmin
import com.example.worldstory.duc.ducutils.isUserCurrentAuthor
import com.example.worldstory.duc.ducutils.isUserCurrentMember
import com.example.worldstory.duc.ducutils.loadImgURL
import com.example.worldstory.duc.ducutils.toActivity
import com.example.worldstory.duc.ducviewmodel.DucAccountManagerViewModel
import com.example.worldstory.duc.ducviewmodelfactory.DucAccountManagerViewModelFactory
import com.example.worldstory.model.Role
import com.example.worldstory.model.User

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Duc_Setting_User_Fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Duc_Setting_User_Fragment : Fragment() {
    private val ducAccountManagerViewModel: DucAccountManagerViewModel by viewModels{
        DucAccountManagerViewModelFactory(requireContext())
    }
    private lateinit var binding: FragmentDucSettingUserBinding
    private lateinit var dialogLogout: Dialog

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentDucSettingUserBinding.inflate(layoutInflater)
        val view =binding.root


        ducAccountManagerViewModel.fetchUserSessionAndRoleByUserSession()
        ducAccountManagerViewModel.userSessionAndRole.observe(viewLifecycleOwner, Observer{
            userAndRole->


            setInfoUser(userAndRole)

        })
        setViewButton()
        setConfigButton()
        setDialogLogOut()
        return view
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_setting_user, container, false)
    }



    private fun setViewButton() {
        if(requireContext().isUserCurrentAdmin() || requireContext().isUserCurrentAuthor()){
            binding.btnSwitchToAdminSettingUserFragment.visibility= View.VISIBLE
            binding.btnLogoutSettingUserFragment.visibility= View.VISIBLE
            binding.btnLoginSettingUserFragment.visibility= View.GONE
        }else if(requireContext().isUserCurrentMember()){
            binding.btnSwitchToAdminSettingUserFragment.visibility= View.GONE
            binding.btnLogoutSettingUserFragment.visibility= View.VISIBLE
            binding.btnLoginSettingUserFragment.visibility= View.GONE

        }else{
            binding.btnSwitchToAdminSettingUserFragment.visibility= View.GONE
            binding.btnLogoutSettingUserFragment.visibility= View.GONE
            binding.btnLoginSettingUserFragment.visibility= View.VISIBLE

        }
    }

    private fun setConfigButton() {
        binding.btnLoginSettingUserFragment.setOnClickListener{
            var intent= Intent(context, DucLoginActivity::class.java)
            startActivity(intent)
        }
        binding.btnSwitchToAdminSettingUserFragment.setOnClickListener{
            requireContext().toActivity(AdminMainActivity::class.java)
        }
        binding.btnToChaptermarkedSettingUserFragment.setOnClickListener{
            requireContext().toActivity(DucChapterMarkedActivity::class.java)
        }
        binding.btnLogoutSettingUserFragment.setOnClickListener{
            //hien thi dialog
            dialogLogout.show()



        }
    }

    private fun setDialogLogOut() {
         dialogLogout= Dialog(requireContext())
        var dialogBinding=DialogComfirmLogoutBinding.inflate(layoutInflater)
        dialogLogout.apply {
            setContentView(dialogBinding.root)
            setCancelable(false)
        }
        dialogBinding.btnBackDialogComfirmLogout.setOnClickListener{
            dialogLogout.dismiss()
        }
        //xu ly dang xuat
        dialogBinding.btnLogoutDialogComfirmLogout.setOnClickListener{
           handleLogout()
        }
    }
    private fun setInfoUser(userAndRole: Pair<User, Role>) {

        binding.imgAvatarSettingUserFragment.loadImgURL(requireContext(),userAndRole.first.imgAvatar)
        binding.txtNickanmeSettingUserFragment.text=userAndRole.first.nickName
        // chuyen den tran user khi nhan anh
        binding.imgAvatarSettingUserFragment.setOnClickListener{
            requireContext().toActivity(DucInfoUserActivity::class.java, getKeyUserInfo(requireContext()),userAndRole.first)
        }
    }
    private fun handleLogout() {
        requireContext().clearUserSession()
        //quay cho ve man hinh chinh
        callLog("========","da clear")
        StartActivity.isActivityRunning=false
        requireContext().toActivity(StartActivity::class.java)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SettingUserFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Duc_Setting_User_Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}