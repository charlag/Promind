package com.charlag.promind.ui.component.new_hint

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.DialogFragment
import android.app.TimePickerDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.format.DateFormat
import android.widget.*
import com.charlag.promind.R
import com.charlag.promind.ui.component.new_hint.NewHintContract.Time
import com.charlag.promind.util.Empty
import com.charlag.promind.util.appComponent
import com.charlag.promind.util.rx.addTo
import com.charlag.promind.util.rx.rxClicks
import com.charlag.promind.util.rx.rxItemCeleted
import com.charlag.promind.util.rx.rxText
import com.charlag.promind.util.view.findView
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import java.util.*
import javax.inject.Inject

class NewHintActivity : AppCompatActivity(), NewHintContract.View {

    @Inject lateinit var presenter: NewHintContract.Presenter

    private lateinit var titleField: EditText
    private lateinit var appPicker: Spinner
    private lateinit var timeFromButton: Button
    private lateinit var timeToButton: Button
    private lateinit var dateButton: Button
    private lateinit var addButton: Button

    private val fromTimePickedSubject: PublishSubject<Time> = PublishSubject.create()
    private val toTimePickedSubject: PublishSubject<Time> = PublishSubject.create()

    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_condition)
        titleField = findView(R.id.et_title)
        appPicker = findView(R.id.picker_app)
        timeFromButton = findView(R.id.btn_time_from)
        timeToButton = findView(R.id.btn_time_to)
        dateButton = findView(R.id.btn_date)
        addButton = findView(R.id.btn_add)

        DaggerNewHintComponent.builder()
                .appComponent(appComponent)
                .newHintPresenterModule(NewHintPresenterModule())
                .build()
                .inject(this)

        presenter.attachView(this)

        val appsAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1)
        appPicker.adapter = appsAdapter

        presenter.appsList
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { apps ->
                    appsAdapter.addAll(apps.map(AppViewModel::name))
                }
                .addTo(disposable)

        presenter.showFromTimePicker.subscribe {
            TimePickerFragment(fromTimePickedSubject).show(fragmentManager, "timePicker")
        }
                .addTo(disposable)

        presenter.showToTimePicker.subscribe {
            TimePickerFragment(toTimePickedSubject).show(fragmentManager, "timePicker")
        }
                .addTo(disposable)

        presenter.showDatePicker.subscribe {
            DatePickerFragment(datePicked).show(fragmentManager, "datePicker")
        }
                .addTo(disposable)
    }

    override val titleText: Observable<String>
        get() = titleField.rxText.map(CharSequence::toString)
    override val appSelected: Observable<Int>
        get() = appPicker.rxItemCeleted
    override val fromTimePicked: Observable<Time> = fromTimePickedSubject
    override val toTimePicked: Observable<Time> = toTimePickedSubject
    override val addPressed: Observable<Empty>
        get() = addButton.rxClicks.map(Empty.map)
    override val fromTimePressed: Observable<Empty>
        get() = timeFromButton.rxClicks.map(Empty.map)
    override val toTimePressed: Observable<Empty>
        get() = timeToButton.rxClicks.map(Empty.map)
    override val datePressed: Observable<Empty>
        get() = dateButton.rxClicks.map(Empty.map)
    override val datePicked: PublishSubject<Date> = PublishSubject.create()

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}

class TimePickerFragment(private val observer: Observer<Time>) :
        DialogFragment(), TimePickerDialog.OnTimeSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val cal = Calendar.getInstance()
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)

        return TimePickerDialog(activity, this, hour, minute, DateFormat.is24HourFormat(activity))
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        observer.onNext(Time(hourOfDay, minute))
    }

}

class DatePickerFragment(private val observer: Observer<Date>) :
        DialogFragment(), DatePickerDialog.OnDateSetListener {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val cal = Calendar.getInstance()
        return DatePickerDialog(activity, this, cal[Calendar.YEAR], cal[Calendar.MONTH],
                cal[Calendar.DAY_OF_MONTH])
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val date = Calendar.getInstance().run {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, dayOfMonth)
            time
        }
        observer.onNext(date)
    }
}
