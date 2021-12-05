package com.kdjj.presentation.view.home.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.StringRes
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.kdjj.presentation.R
import com.kdjj.presentation.common.EventObserver
import com.kdjj.presentation.common.RECIPE_ID
import com.kdjj.presentation.common.RECIPE_STATE
import com.kdjj.presentation.common.extensions.throttleFirst
import com.kdjj.presentation.databinding.FragmentSearchRecipeBinding
import com.kdjj.presentation.model.ResponseError
import com.kdjj.presentation.view.adapter.SearchRecipeListAdapter
import com.kdjj.presentation.view.dialog.ConfirmDialogBuilder
import com.kdjj.presentation.viewmodel.home.search.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce

@AndroidEntryPoint
class SearchRecipeFragment : Fragment() {

    private var _binding: FragmentSearchRecipeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModels()
    private val navigation by lazy { Navigation.findNavController(binding.root) }

    private lateinit var resultListAdapter: SearchRecipeListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setButtonClickObserver()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_search_recipe, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        resultListAdapter = SearchRecipeListAdapter(viewModel)

        binding.recyclerViewSearch.apply {
            adapter = resultListAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val lastVisibleItemPosition = (layoutManager as LinearLayoutManager)
                        .findLastCompletelyVisibleItemPosition()
                    val lastItemPosition = resultListAdapter.itemCount - 1
                    if (lastVisibleItemPosition == lastItemPosition && lastItemPosition >= 0 && dy > 0) {
                        viewModel.loadMoreRecipe()
                    }
                }
            })
        }

        focusInput()
        setEventObservers()
    }

    private fun setButtonClickObserver() {
        lifecycleScope.launchWhenStarted {
            viewModel.clickFlow.throttleFirst()
                .collect {
                    when (it) {
                        is SearchViewModel.ButtonClick.Summary -> {
                            val bundle = bundleOf(
                                RECIPE_ID to it.item.recipeId,
                                RECIPE_STATE to it.item.state
                            )
                            navigation.navigate(R.id.action_searchFragment_to_recipeSummaryActivity, bundle)
                        }
                    }
                }
        }
    }

    private fun setEventObservers() {
        lifecycleScope.launchWhenStarted {
            viewModel.liveKeyword.debounce(500)
                .collect {
                    viewModel.updateSearchKeyword()
                }
        }

        viewModel.liveTabState.observe(viewLifecycleOwner) {
            viewModel.updateSearchKeyword()
        }

        viewModel.eventSearchRecipe.observe(viewLifecycleOwner, EventObserver {
            when (it) {
                is SearchViewModel.SearchRecipeEvent.Exception -> {
                    when (it.error) {
                        ResponseError.NETWORK_CONNECTION, ResponseError.SERVER ->
                            showSnackBar(it.error.stringRes)
                        else -> ConfirmDialogBuilder.create(
                            context ?: return@EventObserver,
                            getString(R.string.errorOccurs),
                            getString(it.error.stringRes)
                        ) { }
                    }
                }
            }
        })
    }

    private fun showSnackBar(@StringRes resId: Int) {
        Snackbar.make(binding.root, resId, Snackbar.LENGTH_LONG).show()
    }

    private fun focusInput() {
        binding.editTextSearch.requestFocus()
        val inputMethodManager =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager ?: return
        inputMethodManager.showSoftInput(binding.editTextSearch, InputMethodManager.SHOW_IMPLICIT)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}