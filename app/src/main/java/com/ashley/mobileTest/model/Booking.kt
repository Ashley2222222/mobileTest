package com.ashley.mobileTest.model
/**
 * @description: 订票信息基类
 * @author: liangxy
 */
data class Booking(
    val shipReference: String,
    val shipToken: String,
    val canIssueTicketChecking: Boolean,
    val expiryTime: String,
    val duration: Int,
    val segments: List<Segment>
)