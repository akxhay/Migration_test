package com.cb.migrationtest

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val repository: UserRepositoryImpl) :
    ViewModel() {

    val users: LiveData<List<UserRecord>>
        get() =
            repository.users.flowOn(Dispatchers.IO)
                .asLiveData(context = viewModelScope.coroutineContext)

    suspend fun insertUser(userRecord: UserRecord): Long {
        return repository.insertUser(userRecord)
    }

    fun deleteUser(
        id: Int?
    ) {
        viewModelScope.launch {
            repository.deleteUser(id)
        }
    }
}
