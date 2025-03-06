package com.example.shopinglist.presentation.itemOverview

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.shopinglist.R
import com.example.shopinglist.databinding.ActivityItemOverviewBinding
import com.example.shopinglist.presentation.addEditNote.AddEditShopItemActivity
import com.example.shopinglist.presentation.addEditNote.AddShopItemFragment
import com.example.shopinglist.presentation.addEditNote.EditShopItemFragment
import com.example.shopinglist.utils.LAND_MODE
import com.example.shopinglist.utils.MAX_POOL_SIZE
import com.example.shopinglist.utils.ONE_PANE_MODE
import com.example.shopinglist.utils.VIEW_TYPE_DISABLED
import com.example.shopinglist.utils.VIEW_TYPE_ENABLED

class ShopItemOverviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityItemOverviewBinding
    private lateinit var viewModel: ShopItemOverviewViewModel
    private lateinit var shopListAdapter: ShopListAdapter
    private var shopItemOverviewContainer:FragmentContainerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityItemOverviewBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[ShopItemOverviewViewModel::class.java]
        shopItemOverviewContainer = binding.shopItemContainer

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
                VIEW_TYPE_ENABLED,
                MAX_POOL_SIZE
            )
            recycledViewPool.setMaxRecycledViews(
                VIEW_TYPE_DISABLED,
                MAX_POOL_SIZE
            )
        }
        initAdaptorListeners(rvShopList)
    }

    private fun initAdaptorListeners(rvShopList: RecyclerView) {
        setupSwipeListener(rvShopList)

        shopListAdapter.onShopItemClickListener = {
            val mode = getOrientationMode()
            when(mode){
                ONE_PANE_MODE -> {
                    val intent = AddEditShopItemActivity.newIntentEditItem(this, it.id)
                    startActivity(intent)
                }
                LAND_MODE -> launchFragment(EditShopItemFragment.newInstanceEditItem(it.id))
            }
        }

        shopListAdapter.onShopItemLongClickListener = { viewModel.changeEnableState(it) }

    }

    private fun initListeners() {
        binding.btnAddShopItem.setOnClickListener {

            val mode = getOrientationMode()
            when(mode){
                ONE_PANE_MODE -> {
                    val intent = AddEditShopItemActivity.newIntentAddItem(this)
                    startActivity(intent)
                }
                LAND_MODE -> launchFragment(AddShopItemFragment.newInstanceAddItem())
            }
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

    private fun getOrientationMode():Int{
        return if (shopItemOverviewContainer == null){
            ONE_PANE_MODE
        }else{
            LAND_MODE
        }
    }

    private fun launchFragment(fragment: Fragment){
        supportFragmentManager.popBackStack()

        supportFragmentManager.beginTransaction()
            .replace(R.id.shopItemContainer, fragment)
            .addToBackStack(null)
            .commit()

    }

    private fun setInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
