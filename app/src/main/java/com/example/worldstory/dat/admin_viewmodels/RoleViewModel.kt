package com.example.worldstory.dat.admin_viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.worldstory.dbhelper.DatabaseHelper
import com.example.worldstory.model.Role

class RoleViewModel(val db: DatabaseHelper) : ViewModel(db) {
    val roles = mutableListOf<Role>()

    init {
        fetchAllRole()
    }

    fun fetchAllRole() {
        roles.addAll(db.getAllRoles())
    }

    fun insertRole(role: Role) {
        db.insertRole(role)
        fetchAllRole()
    }

    fun update(role: Role) {
        db.updateRoleName(role)
        fetchAllRole()
    }

    fun deleteRole(role: Role) {
        role.roleID?.let { db.deleteRole(it) }
    }

    fun deleteAllRole() {
        db.deleteAllRole()
    }
}

class RoleViewModelFactory(private val databaseHelper: DatabaseHelper) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RoleViewModel::class.java)) {
            return RoleViewModel(databaseHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}