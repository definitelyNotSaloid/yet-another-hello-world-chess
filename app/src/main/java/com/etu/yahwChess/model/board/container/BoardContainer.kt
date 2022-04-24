package com.etu.yahwChess.model.board.container

import android.content.ClipData
import android.content.ClipDescription
import android.os.Bundle
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.etu.yahwChess.R
import com.etu.yahwChess.misc.Vector2dInt
import com.etu.yahwChess.model.pieces.Piece
import kotlinx.android.synthetic.main.activity_main.*

// Its just a data container
// Any logic besides moving stuff should be implemented in PieceMover class
class BoardContainer {
    operator fun get(pos: Vector2dInt) : Piece? {
        TODO("implement")
    }
    operator fun set(pos: Vector2dInt, piece: Piece?) {     // force-set (or clear) cell content
        TODO()
    }

    fun move(from: Vector2dInt, to: Vector2dInt) {      // if at to-pos there was some piece, it will be replaced
        TODO()
    }
    fun tryPlaceAt(piece: Piece, pos: Vector2dInt) : Boolean {      // returns true and places piece if cell was empty, returns false and does nothing otherwise
        TODO()
    }

    fun testFun(){
    }
}

    class MainActivity: AppCompatActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            llTop.setOnDragListener(dragListener)
            llBottom.setOnDragListener(dragListener)

            dragView.setOnLongClickListener{
                val clipText = "clip data"
                val item = ClipData.Item(clipText)
                val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
                val data = ClipData(clipText, mimeTypes, item)

                val dragShadowBuilder = View.DragShadowBuilder(it)
                it.startDragAndDrop(data, dragShadowBuilder, it, 0)

                it.visibility = View.INVISIBLE
                true
            }
        }

        val dragListener = View.OnDragListener { view, event ->
            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    event.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)
                }
                DragEvent.ACTION_DRAG_ENTERED -> {
                    view.invalidate()
                    true
                }
                DragEvent.ACTION_DRAG_LOCATION -> true
                DragEvent.ACTION_DRAG_EXITED -> {
                    view.invalidate()
                    true
                }
                DragEvent.ACTION_DROP -> {
                    val item = event.clipData.getItemAt(0)
                    val dragData = item.text
                    Toast.makeText(this, dragData, Toast.LENGTH_SHORT).show()

                    view.invalidate()

                    val v = event.localState as View
                    val owner = v.parent as ViewGroup
                    owner.removeView(v)
                    val destination = view as LinearLayout
                    destination.addView(v)
                    v.visibility = View.VISIBLE
                    true
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    view.invalidate()
                    true
                }
                else -> false
            }
        }

    }