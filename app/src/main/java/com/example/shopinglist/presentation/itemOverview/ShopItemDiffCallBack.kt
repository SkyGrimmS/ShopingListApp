package com.example.shopinglist.presentation.itemOverview

import androidx.recyclerview.widget.DiffUtil
import com.example.shopinglist.domain.ShopItem

class ShopItemDiffCallBack: DiffUtil.ItemCallback<ShopItem>() {
    override fun areItemsTheSame(oldItem: ShopItem, newItem: ShopItem): Boolean {
        return oldItem.id == newItem.id
    }
    override fun areContentsTheSame(oldItem: ShopItem, newItem: ShopItem): Boolean {
        return  oldItem.enabled == newItem.enabled
                && oldItem.name == newItem.name
                && oldItem.count == newItem.count
    }
}