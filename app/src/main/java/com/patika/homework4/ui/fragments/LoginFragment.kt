package com.patika.homework4.ui.fragments

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.example.patikadev.utils.navigateFragment
import com.example.patikadev.utils.saveDataAsString
import com.patika.homework4.R
import com.patika.homework4.base.BaseFragment
import com.patika.homework4.model.LoginResponse
import com.patika.homework4.model.User
import com.patika.homework4.service.BaseCallBack
import com.patika.homework4.service.ServiceConnector
import com.patika.homework4.utils.USER_TOKEN
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : BaseFragment() {

    override fun getLayoutID(): Int = R.layout.fragment_login

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        login_btn.setOnClickListener {
            val email = email_etv.text.toString()
            val password = password_etv.text.toString()
            if (allFieldsAreValid(email, password)) {
                val params = mutableMapOf<String, Any>().apply {
                    put("email", email)
                    put("password", password)
                }
                ServiceConnector.restInterface.login(params)
                    .enqueue(object : BaseCallBack<LoginResponse>() {

                        override fun onSuccess(data: LoginResponse) {
                            User.getCurrentInstance().setUser(data.user)
                            User.getCurrentInstance().token = data.token
                            saveDataAsString(USER_TOKEN, data.token)
                            Toast.makeText(
                                requireContext(),
                                "User is logged in successfully",
                                Toast.LENGTH_SHORT
                            )
                            navigateFragment(R.id.homeFragment)
                        }

                        override fun onFailure() {
                            Log.e("something went", "wrong")

                        }
                    })

            }
        }
    }

    private fun allFieldsAreValid(
        email: String,
        password: String
    ): Boolean {
        var allFieldsAreValid = true

        if (email.isEmpty() || !isValidEmail(email)) {
            allFieldsAreValid = false
        }

        if (password.isEmpty() || password.length < 7) allFieldsAreValid = false


        return allFieldsAreValid
    }


    fun isValidEmail(target: CharSequence): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

}