package com.pranay.apodnasa.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pranay.apodnasa.data.repository.LocalPictureRepository
import com.pranay.apodnasa.model.APODPictureItem
import com.pranay.apodnasa.model.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PicturesViewModel @Inject constructor(private val localPictureRepository: LocalPictureRepository) :
    ViewModel() {

    private val _picturesList: MutableStateFlow<State<List<APODPictureItem>>> =
        MutableStateFlow(State.loading())

    val pictures: StateFlow<State<List<APODPictureItem>>> = _picturesList

    init {
        getPictures()
    }

    fun getPictures() {
        viewModelScope.launch {
            localPictureRepository.getAllPictures()
                .map { resource -> State.fromResource(resource) }
                .collect { state -> _picturesList.value = state }
        }
    }
}