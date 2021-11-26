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
import com.kdjj.presentation.R
import com.kdjj.presentation.common.EventObserver
import com.kdjj.presentation.common.RECIPE_ID
import com.kdjj.presentation.common.RECIPE_STATE
import com.kdjj.presentation.databinding.FragmentOthersRecipeBinding
import com.kdjj.presentation.view.adapter.OthersRecipeListAdapter
import com.kdjj.presentation.viewmodel.others.OthersViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class OthersRecipeFragment : Fragment() {

    private var _binding: FragmentOthersRecipeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: OthersViewModel by viewModels()
    private val navigation by lazy { Navigation.findNavController(binding.root) }

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setButtonClickObserver()
    }

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
        setAdapter()
        setEventObserver()
        setSwipeRefreshLayout()
        setBinding()
        initToolBar()
    }

    private fun setButtonClickObserver() {
        viewModel.othersSubject
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                when (it) {
                    is OthersViewModel.ButtonClick.RecipeItemClicked -> {
                        val bundle = bundleOf(
                            RECIPE_ID to it.item.recipeId,
                            RECIPE_STATE to it.item.state
                        )
                        navigation.navigate(R.id.action_othersFragment_to_recipeSummaryActivity, bundle)
                    }
                    is OthersViewModel.ButtonClick.SearchIconClicked -> {
                        navigation.navigate(R.id.action_othersFragment_to_searchRecipeFragment)
                    }
                }
            }.also {
                compositeDisposable.add(it)
            }
    }

    private fun setEventObserver() {
        viewModel.eventOthersRecipe.observe(viewLifecycleOwner, EventObserver {
            when (it) {
                is OthersViewModel.OtherRecipeEvent.ShowSnackBar -> {
                    showSnackBar(getString(it.error.stringRes))
                }
            }
        })
    }

    private fun setBinding() {
        with(binding) {
            lifecycleOwner = this@OthersRecipeFragment.viewLifecycleOwner
            viewModel = this@OthersRecipeFragment.viewModel
        }
    }

    private fun initToolBar() {
        binding.toolbarOthers.apply {
            title = getString(R.string.others)
            inflateMenu(R.menu.toolbar_menu_search_item)
            setOnMenuItemClickListener {
                when (it.itemId) {
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
                        val lastVisibleItemPosition =
                            (recyclerViewOthersRecipe.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
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

    private fun showSnackBar(msg: String) {
        Snackbar.make(binding.root, msg, Snackbar.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }
}