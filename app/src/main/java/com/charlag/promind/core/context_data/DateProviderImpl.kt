package com.charlag.promind.core.context_data

import java.util.*

/**
 * Created by charlag on 27/03/2017.
 */

class DateProviderImpl : DateProvider {
    override fun currentDate(): Date = Date()
}