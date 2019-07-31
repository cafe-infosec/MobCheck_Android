package com.mobilecheck.assessment.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.mobilecheck.assessment.model.SystemInfo

class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "AssessmentDatabase"
        private val TABLE_ITEMS = "AssessmentTable"
        private val KEY_ID = "id"
        private val KEY_SUCCESS = "itemSuccess"
        private val KEY_FAIL = "itemFail"
        private val KEY_STATUS = "status"
        private val KEY_IS_AUTO = "isAuto"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_ITEMS_TABLE =
            ("CREATE TABLE " + TABLE_ITEMS + "(" + KEY_ID + " TEXT," + KEY_SUCCESS + " TEXT," + KEY_FAIL + " TEXT,"
                    + KEY_STATUS + " INTEGER," + KEY_IS_AUTO + " INTEGER" + ")")
        db?.execSQL(CREATE_ITEMS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS)
        onCreate(db)
    }

    //add Items to database
    fun addItems(items: SystemInfo) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, items.id)
        contentValues.put(KEY_SUCCESS, items.successTitle)
        contentValues.put(KEY_FAIL, items.failTitle)
        contentValues.put(KEY_STATUS, items.status)
        contentValues.put(KEY_IS_AUTO, items.isAuto)
        val success = db.insert(TABLE_ITEMS, null, contentValues)
        db.close()
    }

    //get Item in List
    fun getItems(): List<SystemInfo> {
        val itemList: ArrayList<SystemInfo> = ArrayList<SystemInfo>()
        val selectQuery = "SELECT * FROM $TABLE_ITEMS"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var status: Int
        var isAuto: Int
        var successTitle: String
        var failTitle: String
        var id: String
        if (cursor.moveToFirst()) {
            do {
                isAuto = cursor.getInt(cursor.getColumnIndex("isAuto"))
                status = cursor.getInt(cursor.getColumnIndex("status"))
                successTitle = cursor.getString(cursor.getColumnIndex("itemSuccess"))
                failTitle = cursor.getString(cursor.getColumnIndex("itemFail"))
                id = cursor.getString(cursor.getColumnIndex("id"))
                val item = SystemInfo(
                    id = id,
                    successTitle = successTitle,
                    failTitle = failTitle,
                    status = status,
                    isAuto = isAuto
                )
                itemList.add(item)
            } while (cursor.moveToNext())
        }
        return itemList
    }

    fun getItems(id: String): Cursor? {
        val selectQuery = "SELECT * FROM $TABLE_ITEMS WHERE id LIKE '$id'"
        val db = this.readableDatabase
        var item: SystemInfo
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
        }
        return cursor
    }

    //updateItem
    fun updateItemsManually(item: SystemInfo): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, item.id)
        contentValues.put(KEY_SUCCESS, item.successTitle)
        contentValues.put(KEY_FAIL, item.failTitle)
        contentValues.put(KEY_STATUS, item.status)
        contentValues.put(KEY_IS_AUTO, item.isAuto)
        var id = item.id
        val success = db.update(TABLE_ITEMS, contentValues, "id = '$id'", null)
        db.close()
        return success
    }

    fun updateStatus(id: String, status: Int) {
        val db = this.writableDatabase
        val updateQuery = "UPDATE $TABLE_ITEMS SET $KEY_STATUS='$status' WHERE $KEY_ID='$id'"
        var result = db.execSQL(updateQuery)
        db.close()
    }
}