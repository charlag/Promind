package com.charlag.promind.core

import android.graphics.Bitmap
import com.charlag.promind.core.data.models.Action

/**
 * Created by charlag on 11/02/2017.
 *
 * Value which is used to present user hints.
 */

data class UserHint(val id: Long, val title: String?, val icon: Bitmap?, val action: Action)
