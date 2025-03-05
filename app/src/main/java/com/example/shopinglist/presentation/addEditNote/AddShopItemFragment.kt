package com.example.shopinglist.presentation.addEditNote

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.shopinglist.utils.isCountValid
import com.example.shopinglist.utils.isNameValid

class AddShopItemFragment : BaseShopItemFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[AddEditShopItemViewModel::class.java]
        setupListeners()
    }

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