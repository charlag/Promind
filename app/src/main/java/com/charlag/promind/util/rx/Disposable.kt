package com.charlag.promind.util.rx

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by charlag on 13/05/2017.
 */

fun Disposable.addTo(disposable: CompositeDisposable) = disposable.add(this)