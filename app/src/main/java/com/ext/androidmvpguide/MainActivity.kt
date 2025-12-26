package com.ext.androidmvpguide

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ext.androidmvpguide.adapter.UserAdapter
import com.ext.androidmvpguide.contract.MainContract
import com.ext.androidmvpguide.model.User
import com.ext.androidmvpguide.presenter.MainPresenter
import com.ext.androidmvpguide.repository.UserRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), MainContract.View {

    private lateinit var presenter: MainPresenter
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvEmpty: TextView
    private lateinit var fabRefresh: FloatingActionButton
    private lateinit var userAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
        initPresenter()
        setupRecyclerView()
        setupListeners()

        // Load initial data
        presenter.loadUsers()
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)
        tvEmpty = findViewById(R.id.tvEmpty)
        fabRefresh = findViewById(R.id.fabRefresh)
    }

    private fun initPresenter() {
        val repository = UserRepository()
        presenter = MainPresenter(repository)
        presenter.attachView(this)
    }

    private fun setupRecyclerView() {
        userAdapter = UserAdapter { user ->
            presenter.onUserClicked(user)
        }
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = userAdapter
        }
    }

    private fun setupListeners() {
        fabRefresh.setOnClickListener {
            presenter.loadUsers()
        }
    }

    override fun showLoading() {
        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        tvEmpty.visibility = View.GONE
    }

    override fun hideLoading() {
        progressBar.visibility = View.GONE
    }

    override fun showUsers(users: List<User>) {
        recyclerView.visibility = View.VISIBLE
        tvEmpty.visibility = View.GONE
        userAdapter.setUsers(users)
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showEmpty() {
        recyclerView.visibility = View.GONE
        tvEmpty.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }
}