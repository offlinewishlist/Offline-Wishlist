package com.example.calmlist.presentation.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calmlist.data.Repo.WishRepository
import com.example.calmlist.model.ResultState
import com.example.calmlist.model.Wish
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class WishDetailViewModel(
    private val repository: WishRepository
) : ViewModel() {

    val state = MutableStateFlow<ResultState<Unit>>(ResultState.Loading)

    fun editWish(wish: Wish) {
        viewModelScope.launch {
            repository.editWish(wish).collect {
                state.value = it
            }
        }
    }

    fun deleteWish(wishId: String) {
        viewModelScope.launch {
            repository.deleteWish(wishId).collect {
                state.value = it
            }
        }
    }
}