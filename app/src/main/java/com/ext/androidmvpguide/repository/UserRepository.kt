package com.ext.androidmvpguide.repository


import com.ext.androidmvpguide.model.User
import kotlinx.coroutines.delay

class UserRepository {

    // Simulating API call with delay
    suspend fun getUsers(): List<User> {
        delay(2000) // Simulate network delay
        return listOf(
            User(1, "John Doe", "john@example.com"),
            User(2, "Jane Smith", "jane@example.com"),
            User(3, "Bob Johnson", "bob@example.com"),
            User(4, "Alice Williams", "alice@example.com"),
            User(5, "Charlie Brown", "charlie@example.com"),
                    User(6, "John Doe", "john@example.com"),
        User(7, "Jane Smith", "jane@example.com"),
        User(8, "Bob Johnson", "bob@example.com"),
        User(9, "Alice Williams", "alice@example.com"),
        User(10, "Charlie Brown", "charlie@example.com")
        )
    }

    suspend fun getUserById(id: Int): User? {
        delay(1000)
        return getUsers().find { it.id == id }
    }
}