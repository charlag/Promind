package com.charlag.promind.submit

import android.app.IntentService
import android.content.Intent
import com.charlag.promind.core.UserHint
import com.charlag.promind.core.data.models.Action
import com.charlag.promind.core.data.models.Condition
import com.charlag.promind.core.data.models.Location
import com.charlag.promind.core.data.source.ConditionDAO
import com.squareup.moshi.Moshi
import java.util.*
import javax.inject.Inject

/**
 * Created by charlag on 12/06/2017.
 */

class SubmitService : IntentService("SubmitService") {

    @Inject lateinit var conditionDao: ConditionDAO
    val moshi = Moshi.Builder().build()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onHandleIntent(intent: Intent) {
        val dataString = intent.extras.getString("hint_data")
        val data = moshi.adapter(SudmittedData::class.java).fromJson(dataString) ?: return
        val action = when (data.hint.type) {
            "open_main" -> Action.OpenMainAction(data.hint.data)
            "uri" -> Action.UriAction(data.hint.data)
        }
        val hint = UserHint(-1, data.hint.title, null, action)
        val location = data.condition.locaiton?.let {
            Location(it.lat, it.lon)
        }
        val condition = Condition(data.condition.time?.from, data.condition.time?.to,
                data.condition.date?.let { Date(it) },
                location, data.condition.locaiton?.radius?.toInt(),
                data.condition.locaiton?.inverted ?: false, hint)
        conditionDao.addCondition(condition)
    }

    data class SudmittedData(val condition: ConditionData, val hint: HintData)
    data class ConditionData(val time: TimeData?, val date: Long?, val locaiton: LocationData?)

    data class LocationData(val lat: Double, val lon: Double, val radius: Long,
                            val inverted: Boolean = false)

    data class TimeData(val from: Int, val to: Int)
    data class HintData(val title: String, val type: String, val data: String)
}