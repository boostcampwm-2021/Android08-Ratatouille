package com.kdjj.presentation.viewmodel.my

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.request.*
import com.kdjj.domain.usecase.FlowUseCase
import com.kdjj.domain.usecase.ResultUseCase
import com.kdjj.presentation.common.Event
import com.kdjj.presentation.model.MyRecipeItem
import com.kdjj.presentation.model.SortType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MyRecipeViewModel @Inject constructor(
    private val latestRecipeUseCase: ResultUseCase<FetchLocalLatestRecipeListRequest, List<Recipe>>,
    private val favoriteRecipeUseCase: ResultUseCase<FetchLocalFavoriteRecipeListRequest, List<Recipe>>,
    private val titleRecipeUseCase: ResultUseCase<FetchLocalTitleRecipeListRequest, List<Recipe>>,
    private val getRecipeUpdateFlowRequest: FlowUseCase<EmptyRequest, Int>
) : ViewModel() {

    private val _liveSortType = MutableLiveData<SortType>()
    val liveSortType: LiveData<SortType> get() = _liveSortType

    private val _liveRecipeItemList = MutableLiveData<List<MyRecipeItem>>()
    val liveRecipeItemList: LiveData<List<MyRecipeItem>> get() = _liveRecipeItemList

    private val _liveRecipeItemSelected = MutableLiveData<MyRecipeItem.MyRecipe?>()
    val liveRecipeItemSelected: LiveData<MyRecipeItem.MyRecipe?> get() = _liveRecipeItemSelected

    private val _eventItemDoubleClicked = MutableLiveData<Event<MyRecipeItem.MyRecipe>>()
    val eventItemDoubleClicked: LiveData<Event<MyRecipeItem.MyRecipe>> get() = _eventItemDoubleClicked

    private val _eventSearchIconClicked = MutableLiveData<Event<Unit>>()
    val eventSearchIconClicked: LiveData<Event<Unit>> get() = _eventSearchIconClicked

    private val _eventAddRecipeHasPressed = MutableLiveData<Event<Unit>>()
    val eventAddRecipeHasPressed: LiveData<Event<Unit>> get() = _eventAddRecipeHasPressed

    private val _eventDataLoadFailed = MutableLiveData<Event<Unit>>()
    val eventDataLoadFailed: LiveData<Event<Unit>> get() = _eventDataLoadFailed

    private var isFetching = false
        set(value) {
            field = value
            notifyRecipeDataSetChanged()
        }

    private var recipeList: List<MyRecipeItem.MyRecipe> = listOf()
        set(value) {
            field = value
            notifyRecipeDataSetChanged()
        }

    private var job: Job? = null

    init {
        setSortType(SortType.SORT_BY_TIME)

        viewModelScope.launch {
            val updateFlow = getRecipeUpdateFlowRequest(EmptyRequest)
            updateFlow.collect {
                refreshRecipeList()
            }

        }
    }

    fun setSortType(sortType: SortType) {
        if (_liveSortType.value != sortType) {
            _liveSortType.value = sortType
            fetchRecipeList(0)
        }
    }

    fun refreshRecipeList() {
        fetchRecipeList(0)
    }

    fun fetchRecipeList(page: Int) {
        if (isFetching && page > 0) return
        isFetching = true
        if (page == 0) job?.cancel()

        job = viewModelScope.launch {
            when (_liveSortType.value) {
                SortType.SORT_BY_TIME ->
                    latestRecipeUseCase(FetchLocalLatestRecipeListRequest(page))
                SortType.SORT_BY_FAVORITE ->
                    favoriteRecipeUseCase(FetchLocalFavoriteRecipeListRequest(page))
                SortType.SORT_BY_NAME ->
                    titleRecipeUseCase(FetchLocalTitleRecipeListRequest(page))
                else -> return@launch
            }.onSuccess { fetchedRecipeList ->
                val myRecipeList = fetchedRecipeList.map { MyRecipeItem.MyRecipe(it) }
                _liveRecipeItemList.value?.let {
                    if (page == 0) {
                        recipeList = myRecipeList
                    } else {
                        recipeList = recipeList.plus(myRecipeList)
                    }
                }
            }.onFailure {
                if (it !is CancellationException) {
                    _eventDataLoadFailed.value = Event(Unit)
                }
            }
            isFetching = false
        }
    }

    private fun notifyRecipeDataSetChanged() {
        if (isFetching) {
            _liveRecipeItemList.value =
                listOf(MyRecipeItem.PlusButton) + recipeList + MyRecipeItem.Progress
        } else {
            _liveRecipeItemList.value = listOf(MyRecipeItem.PlusButton) + recipeList
        }
    }

    fun recipeItemSelected(selectedRecipe: MyRecipeItem.MyRecipe) {
        if (_liveRecipeItemSelected.value != selectedRecipe) {
            _liveRecipeItemSelected.value = selectedRecipe
        } else {
            _eventItemDoubleClicked.value = Event(selectedRecipe)
        }
    }

    fun moveToRecipeEditorActivity() {
        _eventAddRecipeHasPressed.value = Event(Unit)
    }

    fun moveToRecipeSearchFragment() {
        _eventSearchIconClicked.value = Event(Unit)
    }
}
