package com.kdjj.presentation.view.home.search

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.request.SaveLocalRecipeRequest
import com.kdjj.domain.usecase.UseCase
import com.kdjj.presentation.R
import com.kdjj.presentation.common.EventObserver
import com.kdjj.presentation.databinding.FragmentSearchRecipeBinding
import com.kdjj.presentation.model.RecipeEditorItem
import com.kdjj.presentation.view.adapter.OthersRecipeListAdapter
import com.kdjj.presentation.view.dialog.ConfirmDialogBuilder
import com.kdjj.presentation.viewmodel.search.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class SearchRecipeFragment : Fragment() {

    private var _binding: FragmentSearchRecipeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModels()

    private lateinit var resultListAdapter: OthersRecipeListAdapter

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
        resultListAdapter = OthersRecipeListAdapter()

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