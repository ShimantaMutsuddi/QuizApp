package com.mutsuddi_s.quizapp.ui.homefragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.mutsuddi_s.quizapp.R
import com.mutsuddi_s.quizapp.databinding.FragmentHomeBinding
import com.mutsuddi_s.quizapp.ui.quizfragment.QuizViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.highestScore.observe(viewLifecycleOwner){

            binding.score.text="${it.toString()} point"

        }
        binding.btnStart.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_quizFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}