package com.example.worldstory.data.dbhelper

import android.provider.BaseColumns
import com.example.worldstory.data.model.User

fun DatabaseHelper.getAllEmails(): List<String> {
    val db = readableDatabase
    val cursor = db.rawQuery(
        """
            SELECT ${Contract.UserEntry.COLUMN_EMAIL} FROM ${Contract.UserEntry.TABLE_NAME}
        """.trimIndent(), null
    )
    val emails = mutableListOf<String>()

    if (cursor.moveToFirst()) {
        do {

            val email =
                cursor.getString(cursor.getColumnIndexOrThrow(Contract.UserEntry.COLUMN_EMAIL))

            emails.add(
                email
            )
        } while (cursor.moveToNext())
    }
    cursor.close()
    return emails
}
fun DatabaseHelper.getUserByEmail(email: String): User? {
    val db = readableDatabase
    val cursor = db.rawQuery(
        """
            SELECT * FROM ${Contract.UserEntry.TABLE_NAME}
            WHERE ${Contract.UserEntry.COLUMN_EMAIL} = ?
        """.trimIndent(), arrayOf(email.toString())
    )
    var user : User?=null

    if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID))
            val userName =
                cursor.getString(cursor.getColumnIndexOrThrow(Contract.UserEntry.COLUMN_USERNAME))
            val hashedPw =
                cursor.getString(cursor.getColumnIndexOrThrow(Contract.UserEntry.COLUMN_PASSWORD))
            val email =
                cursor.getString(cursor.getColumnIndexOrThrow(Contract.UserEntry.COLUMN_EMAIL))
            val nickName =
                cursor.getString(cursor.getColumnIndexOrThrow(Contract.UserEntry.COLUMN_NICKNAME))
            val imgAvatar =
                cursor.getString(cursor.getColumnIndexOrThrow(Contract.UserEntry.COLUMN_IMAGE_AVATAR))
            val roleID =
                cursor.getInt(cursor.getColumnIndexOrThrow(Contract.UserEntry.COLUMN_ROLE_ID_FK))
            val createdDate =
                cursor.getString(cursor.getColumnIndexOrThrow(Contract.UserEntry.COLUMN_CREATED_DATE))
            user=
                User(
                    id,
                    userName,
                    hashedPw,
                    email,
                    imgAvatar,
                    nickName,
                    roleID,
                    createdDate
                )

    }
    cursor.close()
    return user
}