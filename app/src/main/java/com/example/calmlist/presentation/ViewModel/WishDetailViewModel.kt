package com.example.calmlist.presentation.ViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calmlist.data.Repo.WishRepository
import com.example.calmlist.model.ResultState
import com.example.calmlist.model.Wish
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class WishDetailViewModel(
    private val repository: WishRepository,
) : ViewModel() {
    private val _HomeScreenstate = mutableStateOf(HomeScreenSate())
    val HomeScreensate = _HomeScreenstate
var userId: String?=null

    fun editWish(wish: Wish) {
        viewModelScope.launch {
            repository.editWish(wish).collect { result ->
                when (result) {
                    ResultState.Loading -> HomeScreenSate(isLoading = true)
                    is ResultState.Succes -> {
                        _HomeScreenstate.value = HomeScreenSate(
                            success = true,
                        )
                    }

                    is ResultState.error -> {
                        _HomeScreenstate.value = HomeScreenSate(error = result.message)
                    }
                }
            }
        }
    }
//    fun getuserid(){
//        viewModelScope.launch {
//            repository.collect { result ->
//        }
//    }

    fun deleteWish(wishId: String) {
        viewModelScope.launch {
            repository.deleteWish(wishId).collect { result ->
                when (result) {
                    ResultState.Loading -> HomeScreenSate(isLoading = true)
                    is ResultState.Succes -> {
                        _HomeScreenstate.value = HomeScreenSate(
                            success = true,
                        )

                    }

                    is ResultState.error -> {
                        _HomeScreenstate.value = HomeScreenSate(error = result.message)
                    }
                }
            }
        }
    }

    fun saveWish(wish: Wish) {
        viewModelScope.launch {
            repository.saveWishOffline(wish).collect { result ->
                when (result) {
                    ResultState.Loading -> HomeScreenSate(isLoading = true)
                    is ResultState.Succes -> {
                        _HomeScreenstate.value = HomeScreenSate(
                            success = true,
                        )

                    }

                    is ResultState.error -> {
                        _HomeScreenstate.value = HomeScreenSate(error = result.message)
                    }

                }

            }
        }
    }

    fun editWsh(wish: Wish) {
        viewModelScope.launch {
            repository.editWish(wish).collect { result ->
                when (result) {
                    ResultState.Loading -> HomeScreenSate(isLoading = true)
                    is ResultState.Succes -> {
                        _HomeScreenstate.value = HomeScreenSate(
                            success = true,
                        )
                    }

                    is ResultState.error -> {
                        _HomeScreenstate.value = HomeScreenSate(error = result.message)


                    }

                }
            }
        }
    }
    fun getAllWishes(userId: String) {
        viewModelScope.launch {
            repository.getLocalWishes(userId).collect { result ->
                when (result) {
                    ResultState.Loading -> {
                        _HomeScreenstate.value = HomeScreenSate(isLoading = true)
                    }

                    is ResultState.Succes -> {
                        _HomeScreenstate.value = HomeScreenSate(
                            success = true,
                            wishes = result.data
                        )
                    }

                    is ResultState.error -> {
                        _HomeScreenstate.value = HomeScreenSate(
                            error = result.message
                        )
                    }
                }
            }
        }
    }

}

data class HomeScreenSate(
    val isLoading: Boolean = false,
    val error: String? = null,
    val wishes: List<Wish> = emptyList(),

    val success: Boolean? = false,
)