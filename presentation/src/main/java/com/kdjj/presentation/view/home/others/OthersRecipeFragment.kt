package com.kdjj.presentation.view.home.others

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.kdjj.domain.model.*
import com.kdjj.presentation.R
import com.kdjj.presentation.databinding.FragmentOthersRecipeBinding
import com.kdjj.presentation.view.adapter.OthersRecipeListAdapter

class OthersRecipeFragment : Fragment() {

    private var _binding: FragmentOthersRecipeBinding? = null
    private val binding get() = _binding!!

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
        setAdapter()
    }

    private fun setBinding() {
        with(binding) {
            lifecycleOwner = this@OthersRecipeFragment.viewLifecycleOwner
        }
    }

    private fun setAdapter() {
        val adapter = OthersRecipeListAdapter()
        binding.recyclerViewOthersRecipe.adapter = adapter
//        adapter.submitList(
//            listOf(
//                getDummyData("adsfsd"),
//                getDummyData("sdfsd2"),
//            )
//        )
    }
    fun getDummyData(id: String): Recipe =
        Recipe(
            authorId = "dsfsdfsdf",
            recipeId = id,
            state = RecipeState.CREATE,
            stuff = "양파 고기 고기 \n 많이 많이 많이 ",
            isFavorite = false,
            stepList =listOf(
                RecipeStep(
                    stepId = "xcvbb1",
                    seconds = 123,
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
            createTime = System.currentTimeMillis(),
            imgPath = "https://avatars.githubusercontent.com/u/46339857?v=4",
        )
}