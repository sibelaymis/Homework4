package com.patika.homework4.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.patikadev.utils.getDataAsString
import com.example.patikadev.utils.startActivity
import com.patika.homework4.R
import com.patika.homework4.model.User
import com.patika.homework4.utils.DEFAULT_VALUE
import com.patika.homework4.utils.USER_TOKEN
import java.util.*

class SplashActivity : AppCompatActivity() {
    private val DELAY  :  Long = 2 * 1000
    private var token : String?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Timer().schedule(object : TimerTask() {
            override fun run() {
                val isUserLogged=isLoggedIn()
                if(isUserLogged) User.getCurrentInstance().token = token
                //Intent ile login olup olmadığı gönderilir
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                intent.putExtra("isUserLogged", isUserLogged)
                startActivity(intent)
                finish()
            }
        }, DELAY)

    }

    private fun isLoggedIn() : Boolean{
        val token = getToken()
        return token.isNotEmpty()
    }

    private fun getToken() : String{
        token = getDataAsString(USER_TOKEN)!!
        return token!!
    }

}