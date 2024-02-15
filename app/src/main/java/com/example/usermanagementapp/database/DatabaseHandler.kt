package com.example.usermanagementapp.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.usermanagementapp.models.Model

class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context,NEW_DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 2
        private const val NEW_DATABASE_NAME = "Database" // Database name
        private const val TABLE_STUDENT = "HappyPlacesTable" // Table Name
        private const val KEY_ID = "_id"
        private const val KEY_NAME = "name"
        private const val KEY_IMAGE = "image"
        private const val KEY_MOBILE_NUMBER = "mobileNumber"
        private const val KEY_DATE = "date"
        private const val KEY_LOCATION = "location"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE " + TABLE_STUDENT + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_IMAGE + " TEXT,"
                + KEY_MOBILE_NUMBER + " TEXT,"
                + KEY_DATE + " TEXT,"
                + KEY_LOCATION + " TEXT)")
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_STUDENT")
        onCreate(db)
    }

    fun addStudent(student: Model): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, student.name)
        contentValues.put(KEY_IMAGE, student.image)
        contentValues.put(KEY_MOBILE_NUMBER, student.mobileNumber)
        contentValues.put(KEY_DATE, student.date)
        contentValues.put(KEY_LOCATION, student.location)
        val result = db.insert(TABLE_STUDENT, null, contentValues)
        db.close()
        return result
    }

    fun getStudentsList(): ArrayList<Model> {
        val studentList: ArrayList<Model> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_STUDENT"
        val db = this.readableDatabase

        try {
            val cursor: Cursor = db.rawQuery(selectQuery, null)
            if (cursor.moveToFirst()) {
                do {
                    val student = Model(
                        cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                        cursor.getString(cursor.getColumnIndex(KEY_IMAGE)),
                        cursor.getString(cursor.getColumnIndex(KEY_MOBILE_NUMBER)),
                        cursor.getString(cursor.getColumnIndex(KEY_DATE)),
                        cursor.getString(cursor.getColumnIndex(KEY_LOCATION))
                    )
                    studentList.add(student)
                } while (cursor.moveToNext())
            }
            cursor.close()
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        return studentList
    }

    fun update(student: Model): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, student.name)
        contentValues.put(KEY_IMAGE, student.image)
        contentValues.put(KEY_MOBILE_NUMBER, student.mobileNumber)
        contentValues.put(KEY_DATE, student.date)
        contentValues.put(KEY_LOCATION, student.location)
        val success = db.update(TABLE_STUDENT, contentValues, "$KEY_ID=?", arrayOf(student.id.toString()))
        db.close()
        return success
    }

    fun delete(student: Model): Int {
        val db = this.writableDatabase
        val success = db.delete(TABLE_STUDENT, "$KEY_ID=?", arrayOf(student.id.toString()))
        db.close()
        return success
    }
}
