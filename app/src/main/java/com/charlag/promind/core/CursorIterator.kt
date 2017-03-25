package com.charlag.promind.core

import android.database.Cursor

/**
 * Created by charlag on 27/02/2017.
 */

fun Cursor.asSequence(): Sequence<Map< String, Any?>> = Sequence { CursorMapIterator(this) }

private class CursorMapIterator(private val cursor: Cursor): Iterator<Map<String, Any?>> {
    override fun hasNext(): Boolean = cursor.position < cursor.count - 1

    override fun next(): Map<String, Any?>{
        cursor.moveToNext()
        val count = cursor.columnCount
        val map = hashMapOf<String, Any?>()
        for (i in 0..(count - 1)) {
            map.put(cursor.getColumnName(i), cursor.getColumnValue(i))
        }
        return map
    }
}

private fun Cursor.getColumnValue(index: Int): Any? {
    if (isNull(index)) return null

    return when (getType(index)) {
        Cursor.FIELD_TYPE_INTEGER -> getLong(index)
        Cursor.FIELD_TYPE_FLOAT -> getDouble(index)
        Cursor.FIELD_TYPE_STRING -> getString(index)
        Cursor.FIELD_TYPE_BLOB -> getBlob(index)
        else -> null
    }
}