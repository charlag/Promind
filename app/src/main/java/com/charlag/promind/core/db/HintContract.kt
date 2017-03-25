package com.charlag.promind.core.db

import android.provider.BaseColumns

/**
 * Created by charlag on 27/02/2017.
 */

class HintContract {
    object HintEntry : BaseColumns {
        val id = BaseColumns._ID
        val count = BaseColumns._COUNT
        val tableName = "hint"
        val title = "hintTitle"
        val type = "hintType"
        val data = "hintData"
    }

    object HintActionType {
        val openMain = "openMain"
        val url = "url"
    }
}