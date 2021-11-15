package com.kdjj.presentation.view.home.others

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.kdjj.domain.model.*
import com.kdjj.presentation.R
import com.kdjj.presentation.databinding.FragmentOthersRecipeBinding
import com.kdjj.presentation.view.adapter.OthersRecipeListAdapter
import com.kdjj.presentation.viewmodel.others.OthersViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OthersRecipeFragment : Fragment() {

    private var _binding: FragmentOthersRecipeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: OthersViewModel by viewModels()

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
        setBinding()
        initToolBar()
        setAdapter()
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
        }
    }

    private fun setAdapter() {
        val adapter = OthersRecipeListAdapter()
        binding.recyclerViewOthersRecipe.adapter = adapter
        adapter.submitList(
            getDummyDataList()
        )
    }
    fun getDummyDataList(): List<Recipe> =
        listOf(
            Recipe(
                authorId = "dsfsdfsdf",
                recipeId = "sdfs2",
                state = RecipeState.CREATE,
                stuff = "양파 고기 고기 \n 많이 많이 많이 ",
                isFavorite = false,
                stepList =listOf(
                    RecipeStep(
                        stepId = "xcvbb1",
                        seconds = 60,
                        description = "다져 다져 댜져 ",
                        type = RecipeStepType.FRY,
                        name = "끓여 끓여 끓여 ",
                        imgPath = "https://avatars.githubusercontent.com/u/46339857?v=4"
                    ),
                    RecipeStep(
                        stepId = "xcv1233",
                        seconds = 200,
                        description = "기다려 기다려",
                        type = RecipeStepType.FRY,
                        name = "패기",
                        imgPath = "https://avatars.githubusercontent.com/u/46339857?v=4"
                    ),
                ),
                viewCount = 0,
                title = "백종원의 김치가 가가가!!",
                type = RecipeType(1, "한식"),
                createTime = System.currentTimeMillis() - 1000*60,
                imgPath = "https://avatars.githubusercontent.com/u/46339857?v=4",
            ),
            Recipe(
                authorId = "dsfsdfsdf",
                recipeId = "sdfs11",
                state = RecipeState.CREATE,
                stuff = "닭가슴살 50g \n 계란 300개 ",
                isFavorite = false,
                stepList =listOf(
                    RecipeStep(
                        stepId = "xcvbb1",
                        seconds = 60,
                        description = "프라이팬에 구워",
                        type = RecipeStepType.FRY,
                        name = "계란 후라이 ",
                        imgPath = "https://avatars.githubusercontent.com/u/46339857?v=4"
                    ),
                    RecipeStep(
                        stepId = "xcv1233",
                        seconds = 200,
                        description = "팬에 볶아 주세요",
                        type = RecipeStepType.FRY,
                        name = "고기 볶기",
                        imgPath = "https://avatars.githubusercontent.com/u/46339857?v=4"
                    ),
                ),
                viewCount = 0,
                title = "백종원의 김치가 가가가!!",
                type = RecipeType(1, "한식"),
                createTime = System.currentTimeMillis() - 1000*60,
                imgPath = "https://avatars.githubusercontent.com/u/46339857?v=4",
            )
        )
}