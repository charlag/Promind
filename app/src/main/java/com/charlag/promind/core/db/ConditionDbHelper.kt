package com.charlag.promind.core.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

import com.charlag.promind.core.db.ConditionContract.ConditionEntry
import com.charlag.promind.core.db.HintContract.HintEntry

/**
 * Created by charlag on 25/02/2017.
 */

class ConditionDbHelper(context: Context) : SQLiteOpenHelper(context, dbName, null, dbVersion) {
    companion object {
        val dbVersion = 1
        val dbName = "condition.db"
        val sqlCreateConditions = "CREATE TABLE ${ConditionEntry.tableName} (" +
                ConditionEntry.id + " INTEGER PRIMARY KEY," +
                ConditionEntry.latitude + " TEXT," +
                ConditionEntry.longitude + " TEXT," +
                ConditionEntry.locationInverted + " INTEGER," +
                ConditionEntry.timeFrom + " INTEGER," +
                ConditionEntry.timeTo + " INTEGER," +
                ConditionEntry.date + " INTEGER," +
                ConditionEntry.priority + " INTEGER," +
                ConditionEntry.hint + " INTEGER," +
                "FOREIGN KEY(${ConditionEntry.hint}) REFERENCES " +
                "${HintEntry.tableName}(${HintEntry.id}))"

        val sqlCreateHints = "CREATE TABLE ${HintEntry.tableName} (" +
                HintEntry.id + " INTEGER PRIMARY KEY," +
                HintEntry.title + " TEXT," +
                HintEntry.type + " TEXT," +
                HintEntry.data + " TEXT)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(sqlCreateConditions)
        db.execSQL(sqlCreateHints)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}