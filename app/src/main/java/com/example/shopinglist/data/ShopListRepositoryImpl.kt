package com.example.shopinglist.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shopinglist.domain.ShopItem
import com.example.shopinglist.domain.ShopListRepository
import kotlin.random.Random

object ShopListRepositoryImpl: ShopListRepository {

    private var autoIncrementId = 0
    private const val ITEM_COUNT = 20

    private val shopListLiveData = MutableLiveData<List<ShopItem>>()
    private val shopList = sortedSetOf<ShopItem>(
        {oldItem, newItem -> oldItem.id.compareTo(newItem.id)}
    )


    init {
        for (i in 0 until ITEM_COUNT){
            val item = ShopItem("Name $i", i, Random.nextBoolean())
            addShopItem(item)
        }
    }

    override fun addShopItem(shopItem: ShopItem) {
        if (shopItem.id == ShopItem.UNDEFINED_ID){
            shopItem.id = autoIncrementId++
        }
        shopList.add(shopItem)
        updateList()
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        shopList.remove(shopItem)
        updateList()
    }

    override fun editShopItem(shopItem: ShopItem) {
        val oldElement = getShopItem(shopItem.id)
        shopList.remove(oldElement)
        addShopItem(shopItem)
    }

    override fun getShopItem(shopItemId: Int): ShopItem {
        return shopList.find { it.id == shopItemId }
            ?: throw RuntimeException("Element with id $shopItemId not found!")
    }

    override fun getShopList(): LiveData<List<ShopItem>>  {
        return shopListLiveData
    }

    private fun updateList(){
        shopListLiveData.value = shopList.toList()
    }
}


