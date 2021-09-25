package com.example.patikadev.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import com.patika.homework4.utils.DEFAULT_VALUE

inline fun<reified T : AppCompatActivity> Context.startActivity(block : Intent.() -> Unit = {}){

    val intent  = Intent(this , T::class.java)
    startActivity(
        intent.also {
            block.invoke(it)
        }
    )
}

fun Fragment.navigateFragment(id:Int){
    findNavController().navigate(id)
}

fun View.visible(){
    this.visibility = View.VISIBLE
}

fun View.gone(){
    this.visibility = View.GONE
}

//Display popup when fragment call on back
fun Fragment.onBack() =
    requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            requireActivity().finish()
        }

    })

fun Fragment.saveDataAsString(key : String, value : String){
    val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences("TodoSharedPref", MODE_PRIVATE)

    val myEdit = sharedPreferences.edit()

    myEdit.putString(key, value)

    myEdit.commit()
}

fun AppCompatActivity.getDataAsString(key : String): String? {
    val sharedPreferences: SharedPreferences = getSharedPreferences("TodoSharedPref", MODE_PRIVATE)
    return sharedPreferences.getString(key, DEFAULT_VALUE)
}