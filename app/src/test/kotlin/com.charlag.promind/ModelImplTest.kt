package com.charlag.promind

import com.charlag.promind.core.Action
import com.charlag.promind.core.AssistantContext
import com.charlag.promind.core.ModelImpl
import org.junit.Test
import java.util.*
import kotlin.test.assertNotNull

/**
 * Created by charlag on 25/02/2017.
 */

class ModelImplTest {

    @Test
    fun givesHintsDependingOnTime() {
        // given
        val packageName = "com.getsomeheadspace.android"
        val obj = ModelImpl()
        val time = Calendar.getInstance().run {
            set(Calendar.HOUR_OF_DAY, 7)
            time
        }
        val context = AssistantContext(null, time)
        val expectedAction = Action.OpenMainAction(packageName)
        // when
        val hints = obj.getHintsForContext(context)
        // then
        assert(hints.isNotEmpty())
        assert(hints.first().title == "Headspace")
        assertNotNull(hints.firstOrNull { it.action == expectedAction })
    }
}


