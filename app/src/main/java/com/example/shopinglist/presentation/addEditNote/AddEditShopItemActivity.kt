package com.example.shopinglist.presentation.addEditNote

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.shopinglist.R
import com.example.shopinglist.core.ScreenModes
import com.example.shopinglist.databinding.ActivityAddEditItemBinding
import com.example.shopinglist.utils.SCREEN_MODE
import com.example.shopinglist.utils.SHOP_ITEM_ID
import com.example.shopinglist.utils.UNDEFINED_ID

class AddEditShopItemActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddEditItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddEditItemBinding.inflate(layoutInflater)

        setContentView(binding.root)

        if (savedInstanceState == null) {
            handlerScreenMode()
        }

        setInsets()
    }

    private fun handlerScreenMode() {
        val screenMode = getScreenMode()
        val shopItemId = getItemId()

        screenMode?.let {
            val fragment = when (screenMode) {
                ScreenModes.MODE_EDIT -> EditShopItemFragment.newInstanceEditItem(shopItemId)
                ScreenModes.MODE_ADD -> AddShopItemFragment.newInstanceAddItem()
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.shopItemContainer, fragment)
                .commit()
        } ?: throw RuntimeException("Absent screen mode")
    }

    private fun getItemId(): Int {
        return intent.getIntExtra(SHOP_ITEM_ID, UNDEFINED_ID)
    }

    private fun getScreenMode(): ScreenModes? {
        return intent.getStringExtra(SCREEN_MODE)?.let { ScreenModes.valueOf(it) }
    }

    private fun setInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    companion object {
        fun newIntentAddItem(context: Context): Intent {
            val intent = Intent(context, AddEditShopItemActivity::class.java)
            intent.putExtra(SCREEN_MODE, ScreenModes.MODE_ADD.name)
            return intent
        }

        fun newIntentEditItem(context: Context, shopItemId: Int): Intent {
            val intent = Intent(context, AddEditShopItemActivity::class.java)
            intent.putExtra(SCREEN_MODE, ScreenModes.MODE_EDIT.name)
            intent.putExtra(SHOP_ITEM_ID, shopItemId)
            return intent
        }
    }
}





