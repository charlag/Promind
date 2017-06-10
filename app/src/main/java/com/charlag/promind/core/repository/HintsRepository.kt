package com.charlag.promind.core.repository

import com.charlag.promind.core.UserHint
import io.reactivex.Observable

/**
 * Created by charlag on 10/06/2017.
 */

interface HintsRepository {
    val hints: Observable<List<UserHint>>
}