package com.charlag.promind.ui.frame

import android.support.v4.app.FragmentManager
import com.charlag.promind.R
import com.charlag.promind.ui.component.new_hint.NewHintFragment

/**
 * Created by charlag on 18/06/2017.
 */

interface Navigator {
    fun openNewHint()
    fun goBack()
}

class NavigatorImpl(val activity: MainActivity) : Navigator {

    private val frameId = R.id.frame

    private val fragmentManager: FragmentManager
        get() = activity.supportFragmentManager

    override fun openNewHint() {
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_up, R.anim.fade_out, R.anim.fade_in,
                        R.anim.slide_out_down)
                .replace(frameId, NewHintFragment())
                .addToBackStack(null)
                .commit()
    }

    override fun goBack() {
        fragmentManager.popBackStack()
    }

}