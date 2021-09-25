package com.patika.homework4.model


class User {

    var name: String?= null
    var age : Int ?= null
    var email : String ?= null
    var _id : String ?= null
    var token : String ?= null

    companion object{
        var user : User ?= null

        fun getCurrentInstance() : User {
            /*user?.let {
            } ?: kotlin.run {
            }*/

            if(user == null){
                user = User()
            }

            return user!!
        }
    }


    fun setUser(loggedUser: User){
        user?.age = loggedUser.age
        user?.name = loggedUser.name
        user?.email = loggedUser.email
        user?._id = loggedUser._id
    }

}