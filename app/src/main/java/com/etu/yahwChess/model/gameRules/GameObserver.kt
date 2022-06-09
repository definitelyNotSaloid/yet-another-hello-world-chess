package com.etu.yahwChess.model.gameRules

import android.util.Log
import com.etu.yahwChess.misc.CurrentGame
import com.etu.yahwChess.misc.GameState
import com.etu.yahwChess.misc.Player
import com.etu.yahwChess.misc.Vector2dInt
import com.etu.yahwChess.model.pieces.*
import kotlin.math.abs

// this guy checks if any player is winning each turn

enum class HowBadThingsReallyAre {
    NO_THREAT,
    CAN_COVER_KING_WITH_OTHER_PIECE,
    ONLY_KING_CAN_MOVE
}

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

    fun kingOfColor(color: Player) = if (color == Player.WHITE) whiteKing else blackKing

    fun softVictoryCheck() {
        TODO("implement")

        // TODO it should only check if given turn is a winning one, without parsing whole board
    }



    fun hardVictoryCheck() : GameState {
        val king = kingOfColor(game.turn)

        val checkState = checkmateState()
        Log.println(Log.INFO, "GameObserver", "Checkmate state: $checkState")

        if (checkState == HowBadThingsReallyAre.ONLY_KING_CAN_MOVE) {        // if you cant just use some garbage pawn to cover king
            if (!king.possibleMoves().any())
                return if (game.turn == Player.WHITE) GameState.BLACK_WINS else GameState.WHITE_WINS
        }

        for (i in 0..7) {
            for (j in 0..7) {
                val pos = Vector2dInt(i,j)
                if (game.boardContainer[pos]?.color == game.turn) {
                    if (game.boardContainer[pos]!!.possibleMoves().any())
                        return GameState.PLAYING
                }
            }
        }

        return if (targetedBy(king.position).any())
            if (game.turn == Player.WHITE) GameState.BLACK_WINS else GameState.WHITE_WINS
        else
            GameState.DRAW
    }

    fun checkmateState() : HowBadThingsReallyAre {
        val king = kingOfColor(game.turn)

        var count = 0
        for (kingThreat in targetedBy(king.position).filter { it.color!=game.turn }) {
            count++
            if (count>1)
                break
        }

        return when(count) {
            0 -> HowBadThingsReallyAre.NO_THREAT
            1 -> HowBadThingsReallyAre.CAN_COVER_KING_WITH_OTHER_PIECE
            else -> HowBadThingsReallyAre.ONLY_KING_CAN_MOVE
        }
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

    fun kingThreatNullifiedByObstacleAt(cell: Vector2dInt, kingColor: Player) : Piece? {
        val piece = game.boardContainer[cell]
        if (piece is KingPiece)
            throw IllegalArgumentException("what?")

        val king = kingOfColor(kingColor)

        val delta = cell - king.position

        var direction = when {
            delta.x == 0 -> Vector2dInt.SOUTH
            delta.y == 0 -> Vector2dInt.WEST
            delta.x == delta.y -> Vector2dInt.SOUTH + Vector2dInt.EAST
            abs(delta.x) == abs(delta.y) // and x!=y (already checked)
            -> Vector2dInt.SOUTH + Vector2dInt.WEST
            else -> return null         // no reason to check further
        }

        val straightCheck = direction.x == 0 || direction.y == 0        // *insert joke here*

        var stumbledUponKing = false
        var pieceThreateningKing : Piece? = null
        val board = game.boardContainer

        var curCell = cell + direction

        val pieceTypeMatchesCheckingDirection : (Piece) -> Boolean = {
            p: Piece ->
            ((straightCheck && (p is QueenPiece || p is RookPiece)) ||
             (!straightCheck && (p is QueenPiece || p is BishopPiece)))
        }

        while (board.isWithinBorders(curCell)) {
            if (board[curCell]!=null) {
                if (board[curCell] == king) {
                    stumbledUponKing = true
                    break
                }

                if (board[curCell]!!.color != kingColor) {
                    if (pieceTypeMatchesCheckingDirection(board[curCell]!!)) {
                        pieceThreateningKing = board[curCell]
                        break
                    }
                }
                else
                    return null
            }

            curCell+=direction
        }

        if (pieceThreateningKing==null && !stumbledUponKing)
            return null

        direction *=-1
        curCell = cell + direction
        while (board.isWithinBorders(curCell)) {
            if (board[curCell]!=null) {
                if (board[curCell] == king) {
                    return pieceThreateningKing
                }

                if (board[curCell]!!.color != kingColor) {
                    if (pieceTypeMatchesCheckingDirection(board[curCell]!!)) {
                        if (stumbledUponKing)
                            return board[curCell]

                        break
                    }
                }
                else
                    return null
            }

            curCell+=direction
        }

        return null
    }


}