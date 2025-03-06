package com.example.shopinglist.presentation.addEditNote

import com.example.shopinglist.utils.isCountValid
import com.example.shopinglist.utils.isNameValid

class AddShopItemFragment : BaseShopItemFragment() {

    override fun setupListeners() {
        super.setupListeners()
        with(binding) {
            btnSave.setOnClickListener {
                val isNameValid = etName.text?.toString().isNameValid()
                val isCountValid = etCount.text.toString().isCountValid()

                viewModel.addShopItem(
                    etName.text?.toString(),
                    etCount.text.toString(),
                    isNameValid,
                    isCountValid
                )
            }
        }
    }

    companion object {
        fun newInstanceAddItem(): AddShopItemFragment {
            return AddShopItemFragment()
        }
    }
}