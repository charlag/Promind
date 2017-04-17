package com.charlag.promind.new_hint

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.view.View
import com.charlag.promind.core.UserHint
import com.charlag.promind.core.data.Action
import com.charlag.promind.core.data.Condition
import com.charlag.promind.core.data.source.ConditionRepository
import io.reactivex.Observable.combineLatest
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function4
import io.reactivex.schedulers.Schedulers

/**
 * Created by charlag on 10/04/2017.
 */

class NewHintPresenterImpl(view: NewHintContract.View, context: Context,
                           repository: ConditionRepository) :
        NewHintContract.Presenter {
    private val apps: List<ApplicationInfo>
    private val packageManager: PackageManager = context.packageManager

    override val appsList: Single<List<AppViewModel>>
        get() = Single.fromCallable {
            apps.map { packageManager.getApplicationLabel(it).toString() }
                    .map(::AppViewModel)
        }.subscribeOn(Schedulers.computation())

    init {
        apps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)

        val viewState = combineLatest(view.titleText, view.fromTimeText, view.toTimeText,
                view.appSelected, Function4(::ViewState))
        view.addPressed.withLatestFrom(viewState, BiFunction { _: Any, state: ViewState -> state })
                .observeOn(Schedulers.io())
                .subscribe { (title, from, to, selectedApp) ->
                    val minsFrom = timeStringToMinutes(from)
                    val minsTo = timeStringToMinutes(to)
                    val action = Action.OpenMainAction(apps[selectedApp].packageName)
                    val hint = UserHint(-1, title, action)
                    val condition = Condition(null, minsFrom, minsTo, null, hint, false)
                    repository.addCondition(condition)
                }


    }

    private fun timeStringToMinutes(from: String): Int {
        val stringParts = from.split(':')
        return stringParts[0].toInt() * 60 + stringParts[1].toInt()
    }

    private data class ViewState(val title: String, val from: String, val to: String, val selectedApp: Int)
}
