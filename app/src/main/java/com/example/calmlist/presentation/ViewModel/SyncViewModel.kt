package com.example.calmlist.presentation.ViewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calmlist.data.Repo.WishRepository
import com.example.calmlist.model.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SyncViewModel(
    private val repository: WishRepository
) : ViewModel() {

    val syncState = MutableStateFlow<ResultState<Unit>>(ResultState.Loading)

    fun sync(userId: String) {
        viewModelScope.launch {
            repository.syncFromCloud(userId).collect {
                syncState.value = it
            }
        }
    }
}