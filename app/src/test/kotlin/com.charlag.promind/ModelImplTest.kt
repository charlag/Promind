package com.charlag.promind

import com.charlag.promind.core.data.models.AssistantContext
import com.charlag.promind.core.model.ModelImpl
import com.charlag.promind.core.stats.UsageStatsSource
import com.charlag.promind.core.data.models.UserHint
import com.charlag.promind.core.builtin.weather.WeatherHintsProvider
import com.charlag.promind.core.data.models.Action
import com.charlag.promind.core.data.models.Condition
import com.charlag.promind.core.data.source.ConditionDAO
import io.reactivex.Maybe
import io.reactivex.Observable
import org.junit.Test
import java.util.*
import kotlin.test.assertNotNull

/**
 * Created by charlag on 25/02/2017.
 */

class ModelImplTest {

    class TestRepository(val conditions: List<Condition>) : ConditionDAO {
        override fun addCondition(condition: Condition) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun getConditions(time: Int, date: Date): Observable<List<Condition>> =
                Observable.just(conditions)

        override fun removeCondition(id: Int) {}
    }

    class WeatherHintsProviderStub : WeatherHintsProvider {
        override fun weatherHints(context: AssistantContext): Maybe<List<UserHint>> =
                Maybe.just(listOf())

    }

    @Test
    fun givesHintsDependingOnTime() {
        // given
        val packageName = "com.getsomeheadspace.android"
        val expectedAction = Action.OpenMainAction(packageName)
        val expectedHint = UserHint(1, "Headspace", null, expectedAction)
        val testCondition = Condition(6 * 60, 11 * 60, null, null, hint = expectedHint,
                radius = null)

        val repo = TestRepository(listOf(testCondition))
        val statsSource = object : UsageStatsSource {
            override fun getPackagesUsed(): List<UserHint> = arrayListOf()
        }
        val obj = ModelImpl(repo, statsSource, WeatherHintsProviderStub())
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


