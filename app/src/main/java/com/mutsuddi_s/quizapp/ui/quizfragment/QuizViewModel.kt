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



    // The current progress of the bar, encapsulated in a mutable State
    private val _progress = mutableStateOf(1)
    val progress: State<Int> = _progress

    // A reference to the background job that updates the progress
    private var job: Job? = null

    // A callback to execute when the progress is complete
    private var onProgressComplete: (() -> Unit)? = null

    /**
     * Set a callback to be invoked when the progress completes.
     *
     * @param listener The function to be executed when the progress reaches completion.
     */
    fun setOnProgressCompleteListener(listener: () -> Unit) {
        onProgressComplete = listener
    }

    /**
     * Start the progress bar and update its state over a duration of 10 seconds.
     * The callback set with [setOnProgressCompleteListener] will be executed upon completion.
     */
    fun startProgressBar() {
        // Cancel any existing job to avoid multiple progress updates
        job?.cancel()
        job = viewModelScope.launch {
            for (i in 1..10) {
                delay(1000) // Update the progress every second (10 seconds total).
                _progress.value = i
            }
            // Invoke the completion callback, if set
            onProgressComplete?.invoke()
        }
    }

    /**
     * Cancel the progress bar, stopping any ongoing progress updates.
     */
    fun cancelProgressBar() {
        job?.cancel()
    }

}
