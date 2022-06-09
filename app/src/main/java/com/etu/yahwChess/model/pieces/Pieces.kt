package com.etu.yahwChess.model.pieces

import android.util.Log
import com.etu.yahwChess.misc.Player
import com.etu.yahwChess.misc.Vector2dInt
import com.etu.yahwChess.model.board.container.BoardContainer
import com.etu.yahwChess.model.gameRules.HowBadThingsReallyAre
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.lang.RuntimeException
import kotlin.math.abs

// btw we dont care about le tru chessers and call pawn piece as well
@Serializable
sealed class Piece(
    private var prevRegisteredPos: Vector2dInt,
    val color: Player) {

    var hasMoved = false
        protected set

    @Transient
    lateinit var board: BoardContainer
    protected set // SINGLE USE ONLY

    fun linkBoard(board: BoardContainer) {       // because we cant use custom setter on lateinit
        if (this::board.isInitialized)
            throw Exception()

        this.board = board
    }

    abstract val pieceData : PieceData

    constructor(board: BoardContainer, color: Player) : this (Vector2dInt.OUT_OF_BOUNDS, color) {
        this.board = board
    }

    // this predicate only works if checkmate state is can cover with single piece (or smth like that)
    protected fun willCoverKingOrEliminateThreatPredicate (cell:Vector2dInt): Boolean {
        val observer = board.game.gameObserver

            return observer.kingThreatNullifiedByObstacleAt(cell, color)!=null
            || observer.targetedBy(observer.kingOfColor(color).position).first { it.color != color }.position == cell
    }

    init {
    }

    val position : Vector2dInt
        get() {
            if (prevRegisteredPos!=Vector2dInt.OUT_OF_BOUNDS
                && board[prevRegisteredPos] == this) {
                return prevRegisteredPos
            }

            for (i in 0..7) {
                for (j in 0..7){
                    if (board[Vector2dInt(i,j)] == this) {
                        prevRegisteredPos = Vector2dInt(i,j)
                        return prevRegisteredPos
                    }
                }
            }
            prevRegisteredPos = Vector2dInt.OUT_OF_BOUNDS
            return prevRegisteredPos
        }

    abstract fun possibleMoves() : Sequence<Vector2dInt>

    open fun canMoveTo(pos: Vector2dInt) : Boolean {
        require(pos.withinRectangle(Vector2dInt(0,0), Vector2dInt(7,7)))
        { "position $pos is out of board borders" }

        if (this.position == Vector2dInt.OUT_OF_BOUNDS)
            return false

        if (pos == this.position)
            return false

        val checkThreat = board.game.gameObserver.checkmateState()
        if (checkThreat == HowBadThingsReallyAre.ONLY_KING_CAN_MOVE)
            return false

        else if (checkThreat == HowBadThingsReallyAre.CAN_COVER_KING_WITH_OTHER_PIECE) {
            val observer = board.game.gameObserver

            val threat = observer.kingThreatNullifiedByObstacleAt(position, this.color)
            if (threat != null)
                return false

            if (willCoverKingOrEliminateThreatPredicate(pos))
                return true

            return false
        }

        return true
    }

    open fun afterMoveToAction(newPos: Vector2dInt) : () -> Unit {
        return {this.hasMoved = true}
    }
    open fun onTakenAction() : () -> Unit {
        return {}
    }
}

@Serializable
@SerialName("Rook")
class RookPiece : Piece {
    constructor(board: BoardContainer, color: Player): super(Vector2dInt.OUT_OF_BOUNDS, color) {
        this.board = board
    }

    override val pieceData: PieceData
        get() = RookData

    override fun possibleMoves(): Sequence<Vector2dInt> {
        if (position == Vector2dInt.OUT_OF_BOUNDS)
            return sequence {  }

        val threat = board.game.gameObserver.kingThreatNullifiedByObstacleAt(this.position, this.color)
        if (threat!=null) {
            if (canMoveTo(threat.position))
                return sequenceOf(threat.position)

            return sequence {  }
        }

        val checkState = board.game.gameObserver.checkmateState()

        if (checkState == HowBadThingsReallyAre.ONLY_KING_CAN_MOVE)
            return sequence {  }

        return sequence {
            val upperLeft = Vector2dInt(0,0)
            val lowerRight = Vector2dInt(7,7)
            val startingPos = position
            var curPos : Vector2dInt

            // technically, its possible to avoid unnecessary comparisons here
            // TODO?

            for (direction in sequenceOf(
                Vector2dInt.EAST,
                Vector2dInt.WEST,
                Vector2dInt.NORTH,
                Vector2dInt.SOUTH)) {
                    curPos = startingPos + direction

                    while (curPos.withinRectangle(upperLeft, lowerRight)) {
                        if (board[curPos]!=null) {        // cant go past some piece
                            if (board[curPos]?.color != color)      // can jump at cell with piece of opposite color
                                yield(curPos)

                            break
                        }


                        yield(curPos)
                        curPos+=direction
                    }
            }
        }.filter {
            if (checkState == HowBadThingsReallyAre.CAN_COVER_KING_WITH_OTHER_PIECE) {
                willCoverKingOrEliminateThreatPredicate(it)
            }
            else
                true
        }
    }

    override fun canMoveTo(pos: Vector2dInt): Boolean {
        if (!super.canMoveTo(pos))
            return false

        if (pos.x != this.position.x && pos.y != this.position.y)
            return false

        val checkDirection = when {
            pos.x < this.position.x -> Vector2dInt.WEST
            pos.x > this.position.x -> Vector2dInt.EAST
            pos.y < this.position.y -> Vector2dInt.NORTH
            pos.y > this.position.y -> Vector2dInt.SOUTH

            else -> throw RuntimeException("this should be impossible")
        }

        var curPos = position + checkDirection
        while (pos != curPos) {
            if (board[curPos] != null)
                return false
            curPos+=checkDirection
        }

        // checking cell at destination
        if (board[curPos]?.color == this.color)
            return false

        return true
    }
}

@Serializable
@SerialName("Bishop")
class BishopPiece : Piece {
    constructor(board: BoardContainer, color: Player): super(Vector2dInt.OUT_OF_BOUNDS, color) {
        this.board = board
    }

    override val pieceData: PieceData
        get() = BishopData

    override fun possibleMoves(): Sequence<Vector2dInt> {
        val threat = board.game.gameObserver.kingThreatNullifiedByObstacleAt(this.position, this.color)
        if (threat!=null) {
            if (canMoveTo(threat.position))
                return sequenceOf(threat.position)

            return sequence {  }
        }

        val checkState = board.game.gameObserver.checkmateState()

        if (checkState == HowBadThingsReallyAre.ONLY_KING_CAN_MOVE)
            return sequence {  }

        val directions = listOf(
            Vector2dInt(-1,-1),
            Vector2dInt(1,-1),
            Vector2dInt(-1,1),
            Vector2dInt(1,1)
        )

        return sequence<Vector2dInt> {
            val upperLeft = Vector2dInt(0,0)
            val lowerRight = Vector2dInt(7,7)
            val startingPos = position
            var curPos : Vector2dInt

            for (direction in directions) {
                curPos = startingPos + direction

                while (curPos.withinRectangle(upperLeft, lowerRight)) {
                    if (board[curPos]!=null) {        // cant go past some piece
                        if (board[curPos]?.color != color)      // can jump at cell with piece of opposite color
                            yield(curPos)

                        break
                    }

                    yield(curPos)
                    curPos+=direction
                }
            }
        }.filter {
            if (checkState == HowBadThingsReallyAre.CAN_COVER_KING_WITH_OTHER_PIECE) {
                willCoverKingOrEliminateThreatPredicate(it)
            }
            else
                true
        }
    }


    override fun canMoveTo(pos: Vector2dInt): Boolean {
        if (!super.canMoveTo(pos))
            return false

        if (abs(pos.x-this.position.x)!=abs(pos.y-this.position.y))
            return false

        val direction : Vector2dInt = when {
            pos.x < this.position.x && pos.y < this.position.y -> Vector2dInt(-1,-1)
            pos.x > this.position.x && pos.y < this.position.y -> Vector2dInt(1,-1)
            pos.x < this.position.x && pos.y > this.position.y -> Vector2dInt(-1,1)
            pos.x > this.position.x && pos.y > this.position.y -> Vector2dInt(1,1)

            else -> throw RuntimeException("this should be impossible")
        }

        var curPos = this.position + direction

        while (curPos!=pos) {
            if (board[curPos]!=null)
                return false

            curPos+=direction
        }

        if (board[curPos]?.color == this.color)
            return false

        return true
    }

}

@Serializable
@SerialName("Queen")
class QueenPiece : Piece {
    constructor(board: BoardContainer, color: Player): super(Vector2dInt.OUT_OF_BOUNDS, color) {
        this.board = board
    }

    override val pieceData: PieceData
        get() = QueenData

    override fun possibleMoves(): Sequence<Vector2dInt> {
        val threat = board.game.gameObserver.kingThreatNullifiedByObstacleAt(this.position, this.color)
        if (threat!=null) {
            if (canMoveTo(threat.position))
                return sequenceOf(threat.position)

            return sequence {  }
        }

        val checkState = board.game.gameObserver.checkmateState()

        if (checkState == HowBadThingsReallyAre.ONLY_KING_CAN_MOVE)
            return sequence {  }

        val directions = listOf(
            Vector2dInt(-1,-1),
            Vector2dInt(1,-1),
            Vector2dInt(-1,1),
            Vector2dInt(1,1),
            Vector2dInt.WEST,
            Vector2dInt.NORTH,
            Vector2dInt.EAST,
            Vector2dInt.SOUTH
        )

        return sequence {
            val upperLeft = Vector2dInt(0,0)
            val lowerRight = Vector2dInt(7,7)
            val startingPos = position
            var curPos : Vector2dInt

            for (direction in directions) {
                curPos = startingPos + direction

                while (curPos.withinRectangle(upperLeft, lowerRight)) {
                    if (board[curPos]!=null) {        // cant go past some piece
                        if (board[curPos]?.color != color)      // can jump at cell with piece of opposite color
                            yield(curPos)

                        break
                    }

                    yield(curPos)
                    curPos+=direction
                }
            }
        }.filter {
            if (checkState == HowBadThingsReallyAre.CAN_COVER_KING_WITH_OTHER_PIECE) {
                willCoverKingOrEliminateThreatPredicate(it)
            }
            else
                true
        }
    }


    override fun canMoveTo(pos: Vector2dInt): Boolean {
        if (!super.canMoveTo(pos))
            return false

        val direction : Vector2dInt = when {
            pos.x < this.position.x && pos.y < this.position.y -> Vector2dInt(-1,-1)
            pos.x > this.position.x && pos.y < this.position.y -> Vector2dInt(1,-1)
            pos.x < this.position.x && pos.y > this.position.y -> Vector2dInt(-1,1)
            pos.x > this.position.x && pos.y > this.position.y -> Vector2dInt(1,1)

            pos.x < this.position.x && pos.y == this.position.y -> Vector2dInt(-1,0)
            pos.x > this.position.x && pos.y == this.position.y -> Vector2dInt(1,0)
            pos.x == this.position.x && pos.y > this.position.y -> Vector2dInt(0,1)
            pos.x == this.position.x && pos.y < this.position.y -> Vector2dInt(0,-1)

            else -> throw RuntimeException("this should be impossible")
        }

        var curPos = this.position + direction

        while (curPos!=pos) {
            if (board[curPos]!=null)
                return false

            curPos+=direction
        }

        if (board[curPos]?.color == this.color)
            return false

        return true
    }

}

@Serializable
@SerialName("Knight")
class KnightPiece : Piece {
    constructor(board: BoardContainer, color: Player): super(Vector2dInt.OUT_OF_BOUNDS, color) {
        this.board = board
    }

    override val pieceData: PieceData
        get() = KnightData



    override fun possibleMoves(): Sequence<Vector2dInt> {
        val threat = board.game.gameObserver.kingThreatNullifiedByObstacleAt(this.position, this.color)
        if (threat!=null) {
            return sequence {  }
        }

        val checkState = board.game.gameObserver.checkmateState()

        if (checkState == HowBadThingsReallyAre.ONLY_KING_CAN_MOVE)
            return sequence {  }


        val positions = listOf(
            Vector2dInt(2,1) + this.position,
            Vector2dInt(1,2) + this.position,
            Vector2dInt(-2,1) + this.position,
            Vector2dInt(-1,2) + this.position,
            Vector2dInt(2,-1) + this.position,
            Vector2dInt(1,-2) + this.position,
            Vector2dInt(-2,-1) + this.position,
            Vector2dInt(-1,-2) + this.position,
        )

        return sequence {
            for (potentialPos in positions) {
                if (potentialPos.withinRectangle(Vector2dInt(0,0), Vector2dInt(7,7))) {
                    if (board[potentialPos] == null || board[potentialPos]?.color != color)
                        yield(potentialPos)
                }
            }
        }.filter {
            if (checkState == HowBadThingsReallyAre.CAN_COVER_KING_WITH_OTHER_PIECE) {
                willCoverKingOrEliminateThreatPredicate(it)
            }
            else
                true
        }
    }

    override fun canMoveTo(pos: Vector2dInt): Boolean {
        if (!super.canMoveTo(pos))
            return false

        val deltaX = abs(this.position.x - pos.x)
        val deltaY = abs(this.position.y - pos.y)

        if ((deltaX == 1 && deltaY == 2)
            || (deltaX == 2 && deltaY == 1)) {

            if (board[pos]?.color==this.color)
                return false

            return true
        }

        return false
    }

}

@Serializable
@SerialName("King")
class KingPiece : Piece {
    constructor(board: BoardContainer, color: Player): super(Vector2dInt.OUT_OF_BOUNDS, color) {
        this.board = board
    }

    override val pieceData: PieceData
        get() = KingData


    override fun canMoveTo(pos: Vector2dInt): Boolean {
        if(!board.isWithinBorders(pos))
            return false

        if (this.position == Vector2dInt.OUT_OF_BOUNDS)
            return false

        if (pos == this.position)
            return false

        val posThreats = board.game.gameObserver.targetedBy(pos).filter{ it.color != this.color }
        val kingThreats = board.game.gameObserver.targetedBy(position).filter{ it.color != this.color }
        if (posThreats.any())
            return false

        if (kingThreats.any {pos == this.position + this.position.straightOrDiagonalDirectionTo(it.position) * -1})
            return false


        if (!hasMoved && (pos == this.position + Vector2dInt.WEST * 2 || pos == this.position + Vector2dInt.EAST * 2)) {
            val direction = if (pos.x<this.position.x) Vector2dInt.WEST else Vector2dInt.EAST
            val observer = board.game.gameObserver
            if (observer.targetedBy(this.position+direction).any {it.color != color})
                return false

            if (observer.targetedBy(this.position+direction * 2).any {it.color != color})
                return false

            val distToRook = if (direction==Vector2dInt.WEST) 4 else 3
            if (board[this.position + direction*distToRook] is RookPiece &&
                !board[this.position + direction*distToRook]!!.hasMoved &&
                board[this.position + direction*distToRook]!!.canMoveTo(this.position+direction) ){
                return true
            }

            return false
        }

        if (abs(pos.x - this.position.x) >1 || abs(pos.y - this.position.y) >1)
            return false


        return true
    }

    override fun possibleMoves(): Sequence<Vector2dInt> {
        val positions = listOf(
            Vector2dInt(1, 0) + this.position,
            Vector2dInt(1, 1) + this.position,
            Vector2dInt(0, 1) + this.position,
            Vector2dInt(-1, 1) + this.position,
            Vector2dInt(-1, 0) + this.position,
            Vector2dInt(-1, -1) + this.position,
            Vector2dInt(0, -1) + this.position,
            Vector2dInt(1, -1) + this.position,
        )

        return sequence {
            val threats = board.game.gameObserver.targetedBy(position).filter { it.color != color }
            for (potentialPos in positions) {
                if (!board.game.gameObserver.targetedBy(potentialPos).any { it.color != color} &&
                    potentialPos.withinRectangle(Vector2dInt(0, 0), Vector2dInt(7, 7)) &&
                    !threats.any {potentialPos == position + position.straightOrDiagonalDirectionTo(it.position) * -1}) {
                    if (board[potentialPos] == null || board[potentialPos]?.color != color)
                        yield(potentialPos)
                }
            }

            if (canMoveTo(position + Vector2dInt.WEST*2))
                yield(position + Vector2dInt.WEST*2)

            if (canMoveTo(position + Vector2dInt.EAST*2))
                yield(position + Vector2dInt.EAST*2)
        }
    }

    override fun afterMoveToAction(newPos: Vector2dInt): () -> Unit {
        val savedPos = position
        return fun (){      // why. does. making. it. fun. fixes. returns.
            super.afterMoveToAction(newPos).invoke()

            val rookPos: Vector2dInt

            val newRookPos: Vector2dInt

            if (newPos == savedPos + Vector2dInt.EAST * 2) {
                rookPos = Vector2dInt(7, savedPos.y)
                newRookPos = newPos + Vector2dInt.WEST
            } else if (newPos == savedPos + Vector2dInt.WEST * 2) {
                rookPos = Vector2dInt(0, savedPos.y)
                newRookPos = newPos + Vector2dInt.EAST
            } else {
                return
            }
            // move pieces directly to avoid canMoveTo check
            val action = board[rookPos]!!.afterMoveToAction(newRookPos)
            board[newRookPos] = board[rookPos]
            board[rookPos] = null
            action()
        }
    }
}

@Serializable
@SerialName("Pawn")
class PawnPiece : Piece {
    constructor(board: BoardContainer, color: Player): super(Vector2dInt.OUT_OF_BOUNDS, color) {
        this.board = board
    }

    private val enPassageEnemyPawnsPositions = mutableListOf<Vector2dInt>()
    private val forwardDirection = if (color == Player.WHITE) Vector2dInt.NORTH else Vector2dInt.SOUTH
    // funny enough, this list is always at 1 element max
    // but damnt im too lazy to refactor

    private val deadEndY = if (color == Player.WHITE) 0 else 7

    override val pieceData: PieceData
        get() = PawnData

    fun resetEnPassage() {
        enPassageEnemyPawnsPositions.clear()
    }

    override fun possibleMoves(): Sequence<Vector2dInt> {
        if (position.y == deadEndY)
            return sequence {  }

        val threat = board.game.gameObserver.kingThreatNullifiedByObstacleAt(this.position, this.color)
        if (threat!=null) {
            if (canMoveTo(threat.position))
                return sequenceOf(threat.position)

            return sequence {  }
        }

        val checkState = board.game.gameObserver.checkmateState()

        if (checkState == HowBadThingsReallyAre.ONLY_KING_CAN_MOVE)
            return sequence {  }


        val diagonalShifts : List<Vector2dInt> =
            (if (color == Player.WHITE) {
                listOf(
                    Vector2dInt.NORTH + Vector2dInt.WEST,
                    Vector2dInt.NORTH + Vector2dInt.EAST)
            }
            else {
                listOf(
                    Vector2dInt.SOUTH + Vector2dInt.WEST,
                    Vector2dInt.SOUTH + Vector2dInt.EAST)
            }).filter { board.isWithinBorders(it + this.position) }


        val startingLine = if (color == Player.WHITE) 6 else 1

        return sequence {
            if (board[position + forwardDirection] == null) {
                yield(position + forwardDirection)
                if (board.isWithinBorders(position+forwardDirection * 2) && board[position + forwardDirection * 2] == null
                    && position.y == startingLine)     // at starting line
                    yield(position + forwardDirection * 2)
                }

            for (shift in diagonalShifts) {
                if (board[position + shift] != null
                    && board[position + shift]!!.color != color
                )
                    yield(position + shift)
            }

            for (enPas in enPassageEnemyPawnsPositions) {
                if (board[enPas+forwardDirection]==null)
                    yield(enPas+forwardDirection)
            }
        }.filter {
            if (checkState == HowBadThingsReallyAre.CAN_COVER_KING_WITH_OTHER_PIECE) {
                willCoverKingOrEliminateThreatPredicate(it)
            }
            else
                true
        }
    }

    override fun canMoveTo(pos: Vector2dInt): Boolean {
        if (!super.canMoveTo(pos))
            return false

        if (position.y == deadEndY)
            return false

        if (enPassageEnemyPawnsPositions.any { it+forwardDirection == pos} && board[pos]==null)
            return true

        if (this.color == Player.WHITE) {
            // trying to take piece
            if (pos == this.position + Vector2dInt.NORTH + Vector2dInt.WEST
                || pos == this.position + Vector2dInt.NORTH + Vector2dInt.EAST) {
                if (board[pos]!=null && board[pos]!!.color != this.color)
                    return true

                return false
            }

            // moving forward
            else if (pos == this.position + Vector2dInt.NORTH) {
                return board[pos] == null
            }
            else if (pos == this.position + Vector2dInt.NORTH * 2) {
                if (this.position.y != 6)       // on initial pos
                    return false

                return board[pos] == null && board[this.position + Vector2dInt.NORTH] == null
            }

            return false
        }

        else {

            // trying to take piece
            if (pos == this.position + Vector2dInt.SOUTH + Vector2dInt.WEST
                || pos == this.position + Vector2dInt.SOUTH + Vector2dInt.EAST) {
                if (board[pos]!=null && board[pos]!!.color != this.color)
                    return true

                return false
            }

            // moving forward
            else if (pos == this.position + Vector2dInt.SOUTH) {
                return board[pos] == null
            }
            else if (pos == this.position + Vector2dInt.SOUTH * 2) {
                if (this.position.y != 1)       // on initial pos
                    return false

                return board[pos] == null && board[this.position + Vector2dInt.SOUTH] == null
            }

            return false
        }
    }

    override fun afterMoveToAction(newPos: Vector2dInt): () -> Unit {
        val savedPos = position
        return fun() {
            super.afterMoveToAction(newPos).invoke()

            if (abs(newPos.y - savedPos.y)==2) {
                val positions = listOf(
                    newPos+Vector2dInt.EAST,
                    newPos+Vector2dInt.WEST
                )

                for (leftRight in positions) {
                    if (board.isWithinBorders(leftRight)
                        && board[leftRight] is PawnPiece
                        && board[leftRight]?.color != color
                    ) {
                        val piece = board[leftRight] as PawnPiece
                        Log.println(Log.INFO, "Pawn EnPassage", "Notified pawn at $leftRight about enPassage possibility")
                        piece.enPassageEnemyPawnsPositions.add(newPos)
                    }
                }
            }

            if (enPassageEnemyPawnsPositions.any { it+forwardDirection == newPos}) {
                board[newPos-forwardDirection]?.onTakenAction()
                board[newPos-forwardDirection] = null
            }

            if (newPos.y == deadEndY) {
                board[newPos] = QueenPiece(board, color)
            }
        }
    }
}