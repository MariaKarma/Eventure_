package com.example.ev.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.ev.database.User
import com.example.ev.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(private val repository: UserRepository) : ViewModel() {

    private val testUser : LiveData<User>
    get() = repository.getUserByUid(uid).asLiveData(viewModelScope.coroutineContext)

    fun getUser() = repository.getUser().asLiveData(viewModelScope.coroutineContext)
    fun getUserByUid(uid: String)  = repository.getUserByUid(uid).asLiveData(viewModelScope.coroutineContext)

    fun deleteUser(){
        viewModelScope.launch {
            repository.deleteUser()
        }
    }

    fun updateUser(user: User) = viewModelScope.launch {
        repository.updateUser(user)
    }

    fun insertUser(user : User) = viewModelScope.launch {
        repository.insertUserRep(user)
    }
}
