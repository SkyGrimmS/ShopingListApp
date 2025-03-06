package com.example.shopinglist.domain

import com.example.shopinglist.utils.UNDEFINED_ID

data class ShopItem(

    val name: String,
    val count:Int,
    val enabled:Boolean,
    var id:Int = UNDEFINED_ID
)
