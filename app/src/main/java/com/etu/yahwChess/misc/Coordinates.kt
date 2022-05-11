package com.etu.yahwChess.misc

import android.util.Log

fun vectorToCellIndex(cell: Vector2dInt) : String {

    require(cell.x in 0..7) {"x val is out of 0..7 range"}
    require(cell.y in 0..7) {"y val is out of 0..7 range"}
    //Log.println(Log.INFO, "vector to index", "received $cell ; translated to ${'H' - cell.y}${cell.x + 1}")
    return "${'H' - cell.y}${cell.x + 1}"
}

fun cellIndexToVector(cell: String) : Vector2dInt {
    require(cell.length==2) {"invalid format"}
    val y = -(cell[0].uppercaseChar() - 'H')
    val x = Character.digit(cell[1], 10) - 1
    //Log.println(Log.INFO, "index to vector", "received $cell ; translated to ${Vector2dInt(x,y)}")
    // TODO range checks

    return Vector2dInt(x,y)
}
