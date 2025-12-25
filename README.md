# Android MVP Architecture 

A clean, production-ready implementation of the Model-View-Presenter (MVP) architecture pattern in Android with Kotlin.

## 📱 What You'll Build

A user list app demonstrating:
- ✅ Clean separation of concerns
- ✅ Testable business logic
- ✅ Loading states management
- ✅ Error handling
- ✅ Pull to refresh functionality
- ✅ Proper lifecycle management

## 🏗️ Architecture Overview

```
┌─────────────────────────────────────────┐
│           VIEW (MainActivity)            │
│  - Displays UI                          │
│  - Handles user input                   │
│  - Implements MainContract.View         │
└────────────────┬────────────────────────┘
                 │ Interface
                 ↓
┌─────────────────────────────────────────┐
│        PRESENTER (MainPresenter)        │
│  - Business logic                       │
│  - Coordinates between View & Model     │
│  - Android-free (easily testable)      │
└────────────────┬────────────────────────┘
                 │
                 ↓
┌─────────────────────────────────────────┐
│      MODEL (Repository + Data)          │
│  - Data management                      │
│  - API calls / Database operations      │
└─────────────────────────────────────────┘
```

## 📁 Project Structure

```
app/src/main/java/com/ext/androidmvpguide/
├── model/
│   └── User.kt                    # Data model
├── repository/
│   └── UserRepository.kt          # Data source
├── contract/
│   └── MainContract.kt            # View-Presenter interface
├── presenter/
│   └── MainPresenter.kt           # Business logic
├── adapter/
│   └── UserAdapter.kt             # RecyclerView adapter
└── MainActivity.kt                # View implementation

app/src/main/res/layout/
├── activity_main.xml              # Main screen layout
└── item_user.xml                  # List item layout
```

## 🚀 Quick Setup

### 1. Update build.gradle.kts

Add these dependencies to your app-level `build.gradle.kts`:

```kotlin
dependencies {
    // Core Android
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    
    // RecyclerView & CardView
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.cardview:cardview:1.0.0")
    
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
}
```

### 2. Create the Files

Copy all the provided files into your project following the structure above.

### 3. Run the App

Build and run! You'll see:
- A loading spinner while fetching users
- A list of 5 sample users in cards
- Click any user to see a toast message
- Use the FAB button to refresh the list

## 🔑 Key Components Explained

### MainContract.kt
Defines the contract between View and Presenter:
- **View interface**: What the Presenter can ask the View to do
- **Presenter interface**: What the View can ask the Presenter to do

### MainPresenter.kt
The brain of the app:
- Handles all business logic
- Manages coroutines for async operations
- **No Android dependencies** (easily testable)
- Properly cancels jobs on detach

### MainActivity.kt
The View layer:
- Implements `MainContract.View`
- Only handles UI updates
- No business logic
- Attaches/detaches presenter based on lifecycle

### UserRepository.kt
Data layer:
- Simulates network delay (2 seconds)
- Returns sample user data
- Can be easily replaced with real API calls

## 💡 MVP Benefits

| Benefit | Description |
|---------|-------------|
| **Testability** | Presenter has no Android dependencies - easy to unit test |
| **Separation** | Clear boundaries between UI, logic, and data |
| **Maintainability** | Changes in one layer don't affect others |
| **Clarity** | Contract interface makes relationships explicit |

## 🎯 Best Practices Implemented

✅ **Memory Leak Prevention**: View is detached in `onDestroy()`  
✅ **Coroutine Management**: Jobs are cancelled when presenter detaches  
✅ **Null Safety**: Presenter checks if view is null before calling methods  
✅ **Loading States**: Proper loading, success, error, and empty states  
✅ **Clean Code**: Single responsibility for each component  

## 🔄 Data Flow Example

When user clicks refresh button:

1. `MainActivity` receives click → calls `presenter.loadUsers()`
2. `MainPresenter` calls `view.showLoading()`
3. `MainPresenter` fetches data from `UserRepository`
4. Repository returns user list after 2s delay
5. `MainPresenter` calls `view.hideLoading()` and `view.showUsers(users)`
6. `MainActivity` updates RecyclerView with new data

## 🧪 Testing

The MVP pattern makes testing straightforward:

```kotlin
// Example Presenter Test
@Test
fun `loadUsers shows loading then displays users`() = runTest {
    // Given
    val mockView = mock<MainContract.View>()
    val users = listOf(User(1, "John", "john@test.com"))
    whenever(repository.getUsers()).thenReturn(users)
    
    presenter.attachView(mockView)
    
    // When
    presenter.loadUsers()
    
    // Then
    verify(mockView).showLoading()
    verify(mockView).hideLoading()
    verify(mockView).showUsers(users)
}
```

## 📝 Customization Guide

### Add Real API Integration

Replace the simulated delay in `UserRepository.kt`:

```kotlin
suspend fun getUsers(): List<User> {
    return withContext(Dispatchers.IO) {
        // Your API call here
        apiService.fetchUsers()
    }
}
```

### Add Database Support

Extend the repository to cache data:

```kotlin
class UserRepository(
    private val api: ApiService,
    private val dao: UserDao
) {
    suspend fun getUsers(): List<User> {
        return try {
            val users = api.fetchUsers()
            dao.insertUsers(users)
            users
        } catch (e: Exception) {
            dao.getAllUsers() // Fallback to cached data
        }
    }
}
```

### Add Search Functionality

1. Add method to contract:
```kotlin
fun searchUsers(query: String)
```

2. Implement in presenter:
```kotlin
override fun searchUsers(query: String) {
    // Filter logic here
}
```

3. Call from view when user types

## 🐛 Common Issues

| Issue | Solution |
|-------|----------|
| App crashes on rotation | Ensure `detachView()` is called in `onDestroy()` |
| Memory leak warnings | Cancel coroutine job in `detachView()` |
| NullPointerException | Always use safe calls `view?.` in presenter |
| RecyclerView not updating | Call `notifyDataSetChanged()` in adapter |

## 📚 Further Reading

- [Android Architecture Guide](https://developer.android.com/topic/architecture)
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)
- [Testing in Android](https://developer.android.com/training/testing)

## 📄 License

MIT License

Copyright (c) 2025 Excelsior Technologies

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

---

