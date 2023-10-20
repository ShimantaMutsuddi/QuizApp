package com.mutsuddi_s.quizapp.ui.homefragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mutsuddi_s.quizapp.data.model.Question
import com.mutsuddi_s.quizapp.data.repository.QuizRepository
import com.mutsuddi_s.quizapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: QuizRepository): ViewModel() {


    val  highestScore: LiveData<Int> = repository.highestScore


}