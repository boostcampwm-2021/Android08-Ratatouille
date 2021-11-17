package com.kdjj.presentation.view.home.my

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
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
import javax.inject.Inject

@AndroidEntryPoint
class MyRecipeFragment : Fragment() {

    private var _binding: FragmentMyRecipeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MyRecipeViewModel by activityViewModels()
    private val myRecipeAdapter by lazy { MyRecipeListAdapter(viewModel) }
    private val navigation by lazy { Navigation.findNavController(binding.root) }

    @Inject
    lateinit var displayConverter: DisplayConverter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_recipe, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.myViewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar()
        setObservers()
        initSwipeRefreshLayout()
        initRecyclerView()
    }

    private fun initToolBar() {
        binding.toolbarMyRecipe.apply {
            setTitle(R.string.myRecipe)
            inflateMenu(R.menu.toolbar_menu_search_item)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.item_search -> {
                        viewModel.moveToRecipeSearchFragment()
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun setObservers() {
        viewModel.eventAddRecipeHasPressed.observe(viewLifecycleOwner, EventObserver {
            navigation.navigate(R.id.action_myRecipeFragment_to_recipeEditorActivity)
        })

        viewModel.eventSearchIconClicked.observe(viewLifecycleOwner, EventObserver {
            navigation.navigate(R.id.action_myRecipeFragment_to_searchRecipeFragment)
        })

        viewModel.eventItemDoubleClicked.observe(viewLifecycleOwner, EventObserver {
            //TODO: 레시피 개요 페이지로 이동
        })
    }

    private fun initSwipeRefreshLayout() {
        binding.swipeRefreshLayoutMy.apply {
            setOnRefreshListener {
                viewModel.refreshRecipeList()
                isRefreshing = false
            }
        }
    }

    private fun initRecyclerView() {
        binding.recyclerViewMyRecipe.apply {
            val deviceWidth = resources.displayMetrics.widthPixels
            val itemWidth = displayConverter.dpToPx(160).toInt()
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