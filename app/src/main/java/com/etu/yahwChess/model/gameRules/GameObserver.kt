package com.etu.yahwChess.model.gameRules

import android.graphics.Color
import com.etu.yahwChess.misc.CurrentGame
import com.etu.yahwChess.misc.GameState
import com.etu.yahwChess.misc.Player
import com.etu.yahwChess.misc.Vector2dInt
import com.etu.yahwChess.model.pieces.*
import kotlin.math.abs

// this guy checks if any player is winning each turn



class GameObserver(val game: CurrentGame) {
    private val blackKing : KingPiece by lazy {
        var res : KingPiece? = null
        for (i in 0..7) {
            for (j in 0..7) {
                if (game.boardContainer[Vector2dInt(i,j)] is KingPiece
                    && game.boardContainer[Vector2dInt(i,j)]!!.color == Player.BLACK)
                    res = game.boardContainer[Vector2dInt(i,j)] as KingPiece
            }
        }

        res!!
    }

    private val whiteKing : KingPiece by lazy {
        var res : KingPiece? = null
        for (i in 0..7) {
            for (j in 0..7) {
                if (game.boardContainer[Vector2dInt(i,j)] is KingPiece
                    && game.boardContainer[Vector2dInt(i,j)]!!.color == Player.WHITE)
                    res = game.boardContainer[Vector2dInt(i,j)] as KingPiece
            }
        }

        res!!
    }

    fun softVictoryCheck() {
        TODO("implement")

        // TODO it should only check if given turn is a winning one, without parsing whole board
    }

    fun hardVictoryCheck() : GameState {
        var foundBlack = false
        var foundWhite = false

        for (i in 0..7) {
            for (j in 0..7) {
                val pos = Vector2dInt(i,j)
                if (game.boardContainer[pos]?.color == Player.BLACK)
                    foundBlack = true
                else if (game.boardContainer[pos]?.color == Player.WHITE)
                    foundWhite = true
            }
        }

        if (!foundBlack)
            return GameState.WHITE_WINS

        if (!foundWhite)
            return GameState.BLACK_WINS

        return GameState.PLAYING

    }

    fun targetedBy(cell: Vector2dInt) : Sequence<Piece> {
        val directionsDiagonal = listOf(
            Vector2dInt(-1,-1),
            Vector2dInt(1,-1),
            Vector2dInt(-1,1),
            Vector2dInt(1,1)
        )

        val directionsStraight = listOf(
            Vector2dInt.WEST,
            Vector2dInt.NORTH,
            Vector2dInt.EAST,
            Vector2dInt.SOUTH
        )

        val knightPositions = listOf(
            Vector2dInt(2,1) + cell,
            Vector2dInt(1,2) + cell,
            Vector2dInt(-2,1) + cell,
            Vector2dInt(-1,2) + cell,
            Vector2dInt(2,-1) + cell,
            Vector2dInt(1,-2) + cell,
            Vector2dInt(-2,-1) + cell,
            Vector2dInt(-1,-2) + cell,
        ).filter { it.withinRectangle(Vector2dInt(0,0), Vector2dInt(7,7)) }

        return sequence {
            for (direction in directionsStraight) {
                var curPos = cell + direction
                if (curPos.withinRectangle(Vector2dInt(0, 0), Vector2dInt(7, 7)) &&
                    game.boardContainer[curPos] is KingPiece)
                    yield(game.boardContainer[curPos]!!)

                else
                while (curPos.withinRectangle(Vector2dInt(0, 0), Vector2dInt(7, 7))) {
                    if (game.boardContainer[curPos]!=null) {
                        if (game.boardContainer[curPos] is RookPiece
                            || game.boardContainer[curPos] is QueenPiece
                        )
                            yield(game.boardContainer[curPos]!!)

                        break
                    }
                    curPos+=direction
                }
            }

            for (direction in directionsDiagonal) {
                var curPos = cell + direction
                if (curPos.withinRectangle(Vector2dInt(0, 0), Vector2dInt(7, 7)) &&
                    game.boardContainer[curPos] is KingPiece)
                    yield(game.boardContainer[curPos]!!)

                else
                    while (curPos.withinRectangle(Vector2dInt(0, 0), Vector2dInt(7, 7))) {
                        if (game.boardContainer[curPos]!=null) {
                            if (game.boardContainer[curPos] is BishopPiece
                                || game.boardContainer[curPos] is QueenPiece
                            )
                                yield(game.boardContainer[curPos]!!)

                            break
                        }
                        curPos+=direction
                    }
            }

            for (knightJump in knightPositions) {
                if (game.boardContainer[knightJump] is KnightPiece)
                    yield(game.boardContainer[knightJump]!!)
            }

            val blackPawnPositions = listOf(
                cell+Vector2dInt.NORTH+Vector2dInt.EAST,
                cell+Vector2dInt.NORTH+Vector2dInt.WEST
            ).filter {
                it.withinRectangle(Vector2dInt(0, 0), Vector2dInt(7, 7))
            }

            val whitePawnPositions = listOf(
                cell+Vector2dInt.SOUTH+Vector2dInt.EAST,
                cell+Vector2dInt.SOUTH+Vector2dInt.WEST
            ).filter {
                it.withinRectangle(Vector2dInt(0, 0), Vector2dInt(7, 7))
            }

            for (pawnPos in blackPawnPositions) {
                if (game.boardContainer[pawnPos] is PawnPiece
                    && game.boardContainer[pawnPos]!!.color == Player.BLACK)
                    yield(game.boardContainer[pawnPos]!!)
            }

            for (pawnPos in whitePawnPositions) {
                if (game.boardContainer[pawnPos] is PawnPiece
                    && game.boardContainer[pawnPos]!!.color == Player.WHITE)
                    yield(game.boardContainer[pawnPos]!!)
            }
        }
    }

    fun protectsItsKingFrom(piece: Piece) {
        if (piece is KingPiece)
            throw IllegalArgumentException("what?")

        val king = if (piece.color == Player.WHITE) whiteKing else blackKing

        val delta = piece.position - king.position

        val direction : Vector2dInt
    }


}