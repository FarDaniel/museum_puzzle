package hu.szakdolgozat.puzzle.ui.puzzle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import hu.szakdolgozat.puzzle.R
import hu.szakdolgozat.puzzle.databinding.FragmentPuzzleBinding

class PuzzleFragment : Fragment() {

    private lateinit var viewModel: PuzzleViewModel
    lateinit var navController: NavController
    lateinit var binding: FragmentPuzzleBinding
    var userId = -1
    var easyMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let{
        userId = it.getInt("userId")
        easyMode = it.getBoolean("isEasy")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPuzzleBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        viewModel = PuzzleViewModel()

        viewModel.makePuzzlePieces(this)
        viewModel.setBackgroundTo(this, easyMode)

        viewModel.isPuzzleDone.observe(viewLifecycleOwner, Observer{isDone ->
            if(isDone){
                val bundle = bundleOf(
                    "userId" to userId
                )
                navController.navigate(R.id.action_puzzleFragment_to_gratulationsFragment, bundle)
            }
        })
    }
}