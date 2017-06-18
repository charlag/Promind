package com.charlag.promind.ui.component.new_hint

import com.charlag.promind.core.data.models.UserHint
import com.charlag.promind.core.apps.AppsSource
import com.charlag.promind.core.data.models.Action
import com.charlag.promind.core.data.models.Condition
import com.charlag.promind.core.data.source.ConditionDAO
import com.charlag.promind.ui.component.new_hint.NewHintContract.Time
import com.charlag.promind.util.Empty
import com.charlag.promind.util.rx.addTo
import com.stepango.koptional.Optional
import com.stepango.koptional.orNull
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.withLatestFrom
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.util.*

/**
 * Created by charlag on 10/04/2017.
 */

class NewHintPresenterImpl(dao: ConditionDAO, appsSource: AppsSource) :
        NewHintContract.Presenter {
    private val fromTimePressed: PublishSubject<Empty> = PublishSubject.create()

    private val toTimePressed: PublishSubject<Empty> = PublishSubject.create()
    private val fromTimePicked: PublishSubject<Time> = PublishSubject.create()
    private val toTimePicked: PublishSubject<Time> = PublishSubject.create()
    private val titleText: PublishSubject<String> = PublishSubject.create()
    private val appSelected: PublishSubject<Int> = PublishSubject.create()
    private val pickDatePressed: PublishSubject<Empty> = PublishSubject.create()
    private val datePicked: PublishSubject<Date> = PublishSubject.create()
    private val addPressed: PublishSubject<Empty> = PublishSubject.create()
    private val disposable = CompositeDisposable()

    val apps: BehaviorSubject<List<AppsSource.AppData>> = BehaviorSubject.create()
    override val appsList: Observable<List<AppViewModel>> = apps.map { apps ->
        apps.map { AppViewModel(it.title, it.icon) }
    }

    init {
        Single.fromCallable { appsSource.listAllApps() }
                .subscribeOn(Schedulers.computation())
                .subscribe(apps::onNext)

        val viewState = Observables.combineLatest(titleText,
                fromTimePicked.startWithEmpty(),
                toTimePicked.startWithEmpty(),
                datePicked.startWithEmpty(),
                appSelected.startWith(0),
                ::ViewState)

        addPressed.withLatestFrom(viewState, BiFunction { _: Any, state: ViewState -> state })
                .observeOn(Schedulers.io())
                .withLatestFrom(apps) { info, apps -> info to apps }
                .subscribe { (info, apps) ->
                    val minsFrom = info.from.orNull()?.let { (hours, minutes) ->
                        hours * 60 + minutes
                    }
                    val minsTo = info.to.orNull()?.let { (hours, minutes) ->
                        hours * 60 + minutes
                    }
                    val app = apps[info.selectedApp]
                    val action = Action.OpenMainAction(app.packageName)
                    val hint = UserHint(-1, info.title, app.icon, action)
                    val condition = Condition(minsFrom, minsTo, info.date.orNull(), null, 10000,
                            false, hint)
                    dao.addCondition(condition)
                }
    }

    override val showFromTimePicker: Observable<Empty>
        get() = fromTimePressed

    override val showToTimePicker: Observable<Empty>
        get() = toTimePressed
    override val showDatePicker: Observable<Empty>
        get() = pickDatePressed

    override fun attachView(view: NewHintContract.View) {
        view.fromTimePressed.subscribe(fromTimePressed::onNext).addTo(disposable)
        view.toTimePressed.subscribe(toTimePressed::onNext).addTo(disposable)
        view.fromTimePicked.subscribe(fromTimePicked::onNext).addTo(disposable)
        view.toTimePicked.subscribe(toTimePicked::onNext).addTo(disposable)
        view.titleText.subscribe(titleText::onNext).addTo(disposable)
        view.appSelected.subscribe(appSelected::onNext).addTo(disposable)
        view.datePressed.subscribe(pickDatePressed::onNext).addTo(disposable)
        view.datePicked.subscribe(datePicked::onNext).addTo(disposable)
        view.addPressed.subscribe(addPressed::onNext).addTo(disposable)
    }

    override fun detachView() {
        disposable.dispose()
    }

    private fun <T : Any> Observable<T>.startWithEmpty(): Observable<Optional<T>> =
            this.map { Optional.of(it) }.startWith(Optional.empty())

    private data class ViewState(val title: String, val from: Optional<Time>,
                                 val to: Optional<Time>,
                                 val date: Optional<Date>,
                                 val selectedApp: Int)
}
