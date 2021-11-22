package com.kdjj.presentation.view.home.my

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.kdjj.presentation.R
import com.kdjj.presentation.common.*
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
            val bundle = bundleOf(
                RECIPE_ID to it.recipe.recipeId,
                RECIPE_STATE to it.recipe.state
            )
            navigation.navigate(R.id.action_myRecipeFragment_to_recipeSummaryActivity, bundle)
        })

        viewModel.eventDataLoadFailed.observe(viewLifecycleOwner, EventObserver {
            Snackbar.make(binding.root, getString(R.string.dataLoadFailMessage), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.refresh)){
                     viewModel.refreshRecipeList()
                }
                .setActionTextColor(requireContext().getColor(R.color.blue_500))
                .show()
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

            val displayWidth = resources.displayMetrics.widthPixels
            val itemWidth = resources.getDimensionPixelSize(R.dimen.myRecipe_cardView_width)
            val spanCount = maxOf(displayWidth / itemWidth, 2)
            layoutManager = GridLayoutManager(requireContext(), spanCount)
            adapter = myRecipeAdapter

            val space = resources.getDimensionPixelSize(R.dimen.myRecipe_space_size)
            addItemDecoration(SpacesItemDecoration(space))

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val lastVisibleItemPosition = (layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                    val lastItemPosition = myRecipeAdapter.itemCount - 1
                    if (lastVisibleItemPosition == lastItemPosition && myRecipeAdapter.itemCount != 0 && dy > 0) {
                        viewModel.fetchRecipeList(lastVisibleItemPosition)
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