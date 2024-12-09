package com.example.worldstory

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Handler
import android.os.Looper
import com.example.myapplication.R
import com.example.worldstory.dbhelper.Contract
import com.example.worldstory.dbhelper.DatabaseHelper
import com.example.worldstory.duc.SampleDataStory
import com.example.worldstory.duc.ducactivity.DucUserHomeActivity
import com.example.worldstory.duc.ducutils.dateTimeNow
import com.example.worldstory.duc.ducutils.getLoremIpsumLong
import com.example.worldstory.duc.ducutils.hashPassword
import com.example.worldstory.duc.ducutils.numDef
import com.example.worldstory.model.Chapter
import com.example.worldstory.model.Comment
import com.example.worldstory.model.Genre
import com.example.worldstory.model.Image
import com.example.worldstory.model.Paragraph
import com.example.worldstory.model.Rate
import com.example.worldstory.model.Role
import com.example.worldstory.model.Story
import com.example.worldstory.model.User

fun DatabaseHelper.createDataFirstTime(db: SQLiteDatabase?) {

    //them role
    RoleEnum.values().forEach { role ->
        db?.execSQL(
            """
                INSERT INTO ${Contract.RoleEntry.TABLE_NAME} (${Contract.RoleEntry.COLUMN_NAME})
                VALUES ('${role.name}')
            """
        )
    }

    //them du lieu mac dinh
    db?.execSQL(/* sql = */ """
        INSERT INTO ${Contract.UserEntry.TABLE_NAME} 
        (${Contract.UserEntry.COLUMN_USERNAME}, ${Contract.UserEntry.COLUMN_PASSWORD}, 
         ${Contract.UserEntry.COLUMN_EMAIL}, ${Contract.UserEntry.COLUMN_NICKNAME}, 
         ${Contract.UserEntry.COLUMN_IMAGE_AVATAR}, ${Contract.UserEntry.COLUMN_CREATED_DATE}, 
         ${Contract.UserEntry.COLUMN_ROLE_ID_FK})
        VALUES 
        ('admin', '${hashPassword(SampleDataStory.getExamplePassword())}', 'admin@example.com', 'Admin',  "https://drive.google.com/uc?id=1mhFCwRj-lk34oo3tnND3A_6yGCq2aDpz", '${dateTimeNow()}', 1),
        ('author', '${hashPassword(SampleDataStory.getExamplePassword())}', 'author@example.com', 'Author', "https://drive.google.com/uc?id=1fPVkJqspSh0IQsQ_8teVapd5qf_q1ppV",  '${dateTimeNow()}', 2),
        ('member', '${hashPassword(SampleDataStory.getExamplePassword())}', 'member@example.com', 'Member',"https://drive.google.com/uc?id=1-dvznsZJ_MEzxHP9kam5X36Zxj2Q6pVG",  '${dateTimeNow()}', 3)
    """
    )

}




