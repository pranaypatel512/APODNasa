package com.pranay.apodnasa.ui

import androidx.lifecycle.ViewModel
import com.pranay.apodnasa.data.repository.LocalPictureRepository
import com.pranay.apodnasa.model.APODPictureItem
import com.pranay.apodnasa.model.State
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class PicturesViewModel @Inject constructor(private val localPictureRepository: LocalPictureRepository) :
    ViewModel() {

    private val _selectedItem: MutableStateFlow<State<APODPictureItem>> =
        MutableStateFlow(State.loading())

    val selectedItem: StateFlow<State<APODPictureItem>> = _selectedItem

    fun getAllMediaItems() = localPictureRepository.getAllMediaItems()

    /**
     * Call once user click on any picture. setup item to show in details screen
     */
    fun setSelection(selectedPicture: APODPictureItem) {
        _selectedItem.value = State.Success(selectedPicture)
    }
}