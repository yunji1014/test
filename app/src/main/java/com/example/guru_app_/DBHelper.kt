package com.example.guru_app_

/*
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper (context: Context) : SQLiteOpenHelper(context, "LoginDB", null, 1){

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("create Table users(id TEXT primary key, password TEXT, name TEXT, birth TEXT, mail TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("drop Table if exists users")
    }

    fun insertData(id: String?, password: String?, name: String?, birth: String?, mail: String?): Boolean{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("id", id)
        contentValues.put("password", password)
        contentValues.put("name", name)
        contentValues.put("birth", birth)
        contentValues.put("mail", mail)
        val result = db.insert("users", null, contentValues)
        db.close()
        return if(result == -1L) false else true
    }

    //id 중복 확인. id가 존재하면 true
    fun checkID(id: String?): Boolean{
        val db = this.readableDatabase
        var res = true
        val cursor = db.rawQuery("Select * from users where id =?", arrayOf(id))
        if(cursor.count <= 0) res = false
        return res
    }

    fun checkMailpass(id: String?, password: String?) : Boolean{
        val db = this.readableDatabase
        var res = true
        val cursor = db.rawQuery("Select * from id =? and password = ?", arrayOf(id, password))
        if(cursor.count <= 0) res = false
        return res
        //Select * from users where mail =? and password~
    }


}

 */