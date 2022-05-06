package com.etu.yahwChess.misc

import kotlinx.serialization.Serializable

// used for coordinates mostly

@Serializable
data class Vector2dInt(val x: Int, val y: Int) {
    constructor() : this(0, 0)

    operator fun plus(other: Vector2dInt): Vector2dInt {
        return Vector2dInt(x + other.x, y + other.y)
    }

    operator fun minus(other: Vector2dInt): Vector2dInt {
        return Vector2dInt(x - other.x, y - other.y)
    }

    companion object {
        val NORTH = Vector2dInt(0, -1)
        val EAST = Vector2dInt(1, 0)
        val SOUTH = Vector2dInt(0, 1)
        val WEST = Vector2dInt(-1, 0)

        val OUT_OF_BOUNDS = Vector2dInt(-1,-1)
    }

    override fun toString(): String {
        return "($x, $y)"
    }

    operator fun times(scalar: Int): Vector2dInt {
        return Vector2dInt(x * scalar, y * scalar)
    }

    // true if point is within specified rectangle, INCLUDING its borders
    fun withinRectangle(upperLeft: Vector2dInt, lowerRight: Vector2dInt) : Boolean {
        require(upperLeft.x <= lowerRight.x && upperLeft.y <= lowerRight.y)     // TODO some comment

        return this.x in upperLeft.x..lowerRight.x
                && this.y in upperLeft.y..lowerRight.y
    }
}