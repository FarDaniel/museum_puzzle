package hu.szakdolgozat.puzzle.logic.puzzle

import android.view.MotionEvent
import android.view.View
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.images.view.*
import kotlin.math.atan2
import android.view.animation.LinearInterpolator
import kotlin.random.Random
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.DisplayMetrics
import hu.szakdolgozat.puzzle.logic.position.Position
import hu.szakdolgozat.puzzle.logic.position.Position.Directions
import hu.szakdolgozat.puzzle.ui.puzzle.PuzzleFragment
import hu.szakdolgozat.puzzle.ui.puzzle.PuzzleViewModel
import kotlin.collections.ArrayList


class Piece(
    val view: View,
    private val puzzleViewModel: PuzzleViewModel,
    puzzleFragment: PuzzleFragment,
    image: Int,
    var wantedPieces: ArrayList<WantedPiece> = ArrayList()
) {

    private val posdelta = Position()
    private var d: Float = 0f
    var angle = 0f
    val position = Position(0, 0)
    lateinit var bitmap: Bitmap
    private var isActive = false
    var layout: RelativeLayout = puzzleFragment.binding.puzzleLayout
    private var dp: Int = -1
    var width: Int = 0
    var height: Int = 0

    init {
        //getting the dpi of the screen so we can use dp from here 100* (dp / 160)
        puzzleFragment.activity?.resources?.displayMetrics?.densityDpi?.let {
            dp = it
        }

        setImage(image)
        onTouchListener()
        var metrics = DisplayMetrics()
        puzzleFragment.activity?.resources?.displayMetrics?.let {
            metrics = it
        }
        rotate(Random.nextInt(0, 360).toFloat(), this)
        move(
            Position(
                Random.nextInt(0, metrics.widthPixels - width),
                Random.nextInt(0, metrics.heightPixels - height)
            ), this
        )

    }

    private fun onTouchListener() {
        view.setOnTouchListener(fun(v: View, m: MotionEvent): Boolean {
            val x = m.rawX.toInt()
            val y = m.rawY.toInt()

            if (isTransparent(m.x.toInt(), m.y.toInt()) || isActive) {
                //It's needed, because we couldn't rotate the piece
                //(while you rotate your finger can touch transparent pixels, ending the rotation)
                isActive = true
                when (m.actionMasked) {
                    MotionEvent.ACTION_DOWN -> {
                        val params =
                            view.layoutParams as RelativeLayout.LayoutParams

                        posdelta.x = x - params.leftMargin
                        posdelta.y = y - params.topMargin

                        for (wp in wantedPieces)
                            if (wp.found)
                                wp.chose()

                        chose()
                    }
                    MotionEvent.ACTION_MOVE -> {
                        move(Position(x - posdelta.x, y - posdelta.y), this)

                        if (m.pointerCount == 2) {
                            val tempAngle = rotation(m) - d
                            rotate(angle + tempAngle, this)
                            angle = view.rotation
                        }
                    }
                    MotionEvent.ACTION_POINTER_DOWN -> {
                        d = rotation(m)
                    }
                    MotionEvent.ACTION_UP -> {
                        isActive = false
                        drop(this)
                    }

                    else -> {
                    }
                }
                invalidate()

                for (wp in wantedPieces)
                    if (wp.found)
                        wp.invalidate()

                return true
            } else {
                return false
            }
        })
    }

    private fun rotation(event: MotionEvent): Float {
        //Calculating the rotation from the finger movement
        val xDelta = (event.getX(0) - event.getX(1)).toDouble()
        val yDelta = (event.getY(0) - event.getY(1)).toDouble()
        val radians = atan2(yDelta, xDelta)
        return Math.toDegrees(radians).toFloat()
    }

    private fun setImage(image: Int) {
        //setting the image of the view
        view.picture.setImageResource(image)
        //adding this view to the activity
        layout.addView(view)


        //making a bitmap, so we can detect if we touch transparent pixel or not
        var width = view.picture.drawable.intrinsicWidth
        width = if (width > 0) width else 1
        var height = view.picture.drawable.intrinsicHeight
        height = if (height > 0) height else 1

        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.picture.drawable.setBounds(0, 0, canvas.width, canvas.height)
        view.picture.drawable.draw(canvas)

        this.width = 165 * (dp / 160)
        this.height = 165 * (dp / 160)
        //scaling the bitmap
        bitmap = Bitmap.createScaledBitmap(bitmap, this.width, this.height, false)
        view.picture.setImageBitmap(bitmap)

    }

    private fun isTransparent(x: Int, y: Int): Boolean {
        if (x < width && y < height && x >= 0 && y >= 0)
            return bitmap.getPixel(x, y) != 0
        return false
    }

    fun connect(piece: Piece) {
        //We search out this piece from the other piece's wanteds,
        // it's a function which is called when a wanted piece made a one way connection,
        // so it can be a two way one.
        for (i in 0 until piece.wantedPieces.size) {
            if (piece.wantedPieces[i].ownPiece == this)
                piece.wantedPieces[i].found = true
        }

        //if all the pieces are connected, the puzzle is done
        if (mergeWantedPieces(piece))
            puzzleViewModel.done()
    }

    fun move(pos: Position, from: Piece) {
        val params = view.layoutParams as RelativeLayout.LayoutParams

        position.x = pos.x
        position.y = pos.y
        params.leftMargin = position.x
        params.topMargin = position.y
        params.rightMargin = 6000 //minimum (left + szélesség)
        params.bottomMargin = 6000

        view.layoutParams = params

        //moving all the connected pieces too
        for (i in 0 until wantedPieces.size)
            if (wantedPieces[i].found && wantedPieces[i].ownPiece != from) {
                wantedPieces[i].move()
            }
    }

    fun chose() {

        //puting the view on top of others
        layout.removeView(view)
        layout.addView(view)
    }

    fun drop(from: Piece) {

        var i = 0
        var size = wantedPieces.size
        //droping all the wanted pieces of this piece (the wantedPiece class handels it)
        while (i < size) {
            if (wantedPieces[i].ownPiece != from) {
                wantedPieces[i].drop()
                if (size != wantedPieces.size) {
                    return
                }
            }
            i++
        }

    }

    fun invalidate() {
        layout.invalidate()

    }

    fun mergeWantedPieces(with: Piece): Boolean {
        // Every group of pieces have a leader, who have all the wantedPieces
        // So we're building a tree out of the connections (have no loops in the graph)
        val wantedPiecesOfGroup = with.takeWantedPiecesOfGroup(this)
        val wantedPiecesOfOwnGroup = this.takeWantedPiecesOfGroup(with)

        var i = 0
        while (i < wantedPiecesOfGroup.size) {
            var j = 0
            while (j < wantedPiecesOfOwnGroup.size) {
                if (wantedPiecesOfGroup[i].ownPiece == wantedPiecesOfOwnGroup[j].ownPiece ||
                    wantedPiecesOfGroup[i].ownPiece == wantedPiecesOfOwnGroup[j].owner
                ) {
                    wantedPiecesOfGroup[i].delete()
                    drop(this)
                }
                j++
            }
            i++
        }
        val unitedGroup = takeWantedPiecesOfGroup(this)
        unitedGroup.forEach {
            if (!it.found)
                return false
        }
        return true
    }

    private fun takeWantedPiecesOfGroup(from: Piece): ArrayList<WantedPiece> {
        //recursively getting all the wanted pieces, of a group
        val out = ArrayList<WantedPiece>()

        wantedPieces.forEach {
            if (!it.found) {
                out.add(it)
            } else {
                if (it.ownPiece != from)
                    out.addAll(it.ownPiece.takeWantedPiecesOfGroup(this))
            }
        }
        return out
    }

    fun addWantedPiece(pos: Position, piece: Piece) {
        piece.wantedPieces.add(WantedPiece(pos.inv(), piece, this))
        wantedPieces.add(WantedPiece(pos, this, piece))
    }

    fun addWantedPiece(dir: Directions, piece: Piece) {

        val pos: Position = when (dir) {
            Directions.LEFT -> Position(width - 1, 0)
            Directions.RIGHT -> Position(-width + 1, 0)
            Directions.DOWN -> Position(0, height - 1)
            Directions.UP -> Position(0, -height + 1)
        }
        piece.wantedPieces.add(WantedPiece(pos.inv(), piece, this))
        wantedPieces.add(WantedPiece(pos, this, piece))
    }

    fun rotate(angle: Float, from: Piece) {

        view.animate().rotation(angle).setDuration(0)
            .setInterpolator(LinearInterpolator()).start()

        this.angle = angle

        //All the Pieces connected to a rotated one, have to be rotated to the exact same angle
        for (i in 0 until wantedPieces.size)
            if (wantedPieces[i].found && wantedPieces[i].ownPiece != from) {
                wantedPieces[i].rotate(angle)
            }
    }
}
