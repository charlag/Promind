package com.charlag.promind.core

import android.location.Location

import java.util.Date

/**
 * Created by charlag on 11/02/2017.
 *
 * Context required by Model to give hints.
 */

class AssistantContext(internal val location: Location?, internal val date: Date)
