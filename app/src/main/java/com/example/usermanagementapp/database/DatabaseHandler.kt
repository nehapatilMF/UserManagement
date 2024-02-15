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

    fun addDummyDataIfEmpty() {
        if (isDatabaseEmpty()) {
            val dummyDataList = listOf(
                Model(0, "Aarav Patel", "image_path_1", "9876543210", "15/02/1995", "Mumbai"),
                Model(0, "Aisha Sharma", "image_path_2", "8765432109", "20/05/1992", "Delhi"),
                Model(0, "Arjun Gupta", "image_path_3", "7654321098", "08/11/1998", "Jaipur"),
                Model(0, "Diya Verma", "image_path_4", "6543210987", "03/09/1990", "Bangalore"),
                Model(0, "Kabir Singh", "image_path_5", "5432109876", "12/07/1994", "Chennai"),
                Model(0, "Neha Kapoor", "image_path_6", "4321098765", "25/04/1997", "Hyderabad"),
                Model(0, "Rahul Sharma", "image_path_7", "3210987654", "09/03/1993", "Kolkata"),
                Model(0, "Sanya Malik", "image_path_8", "2109876543", "18/06/1991", "Ahmedabad"),
                Model(0, "Vijay Yadav", "image_path_9", "1098765432", "30/12/1996", "Pune"),
                Model(0, "Zara Khan", "image_path_10", "0123456789", "07/08/1999", "Lucknow")
            )

            for (model in dummyDataList) {
                addStudent(model)
            }
        }
    }
    private fun isDatabaseEmpty(): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_STUDENT", null)
        val isEmpty = cursor.count == 0
        cursor.close()
        db.close()
        return isEmpty
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
