package com.kdjj.presentation.view.home.others

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.kdjj.domain.model.*
import com.kdjj.domain.model.exception.ApiException
import com.kdjj.domain.model.exception.NetworkException
import com.kdjj.presentation.R
import com.kdjj.presentation.common.EventObserver
import com.kdjj.presentation.common.RECIPE_ID
import com.kdjj.presentation.common.RECIPE_STATE
import com.kdjj.presentation.databinding.FragmentOthersRecipeBinding
import com.kdjj.presentation.view.adapter.OthersRecipeListAdapter
import com.kdjj.presentation.viewmodel.others.OthersViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OthersRecipeFragment : Fragment() {

    private var _binding: FragmentOthersRecipeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: OthersViewModel by viewModels()
    private val navigation by lazy { Navigation.findNavController(binding.root) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_others_recipe,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeNetworkEvent()
        observeMoveToSearchEvent()
        observeRecipeItemClickEvent()
        setAdapter()
        setSwipeRefreshLayout()
        setBinding()
        initToolBar()
    }

    private fun setBinding() {
        with(binding) {
            lifecycleOwner = this@OthersRecipeFragment.viewLifecycleOwner
            viewModel = this@OthersRecipeFragment.viewModel
        }
    }

    private fun initToolBar() {
        binding.toolbarOthers.apply {
            title = getString(R.string.othersRecipe)
            inflateMenu(R.menu.toolbar_menu_search_item)
            setOnMenuItemClickListener{
                when(it.itemId){
                    R.id.item_search -> {
                        viewModel.moveToRecipeSearchFragment()
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun setAdapter() {
        val adapter = OthersRecipeListAdapter(viewModel)

        with(binding) {
            recyclerViewOthersRecipe.adapter = adapter

            recyclerViewOthersRecipe.addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    recyclerViewOthersRecipe.adapter?.let { adapter ->
                        val lastVisibleItemPosition = (recyclerViewOthersRecipe.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                        val lastItemPosition = adapter.itemCount - 1
                        if (lastVisibleItemPosition == lastItemPosition && adapter.itemCount != 0 && dy > 0) {
                            this@OthersRecipeFragment.viewModel.fetchNextRecipeListPage(false)
                        }
                    }
                }
            })
        }
    }

    private fun setSwipeRefreshLayout() {
        with(binding) {
            swipeRefreshLayoutOthers.setOnRefreshListener {
                this@OthersRecipeFragment.viewModel.refreshList()
                swipeRefreshLayoutOthers.isRefreshing = false
            }
        }
    }

    private fun observeNetworkEvent() {
        viewModel.eventException.observe(viewLifecycleOwner, EventObserver {
            when (it) {
                is NetworkException -> {
                    showSnackBar(getString(R.string.networkErrorMessage))
                }
                is ApiException -> {
                    showSnackBar(getString(R.string.severErrorMessage))
                }
            }
        })
    }

    private fun observeMoveToSearchEvent() {
        viewModel.eventSearchIconClicked.observe(viewLifecycleOwner, EventObserver {
            navigation.navigate(R.id.action_othersFragment_to_searchRecipeFragment)
        })
    }

    private fun observeRecipeItemClickEvent() {
        viewModel.eventRecipeItemClicked.observe(viewLifecycleOwner, EventObserver {
            val bundle = bundleOf(
                RECIPE_ID to it.recipeId,
                RECIPE_STATE to it.state
            )
            navigation.navigate(R.id.action_othersFragment_to_recipeSummaryActivity, bundle)
        })
    }

    private fun showSnackBar(msg: String) {
        Snackbar.make(binding.root, msg, Snackbar.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}