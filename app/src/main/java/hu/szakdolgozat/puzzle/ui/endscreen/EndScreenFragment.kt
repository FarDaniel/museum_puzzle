package hu.szakdolgozat.puzzle.ui.endscreen

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.navigation.NavController
import androidx.navigation.Navigation
import hu.szakdolgozat.puzzle.R
import hu.szakdolgozat.puzzle.databinding.FragmentEndScreenBinding

class EndScreenFragment : Fragment() {
    lateinit var navController: NavController
    lateinit var binding: FragmentEndScreenBinding
    var userId = -1

    private lateinit var qrgEncoder: QRGEncoder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userId = arguments?.getInt("userId") ?: -1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEndScreenBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        //making the encoder to generate the qr-code
        qrgEncoder = QRGEncoder(calculateAnswer(), null, QRGContents.Type.TEXT, 4000)
        qrgEncoder.colorWhite = Color.TRANSPARENT
        qrgEncoder.colorBlack = Color.BLACK
        //making, and showing the qr-code
        binding.qrcode.setImageBitmap(qrgEncoder.bitmap)

        binding.constraintlayoutEndScreen.setOnClickListener {
            navController.navigate(R.id.action_endScreenFragment_to_landingFragment)
        }
    }

    private fun calculateAnswer(): String {
        //It's the code for the kutagodj app to read, it's constructed like KUTAKODJ:<regexNumber>:OK
        return "KUTAKODJ:$userId:OK"
    }
}