package com.example.shopinglist.presentation.addEditNote

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.shopinglist.utils.SHOP_ITEM_ID
import com.example.shopinglist.utils.UNDEFINED_ID
import com.example.shopinglist.utils.isCountValid
import com.example.shopinglist.utils.isNameValid

class EditShopItemFragment : BaseShopItemFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getShopItem(getItemId())

    }

    override fun setupListeners() {
        super.setupListeners()

        with(binding) {
            btnSave.setOnClickListener {
                val isNameValid = etName.text?.toString().isNameValid()
                val isCountValid = etCount.text.toString().isCountValid()

                viewModel.editShopItem(
                    etName.text?.toString(),
                    etCount.text.toString(),
                    isNameValid,
                    isCountValid
                )
            }
        }
    }

    override fun setupObserves() {
        super.setupObserves()

        viewModel.shopItem.observe(viewLifecycleOwner) {
            binding.etName.setText(it.name)
            binding.etCount.setText(String.format(it.count.toString()))
        }
    }


    private fun getItemId(): Int {
        val id = requireArguments().getInt(SHOP_ITEM_ID)
        if (id == UNDEFINED_ID) {
            throw RuntimeException("Param shop item ID is absent")
        }
        return id
    }

    companion object {

        fun newInstanceEditItem(shopItemId: Int): EditShopItemFragment {
            return EditShopItemFragment().apply {
                arguments = Bundle().apply {
                    putInt(SHOP_ITEM_ID, shopItemId)
                }
            }
        }
    }
}