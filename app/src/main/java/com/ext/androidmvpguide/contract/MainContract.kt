package com.ext.androidmvpguide.contract

import com.ext.androidmvpguide.model.User

interface MainContract {

    interface View {
        fun showLoading()
        fun hideLoading()
        fun showUsers(users: List<User>)
        fun showError(message: String)
        fun showEmpty()
    }

    interface Presenter {
        fun attachView(view: View)
        fun detachView()
        fun loadUsers()
        fun onUserClicked(user: User)
    }
}