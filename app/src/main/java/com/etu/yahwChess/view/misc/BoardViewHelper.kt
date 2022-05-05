package com.etu.yahwChess.view.misc

import android.util.Log
import android.view.View.NO_ID
import android.widget.GridLayout
import androidx.core.view.children
import com.etu.yahwChess.misc.cellIndexToVector
import com.etu.yahwChess.view.board.piece.CellView
import java.lang.RuntimeException

class BoardViewHelper(val boardView : GridLayout) {
    private val viewGrid = ArrayList<ArrayList<CellView>>(8)

    init {
        val viewGridButWithTmpNulls = ArrayList<ArrayList<CellView?>>(8)
        for (i in 0..7) {
            viewGridButWithTmpNulls.add(ArrayList(8))
            for (j in 0..7) {
                viewGridButWithTmpNulls[i].add(null)
            }
        }

        for (child in boardView.children) {
            if (child.id != NO_ID && child is CellView) {
                val pos = cellIndexToVector(child.cellIndex)
                Log.println(Log.INFO, "build", "found view at $pos, name = ${child.resources.getResourceName(child.id)}")
                viewGridButWithTmpNulls[pos.y][pos.x] = child
            }
        }

        if (viewGridButWithTmpNulls.any { it.contains(null) }) throw RuntimeException("Fix your board damn")

        for (i in 0..7) {
            viewGrid.add(ArrayList(8))
            for (j in 0..7) {
                viewGrid[i].add(viewGridButWithTmpNulls[i][j] as CellView)
            }
        }
    }

    fun getCellView(cellIndex : String) : CellView {
        val pos = cellIndexToVector(cellIndex)
        return viewGrid[pos.y][pos.x]
    }
}