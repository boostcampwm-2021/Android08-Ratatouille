package com.kdjj.presentation.view.home.my

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.kdjj.presentation.R
import com.kdjj.presentation.databinding.FragmentMyRecipeBinding
import com.kdjj.presentation.view.adapter.MyRecipeListAdapter
import com.kdjj.presentation.viewmodel.my.MyRecipeViewModel

class MyRecipeFragment : Fragment() {

    private var _binding: FragmentMyRecipeBinding? = null
    private val binding get() = _binding!!
    private val myRecipeAdapter by lazy { MyRecipeListAdapter() }
    private val viewModel: MyRecipeViewModel by viewModels()
    private val navigation by lazy { Navigation.findNavController(binding.root) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_recipe, container, false)
        binding.lifecycleOwner = this
        binding.myViewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar()
        setObservers()
        initRecyclerView()
    }

    private fun initToolBar() {
        binding.toolbarMyRecipe.apply {
            setTitle(R.string.myRecipe)
            inflateMenu(R.menu.toolbar_menu_search_item)
        }
    }

    private fun setObservers() {
        viewModel.liveSortType.observe(viewLifecycleOwner) { sortType ->
            when(sortType){
                SortType.SORT_BY_TIME -> viewModel.fetchLocalLatestRecipeList()
                SortType.SORT_BY_FAVORITE -> viewModel.fetchLocalFavoriteRecipeList()
            }
        }
    }

    private fun initRecyclerView() {
        binding.recyclerViewMyRecipe.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = myRecipeAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}