package com.ashley.mobileTest.model


/**
 * @description: 出发地和目的地信息
 * @author: liangxy
 */
data class OriginDestinationPair(
    val destination: Location,
    val destinationCity: String,
    val origin: Location,
    val originCity: String
)
