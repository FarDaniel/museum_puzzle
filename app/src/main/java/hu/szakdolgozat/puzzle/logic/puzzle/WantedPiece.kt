package hu.szakdolgozat.puzzle.logic.puzzle

import hu.szakdolgozat.puzzle.logic.position.Position
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

class WantedPiece (val relativePosition: Position, val owner: Piece, val ownPiece: Piece){
    //The position relative to the whole activity
    var actualPosition = ownPiece.position+relativePosition
    var found = false

    fun check() {
        //Checking if the two pieces needs to connected
        countPosition()

        if (ownPiece.position.atThere(actualPosition) && abs(
                sin(Math.toRadians(owner.angle.toDouble())) - sin(
                    Math.toRadians(ownPiece.angle.toDouble())
                )
            ) < 0.2
        ) {
            found = true
            owner.connect(ownPiece)
            //Moving the owner to the other piece "popping" them together"
            ownPiece.move(ownPiece.position,ownPiece)
            ownPiece.rotate(ownPiece.angle,ownPiece)
        }
    }
    fun move(){
                countPosition()
                ownPiece.move(actualPosition,owner)
        }
    fun invalidate(){
            ownPiece.invalidate()
        }
    fun chose(){
            ownPiece.chose()
        }
    fun drop(){
            if(!found) {
                //If it isn't found yet, we check if it should be or not, in every drop
                check()
            }
            else {
                // If it's founded, we drop all of it's wantedPieces
                ownPiece.drop(owner)
            }
        }
    fun countPosition(){
        // Calculating the exact position, where should the Piece be (if the owner is rotated,
        // we need to rotate the piece relative to the owners pivot point)
        val tempPos = Position(
            ((relativePosition.x) * cos(Math.toRadians(owner.angle.toDouble()))
                    - ((relativePosition.y) * sin(Math.toRadians(owner.angle.toDouble())))).toInt(),
            ((relativePosition.y) * cos(Math.toRadians(owner.angle.toDouble()))
                    + ((relativePosition.x) * sin(Math.toRadians(owner.angle.toDouble())))).toInt()
        )
        actualPosition = owner.position + tempPos
    }
    fun rotate(angle: Float){
        ownPiece.rotate(angle, owner)
    }
    fun delete(){
        owner.wantedPieces.remove(this)
        val iterator = ownPiece.wantedPieces.iterator()
        for(wp in iterator)
            if(wp.ownPiece == owner)
                iterator.remove()
    }
    }