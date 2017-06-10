package com.charlag.promind.core.data.source

import android.content.ContentValues
import com.charlag.promind.core.UserHint
import com.charlag.promind.core.data.models.Action
import com.charlag.promind.core.data.models.Condition
import com.charlag.promind.core.data.models.Location
import com.charlag.promind.core.data.source.db.ConditionContract.ConditionEntry
import com.charlag.promind.core.data.source.db.HintContract.HintEntry
import com.charlag.promind.core.data.source.db.ConditionDbHelper
import com.charlag.promind.core.data.source.db.HintContract
import io.reactivex.Observable
import org.jetbrains.anko.db.asMapSequence
import java.sql.SQLException
import java.util.*

/**
 * Created by charlag on 25/02/2017.
 */

class ConditionDBDAO(private val dbHelper: ConditionDbHelper) : ConditionDAO {

    val querySQL: String

    init {
        val where = "${ConditionEntry.timeFrom} <= ? AND " +
                "? <= ${ConditionEntry.timeTo} AND " +
                "( ${ConditionEntry.date} IS NULL OR " +
                "DATE(${ConditionEntry.date}) == DATE(?) )"

        querySQL = "SELECT * FROM ${ConditionEntry.tableName} " +
                "INNER JOIN ${HintEntry.tableName} " +
                "ON ${ConditionEntry.tableName}.${ConditionEntry.hint}=" +
                "${HintEntry.tableName}.${HintEntry.id} WHERE $where"
    }

    // probably should notify about changes continuously but for now it works like 'Single'
    override fun getConditions(time: Int, date: Date): Observable<List<Condition>> {
        return Observable.create { subscriber ->
            val result = dbHelper.use {
                val timeString = time.toString()
                rawQuery(querySQL, arrayOf(timeString, timeString, timeString))
                        .use {
                            it.asMapSequence()
                                    .map { map ->
                                        val hintId = map[HintEntry.id] as Long
                                        val hintTitle = map[HintEntry.title] as String
                                        val hintType = map[HintEntry.type] as String
                                        val hintData = map[HintEntry.data] as String?
                                        val action = when (hintType) {
                                            HintContract.HintActionType.openMain -> Action.OpenMainAction(
                                                    hintData!!)
                                            HintContract.HintActionType.url -> Action.UriAction(
                                                    hintData!!)
                                            else -> throw SQLException("Failed to map hint type")
                                        }
                                        val latitude = map[ConditionEntry.latitude] as Double?
                                        val longitude = map[ConditionEntry.longitude] as Double?
                                        val locationInverted = map[ConditionEntry.locationInverted] != 0
                                        val radius = map[ConditionEntry.radius] as Int
                                        val timeFrom = map[ConditionEntry.timeFrom] as Long?
                                        val timeTo = map[ConditionEntry.timeTo] as Long?
                                        val rawDate = map[ConditionEntry.date] as Long?

                                        val hint = UserHint(hintId, hintTitle, action)
                                        val location = if (latitude != null && longitude != null)
                                            Location(latitude, longitude)
                                        else null
                                        val changedDate = rawDate?.let(::Date)
                                        Condition(timeFrom?.toInt(), timeTo?.toInt(), changedDate,
                                                location,
                                                radius, locationInverted, hint)
                                    }
                                    .toList()
                        }
            }

            subscriber.onNext(result)
            subscriber.onComplete()
        }
    }

    override fun addCondition(condition: Condition) {
        val db = dbHelper.writableDatabase

        val hintValues = ContentValues()
        if (condition.hint.id >= 0) {
            hintValues.put(HintContract.HintEntry.id, condition.hint.id)
        }
        hintValues.put(HintContract.HintEntry.title, condition.hint.title)
        hintValues.put(HintContract.HintEntry.type, condition.hint.action.type)
        hintValues.put(HintContract.HintEntry.data, condition.hint.action.data)

        val hintId = db.insert(HintContract.HintEntry.tableName, null, hintValues)

        val values = ContentValues()
        values.put(ConditionEntry.latitude, condition.location?.latitude)
        values.put(ConditionEntry.longitude, condition.location?.longitude)
        values.put(ConditionEntry.timeFrom, condition.timeFrom)
        values.put(ConditionEntry.timeTo, condition.timeTo)
        values.put(ConditionEntry.radius, condition.radius)
        values.put(ConditionEntry.date, condition.date?.time)
        values.put(ConditionEntry.hint, hintId)
        values.put(ConditionEntry.locationInverted, condition.locationInverted)

        db.insert(ConditionEntry.tableName, null, values)

        db.close()
    }

    override fun removeCondition(id: Int) {
    }
}
