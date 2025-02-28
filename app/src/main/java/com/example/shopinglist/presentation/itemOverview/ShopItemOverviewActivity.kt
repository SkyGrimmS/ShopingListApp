package com.example.shopinglist.presentation.itemOverview

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.shopinglist.databinding.ActivityMainBinding
import com.example.shopinglist.presentation.addEditNote.AddEditShopItemActivity

class ShopItemOverviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: ShopItemOverviewViewModel
    private lateinit var shopListAdapter: ShopListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[ShopItemOverviewViewModel::class.java]


        initListeners()
        setupRecyclerView()
        initObservers()
        setContentView(binding.root)
        setInsets()
    }

    private fun setupRecyclerView() {
        val rvShopList = binding.rvShopList
        with(rvShopList) {
            shopListAdapter = ShopListAdapter()
            adapter = shopListAdapter

            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.VIEW_TYPE_ENABLED,
                ShopListAdapter.MAX_POOL_SIZE
            )
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.VIEW_TYPE_DISABLED,
                ShopListAdapter.MAX_POOL_SIZE
            )
        }
        initAdaptorListeners(rvShopList)
    }

    private fun initAdaptorListeners(rvShopList: RecyclerView) {
        setupSwipeListener(rvShopList)

        shopListAdapter.onShopItemClickListener = {
            val intent = AddEditShopItemActivity.newIntentEditItem(this, it.id)
            startActivity(intent)
        }

        shopListAdapter.onShopItemLongClickListener = { viewModel.changeEnableState(it) }

    }

    private fun initListeners() {
        binding.btnAddShopItem.setOnClickListener {
            val intent = AddEditShopItemActivity.newIntentAddItem(this)
            startActivity(intent)
        }
    }

    private fun initObservers() {
        viewModel.shopList.observe(this) {
            shopListAdapter.submitList(it)
        }
    }

    private fun setupSwipeListener(rvShopList: RecyclerView) {
        val callback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = shopListAdapter.currentList[viewHolder.adapterPosition]
                viewModel.deleteShopItem(item)
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(rvShopList)
    }

    private fun setInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
