package hu.szakdolgozat.puzzle.ui.landing

import androidx.core.os.bundleOf
import hu.szakdolgozat.puzzle.R

class LandingViewModel(var fragment: LandingFragment) {
    var userId = -1

    fun handleRegexText(text: String): String{
        val correction: String
                    if (text.isNotEmpty()) {
                        userId = Integer.parseInt(text)
                        if (checkRegex()) {
                            correction =
                                fragment.resources.getString(R.string.without_code_check_other_app)

                            val bundle = bundleOf("userId" to userId)
                            userId = 0
                            fragment.binding.regex.text = null
                            fragment.navController.navigate(
                                R.id.action_landingFragment_to_difficultyPickerFragment,
                                bundle
                            )
                        } else {
                            correction =
                                fragment.resources.getString(R.string.wrong_code)
                        }
                    } else {
                        correction =
                            fragment.resources.getString(R.string.without_code_check_other_app)
                    }

               return correction
    }

    private fun checkRegex(): Boolean {
        val index12 = userId / 10000
        val index34 = (userId / 100) % 100
        val index56 = userId % 100

        //We only check the regex if it's exactly 6 characters long.
        return (index34 % 2 == 0 && ((index12 * 100 + index56) % 7 == 2)  && userId.toString().length >= 6)
    }

}