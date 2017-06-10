package com.charlag.promind.ui.component.hints_screen

import android.Manifest
import com.charlag.promind.core.action.ActionHandler
import com.charlag.promind.core.app_data.AppDataProvider
import com.charlag.promind.core.AssistantContext
import com.charlag.promind.core.Model
import com.charlag.promind.core.UserHint
import com.charlag.promind.core.context_data.DateProvider
import com.charlag.promind.core.context_data.LocationProvider
import com.charlag.promind.core.data.models.Action
import com.charlag.promind.core.data.models.Location
import com.charlag.promind.core.repository.HintsRepository
import com.charlag.promind.util.Empty
import com.charlag.promind.util.rx.addTo
import com.charlag.promind.util.rx.onErrorComplete
import com.stepango.koptional.Optional
import com.stepango.koptional.orNull
import com.stepango.koptional.toOptional
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
                           private val appDataSource: AppDataProvider,
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
//        val location = permissionGrantedInput.filter { hasLocationPermission(it) }
//                .switchMap { locationProvider.currentLocation() }
//                .map { it.toOptional() }
//                .startWith(Optional.empty<Location>())

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
        return when (hint.action) {
            is Action.OpenMainAction -> {
                val appData = appDataSource.getAppData(hint.action.packageName)
                HintViewModel(hint.title ?: appData.name, appData.icon)
            }
            is Action.UriAction -> HintViewModel(hint.title ?: "", null)
        }
    }

    override fun attachView(v: HintsScreenContract.View) {
        v.permissionsGranted.subscribe(permissionGrantedInput::onNext).addTo(disposable)
        v.hintSelected.subscribe(hintSelectedInput::onNext).addTo(disposable)
        v.usagePermissionGranted.subscribe(usagePermissionGrantedInput::onNext).addTo(disposable)
        v.refreshed.subscribe(refreshedInput::onNext).addTo(disposable)
        v.requestUsagePermissionClicked.subscribe(usagePermissionClickedInput::onNext).addTo(
                disposable)
        if (!v.isLocationPermissionGranted) requestLocationPermissionSubject.onNext(Empty)
    }

    override fun detachView() {
        disposable.dispose()
    }

    private fun hasLocationPermission(permissions: List<String>): Boolean = permissions.any {
        it == Manifest.permission.ACCESS_FINE_LOCATION
                || it == Manifest.permission.ACCESS_COARSE_LOCATION
    }

}
