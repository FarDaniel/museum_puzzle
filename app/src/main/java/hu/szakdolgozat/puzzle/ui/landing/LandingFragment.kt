package hu.szakdolgozat.puzzle.ui.landing

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import hu.szakdolgozat.puzzle.databinding.FragmentLandingBinding

class LandingFragment : Fragment() {

    lateinit var navController: NavController
    lateinit var binding: FragmentLandingBinding
    var userId = -1
    lateinit var viewModel: LandingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = arguments?.getInt("userId") ?: -1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLandingBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        viewModel = LandingViewModel(this)

        binding.regex.addTextChangedListener(object : TextWatcher {
            //We don't have a "done" button, the app checks, if the code is right, if it is,
            //then automatically switches to the next Activity
            override fun afterTextChanged(s: Editable?) {
                if (s != null && s.isNotEmpty()) {
                    binding.correction.text = viewModel.handleRegexText(s.toString())
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
    }
}