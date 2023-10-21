package com.mutsuddi_s.quizapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mutsuddi_s.quizapp.data.local.SharedPreferencesManager
import com.mutsuddi_s.quizapp.data.model.Question
import com.mutsuddi_s.quizapp.data.remote.ApiInterface
import com.mutsuddi_s.quizapp.utils.NetworkChecker
import com.mutsuddi_s.quizapp.utils.Resource
import javax.inject.Inject

/**
 * QuizRepository is a class responsible for managing the data related to a quiz app.
 *
 * @constructor Creates a QuizRepository instance with the provided dependencies.
 * @param apiInterface An instance of ApiInterface used for making network requests to fetch quiz questions.
 * @param networkChecker An instance of NetworkChecker to check for internet connectivity.
 * @param sharedPreferences An instance of SharedPreferencesManager for managing local storage of data.
 */
class QuizRepository @Inject constructor(
    private val apiInterface: ApiInterface,
    private val networkChecker: NetworkChecker,
    private val sharedPreferences: SharedPreferencesManager
){

    private val TAG = "QuizRepository"

    // LiveData to hold the list of quiz questions along with their loading status.
    private val _questions = MutableLiveData<Resource<List<Question>>>()

    // Accessible LiveData for observing the list of quiz questions.
    val questions: LiveData<Resource<List<Question>>>
        get() = _questions

    // LiveData to keep track of the current question index in the quiz.
    private val _currentIndex = MutableLiveData(0)
    val currentIndex: LiveData<Int> = _currentIndex

    // LiveData to keep track of the user's score in the quiz.
    private val _score = MutableLiveData(0)
    val score: LiveData<Int> = _score

    // LiveData to store and observe the highest score achieved in the quiz.
    private val _highestScore = MutableLiveData(sharedPreferences.getHighestScore())
    val highestScore: LiveData<Int> = _highestScore

    // LiveData to control the navigation within the quiz.
    private val _checkNavigation = MutableLiveData<Boolean>().apply { value = false }
    val checkNavigation: LiveData<Boolean> = _checkNavigation

    /**
     * Fetches a list of quiz questions from a remote data source and updates the _questions LiveData accordingly.
     */
    suspend fun getAllQuestions() {
        _questions.postValue(Resource.Loading())
        var isNetwork: Boolean = networkChecker.isNetworkAvailable()
        if (isNetwork) {
            try {
                val result = apiInterface.getAllQues()
                Log.d(TAG, "getAllQuestions: $result}")
                if (result.body() != null) {
                    _questions.postValue(Resource.Success(result.body()!!.questions))
                } else {
                    _questions.postValue(Resource.Error("Network Failure"))
                }
            } catch (e: Exception) {
                _questions.postValue(Resource.Error(e.message.toString()))
            }
        } else {
            _questions.postValue(Resource.Error("No internet connection"))
        }
    }

    /**
     * Moves to the next quiz question by updating the _currentIndex LiveData.
     * If there are no more questions, it sets _checkNavigation to true to signal the end of the quiz.
     */
    fun moveToNextQuestion() {
        val current = _currentIndex.value ?: 0
        val questionsList = _questions.value?.data ?: emptyList()

        if (current < questionsList.size - 1) {
            _currentIndex.value = current + 1
        } else {
            _checkNavigation.value = true
        }
    }

    /**
     * Updates the user's total score by adding the provided points.
     * Also, checks if the updated score is a new highest score and saves it if necessary.
     *
     * @param point The points to be added to the user's total score.
     */
    fun totalScore(point: Int) {
        _score.value = _score.value?.plus(point)
        if (_score.value!! > _highestScore.value ?: 0) {
            _highestScore.value = _score.value
            saveHighestScore(_score.value)
        }
    }

    /**
     * Saves the highest score achieved in the quiz to local storage using SharedPreferencesManager.
     *
     * @param score The highest score to be saved.
     */
    private fun saveHighestScore(score: Int?) {
        sharedPreferences.saveHighestScore(score!!)
    }

    /**
     * Resets the _checkNavigation value to false, indicating that the user is not navigating within the quiz.
     */
    fun setNavigationFalse() {
        _checkNavigation.value = false
    }

    /**
     * Resets the current question index and user's score to zero.
     */
    fun setCurrentIndexZero() {
        _currentIndex.value = 0
        _score.value = 0
    }
}


