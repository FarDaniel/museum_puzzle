package hu.szakdolgozat.puzzle.interactor

import android.annotation.SuppressLint
import hu.szakdolgozat.puzzle.R
import hu.szakdolgozat.puzzle.logic.position.Position
import hu.szakdolgozat.puzzle.logic.puzzle.Piece
import hu.szakdolgozat.puzzle.ui.puzzle.PuzzleFragment
import hu.szakdolgozat.puzzle.ui.puzzle.PuzzleViewModel
import org.json.JSONObject

class PuzzleInteractor(val fragment: PuzzleFragment, val viewModel: PuzzleViewModel) {

    @SuppressLint("InflateParams")
    fun loadPuzzle(): Int {

        fragment.activity?.let {
            val puzzlePieces = ArrayList<Piece>()
            val puzzleData = JSONObject(
                it.assets.open("puzzle.json")
                    .bufferedReader().readText()
            ).getJSONObject("puzzle")
            val pieces = puzzleData.getJSONArray("pieces")
            val neighbors = puzzleData.getJSONArray("neighbors")

            for (i in 0 until pieces.length()) {
                val id = fragment.resources.getIdentifier(
                    pieces.getJSONObject(i).getString("texture"),
                    "drawable",
                    fragment.activity?.packageName
                )

                puzzlePieces.add(
                    Piece(
                        fragment.layoutInflater.inflate(R.layout.images, null),
                        viewModel,
                        fragment,
                        id
                    )
                )
            }
            for(i in 0 until neighbors.length()){
                val ownerId = neighbors.getJSONObject(i).getInt("onwerid")
                val ownItemId = neighbors.getJSONObject(i).getInt("ownid")
                val relativePos = neighbors.getJSONObject(i).getString("relativepos")
                puzzlePieces[ownerId].addWantedPiece(getRelativePosition(relativePos), puzzlePieces[ownItemId])
            }
            return puzzlePieces[0].width
        }
        return 0
    }

    private fun getRelativePosition(direction: String): Position.Directions{
        return when(direction){
            "left" -> {
                Position.Directions.LEFT
            }
            "right" -> {
                Position.Directions.RIGHT
            }
            "down" -> {
                Position.Directions.DOWN
            }
            "up" -> {
                Position.Directions.UP
            }
            else -> {
                Position.Directions.UP
            }
        }
    }

}