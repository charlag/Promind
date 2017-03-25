package com.charlag.promind.core

import io.reactivex.Observable
import java.util.Date


/**
 * Created by charlag on 25/02/2017.
 */

interface ConditionRepository {
    fun getConditions(time: Int, date: Date): Observable<List<Condition>>
    fun addCondition()
    fun removeCondition(id: Int)
}