package com.charlag.promind

import com.charlag.promind.core.*
import io.reactivex.Observable
import org.junit.Test
import java.util.*
import kotlin.test.assertNotNull

/**
 * Created by charlag on 25/02/2017.
 */

class ModelImplTest {

    class TestRepository(val conditions: List<Condition>) : ConditionRepository {
        override fun getConditions(time: Int, date: Date): Observable<List<Condition>> =
                Observable.just(conditions)

        override fun addCondition() {}

        override fun removeCondition(id: Int) {}
    }

    @Test
    fun givesHintsDependingOnTime() {
        // given
        val packageName = "com.getsomeheadspace.android"
        val expectedAction = Action.OpenMainAction(packageName)
        val expectedHint = UserHint("Headspace", expectedAction)
        val testCondition = Condition(null, 6 * 60, 11 * 60, null, expectedHint)

        val repo = TestRepository(listOf(testCondition))
        val obj = ModelImpl(repo)
        val time = Calendar.getInstance().run {
            set(Calendar.HOUR_OF_DAY, 7)
            time
        }
        val context = AssistantContext(null, time)
        // when
        val hints = obj.getHintsForContext(context).blockingFirst()
        // then
        assert(hints.isNotEmpty())
        assert(hints.first().title == expectedHint.title)
        assertNotNull(hints.firstOrNull { it.action == expectedAction })
    }
}


