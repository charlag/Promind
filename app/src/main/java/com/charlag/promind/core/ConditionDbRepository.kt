package com.charlag.promind.core

/**
 * Created by charlag on 25/02/2017.
 */

// TODO: implement properly
class ConditionDbRepository : ConditionRepository {
    override fun getConditions(): Sequence<Condition> {
        //        val conditions = conditionSource.getConditionsForContext(context)
//        val db = dbHelper.readableDatabase
//        val projection = arrayOf(ConditionContract.ConditionEntry.timeFrom,
//                ConditionContract.ConditionEntry.timeTo)
//
//        val selection = "time($ConditionContract.ConditionEntry.timeFrom) >= time($context.date) " +
//                "AND time(${ConditionContract.ConditionEntry.timeTo}) <= time($context.date)"
//
//        val cursor = db.query(
//                ConditionContract.tableName,
//                projection,
//                selection,
//                null,
//                null,
//                null,
//                null
//        )
//
//        val conditions = arrayListOf<Condition>()
//        while (cursor.moveToNext()) {
//            val timeFrom = cursor.getLong(cursor.getColumnIndexOrThrow(ConditionContract.ConditionEntry.timeFrom))
//            val timeTo = cursor.getLong(cursor.getColumnIndexOrThrow(ConditionContract.ConditionEntry.timeTo))
//
//        }
//        cursor.close()

        val headspaceAction = Action.OpenMainAction("com.getsomeheadspace.android")
        val headspaceCondition = Condition(null, 6 * 60, 11 * 60, null, UserHint("Headspace", headspaceAction))
        return sequenceOf(headspaceCondition)
    }

    override fun addCondition() {
    }

    override fun removeCondition(id: Int) {
    }
}