package com.ext.androidmvpguide.presenter

import android.widget.Toast
import androidx.leanback.widget.Presenter
import com.ext.androidmvpguide.contract.MainContract
import com.ext.androidmvpguide.model.User
import com.ext.androidmvpguide.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainPresenter(
    private val repository: UserRepository
) : MainContract.Presenter {

    private var view: MainContract.View? = null
    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)

    override fun attachView(view: MainContract.View) {
        this.view = view
    }

    override fun detachView() {
        view = null
        job.cancel()
    }

    override fun loadUsers() {
        view?.showLoading()

        coroutineScope.launch {
            try {
                val users = withContext(Dispatchers.IO) {
                    repository.getUsers()
                }

                view?.hideLoading()

                if (users.isEmpty()) {
                    view?.showEmpty()
                } else {
                    view?.showUsers(users)
                }
            } catch (e: Exception) {
                view?.hideLoading()
                view?.showError(e.message ?: "Unknown error occurred")
            }
        }
    }

    override fun onUserClicked(user: User) {
        // Handle user click - could navigate to detail screen
        // For demo, just show a message through view
        view?.showError("Clicked: ${user.name}")
    }
}