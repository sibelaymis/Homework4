package com.patika.homework4.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import com.patika.homework4.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Intentten gelen bilgiye göre fragment açılır
        val intent=getIntent()
        val isUserLogged:Boolean= intent.getBooleanExtra("isUserLogged",false)
        val navController = findNavController(R.id.fragment_container_view)
        if(isUserLogged) {
            navController.navigate(R.id.homeFragment)
        }
        else{
            navController.navigate(R.id.loginFragment)
        }
    }
}