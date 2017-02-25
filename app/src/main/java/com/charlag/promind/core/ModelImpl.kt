package com.charlag.promind.core

import java.util.*

/**
 * Created by charlag on 11/02/2017.
 *
 * Implementation of Model. Uses database to get conditions and checks provides hints for the
 * matching conditions.
 */

// TODO: make package scoped
class ModelImpl : Model {

    override fun getHintsForContext(context: AssistantContext): List<UserHint> {
        val sixAm = Calendar.getInstance().run {
            set(0, 0, 0, 6, 0)
            time
        }
        val noon = Calendar.getInstance().run {
            set(0, 0, 0, 12, 0)
            time
        }

        val morningTime = TimeInterval(sixAm, noon, false)
        val headspaceAction = Action.OpenMainAction("com.getsomeheadspace.android")
        val headspaceCondition = Condition(null, morningTime, UserHint("Headspace", headspaceAction))

        val fourPm = Calendar.getInstance().run {
            set(0, 0, 0, 16, 0)
            time
        }

        val mealsAction = Action.UriAction("geo:0,0?q=restaurants")
        val mealsCondition = Condition(null, TimeInterval(noon, fourPm, false),
                UserHint("Meals", mealsAction))

        val conditions = listOf(headspaceCondition, mealsCondition)

        return conditions.filter { condition ->
            condition.location?.equals(context.location) ?: true &&
                    condition.timeInterval?.inInterval(context.date, Calendar.getInstance()) ?: true
        }
                .map(Condition::hint)
//        val calendar = Calendar.getInstance()
//        calendar.time = context.date
//        val hour = calendar.get(Calendar.HOUR_OF_DAY)
//
//        if (hour < 12) {
//            val intent = appContext.packageManager.getLaunchIntentForPackage("com.getsomeheadspace.android")
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            hints.add(object : UserHint {
//                override val title = "Headspace"
//                override val action = intent
//            })
//        }
//
//        if (hour > 22) {
//            hints.add(object : UserHint {
//                override val title = "Calendar"
//                override val action = null
//            })
//        }
//
//        if (isMealHour(hour)) {
//            val intentUri = Uri.parse("geo:0,0?q=restaurants")
//            val mapIntent = Intent(Intent.ACTION_VIEW, intentUri)
//            hints.add(object : UserHint {
//                override val title = "Meals"
//                override val action = mapIntent
//            })
//        }
//
//        if (homeLocation != null && homeLocation.distanceTo(context.location) > 10 * 1000) {
//            hints.add(object : UserHint {
//                override val title = "Get home directions"
//                override val action = null
//            })
//        }
//
//        return hints
    }
}
