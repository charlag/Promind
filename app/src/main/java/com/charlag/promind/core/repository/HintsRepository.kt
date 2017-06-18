package com.charlag.promind.core.repository

import com.charlag.promind.core.data.models.UserHint
import io.reactivex.Observable

/**
 * Created by charlag on 10/06/2017.
 *
 * Repository should gather context and other required information itself and present list of
 * hints which views can consume.
 */

interface HintsRepository {
    val hints: Observable<List<UserHint>>
}