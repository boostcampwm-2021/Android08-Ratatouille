package com.kdjj.presentation.view.home.my

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.RecipeState
import com.kdjj.domain.model.RecipeType
import com.kdjj.presentation.R
import com.kdjj.presentation.databinding.FragmentMyRecipeBinding
import com.kdjj.presentation.model.MyRecipeItem
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
        initRadioButtons()
        initRecyclerView()
    }

    private fun initToolBar() {
        binding.toolbarMyRecipe.apply {
            setTitle(R.string.myRecipe)
            inflateMenu(R.menu.toolbar_menu_search_item)
        }
    }

    private fun initRadioButtons() {
        binding.radioGroupMyRecipe.setOnCheckedChangeListener { group, _ ->
            when (group.checkedRadioButtonId) {
                R.id.radioButton_orderByDate -> Log.d("selected radio button", "최신순")
                R.id.radioButton_orderByFavorite -> Log.d("selected radio button", "즐겨찾기순")
                R.id.radioButton_orderByName -> Log.d("selected radio button", "이름순")
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