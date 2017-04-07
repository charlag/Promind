package com.charlag.promind.core.data.source

import com.charlag.promind.core.data.Condition
import io.reactivex.Observable
import java.util.*


/**
 * Created by charlag on 25/02/2017.
 */

interface ConditionRepository {
    fun getConditions(time: Int, date: Date): Observable<List<Condition>>
    fun addCondition(condition: Condition)
    fun removeCondition(id: Int)
}