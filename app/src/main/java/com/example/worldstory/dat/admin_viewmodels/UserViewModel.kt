package com.example.worldstory.dat.admin_viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import org.mindrot.jbcrypt.BCrypt
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.worldstory.dbhelper.DatabaseHelper
import com.example.worldstory.duc.SampleDataStory
import com.example.worldstory.duc.ducutils.dateTimeNow
import com.example.worldstory.model.User


class UserViewModel(val db: DatabaseHelper) : ViewModel() {
    val __users = MutableLiveData<List<User>>()
    val userName = MutableLiveData<String>()
    val passWord = MutableLiveData<String>()
    val nickName = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val avtId = mutableListOf<String>()
    var roleID: Int = -1

    init {
        fetchAllUsers()
    }

    fun fetchAllUsers() {
        __users.value = db.getAllUsers()
    }


    fun onAddUser(): Long {
        Log.w("id", avtId.get(0).toString())
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
        fetchAllUsers()
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
        fetchAllUsers()
    }

    fun updatetUserPw(u: User?) {
        val user = User(
            userID = u?.userID,
            userName = u?.userName ?: "",
            nickName = u?.nickName ?: "",
            hashedPw = hashPassword(password = passWord.toString()),
            email = u?.email ?: "",
            roleID = u?.roleID?:-1,
            createdDate = u?.createdDate ?: "",
            imgAvatar = u?.imgAvatar ?: ""
        )
        db.updateUserPw(user)
        resetValue()
        fetchAllUsers()
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
        Log.w("idimg",avtId.first().toString())
        db.updateUserAvt(user)
        resetValue()
        fetchAllUsers()
    }

    fun updateIn4(u:User?){
        val user = User(
            userID = u?.userID,
            userName =u?.userName ?: "",
            nickName = nickName.value.toString(),
            hashedPw = u?.hashedPw ?: "",
            email = email.value.toString(),
            roleID = u?.roleID ?: -1,
            createdDate = u?.createdDate ?: "",
            imgAvatar = u?.imgAvatar?:""
        )
        db.updateUserIn4(user)
        resetValue()
        fetchAllUsers()
    }
    fun delUser(user: User) {
        user.userID?.let { db.deleteUser(it) }
        fetchAllUsers()
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

    fun resetValue(){
        userName.value = ""
        passWord.value = ""
        nickName.value = ""
        email.value = ""
        roleID=-1
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