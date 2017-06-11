package com.charlag.promind.core

import com.charlag.promind.core.data.models.Condition
import com.charlag.promind.core.data.source.ConditionDAO
import com.charlag.promind.core.stats.UsageStatsSource
import io.reactivex.Observable
import java.util.*

/**
 * Created by charlag on 11/02/2017.
 *
 * Implementation of Model. Uses database to get conditions and checks provides hints for the
 * matching conditions.
 */

class ModelImpl(private val repository: ConditionDAO,
                private val statsSource: UsageStatsSource) : Model {

    override fun getHintsForContext(context: AssistantContext): Observable<List<UserHint>> {
        val time = Calendar.getInstance().run {
            time = context.date
            get(Calendar.HOUR_OF_DAY) * 60 + get(Calendar.MINUTE)
        }


        return repository.getConditions(time, context.date)
                .filterByConditions(context)
                .map { conditions ->
                    conditions.map { it.hint } + statsSource.getPackagesUsed()
                }
    }

    private fun Observable<List<Condition>>.filterByConditions(context: AssistantContext):
            Observable<List<Condition>> = this.map { conditions ->
        conditions.filter { condition ->
            filterByLocation(condition, context) &&
                    filterByTime(condition, context) &&
                    filterByDate(condition, context)
        }
    }

    private fun filterByLocation(condition: Condition, context: AssistantContext): Boolean {
        if (condition.location == null || condition.radius == null) return true
        if (context.location == null) return false
        val distanceCriteria = condition.radius
        val distance = condition.location.distanceTo(context.location)
        return if (condition.locationInverted) distance > distanceCriteria
        else distance < distanceCriteria
    }

    private fun filterByTime(condition: Condition, context: AssistantContext): Boolean {
        if (condition.timeFrom == null || condition.timeTo == null) return true
        val time = Calendar.getInstance().run {
            time = context.date
            get(Calendar.HOUR_OF_DAY) * 60 + get(Calendar.MINUTE)
        }
        return condition.timeFrom <= time && time <= condition.timeTo
    }

    private fun filterByDate(condition: Condition, context: AssistantContext): Boolean {
        if (condition.date == null) return true
        val today = Calendar.getInstance().run {
            time = context.date
            get(Calendar.DATE)
        }
        val conditionDate = Calendar.getInstance().run {
            time = condition.date
            get(Calendar.DATE)
        }
        return today == conditionDate
    }
}