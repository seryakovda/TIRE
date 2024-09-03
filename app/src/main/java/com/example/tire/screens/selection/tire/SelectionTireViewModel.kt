package com.example.tire.screens.selection.tire

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tire.data.Repository
import com.example.tire.data.TireList
import com.example.tire.NavHostViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectionTireViewModel @Inject constructor(
    private var repository: Repository
): ViewModel() {

    private var _authState: MutableLiveData<NavHostViewModel.AuthState> = MutableLiveData(
        NavHostViewModel.AuthState.AUTH)

    val serialNumber: MutableLiveData<String> = MutableLiveData("")
    val chassisNumber: MutableLiveData<String> = MutableLiveData("")
    val tireList: MutableLiveData<List<TireList>> = MutableLiveData()

    val changeSerialNumber: (String) -> Unit = {
        serialNumber.value = it
        filter()
    }

    val changeСhassisNumber: (String) -> Unit = {
        chassisNumber.value = it
        filter()
    }


    ///////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////

    val selectedTire:(TireList) -> Unit = {
        viewModelScope.launch(Dispatchers.IO) {
            repository.setDataTireForEdit(it)
        }
    }

    val addTire:(String) -> Unit = {
        viewModelScope.launch(Dispatchers.IO) {
            tireList.postValue(repository.addTire(serialNumber.value))
        }
    }

    fun filter() {
        viewModelScope.launch(Dispatchers.IO) {
            tireList.postValue(repository.getListTire(serialNumber.value,chassisNumber.value))
        }
    }
}