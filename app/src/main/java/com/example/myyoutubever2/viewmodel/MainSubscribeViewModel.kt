package com.example.myyoutubever2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myyoutubever2.database.entity.SubscribeDB
import com.example.myyoutubever2.database.repository.SubscribeRepository
import kotlinx.coroutines.launch

class MainSubscribeViewModel : ViewModel() {
    private val repo = SubscribeRepository()

    private val _mySubscribeList = MutableLiveData<List<SubscribeDB>>()
    val mySubscriList: LiveData<List<SubscribeDB>> = _mySubscribeList

    fun getMySubscribeList(seq: Int) {
         viewModelScope.launch {
            _mySubscribeList.value = repo.getMySubscribeList(seq)
        }
    }
}