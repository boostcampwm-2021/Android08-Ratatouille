package com.kdjj.presentation.view.home.my

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kdjj.presentation.R
import com.kdjj.presentation.common.DisplayConverter
import com.kdjj.presentation.common.EventObserver
import com.kdjj.presentation.databinding.FragmentMyRecipeBinding
import com.kdjj.presentation.view.adapter.MyRecipeListAdapter
import com.kdjj.presentation.viewmodel.my.MyRecipeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyRecipeFragment : Fragment() {

    private var _binding: FragmentMyRecipeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MyRecipeViewModel by activityViewModels()
    private val myRecipeAdapter by lazy { MyRecipeListAdapter(viewModel) }
    private val navigation by lazy { Navigation.findNavController(binding.root) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_recipe, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.myViewModel = viewModel
        DisplayConverter.setDensity(resources.displayMetrics.density)
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
        viewModel.liveAddRecipeHasPressed.observe(viewLifecycleOwner, EventObserver {
            navigation.navigate(R.id.action_myRecipeFragment_to_recipeEditorActivity)
        })

        viewModel.liveItemDoubleClicked.observe(viewLifecycleOwner, EventObserver {
            Log.d("aaa", it.toString())
        })
    }

    private fun initRecyclerView() {
        binding.recyclerViewMyRecipe.apply {
            val deviceWidth = resources.displayMetrics.widthPixels
            val itemWidth = DisplayConverter.dpToPx(160).toInt()
            val spanCount = deviceWidth / itemWidth
            layoutManager = GridLayoutManager(requireContext(), spanCount)
            adapter = myRecipeAdapter

            addItemDecoration(SpacesItemDecoration(spanCount, deviceWidth, itemWidth))

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val lastVisibleItemPosition =
                        (layoutManager as GridLayoutManager).findLastVisibleItemPosition()
                    val itemCount = myRecipeAdapter.itemCount

                    if (!canScrollVertically(1) && lastVisibleItemPosition + 1 == itemCount) {
                        viewModel.fetchMoreRecipeData(lastVisibleItemPosition)
                    }
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}