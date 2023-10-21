package com.mutsuddi_s.quizapp.ui.quizfragment

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.snackbar.Snackbar
import com.mutsuddi_s.quizapp.R
import com.mutsuddi_s.quizapp.data.model.Question
import com.mutsuddi_s.quizapp.databinding.FragmentQuizBinding
import com.mutsuddi_s.quizapp.utils.Resource
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class QuizFragment : Fragment() {


    // View Binding for this fragment
    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!

    // View Model for handling quiz-related data and logic
    private val viewModel: QuizViewModel by viewModels()

    // Total number of questions in the quiz
    var totalQestion = 0

    // CountDownTimer for the quiz timer
    //private lateinit var countDownTimer: CountDownTimer
    private var remainingTimeMillis: Long = 10000
    private val intervalInMillis: Long = 1000
    private val handler = Handler(Looper.getMainLooper())

    // TAG for logging purposes
    private val TAG = "QuizFragment"


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe the quiz data from the ViewModel
        viewModel.questions.observe(viewLifecycleOwner){ response ->
            when(response)
            {

                is Resource.Success -> {
                    stopShimmer()
                    binding.shimmer.visibility=View.GONE

                    binding.mainLayout.visibility=View.VISIBLE

                    val questions = response.data
                    totalQestion= questions!!.size
                    Log.d(TAG, "totalQestion: "+totalQestion)

                    if (questions?.isNotEmpty()!!) {
                        viewModel.currentIndex.observe(viewLifecycleOwner, Observer { currentIndex ->

                            //countDownTimer.start()
                            viewModel.startProgressBar()
                            resetAnswerOptionStyles()
                            displayQuestion(questions[currentIndex],currentIndex)



                        })
                    } else {

                    }
                }

                is Resource.Error -> {

                    Snackbar.make(view, response.errorMessage.toString(), Snackbar.LENGTH_SHORT).show();

                }


                is Resource.Loading ->{


                }

            }

        }

        // Set up the time progress bar based on the remaining time
       // binding.timeProgressBar.max = (remainingTimeMillis / intervalInMillis).toInt()

        // Observe the player's score
        viewModel.score.observe(viewLifecycleOwner){
            Log.d(TAG, "onViewCreated: $it")
            binding.score.text="Score: $it"
        }

        // Observe navigation events
        viewModel.checkNavigation.observe(viewLifecycleOwner){
            if(it)
            {
                findNavController().navigate(R.id.action_quizFragment_to_homeFragment)
                viewModel.setNavigationFalse()

            }
        }

        //check
        binding.composeTimer.setContent {
            timer()
        }

        viewModel.setOnProgressCompleteListener {
           viewModel.moveToNextQuestion()
        }

        // Initialize the CountDownTimer
        /*countDownTimer = object : CountDownTimer(remainingTimeMillis, intervalInMillis) {
            override fun onTick(millisUntilFinished: Long) {


                val safeBinding = _binding
                if (safeBinding != null) {

                    val progress = ((remainingTimeMillis - millisUntilFinished) / intervalInMillis).toInt()
                    // safeBinding.quiz.text=progress.toString()

                    safeBinding.tvProgress.text = "${millisUntilFinished / 1000} s"
                   // safeBinding.timeProgressBar.progress = progress
                }
            }


            override fun onFinish() {

                val safeBinding = _binding
                if (safeBinding != null) {
                    safeBinding.progressBar.progress = 0
                }
                viewModel.moveToNextQuestion()
            }
        }*/
    }

    @Composable
    fun timer(){

        ProgressBar(viewModel)
    }


    // Reset the styles of answer options
    private fun resetAnswerOptionStyles() {
        val options = listOf( binding.optionOne,  binding.optionTwo,  binding.optionThree,  binding.optionFour)
        for (optionView in options) {
            optionView.setBackgroundResource(R.drawable.bg_card)
            optionView.setTextColor(Color.BLACK)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.setCurrentIndexZero()
    }

    private fun startShimmer() {
        val safeBinding = _binding
        safeBinding?.shimmer?.startShimmer() // Start shimmer effect
    }

    override fun onPause() {
        stopShimmer()
        super.onPause()
    }
    private fun stopShimmer() {
        val safeBinding = _binding
        safeBinding?.shimmer?.stopShimmer()


    }



    // Display a question and its answer options
    private fun displayQuestion(question: Question, currentIndex: Int) {

        // Show answer options
        binding.optionOne.visibility= View.VISIBLE
        binding.optionTwo.visibility= View.VISIBLE
        binding.optionThree.visibility= View.VISIBLE
        binding.optionFour.visibility= View.VISIBLE

        // Shuffle the answer options for randomness
        val shuffledAnswers = listOf(
            question.answers.A,
            question.answers.B,
            question.answers.C,
            question.answers.D
        ).shuffled()
        val answerOptions = listOf(
            binding.optionOne,
            binding.optionTwo,
            binding.optionThree,
            binding.optionFour
        )


        // Populate answer options with shuffled answers
        for (i in 0 until shuffledAnswers.size) {
            val answer = shuffledAnswers.getOrNull(i)
            val answerOption = answerOptions[i]
            answerOption.text = answer
            if (answer != null) {

                answerOption.text = answerOption.text
                answerOption.visibility = View.VISIBLE
            } else {
                answerOption.text = ""
                answerOption.visibility = View.INVISIBLE
            }
        }

        // Set the question text and score
        binding.question.text = question.question
        binding.txtPoint.text = question.score.toString()



        binding.questionNumber.text="Question: ${currentIndex+1}/$totalQestion"


        // Load question image using Glide
        val placeholderDrawable = R.drawable.logo
        Glide.with(requireContext())
            .load(question.questionImageUrl)
            .placeholder(placeholderDrawable)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.image ?: ImageView(context).apply { setImageResource(placeholderDrawable) })

        // Set click listeners for answer options
        binding.optionOne.setOnClickListener { checkAnswer(
            question,
            question.answers.A,
            binding.optionOne) }
        binding.optionTwo.setOnClickListener { checkAnswer(
            question,
            question.answers.B,
            binding.optionTwo
        ) }
        binding.optionThree.setOnClickListener { checkAnswer(
            question,
            question.answers.C,
            binding.optionThree
        ) }
        binding.optionFour.setOnClickListener { checkAnswer(
            question,
            question.answers.D,
            binding.optionFour
        ) }


    }

    private fun checkAnswer(question: Question, selectedAnswer: String, option: TextView) {


        viewModel.cancelProgressBar()
        val correctAnswer = question.correctAnswer
        val answerMap = mapOf(
            "A" to question.answers.A,
            "B" to question.answers.B,
            "C" to question.answers.C,
            "D" to question.answers.D
        )


        Log.d(TAG, "checkAnswer: ${answerMap[correctAnswer]}")
        Log.d(TAG, "selectedAnswer: $selectedAnswer")
        Log.d(TAG, "selectedAnswer2: ${option.text}")
        //Log.d(TAG, "question.correctAnswer: ${question.answers.selectedAnswer}")
        if (answerMap[correctAnswer] == option.text) {
            // Handle correct answer
            // Toast.makeText(requireActivity(),"Right answer",Toast.LENGTH_SHORT).show()
            //Log.d(TAG, "checkAnswer: ${question.score}")
            option.setBackgroundResource(R.drawable.correct_option_border_bg)
            viewModel.totalScore(question.score)




        } else {
            // Handle wrong answer

            option.setBackgroundResource(R.drawable.wrong_option_border_bg)
            showCorrectAnswer(answerMap[correctAnswer])
        }
        handler.postDelayed({ viewModel.moveToNextQuestion() }, 2000)

        binding.optionOne.isClickable = false
        binding.optionTwo.isClickable = false
        binding.optionThree.isClickable = false
        binding.optionFour.isClickable = false
    }

    private fun showCorrectAnswer(correctAnswer: String?) {
        when (correctAnswer) {

            binding.optionOne.text.toString() -> binding.optionOne.setBackgroundResource(R.drawable.correct_option_border_bg)
            binding.optionTwo.text.toString() -> binding.optionTwo.setBackgroundResource(R.drawable.correct_option_border_bg)
            binding.optionThree.text.toString() -> binding.optionThree.setBackgroundResource(R.drawable.correct_option_border_bg)
            binding.optionFour.text.toString() -> binding.optionFour.setBackgroundResource(R.drawable.correct_option_border_bg)

        }
    }





}