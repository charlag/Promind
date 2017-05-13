package com.charlag.promind.new_hint

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.view.View
import com.charlag.promind.core.UserHint
import com.charlag.promind.core.data.Action
import com.charlag.promind.core.data.Condition
import com.charlag.promind.core.data.source.ConditionRepository
import com.charlag.promind.new_hint.NewHintContract.Time
import com.charlag.promind.util.Empty
import com.charlag.promind.util.rx.addTo
import io.reactivex.Observable
import io.reactivex.Observable.combineLatest
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function4
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.ReplaySubject

/**
 * Created by charlag on 10/04/2017.
 */

class NewHintPresenterImpl(view: NewHintContract.View, context: Context,
                           repository: ConditionRepository) :
        NewHintContract.Presenter {
    private val apps: List<ApplicationInfo>
    private val packageManager: PackageManager = context.packageManager

    private val fromTimePressed: PublishSubject<Empty> = PublishSubject.create()
    private val toTimePressed: PublishSubject<Empty> = PublishSubject.create()

    private val disposable = CompositeDisposable()


    override val appsList: Single<List<AppViewModel>>
        get() = Single.fromCallable {
            apps.map { packageManager.getApplicationLabel(it).toString() }
                    .map(::AppViewModel)
        }.subscribeOn(Schedulers.computation())

    init {
        apps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)

        val viewState = combineLatest(view.titleText, view.fromTimePicked, view.toTimePicked,
                view.appSelected, Function4(::ViewState))
        view.addPressed.withLatestFrom(viewState, BiFunction { _: Any, state: ViewState -> state })
                .observeOn(Schedulers.io())
                .subscribe { (title, from, to, selectedApp) ->
                    val minsFrom = from.hours * 60 + from.minutes
                    val minsTo = to.hours * 60 + to.minutes
                    val action = Action.OpenMainAction(apps[selectedApp].packageName)
                    val hint = UserHint(-1, title, action)
                    val condition = Condition(null, minsFrom, minsTo, null, hint, false)
                    repository.addCondition(condition)
                }

        view.fromTimePressed.subscribe(fromTimePressed::onNext).addTo(disposable)
        view.toTimePressed.subscribe(toTimePressed::onNext).addTo(disposable)
    }

    override val showFromTimePicker: Observable<Empty>
        get() = fromTimePressed
    override val showToTimePicker: Observable<Empty>
        get() = toTimePressed

    private data class ViewState(val title: String, val from: Time, val to: Time,
                                 val selectedApp: Int)
}
