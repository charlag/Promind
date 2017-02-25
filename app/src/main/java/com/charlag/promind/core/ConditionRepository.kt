package com.charlag.promind.core

/**
 * Created by charlag on 25/02/2017.
 */

interface ConditionRepository {
    fun getConditions(): Sequence<Condition>
    fun addCondition()
    fun removeCondition(id: Int)
}