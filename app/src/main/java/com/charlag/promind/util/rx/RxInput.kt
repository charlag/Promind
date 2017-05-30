package com.charlag.promind.util.rx

/**
 * Created by charlag on 28/05/2017.
 */
import android.util.Log
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

/**
 * Holder for the [Observable] inputs. Creates subjects for you so you don't have to create a
 * subject for every view input.
 * Usually when there are reactive relationships between presenter and view, presenter has to
 * create a subject for every input of the view because view can be detached in every moment but we
 * don't want to terminate our observables. Also presenter should take care of not forwarding
 * any of the onCompleted/onError events to the subject (or use RxRelay).
 * This snippet does exactly that. It lets you create a proxy for your inputs which is backed
 * by the subjects and never terminates. It also subscribes to all of the [Observable] inputs
 * at once. You can describe all your logic/observables in your presenter's init and let
 * this holder to take care of forwarding any observables from the view.
 *
 * It doesn't use kotlin-reflect to avoid heavy dependencies. It's written for RxJava 1.* but can
 * be easily ported to RxJava 2.*.
 *
 * @param T type of input
 */
interface InputsHolder<T : Any> {
    /**
     * Contains an object what implements [T] using subjects.
     */
    val proxiedInputs: T

    /**
     * For every method that returns [Observable] and doesn't have have parameters it forwards
     * onNext events to the proxy.
     * @param inputs to forward to the proxy
     */
    fun subscribe(inputs: T)

    /**
     * Unsubscribes from the input so no more events from any added inputs will be forwarded to the
     * proxy. New inputs will be forwarded.
     */
    fun clear()
}

/**
 * Creates a new [InputsHolder] instance for the given [klass].
 * It scans for every method without arguments and with return type of
 * [Observable] (or it's subtype) and adds it to the [InputsHolder] (so, Kotlin properties work as
 * well becasause they're just getters for Java). All calls to such methods will be intercepted
 * and a subject from the holder will be returned instead of an actual value.
 * @param klass class of which proxy will be created
 * @param T type of the input
 * @return holder for the inputs
 */
fun <T : Any> rxInputOf(klass: Class<T>): InputsHolder<T> {
    val observableMethods = klass.declaredMethods.filter {
        Observable::class.java.isAssignableFrom(it.returnType) &&
                it.parameterTypes.isEmpty()
    }
    val subjects = mutableMapOf<Method, PublishSubject<Any>>()
    observableMethods
            .forEach { method ->
                subjects[method] = PublishSubject.create()
            }

    return InputsHolderImpl(klass, observableMethods.toList(), subjects)
}


private class InputsHolderImpl<T : Any>(klass: Class<T>,
                                        private val methods: List<Method>,
                                        private val subjects: Map<Method, PublishSubject<Any>>)
    : InputsHolder<T> {

    val handler = object : InvocationHandler {
        override fun invoke(proxy: Any, method: Method, args: Array<out Any>?): Any {
            Log.d("RxInput", "method: $method")
            Log.d("RxInput", "start")
            if (method.declaringClass == Object::class.java) {
                Log.d("RxInput", "is object method")
                return method.invoke(original, args)
            }
            if (subjects[method] != null) {
                Log.d("RxInput", "in subjects")
                return subjects[method] as Any
            } else {
                Log.d("RxInput", "not in subjects")
                return if (args != null) method.invoke(original, args) else method.invoke(original)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override val proxiedInputs = Proxy.newProxyInstance(klass.classLoader, arrayOf(klass),
            handler) as T

    var original: T? = null
    val disposable = CompositeDisposable()

    override fun subscribe(inputs: T) {
        original = inputs
        methods.forEach { method ->
            @Suppress("UNCHECKED_CAST")
            val observable = method.invoke(inputs) as Observable<Any>
            val subject = subjects[method]
            observable.subscribe { subject?.onNext(it) }.addTo(disposable)
        }
    }

    override fun clear() {
        original = null
        disposable.clear()
    }
}