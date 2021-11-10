package com.kdjj.presentation.view.home.my

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.kdjj.presentation.R
import com.kdjj.presentation.databinding.FragmentMyRecipeBinding

class MyRecipeFragment : Fragment() {

    private var _binding: FragmentMyRecipeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_recipe, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar()
        initRadioButtons()
    }

    private fun initToolBar() {
        binding.toolbarMyRecipe.apply {
            setTitle(R.string.myRecipe)
            inflateMenu(R.menu.toolbar_menu_search_item)
        }
    }

    private fun initRadioButtons() {
        binding.radioGroup.setOnCheckedChangeListener { group, _ ->
            when (group.checkedRadioButtonId) {
                R.id.radioButton_orderByDate -> {
                    Log.d("aaa", "1")
                }
                R.id.radioButton_orderByFavorite -> {
                    Log.d("aaa", "2")
                }
                R.id.radioButton_orderByName -> {
                    Log.d("aaa", "3")
                }
            }
        }
    }
}