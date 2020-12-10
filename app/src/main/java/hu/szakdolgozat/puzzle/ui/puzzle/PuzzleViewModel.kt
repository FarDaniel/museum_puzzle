package hu.szakdolgozat.puzzle.ui.puzzle

import androidx.lifecycle.MutableLiveData
import hu.szakdolgozat.puzzle.R
import hu.szakdolgozat.puzzle.interactor.PuzzleInteractor

class PuzzleViewModel {
    var widthOfPieces = 0
    var isPuzzleDone = MutableLiveData<Boolean>(false)

    fun makePuzzlePieces(puzzleFragment: PuzzleFragment){

        puzzleFragment.activity?.let {
            val interactor = PuzzleInteractor(puzzleFragment, this)
            widthOfPieces = interactor.loadPuzzle()
        }
    }

    fun setBackgroundTo(puzzleFragment: PuzzleFragment, isEasy: Boolean) {
        if(isEasy) {
            puzzleFragment.binding.puzzleBackground.setImageResource(R.drawable.easy_puzzle_background)

            //4.6 because of the resolution of the background
            puzzleFragment.binding.puzzleBackground.layoutParams.width = (widthOfPieces *4.6).toInt()
        }else {
            puzzleFragment.binding.puzzleBackground.setImageResource(R.drawable.normal_puzzle_background)
        }
    }

    fun done() {
        isPuzzleDone.value = true
    }

}