package com.example.tire.screens.tire.view

import androidx.lifecycle.ViewModel
import com.example.tire.data.Repository
import com.example.tire.data.RemoteSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TireViewViewModel @Inject constructor(
    private val repository: Repository,
    val remoteSource: RemoteSource
) : ViewModel() {

    fun getSessionHandle(): String {
        return repository.getSessionHandle()
    }

    fun getIdImage(): String {
        return repository.getIdImageForViewFullScreen()
    }
}