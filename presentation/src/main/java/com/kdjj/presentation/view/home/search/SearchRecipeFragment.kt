package com.kdjj.presentation.view.home.search

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kdjj.presentation.R
import com.kdjj.presentation.common.EventObserver
import com.kdjj.presentation.common.RECIPE_ID
import com.kdjj.presentation.common.RECIPE_STATE
import com.kdjj.presentation.databinding.FragmentSearchRecipeBinding
import com.kdjj.presentation.view.adapter.SearchRecipeListAdapter
import com.kdjj.presentation.view.dialog.ConfirmDialogBuilder
import com.kdjj.presentation.viewmodel.search.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class SearchRecipeFragment : Fragment() {

    private var _binding: FragmentSearchRecipeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModels()
    private val navigation by lazy { Navigation.findNavController(binding.root) }

    private lateinit var resultListAdapter: SearchRecipeListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_recipe, container, false)
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
        setObservers()
    }

    private fun setObservers() {
        Observable.create<Unit> { emitter ->
            viewModel.liveKeyword.observe(viewLifecycleOwner) {
                emitter.onNext(Unit)
            }
        }
            .debounce(500, TimeUnit.MILLISECONDS)
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                viewModel.updateSearchKeyword()
            }, {
                it.printStackTrace()
            })

        viewModel.eventException.observe(viewLifecycleOwner, EventObserver {
            ConfirmDialogBuilder.create(
                context ?: return@EventObserver,
                "오류",
                "오류가 발생했습니다."
            ) { }
        })

        viewModel.liveTabState.observe(viewLifecycleOwner) {
            viewModel.updateSearchKeyword()
        }

        viewModel.eventSummary.observe(viewLifecycleOwner, EventObserver {
            val bundle = bundleOf(
                RECIPE_ID to it.recipeId,
                RECIPE_STATE to it.state
            )
            navigation.navigate(R.id.action_searchFragment_to_recipeSummaryActivity, bundle)
        })
    }

    private fun focusInput() {
        binding.editTextSearch.requestFocus()
        val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager ?: return
        inputMethodManager.showSoftInput(binding.editTextSearch, InputMethodManager.SHOW_IMPLICIT)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}