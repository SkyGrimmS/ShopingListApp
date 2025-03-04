package com.example.shopinglist.presentation.addEditNote

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.shopinglist.R
import com.example.shopinglist.databinding.ActivityAddEditItemBinding
import com.example.shopinglist.domain.ShopItem

class AddEditShopItemActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddEditItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddEditItemBinding.inflate(layoutInflater)

        setContentView(binding.root)
        handlerScreenMode()
        setInsets()
    }

    @SuppressLint("CommitTransaction")
    private fun handlerScreenMode() {
        val screenMode = getScreenMode()
        val shopItemId = getItemId()

        screenMode?.let {
            val fragment = when (screenMode) {
                MODE_EDIT -> AddEditShopItemFragment.newInstanceEditItem(shopItemId)
                MODE_ADD -> AddEditShopItemFragment.newInstanceAddItem()
                else -> throw throw RuntimeException("Unknown screen mode $screenMode")
            }

            supportFragmentManager.beginTransaction()
                .add(R.id.shopItemContainer, fragment)
                .commit()
        } ?: throw throw RuntimeException("Absent screen mode")
    }

    private fun getItemId(): Int {
        return intent.getIntExtra(SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
    }

    private fun getScreenMode(): String? {
        return intent.getStringExtra(SCREEN_MODE)
    }

    private fun setInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    companion object {
        private const val SCREEN_MODE = "extra_mod"
        private const val SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_EDIT = "mod_edit"
        private const val MODE_ADD = "mod_add"

        fun newIntentAddItem(context: Context): Intent {
            val intent = Intent(context, AddEditShopItemActivity::class.java)
            intent.putExtra(SCREEN_MODE, MODE_ADD)
            return intent
        }

        fun newIntentEditItem(context: Context, shopItemId: Int): Intent {
            val intent = Intent(context, AddEditShopItemActivity::class.java)
            intent.putExtra(SCREEN_MODE, MODE_EDIT)
            intent.putExtra(SHOP_ITEM_ID, shopItemId)
            return intent
        }
    }
}





