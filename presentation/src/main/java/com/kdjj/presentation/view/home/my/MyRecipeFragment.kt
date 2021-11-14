package com.kdjj.presentation.view.home.my

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.kdjj.presentation.R
import com.kdjj.presentation.databinding.FragmentMyRecipeBinding
import com.kdjj.presentation.model.MyRecipeItem
import com.kdjj.presentation.view.adapter.MyRecipeListAdapter
import com.kdjj.presentation.viewmodel.my.MyRecipeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyRecipeFragment : Fragment() {

    private var _binding: FragmentMyRecipeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MyRecipeViewModel by  activityViewModels()
    private val myRecipeAdapter by lazy { MyRecipeListAdapter(viewModel) }
    private val navigation by lazy { Navigation.findNavController(binding.root) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
            when (sortType) {
                SortType.SORT_BY_TIME -> viewModel.fetchLocalLatestRecipeList()
                SortType.SORT_BY_FAVORITE -> viewModel.fetchLocalFavoriteRecipeList()
            }
        }

        viewModel.liveAddRecipeHasPressed.observe(viewLifecycleOwner){
            navigation.navigate(R.id.action_myRecipeFragment_to_recipeEditorActivity)
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