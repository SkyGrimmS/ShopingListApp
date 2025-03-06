package com.example.shopinglist.presentation.addEditNote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shopinglist.data.ShopListRepositoryImpl
import com.example.shopinglist.domain.AddShopItemUseCase
import com.example.shopinglist.domain.EditShopItemUseCase
import com.example.shopinglist.domain.GetShopItemUseCase
import com.example.shopinglist.domain.ShopItem

class AddEditShopItemViewModel : ViewModel() {
    private val repository = ShopListRepositoryImpl

    private val getShopItemUseCase = GetShopItemUseCase(repository)
    private val addShopItemUseCase = AddShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)


    private val _errorInputName = MutableLiveData<Boolean>()
    val errorInputName: LiveData<Boolean>
        get() = _errorInputName

    private val _errorInputCount = MutableLiveData<Boolean>()
    val errorInputCount: LiveData<Boolean>
        get() = _errorInputCount

    private val _shopItem = MutableLiveData<ShopItem>()
    val shopItem: LiveData<ShopItem>
        get() = _shopItem

    private val _shootCloseScreen = MutableLiveData<Unit>()
    val shootCloseScreen: LiveData<Unit>
        get() = _shootCloseScreen

    fun getShopItem(shopItemId: Int) {
        val item = getShopItemUseCase.getShopItem(shopItemId)
        _shopItem.value = item
    }

    fun editShopItem(
        inputName: String?,
        inputCount: String?,
        isNameValid: Boolean,
        isCountValid: Boolean,
    ) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val fieldsValid = isNameValid && isCountValid

        if (fieldsValid) {
            _shopItem.value?.let {
                val item = it.copy(name = name, count = count)
                editShopItemUseCase.editShopItem(item)
                finishWork()
            }
        } else {
            handleError(isCountValid, isNameValid)
        }
    }

    fun addShopItem(
        inputName: String?,
        inputCount: String,
        isNameValid: Boolean,
        isCountValid: Boolean,
    ) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val fieldsValid = isNameValid && isCountValid
        if (fieldsValid) {
            val shopItem = ShopItem(name, count, true)
            addShopItemUseCase.addShopItem(shopItem)

            finishWork()
        } else {
            handleError(isCountValid, isNameValid)
        }
    }

    private fun handleError(isCountValid: Boolean, isNameValid: Boolean) {
        if (!isCountValid) {
            _errorInputCount.value = true
        }
        if (!isNameValid) {
            _errorInputName.value = true
        }
    }

    private fun parseName(inputName: String?): String {
        return inputName?.trim() ?: ""
    }

    private fun parseCount(inputCount: String?): Int {
        return try {
            inputCount?.trim()?.toInt() ?: 0
        } catch (e: Exception) {
            0
        }
    }

    fun resetErrorInputName() {
        _errorInputName.value = false
    }

    fun resetErrorInputCount() {
        _errorInputCount.value = false
    }

    private fun finishWork() {
        _shootCloseScreen.value = Unit
    }
}