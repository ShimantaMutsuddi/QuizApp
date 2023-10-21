package com.mutsuddi_s.quizapp.ui.quizfragment

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mutsuddi_s.quizapp.data.model.Question
import com.mutsuddi_s.quizapp.data.repository.QuizRepository
import com.mutsuddi_s.quizapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(private val repository: QuizRepository): ViewModel() {
    private val TAG = "MainViewModel"
    /**
     * Initialize the ViewModel by launching a coroutine in the IO dispatcher to fetch all the questions from the repository.
     * This initialization can be done in the ViewModel's constructor to load the questions when the ViewModel is created.
     */
    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllQuestions()
        }
    }

    /**
     * LiveData property that provides access to the list of quiz questions with their associated states (loading, success, error).
     */
    val questions: LiveData<Resource<List<Question>>>
        get()=repository.questions

    /**
     * LiveData property that holds the current index (position) of the quiz question being displayed.
     */
    val currentIndex: LiveData<Int> = repository.currentIndex

    /**
     * LiveData property that represents whether the app should navigate to the next question.
     */
    val checkNavigation: LiveData<Boolean> = repository.checkNavigation

    /**
     * LiveData property that represents the user's current quiz score.
     */
    val score: LiveData<Int> = repository.score

    /**
     * Function to update the user's total score by adding the given points.
     *
     * @param point: The number of points to add to the user's score.
     */
    fun totalScore(point:Int) {
        repository.totalScore(point)
    }

    /**
     * Function to move to the next question in the quiz.
     * This function triggers navigation to the next question in the quiz sequence.
     */
    fun moveToNextQuestion() {
        repository.moveToNextQuestion()
    }

    /**
     * Function to set the checkNavigation LiveData to false.
     * It is typically used after the user navigates to the next question.
     */
    fun setNavigationFalse() {
        repository.setNavigationFalse()
    }

    /**
     * Function to reset the currentIndex LiveData to zero.
     * It is typically used when the quiz is restarted or reset to the first question.
     */
    fun setCurrentIndexZero() {
        repository.setCurrentIndexZero()
    }




    private val _progress = mutableStateOf(0)
    val progress: State<Int> = _progress

    private val _isRunning = mutableStateOf(false)
    val isRunning: State<Boolean> = _isRunning

    private var job: Job? = null

    private var onProgressComplete: (() -> Unit)? = null

    fun setOnProgressCompleteListener(listener: () -> Unit) {
        onProgressComplete = listener
    }

    fun startProgressBar() {
        job?.cancel()
        _isRunning.value = true
        job = viewModelScope.launch {
            for (i in 1..10) {
                delay(1000) // Update the progress every second (10 seconds total).
                _progress.value = i
            }
            _isRunning.value = false
            onProgressComplete?.invoke()
        }
    }

    fun cancelProgressBar() {
        job?.cancel()
        _isRunning.value = false
    }

}
