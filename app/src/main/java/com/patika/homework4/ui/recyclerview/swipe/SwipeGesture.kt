package com.patika.homework4.ui.recyclerview.swipe

import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.patika.homework4.R
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

abstract class SwipeGesture(context: Context):
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    private val leftBackgroundColor = ContextCompat.getColor(context, R.color.red_color)
    private val rightBackgroundColor = ContextCompat.getColor(context,R.color.green_color)

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    //customize swipe color and icon
    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        RecyclerViewSwipeDecorator.Builder(
            c,
            recyclerView,
            viewHolder,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )
            .addSwipeLeftBackgroundColor(leftBackgroundColor)
            .addSwipeLeftActionIcon(R.drawable.ic_action_name_delete)
            .addSwipeRightBackgroundColor(rightBackgroundColor)
            .addSwipeRightActionIcon(R.drawable.ic_action_name_ok)
            .create()
            .decorate()
        super.onChildDraw(
            c, recyclerView,
            viewHolder, dX, dY, actionState, isCurrentlyActive
        )
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        TODO("Not yet implemented")
    }
}