package com.example.worldstory.dat.admin_viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import org.mindrot.jbcrypt.BCrypt
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.worldstory.dbhelper.DatabaseHelper
import com.example.worldstory.duc.SampleDataStory
import com.example.worldstory.duc.ducutils.dateTimeNow
import com.example.worldstory.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class UserViewModel(val db: DatabaseHelper) : ViewModel() {
    private val _users = MutableLiveData<List<User>>()
    private val tempUsers = mutableListOf<User>()
    val users: LiveData<List<User>> get() = _users
    val userName = MutableLiveData<String>()
    val passWord = MutableLiveData<String>()
    val nickName = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val avtId = mutableListOf<String>()
    var roleID: Int = -1
    var roleSeclected = 1

    private val _roleMap = MutableLiveData<Map<Int, List<User>>>()
    val roleMap: LiveData<Map<Int, List<User>>> get() = _roleMap

    private val tempUserList = mutableListOf<User>()
    private val _userList = MutableLiveData<List<User>>()
    val userList: LiveData<List<User>> get() = _userList

    init {
//        fetchAllUsers()
//        setUsersByRoleId(roleSeclected)
    }

    fun fetchAllUsers() {
        tempUsers.clear()
        tempUsers.addAll(db.getAllUsers())
        _users.value = tempUsers
    }


    fun setUsersByRoleId(id: Int) {
        tempUserList.clear()

        viewModelScope.launch {
            // Chạy việc truy xuất dữ liệu từ database trên Dispatchers.IO
            val tempUserList = withContext(Dispatchers.IO) {
                val tempList = mutableListOf<User>()
                tempList.addAll(db.getUserByRoleID(id))
                tempList
            }

            // Cập nhật `_roleMap` trước
            _roleMap.value = mapOf(id to tempUserList)

            // Sau đó cập nhật `_userList`
            _userList.value = tempUserList
        }
//
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                tempUserList.addAll(db.getUserByRoleID(id))
//                Log.w("sizev", tempUserList.toString())
//                Log.w("else", "y")
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//            withContext(Dispatchers.Main) {
//                    _roleMap.value = mapOf(id to tempUserList)
//                    _userList.value = tempUserList
//                Log.w("sizu", userList.value.toString())
//            }
//
//        }

    }


    fun onAddUser(): Long {
        Log.w("id", avtId[0])
        val hashedpw = hashPassword(password = passWord.value.toString())
        val user = User(
            null,
            userName.value.toString(),
            hashedpw,
            email.value.toString(),
            transform(avtId.first().toString()),
            nickName.value.toString(),
            roleID,
            dateTimeNow()
        )
        val l: Long = db.insertUser(user)
        resetValue()
        setUsersByRoleId(roleSeclected)
        return l
    }

    fun deleteAllUser() {
        db.deleteAllUser()
    }


    fun insertUser(user: User) {
        db.insertUser(user)
        fetchAllUsers()
    }

    fun updatetUserRole(u: User?) {
        val user = User(
            userID = u?.userID,
            userName = u?.userName ?: "",
            nickName = u?.nickName ?: "",
            hashedPw = u?.hashedPw ?: "",
            email = u?.email ?: "",
            roleID = roleID,
            createdDate = u?.createdDate ?: "",
            imgAvatar = u?.imgAvatar ?: ""
        )
        db.updateUserRole(user)
        resetValue()
        setUsersByRoleId(roleSeclected)
    }

    fun updatetUserPw(u: User?) {
        val user = User(
            userID = u?.userID,
            userName = u?.userName ?: "",
            nickName = u?.nickName ?: "",
            hashedPw = hashPassword(password = passWord.toString()),
            email = u?.email ?: "",
            roleID = u?.roleID ?: -1,
            createdDate = u?.createdDate ?: "",
            imgAvatar = u?.imgAvatar ?: ""
        )
        db.updateUserPw(user)
        resetValue()
        setUsersByRoleId(roleSeclected)
    }

    fun updateAvt(u: User?) {
        val user = User(
            userID = u?.userID,
            userName = u?.userName ?: "",
            nickName = u?.nickName ?: "",
            hashedPw = u?.hashedPw ?: "",
            email = u?.email ?: "",
            roleID = u?.roleID ?: -1,
            createdDate = u?.createdDate ?: "",
            imgAvatar = transform(avtId.first())
        )
        Log.w("idimg", avtId.first().toString())
        db.updateUserAvt(user)
        resetValue()
        setUsersByRoleId(roleSeclected)
    }

    fun updateIn4(u: User?) {
        val user = User(
            userID = u?.userID,
            userName = u?.userName ?: "",
            nickName = nickName.value.toString(),
            hashedPw = u?.hashedPw ?: "",
            email = email.value.toString(),
            roleID = u?.roleID ?: -1,
            createdDate = u?.createdDate ?: "",
            imgAvatar = u?.imgAvatar ?: ""
        )
        db.updateUserIn4(user)
        resetValue()
        setUsersByRoleId(roleSeclected)
    }

    fun delUser(user: User) {
        user.userID?.let { db.deleteUser(it) }
        setUsersByRoleId(roleSeclected)
    }

    fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    fun checkPassword(rawPassword: String, hashedPassword: String): Boolean {
        return BCrypt.checkpw(rawPassword, hashedPassword)
    }

    fun getUser(id: Int): User? {
        return db.getUserByUsersId(id)
    }

    fun transform(id: String): String {
        return "https://drive.usercontent.google.com/download?id=${id}&export=view"
    }

    fun resetValue() {
        userName.value = ""
        passWord.value = ""
        nickName.value = ""
        email.value = ""
        roleID = -1
        avtId.clear()
    }
}

class UserViewModelFactory(private val databaseHelper: DatabaseHelper) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(databaseHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}