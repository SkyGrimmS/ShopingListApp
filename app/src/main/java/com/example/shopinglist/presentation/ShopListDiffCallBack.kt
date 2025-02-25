package com.example.shopinglist.presentation

import androidx.recyclerview.widget.DiffUtil
import com.example.shopinglist.domain.ShopItem

class ShopListDiffCallBack(
    private val oldList: List<ShopItem>,
    private val newList: List<ShopItem>,
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].name == newList[newItemPosition].name &&
                oldList[oldItemPosition].count == newList[newItemPosition].count &&
                oldList[oldItemPosition].enabled == newList[newItemPosition].enabled
    }
}