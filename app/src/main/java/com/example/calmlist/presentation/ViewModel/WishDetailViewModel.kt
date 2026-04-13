package com.example.calmlist.presentation.ViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calmlist.data.Repo.WishRepository
import com.example.calmlist.model.ResultState
import com.example.calmlist.model.Wish
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.UUID


class WishDetailViewModel(
    private val repository: WishRepository,
) : ViewModel() {
    private val _HomeScreenstate = mutableStateOf(HomeScreenSate())
    val HomeScreensate = _HomeScreenstate
var userId: String?=null
    private val _addWishState = mutableStateOf(AddWishState())
    val addWishState = _addWishState
    private val _selectedWishId = mutableStateOf<String?>(null)
    val selectedWishId = _selectedWishId

    private val _selectedWish = mutableStateOf<Wish?>(null)
    val selectedWish = _selectedWish

    fun setSelectedWishId(id: String) {
        _selectedWishId.value = id
        loadSelectedWish()
    }

    private fun loadSelectedWish() {
        val id = _selectedWishId.value ?: return


        val cachedWish = _HomeScreenstate.value.wishes.find { it.id == id }
        if (cachedWish != null) {
            _selectedWish.value = cachedWish
            return
        }

        viewModelScope.launch {
            repository.getLocalWishes(userId ?: "").collect { result ->
                if (result is ResultState.Succes) {
                    _selectedWish.value = result.data.find { it.id == id }
                }
            }
        }
    }


    fun updateTitle(value: String) {
        _addWishState.value = _addWishState.value.copy(title = value, error = null)
    }

    fun updateNote(value: String) {
        _addWishState.value = _addWishState.value.copy(note = value, error = null)
    }

    fun updateImage(path: String) {
        _addWishState.value = _addWishState.value.copy(imagePath = path, error = null)
    }

    fun updateAudio(path: String) {
        _addWishState.value = _addWishState.value.copy(audioPath = path, error = null)
    }

    fun submitWish(userId: String) {
        val state = _addWishState.value

        if (state.title.isBlank() &&
            state.imagePath.isNullOrBlank() &&
            state.audioPath.isNullOrBlank()
        ) {
            _addWishState.value = state.copy(
                error = "Add a title, image, or voice note before saving"
            )
            return
        }

        val wish = Wish(
            id = UUID.randomUUID().toString(),
            title = state.title.ifBlank { null },
            note = state.note.ifBlank { null },
            imagePath = state.imagePath,
            audioPath = state.audioPath,
            timestamp = System.currentTimeMillis(),
            userId = userId
        )

        saveWish(wish)


        _addWishState.value = AddWishState()
    }

    fun editWish(wish: Wish) {
        viewModelScope.launch {
            repository.editWish(wish).collect { result ->
                when (result) {
                    ResultState.Loading -> {
                        _HomeScreenstate.value = HomeScreenSate(isLoading = true)
                    }
                    is ResultState.Succes -> {
                        _HomeScreenstate.value = HomeScreenSate(success = true)
                    }

                    is ResultState.error -> {
                         _HomeScreenstate.value = HomeScreenSate(error = result.message)
                    }
                }
            }
        }
    }


    fun deleteWish(wishId: String) {
        viewModelScope.launch {
            repository.deleteWish(wishId).collect { result ->
                when (result) {
                    ResultState.Loading -> { } // Optional loading
                    is ResultState.Succes -> {
                       // Room Flow will handle the list update
                       _HomeScreenstate.value = HomeScreenSate(success = true)
                    }

                    is ResultState.error -> {
                         // Handle error
                    }
                }
            }
        }
    }

    fun saveWish(wish: Wish) {
        viewModelScope.launch {
            repository.saveWishOffline(wish).collect { result ->
                when (result) {
                    ResultState.Loading -> {
                        _addWishState.value = _addWishState.value.copy(isSaving = true)
                    }
                    is ResultState.Succes -> {
                        _addWishState.value = AddWishState(success = true) // Reset add wish state and set success
                    }

                    is ResultState.error -> {
                         _addWishState.value = _addWishState.value.copy(error = result.message, isSaving = false)
                    }
                }
            }
        }
    }


    fun getAllWishes(userId: String) {
        this.userId = userId
        viewModelScope.launch {
            repository.getLocalWishes(userId).collect { result ->
                when (result) {
                    ResultState.Loading -> {
                        _HomeScreenstate.value = HomeScreenSate(isLoading = true)
                    }

                    is ResultState.Succes -> {
                        _HomeScreenstate.value = HomeScreenSate(
                            success = false,
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

    fun resetAddWishState() {
        _addWishState.value = AddWishState()
    }

    fun resetHomeScreenState() {
        _HomeScreenstate.value = HomeScreenSate()
    }
}

data class HomeScreenSate(
    val isLoading: Boolean = false,
    val error: String? = null,
    val wishes: List<Wish> = emptyList(),

    val success: Boolean? = false,
)
data class AddWishState(
    val title: String = "",
    val note: String = "",
    val imagePath: String? = null,
    val audioPath: String? = null,
    val isSaving: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)
