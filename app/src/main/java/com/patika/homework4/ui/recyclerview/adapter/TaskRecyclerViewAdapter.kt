package com.patika.homework4.ui.recyclerview.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.patika.homework4.R
import com.patika.homework4.ui.recyclerview.model.TaskModel

class TaskRecyclerViewAdapter(private val taskList: MutableList<TaskModel>,private val block : (TaskModel, Int) -> Unit ): RecyclerView.Adapter<TaskViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_task,parent,false))
    }


    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task=taskList[position]
        holder.setData(task)
        holder.setOnItemClickListener(task, this@TaskRecyclerViewAdapter.block)
    }

    override fun getItemCount(): Int = taskList.size

    fun deleteItem(position: Int){
        taskList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun addItem(task: TaskModel,position: Int){
        taskList.add(position,task)
        notifyItemInserted(position)
    }

    fun completeItem(position: Int){
        var task=taskList[position]
        task.completed=true
        notifyItemChanged(position)
    }

}
class TaskViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val taskText=itemView.findViewById<AppCompatTextView>(R.id.task_text)
    private val isCompleted=itemView.findViewById<AppCompatImageView>(R.id.task_checkbox)
    private val completedBtn=itemView.findViewById<AppCompatImageView>(R.id.completeButton)
    private val deleteBtn=itemView.findViewById<AppCompatImageView>(R.id.deleteButton)

    fun setData(task: TaskModel){
        taskText.text=task.description
        isCompleted.setImageResource(if(task.completed) R.drawable.ic_marked else R.drawable.ic_unmarked)
    }
    fun setOnItemClickListener(
        task: TaskModel,
        block: (TaskModel, Int) -> Unit
    ) {
        completedBtn.setOnClickListener {

            block.invoke(task, it.id)
        }

        deleteBtn.setOnClickListener {
            block.invoke(task, it.id)
        }
    }
}