package com.charlag.promind.core


import com.charlag.promind.core.data.Location
import java.util.*

/**
 * Created by charlag on 11/02/2017.
 *
 * Context required by Model to give hints.
 */

class AssistantContext(internal val location: Location?, internal val date: Date)
