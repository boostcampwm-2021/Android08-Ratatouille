package com.kdjj.presentation.viewmodel.my

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kdjj.presentation.view.home.my.SortType
import javax.inject.Inject

internal class MyRecipeViewModel @Inject constructor(
) : ViewModel() {

    private val _liveSortType = MutableLiveData<SortType>(SortType.SORT_BY_TIME)
    val liveSortType: LiveData<SortType> get() = _liveSortType

    fun fetchLocalLatestRecipeList(){
        Log.d("aaa", "fetchLocalLatest")
    }

    fun fetchLocalFavoriteRecipeList(){
        Log.d("aaa", "fetchLocalFavorite")
    }

    fun setSortType(sortType: SortType){
        if(_liveSortType.value != sortType){
            _liveSortType.value = sortType
        }
    }
}