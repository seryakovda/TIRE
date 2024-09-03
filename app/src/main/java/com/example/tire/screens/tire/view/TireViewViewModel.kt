package com.example.tire.screens.tire.view

import androidx.lifecycle.ViewModel
import com.example.tire.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TireViewViewModel @Inject constructor(
    private var repository: Repository,
): ViewModel()  {
    fun getSessionHandle():String{
        return repository.getSessionHandle()
    }
    fun getIdImage():String{
        return repository.getIdImageForViewFullScreen()
    }
}