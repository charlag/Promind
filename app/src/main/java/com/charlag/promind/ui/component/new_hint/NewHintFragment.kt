package com.charlag.promind.ui.component.new_hint

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.charlag.promind.R
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

/**
 * Created by charlag on 18/06/2017.
 */

class NewHintFragment : Fragment(), NewHintContract.View {
    @Inject @JvmField var presenter: NewHintContract.Presenter? = null

    private lateinit var toolbar: Toolbar
    private lateinit var titleField: EditText
    private lateinit var appPicker: Spinner
    private lateinit var timeFromButton: Button
    private lateinit var timeToButton: Button
    private lateinit var dateButton: Button

    private val fromTimePickedSubject: PublishSubject<NewHintContract.Time> =
            PublishSubject.create()
    private val toTimePickedSubject: PublishSubject<NewHintContract.Time> = PublishSubject.create()

    private val disposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_new_hint, container, false)
        toolbar = view.findView(R.id.toolbar)
        titleField = view.findView(R.id.et_title)
        appPicker = view.findView(R.id.picker_app)
        timeFromButton = view.findView(R.id.btn_time_from)
        timeToButton = view.findView(R.id.btn_time_to)
        dateButton = view.findView(R.id.btn_date)
        toolbar.inflateMenu(R.menu.new_hint)
        toolbar.setNavigationIcon(R.drawable.ic_close_black_24dp)
        toolbar.setNavigationOnClickListener { activity.onBackPressed() }
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (presenter == null) {
            DaggerNewHintComponent.builder()
                    .appComponent(context.appComponent)
                    .newHintPresenterModule(NewHintPresenterModule())
                    .build()
                    .inject(this)
        }
        val presenter = presenter!!

        presenter.attachView(this)

        val appsAdapter = ArrayAdapter<String>(context, android.R.layout.simple_list_item_1)
        appPicker.adapter = appsAdapter

        presenter.appsList
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { apps ->
                    appsAdapter.addAll(apps.map(AppViewModel::name))
                }
                .addTo(disposable)

        presenter.showFromTimePicker.subscribe {
            TimePickerFragment(fromTimePickedSubject).show(childFragmentManager, "timePicker")
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
    override val fromTimePicked: Observable<NewHintContract.Time> = fromTimePickedSubject
    override val toTimePicked: Observable<NewHintContract.Time> = toTimePickedSubject
    override val addPressed: PublishSubject<Empty> = PublishSubject.create()
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
        presenter?.detachView()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        addPressed.onNext(Empty)
        return true
    }
}

class TimePickerFragment(private val observer: Observer<NewHintContract.Time>) :
        android.support.v4.app.DialogFragment(), TimePickerDialog.OnTimeSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val cal = Calendar.getInstance()
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)

        return TimePickerDialog(activity, this, hour, minute, DateFormat.is24HourFormat(activity))
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        observer.onNext(NewHintContract.Time(hourOfDay, minute))
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
