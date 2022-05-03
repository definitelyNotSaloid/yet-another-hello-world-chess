package com.etu.yahwChess.misc

import android.widget.GridLayout
import androidx.core.view.children
import com.etu.yahwChess.model.board.container.BoardContainer
import com.etu.yahwChess.model.board.pieceMover.PieceMover
import com.etu.yahwChess.view.BoardViewHelper
import com.etu.yahwChess.view.board.piece.CellView

class CurrentGame(val boardView : GridLayout) {
    val boardContainer = BoardContainer(this)
    val boardViewHelper = BoardViewHelper(boardView)
    val pieceMover = PieceMover(boardContainer)

    init {
        for (child in boardView.children) {
            child as CellView
            child.board = boardContainer
        }
    }


}