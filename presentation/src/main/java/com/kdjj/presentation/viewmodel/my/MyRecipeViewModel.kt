package com.kdjj.presentation.viewmodel.my

import androidx.lifecycle.*
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
    private val getRecipeUpdateFlowUseCase: FlowUseCase<EmptyRequest, Int>
) : ViewModel() {

    private val _liveSortType = MutableLiveData<SortType>()
    val liveSortType: LiveData<SortType> get() = _liveSortType

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

    private val _liveRecipeList = MutableLiveData(listOf<MyRecipeItem.MyRecipe>())
    private val _liveFetching = MutableLiveData(false)
    val liveRecipeItemList: LiveData<List<MyRecipeItem>> = MediatorLiveData<List<MyRecipeItem>>().apply {
        addSource(_liveRecipeList) { recipeList ->
            value = if (_liveFetching.value == true) {
                listOf(MyRecipeItem.PlusButton) + recipeList + MyRecipeItem.Progress
            } else {
                listOf(MyRecipeItem.PlusButton) + recipeList
            }
        }

        addSource(_liveFetching) { isFetching ->
            _liveRecipeList.value?.let { recipeList ->
                value = if (isFetching) {
                    listOf(MyRecipeItem.PlusButton) + recipeList + MyRecipeItem.Progress
                } else {
                    listOf(MyRecipeItem.PlusButton) + recipeList
                }
            }
        }
    }

    private var job: Job? = null

    init {
        setSortType(SortType.SORT_BY_TIME)

        viewModelScope.launch {
            getRecipeUpdateFlowUseCase(EmptyRequest)
                    .collect {
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
        if (_liveFetching.value == true && page > 0) return
        _liveFetching.value = true
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
                if (page == 0) {
                    _liveRecipeList.value = myRecipeList
                } else {
                    _liveRecipeList.value = _liveRecipeList.value?.plus(myRecipeList)
                }
            }.onFailure {
                if (it !is CancellationException) {
                    _eventDataLoadFailed.value = Event(Unit)
                }
            }
            _liveFetching.value = false
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
