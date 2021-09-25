package com.patika.homework4.ui.recyclerview.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.patika.homework4.R
import com.patika.homework4.base.BaseFragment
import com.patika.homework4.ui.recyclerview.adapter.TaskRecyclerViewAdapter
import com.patika.homework4.ui.recyclerview.model.TaskModel
import kotlinx.android.synthetic.main.fragment_home.*
import com.example.patikadev.utils.onBack
import com.patika.homework4.service.BaseCallBack
import com.patika.homework4.service.ServiceConnector
import com.patika.homework4.ui.recyclerview.model.ResponseTask
import com.patika.homework4.ui.recyclerview.model.ResponseTaskList
import com.patika.homework4.ui.recyclerview.swipe.SwipeGesture


class HomeFragment : BaseFragment() {

    private var taskRecyclerviewAdapter: TaskRecyclerViewAdapter? = null
    private lateinit var itemTouchHelper: ItemTouchHelper
    private var taskList: MutableList<TaskModel> = mutableListOf()
    private var isLoading: Boolean =false
    private var isLastPage: Boolean = false
    private lateinit var layoutManager: LinearLayoutManager

    private val limit :Int = 10
    private var skip :Int = 0

    override fun getLayoutID(): Int = R.layout.fragment_home

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBack()
        //get first page of list
        getItemsWithPagination()

        prepareRecyclerView()
        //show material dialog when click add fab button
        add_fab.setOnClickListener { this.showCustomDialog() }
    }

    //initialize recyclerview
    fun prepareRecyclerView() {
        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerview.layoutManager = layoutManager
        //taskRecyclerviewAdapter = TaskRecyclerViewAdapter(taskList)


    taskRecyclerviewAdapter = TaskRecyclerViewAdapter(taskList){clickedObject, itemID ->
            when(itemID){
                R.id.completeButton -> {
                    completeTask(clickedObject, taskList.indexOf(clickedObject))

                }
                R.id.deleteButton ->{
                    deleteTask(clickedObject, taskList.indexOf(clickedObject))

                }
            }
        }
        recyclerview.adapter = taskRecyclerviewAdapter
        //add swipe property to recyclerview
        /*val swipeGesture = object : SwipeGesture(requireContext()) {
            override fun onMove(
                recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        //delete task by id
                        deleteTask(taskList[position], position)
                    }
                    ItemTouchHelper.RIGHT -> {
                        //update task by id
                        completeTask(taskList[position], position)
                    }
                }
            }


        }

        itemTouchHelper = ItemTouchHelper(swipeGesture)
        itemTouchHelper.attachToRecyclerView(recyclerview)*/

        recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                if (!isLoading && !isLastPage) {
                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {//eğer yüklenme işlemi yoksa ve liste en aşağıdaysa

                        addLoadMoreData() //yeni değerleri yükle
                    }
                }
            }
        })
    }

    private fun addLoadMoreData() {
        isLoading = true
        skip += limit //increase skipped data
        getItemsWithPagination() //get new items
    }

    private fun getItemsWithPagination() {
        val params = mutableMapOf<String, Int>().apply {
            put("limit", limit)
            put("skip", skip)
        }
        //call list service
        ServiceConnector.restInterface.getTasksWithPagination(params)
            .enqueue(object : BaseCallBack<ResponseTaskList>() {

                override fun onSuccess(data: ResponseTaskList) {
                    if (taskRecyclerviewAdapter == null) {
                        prepareRecyclerView()
                    }
                    //add new items to list
                    if(data.taskList.isEmpty()) isLastPage=true
                    taskList.addAll(data.taskList)
                    taskRecyclerviewAdapter?.notifyDataSetChanged() //dataset is reload
                    isLoading = false //not loading

                }

                override fun onFailure() {
                    Log.e("something went", "wrong")

                }
            })

    }
//prepare custom material dialog
    fun showCustomDialog(){
        val dialog=MaterialDialog(requireContext()).noAutoDismiss().customView(R.layout.custom_dialog)
        dialog.findViewById<AppCompatButton>(R.id.add_task).setOnClickListener {
            //add new task when click apply button
            val newTaskDescription=dialog.findViewById<AppCompatEditText>(R.id.new_task_text)
            val description=newTaskDescription.text.toString()
            if (!description.isNullOrEmpty()) {
                addTask(description)
                dialog.cancel()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Please enter the task description",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    //close material dialog when click dismiss button
        dialog.findViewById<AppCompatButton>(R.id.dismiss).setOnClickListener {
            dialog.cancel()
        }
        dialog.show()
    }
    //add new task with api call
    private fun addTask(description:String) {
        val params = mutableMapOf<String, Any>().apply {
            put("description", description)
        }
        ServiceConnector.restInterface.addTask(params)
            .enqueue(object : BaseCallBack<ResponseTask>() {

                override fun onSuccess(data: ResponseTask) {
                    Log.d("success","Tasks is get in successfully")
                    Toast.makeText(requireContext(),"Task is added",Toast.LENGTH_SHORT).show()
                    taskList.add(data.task)
                    skip+=1
                    getItemsWithPagination()
                }

                override fun onFailure() {
                    Log.e("something went", "wrong")

                }
            })
    }

    //update completed of task with true
    fun completeTask(task: TaskModel, position: Int) {
        val params = mutableMapOf<String, Any>().apply {
            put("completed", true)
        }
        task._id?.let {
            ServiceConnector.restInterface.completeTask(it, params)
                .enqueue(object : BaseCallBack<ResponseTask>() {

                    override fun onSuccess(data: ResponseTask) {
                        Log.d("success","Tasks is complete in successfully")
                        Toast.makeText(requireContext(),"Task is completed",Toast.LENGTH_SHORT).show()
                        taskRecyclerviewAdapter?.completeItem(position)
                    }

                    override fun onFailure() {
                        Log.e("something went", "wrong")
                        taskRecyclerviewAdapter?.notifyDataSetChanged()
                    }
                })
        }
    }

//delete task
    fun deleteTask(task: TaskModel, position: Int) {
        task._id?.let {
            ServiceConnector.restInterface.deleteTask(it)
                .enqueue(object : BaseCallBack<ResponseTask>() {

                    override fun onSuccess(data: ResponseTask) {
                        Log.d("success","Tasks is delete in successfully")
                        Toast.makeText(requireContext(),"Task is deleted",Toast.LENGTH_SHORT).show()
                        taskList.remove(data.task)
                        taskRecyclerviewAdapter?.deleteItem(position)
                    }

                    override fun onFailure() {
                        Log.e("something went", "wrong")

                    }
                })
        }
    }
}