package hu.szakdolgozat.puzzle.logic.position

import kotlin.math.abs

class Position(var x:Int = -1, var y:Int =-1){

    enum class Directions{
        UP,DOWN,LEFT,RIGHT
    }

    fun atThere(pos: Position): Boolean{
        //we check if it's near to another point or not
        return (abs(pos.x-x) < 20 && abs(pos.y-y) < 20)
    }
    fun inv(): Position {
        return Position(-x, -y)
    }
    operator fun plus(pos: Position): Position {
        return Position(pos.x+x,pos.y+y)
    }
    operator fun minus(pos: Position): Position {
        return Position(pos.x-x,pos.y-y)
    }
}