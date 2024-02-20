package com.example.piterbridges.presentation.bridges

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.piterbridges.data.remote.repository.BridgesRepository
import com.example.piterbridges.presentation.LoadState
import com.example.piterbridges.presentation.bridges.model.Bridge
import kotlinx.coroutines.launch
import javax.inject.Inject

class ListBridgesViewModel @Inject constructor(
    private val repository: BridgesRepository,
) : ViewModel() {

    private val _bridgesLiveData = MutableLiveData<LoadState<List<Bridge>>>()
    val bridgesLiveData: LiveData<LoadState<List<Bridge>>> = _bridgesLiveData

    fun loadBridges() {
        viewModelScope.launch {
            _bridgesLiveData.postValue(LoadState.Loading())
            try {
                val bridges = repository.getBridges()
                _bridgesLiveData.postValue(LoadState.Data(bridges))
            } catch (e: Exception) {
                _bridgesLiveData.postValue(LoadState.Error(e))
            }
        }
    }
}