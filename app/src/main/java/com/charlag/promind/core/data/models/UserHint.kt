package com.charlag.promind.core.data.models

import android.graphics.Bitmap
import com.charlag.promind.core.data.models.Action

/**
 * Created by charlag on 11/02/2017.
 *
 * Value which holds information about hint, presented to user.
 */

data class UserHint(val id: Long, val title: String?, val icon: Bitmap?, val action: Action)
