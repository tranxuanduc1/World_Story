package com.example.worldstory.dat.admin_viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.worldstory.dbhelper.DatabaseHelper
import com.example.worldstory.model.Role

class RoleViewModel(val db: DatabaseHelper) : ViewModel(db) {
    val roles = mutableListOf<Role>()
    val id_role_map = mutableMapOf<Int?, String>()

    init {
        fetchAllRole()
    }

    fun fetchAllRole() {
        roles.clear()
        roles.addAll(db.getAllRoles())
        roles.forEach { r ->
            id_role_map[r.roleID] = r.roleName
        }
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
        fetchAllRole()
    }

    fun deleteAllRole() {
        db.deleteAllRole()
        fetchAllRole()
    }

    fun getRole(id: Int): String? {
        val roleId = db.getRoleIdByUserId(id)
        return db.getRoleNameByRoleId(roleId)
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