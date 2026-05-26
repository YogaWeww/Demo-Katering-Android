package com.example.demokateringnena.data

data class Order(
    val id: String = "",
    val customerName: String = "",
    val customerPhone: String = "",
    val description: String = "",
    val quantity: Int = 0,
    val scheduleDate: String = "",
    val status: String = ""
)
