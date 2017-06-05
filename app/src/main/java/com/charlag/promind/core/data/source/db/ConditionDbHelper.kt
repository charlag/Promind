package com.charlag.promind.core.data.source.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.charlag.promind.core.data.source.db.ConditionContract.ConditionEntry
import com.charlag.promind.core.data.source.db.HintContract.HintEntry
import org.jetbrains.anko.db.*

/**
 * Created by charlag on 25/02/2017.
 */

class ConditionDbHelper(context: Context)
    : ManagedSQLiteOpenHelper(context, dbName, null, dbVersion) {
    companion object {
        val dbVersion = 2
        val dbName = "condition.db"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(ConditionEntry.tableName, true,
                ConditionEntry.id to INTEGER,
                ConditionEntry.latitude to TEXT,
                ConditionEntry.longitude to TEXT,
                ConditionEntry.locationInverted to INTEGER,
                ConditionEntry.timeFrom to INTEGER,
                ConditionEntry.timeTo to INTEGER,
                ConditionEntry.date to INTEGER,
                ConditionEntry.hint to INTEGER,
                FOREIGN_KEY(ConditionEntry.hint, HintEntry.tableName, HintEntry.id))
        db.createTable(HintEntry.tableName, true,
                HintEntry.id to INTEGER + PRIMARY_KEY + UNIQUE,
                HintEntry.title to TEXT,
                HintEntry.type to TEXT,
                HintEntry.data to TEXT)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.dropTable(ConditionEntry.tableName, true)
        db?.dropTable(HintEntry.tableName, true)
    }
}