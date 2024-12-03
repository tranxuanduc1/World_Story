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
    val avtId= mutableListOf<String>()
    var roleID: Int = -1

    init {
        fetchAllUsers()
    }

    fun fetchAllUsers() {
        __users.value = db.getAllUsers()
    }


    fun onAddUser(): Long {
        Log.w("id",avtId.get(0).toString())
        val hashedpw = hashPassword(password = passWord.value.toString())
        val user = User(
            null,
            userName.value.toString(),
            hashedpw,
            email.value.toString(),
            transform(avtId.get(0).toString()),
            nickName.value.toString(),
            roleID,
            dateTimeNow()
        )
        val l: Long = db.insertUser(user)
        userName.value = ""
        passWord.value = ""
        nickName.value = ""
        email.value=""
        Log.i("ham insert", "da insert")
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

    fun updatetUser(u: User) {}
//        val u = User(
//            userID = id,
//            userName = userName.value.toString(),
//            "",
//            "",
//            nickName = nickName.value.toString(),
//            roleID = roleID,
//            ""
//        )
//        val userName = userName.value?: u.userName
//        val nickName=nickName.value?:u.nickName
//        val password=passWord?:u.hashedPw
//        if(password.equals(this.passWord)){
//            password=hashPassword(password)
//        }
//        db.updateUser(u)
//        fetchAllUsers()
//    }

    fun delUser(user: User) {
        user.userID?.let { db.deleteUser(it) }
        fetchAllUsers()
    }

    fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    fun getUser(id: Int): User {
        return db.getUserByUsersId(id)!!
    }

    fun transform(id: String): String {
        return "https://drive.usercontent.google.com/download?id=${id}&export=view"
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