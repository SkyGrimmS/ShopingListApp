package com.example.shopinglist.utils

fun String?.isNameValid():Boolean = !this.isNullOrBlank()

fun String?.isCountValid(): Boolean {
    val number = this?.toIntOrNull()
    return number?.let {
        it >= 0
    } ?: false
}
