package com.example.shopinglist.presentation.addEditNote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.shopinglist.R
import com.example.shopinglist.databinding.FregmentShopItemBinding
import com.example.shopinglist.domain.ShopItem
import com.example.shopinglist.utils.isCountValid
import com.example.shopinglist.utils.isNameValid

class AddEditShopItemFragment(
    private val screenMode: String = MODE_UNKNOWN,
    private val shopItemId: Int = ShopItem.UNDEFINED_ID,
) : Fragment() {


    private lateinit var binding: FregmentShopItemBinding
    private lateinit var viewModel: AddEditShopItemViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FregmentShopItemBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[AddEditShopItemViewModel::class.java]
        handlerScreenMode(screenMode)
        setupListeners()
        observeViewModel()
        setInsets()
    }

    private fun setupListeners() {
        binding.etName.doAfterTextChanged { viewModel.resetErrorInputName() }
        binding.etCount.doAfterTextChanged { viewModel.resetErrorInputCount() }
    }

    private fun observeViewModel() {
        viewModel.errorInputCount.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.error_input_count)
            } else {
                null
            }
            binding.tilCount.error = message
        }

        viewModel.errorInputName.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.error_input_name)
            } else {
                null
            }
            binding.tilName.error = message
        }

        viewModel.shootCloseScreen.observe(viewLifecycleOwner) {
            activity?.onBackPressed()
        }
    }

    private fun handlerScreenMode(screenMode: String) {
        if (screenMode !== MODE_EDIT && screenMode !== MODE_ADD) {
            throw RuntimeException("Param screen mode is absent")
        }

        with(binding) {

            btnSave.setOnClickListener {
                val isNameValid = etName.text?.toString().isNameValid()
                val isCountValid = etCount.text.toString().isCountValid()
                when (screenMode) {
                    MODE_EDIT -> viewModel.editShopItem(
                        etName.text?.toString(),
                        etCount.text.toString(),
                        isNameValid,
                        isCountValid
                    )

                    MODE_ADD -> viewModel.addShopItem(
                        etName.text?.toString(),
                        etCount.text.toString(),
                        isNameValid,
                        isCountValid
                    )
                }
            }
            if (screenMode == MODE_EDIT) {
                launchEditMode()
            }
        }
    }


    private fun launchEditMode() {
        viewModel.getShopItem(shopItemId)
        viewModel.shopItem.observe(viewLifecycleOwner) {
            binding.etName.setText(it.name)
            binding.etCount.setText(String.format(it.count.toString()))
        }
    }


    private fun setInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    companion object {
        private const val EXTRA_SCREEN_MODE = "extra_mod"
        private const val EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_EDIT = "mod_edit"
        private const val MODE_ADD = "mod_add"

        private const val MODE_UNKNOWN = ""

        fun newInstanceAddItem(): AddEditShopItemFragment {
            return AddEditShopItemFragment(MODE_ADD)
        }

        fun newInstanceEditItem(shopItemId: Int): AddEditShopItemFragment {
            return AddEditShopItemFragment(MODE_EDIT, shopItemId)
        }
    }

}