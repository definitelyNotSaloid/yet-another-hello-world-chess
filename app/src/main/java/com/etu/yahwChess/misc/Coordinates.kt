package com.etu.yahwChess.misc

@Suppress("SelfAssignment")
public class Coordinates(private var x: Int, private var y: Int) {

    init {
        this.x = x;
        this.y = y;
    }

    fun setX(x: Int) {
        this.x = x;
    }

    fun getX(): Int {
        return x;
    }

    fun getY(): Int {
        return y;
    }

    fun setY(y: Int) {
        this.y = y;
    }
}
