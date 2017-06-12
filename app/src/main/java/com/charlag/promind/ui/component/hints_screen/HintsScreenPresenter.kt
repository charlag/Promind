package com.charlag.promind.ui.component.hints_screen

import android.Manifest
import com.charlag.promind.core.UserHint
import com.charlag.promind.core.action.ActionHandler
import com.charlag.promind.core.data.models.Action
import com.charlag.promind.core.repository.HintsRepository
import com.charlag.promind.util.Empty
import com.charlag.promind.util.rx.addTo
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.Observables
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.ReplaySubject
import java.util.concurrent.TimeUnit

/**
 * Created by charlag on 27/03/2017.
 */

class HintsScreenPresenter(private val hintsRepository: HintsRepository,
                           actionHandler: ActionHandler)
    : HintsScreenContract.Presenter {

    private val permissionGrantedInput: PublishSubject<List<String>> = PublishSubject.create()
    private val hintSelectedInput: PublishSubject<Int> = PublishSubject.create()
    private val usagePermissionGrantedInput: PublishSubject<Boolean> = PublishSubject.create()
    private val refreshedInput: PublishSubject<Empty> = PublishSubject.create()
    private val usagePermissionClickedInput: PublishSubject<Empty> = PublishSubject.create()

    private val hintsSubject: ReplaySubject<List<UserHint>> = ReplaySubject.createWithSize(1)
    private val requestLocationPermissionSubject: PublishSubject<Empty> = PublishSubject.create()
    private val requestUsagePermissionSubject: BehaviorSubject<Empty> = BehaviorSubject.create()
    override val refreshing: BehaviorSubject<Boolean> = BehaviorSubject.create()
    override val showUsagePermissionInfo: BehaviorSubject<Boolean> = BehaviorSubject.create()

    private val disposable = CompositeDisposable()

    init {
        Observables.combineLatest(usagePermissionGrantedInput.startWith(false),
                permissionGrantedInput.startWith(listOf<String>()),
                refreshedInput.startWith(Empty)) { _, _, _ -> Unit }
                .throttleFirst(800, TimeUnit.MILLISECONDS)
                .switchMap {
                    refreshing.onNext(true)
                    hintsRepository.hints.onErrorReturn { listOf() }
                }.subscribe {
            hintsSubject.onNext(it)
            refreshing.onNext(false)
        }

        hintSelectedInput
                .withLatestFrom(hintsSubject, BiFunction<Int, List<UserHint>, Action> {
                    position, hints ->
                    hints[position].action
                }).subscribe(actionHandler::handle)

        usagePermissionGrantedInput.subscribe { showUsagePermissionInfo.onNext(!it) }
                .addTo(disposable)

        usagePermissionClickedInput.subscribe {
            requestUsagePermissionSubject.onNext(Empty)
        }.addTo(disposable)
    }

    override val hints: Observable<List<HintViewModel>> = hintsSubject.map { hints ->
        hints.map(this::hintToViewModel)
    }

    override val requestLocationPermission: Observable<Empty> = requestLocationPermissionSubject
    override val requestUsagePermission: Observable<Empty> = requestUsagePermissionSubject

    private fun hintToViewModel(hint: UserHint): HintViewModel {
        return HintViewModel(hint.title ?: "", hint.icon)
    }

    override fun attachView(view: HintsScreenContract.View) {
        view.permissionsGranted.subscribe(permissionGrantedInput::onNext).addTo(disposable)
        view.hintSelected.subscribe(hintSelectedInput::onNext).addTo(disposable)
        view.usagePermissionGranted.subscribe(usagePermissionGrantedInput::onNext).addTo(disposable)
        view.refreshed.subscribe(refreshedInput::onNext).addTo(disposable)
        view.requestUsagePermissionClicked.subscribe(usagePermissionClickedInput::onNext).addTo(
                disposable)
        if (!view.isLocationPermissionGranted) requestLocationPermissionSubject.onNext(Empty)
    }

    override fun detachView() {
        disposable.dispose()
    }

    private fun hasLocationPermission(permissions: List<String>): Boolean = permissions.any {
        it == Manifest.permission.ACCESS_FINE_LOCATION
                || it == Manifest.permission.ACCESS_COARSE_LOCATION
    }

}
