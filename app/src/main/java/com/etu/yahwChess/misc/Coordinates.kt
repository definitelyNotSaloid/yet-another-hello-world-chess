package com.etu.yahwChess.misc

fun vectorToCellIndex(cell: Vector2dInt) : String {
    require(cell.x in 0..7) {"x val is out of 0..7 range"}
    require(cell.y in 0..7) {"y val is out of 0..7 range"}

    return "${'H' - cell.y}${cell.x + 1}"
}

fun cellIndexToVector(cell: String) : Vector2dInt {
    require(cell.length==2) {"invalid format"}
    val y = -(cell[0].uppercaseChar() - 'H')
    val x = Character.digit(cell[1], 10) - 1

    // TODO range checks

    return Vector2dInt(x,y)
}
