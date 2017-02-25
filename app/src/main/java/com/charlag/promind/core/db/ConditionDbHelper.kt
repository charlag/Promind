package com.charlag.promind.core.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

import com.charlag.promind.core.db.ConditionContract.ConditionEntry

/**
 * Created by charlag on 25/02/2017.
 */

class ConditionDbHelper(context: Context) : SQLiteOpenHelper(context, dbName, null, dbVersion) {
    companion object {
        val dbVersion = 1
        val dbName = "condition.db"
        val sqlCreateEntries = "CREATE_TABLE $ConditionEntry.tableName (" +
                "$ConditionEntry.id INTEGER PRIMARY KEY" +
                "$ConditionEntry.latitude TEXT," +
                "$ConditionEntry.longitude TEXT," +
                "$ConditionEntry.locationInverted INTEGER," +
                "$ConditionEntry.timeFrom + INTEGER," +
                "$ConditionEntry.timeTo + INTEGER," +
                "$ConditionEntry.date + INTEGER)"

    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(sqlCreateEntries)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}