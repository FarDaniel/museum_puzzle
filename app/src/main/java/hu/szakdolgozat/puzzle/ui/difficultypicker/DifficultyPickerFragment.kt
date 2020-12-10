package hu.szakdolgozat.puzzle.ui.difficultypicker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import hu.szakdolgozat.puzzle.R
import hu.szakdolgozat.puzzle.databinding.FragmentDifficultyPickerBinding

class DifficultyPickerFragment : Fragment() {

    lateinit var navController: NavController
    lateinit var binding: FragmentDifficultyPickerBinding
    var userId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = arguments?.getInt("userId") ?: -1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDifficultyPickerBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        binding.difficultyPicker = this
    }

    fun start(isEasy: Boolean) {
        val bundle = bundleOf(
            "userId" to userId,
            "isEasy" to isEasy
        )
        if(isEasy)
        navController.navigate(
            R.id.action_difficultyPickerFragment_to_puzzleFragment_easyMode,
            bundle
        )
        else
            navController.navigate(
                R.id.action_difficultyPickerFragment_to_puzzleFragment_normalMode,
                bundle
            )
    }

}