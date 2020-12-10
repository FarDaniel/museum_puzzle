package hu.szakdolgozat.puzzle.ui.gratulations

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import hu.szakdolgozat.puzzle.R
import hu.szakdolgozat.puzzle.databinding.FragmentGratulationsBinding

class GratulationsFragment : Fragment() {

    lateinit var navController: NavController
    lateinit var binding: FragmentGratulationsBinding
    var userId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = arguments?.getInt("userId") ?: -1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGratulationsBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        binding.constraintlayoutGratulations.setOnClickListener {
            nextFragment()
        }
    }

    fun nextFragment(){
        val bundle = bundleOf(
            "userId" to userId
        )
        navController.navigate(R.id.action_gratulationsFragment_to_endScreenFragment,
        bundle
        )
    }
}