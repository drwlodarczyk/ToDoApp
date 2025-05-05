package com.example.todoapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.todoapp.com.example.todoapp.Task
import com.example.todoapp.com.example.todoapp.TaskAdapter
import android.content.Intent
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerViewTasks: RecyclerView
    private lateinit var editTextTask: EditText
    private lateinit var buttonAddTask: Button
    private lateinit var taskAdapter: TaskAdapter
    private val tasks = mutableListOf<Task>()

    // Room Database
    private lateinit var db: AppDatabase
    private lateinit var taskDao: TaskDao

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize RecyclerView and other views
        recyclerViewTasks = findViewById(R.id.recyclerViewTasks)
        editTextTask = findViewById(R.id.editTextTask)
        buttonAddTask = findViewById(R.id.buttonAddTask)

        // Initialize Room Database
        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "task-db").build()
        taskDao = db.taskDao()

        // Set up RecyclerView
        taskAdapter = TaskAdapter(tasks)
        recyclerViewTasks.layoutManager = LinearLayoutManager(this)
        recyclerViewTasks.adapter = taskAdapter

        // Add task button listener
        buttonAddTask.setOnClickListener {
            val taskTitle = editTextTask.text.toString()
            if (taskTitle.isNotBlank()) {
                val newTask = TaskEntity(title = taskTitle)
                // Add the task to the database asynchronously
                CoroutineScope(Dispatchers.IO).launch {
                    taskDao.insert(newTask)
                    loadTasks()  // Refresh task list after insertion
                }
                editTextTask.text.clear()
            }
        }

        // Load tasks when the app starts
        loadTasks()
    }

    private fun loadTasks() {
        // Load tasks asynchronously from the database
        CoroutineScope(Dispatchers.IO).launch {
            val taskList = taskDao.getAllTasks()
            runOnUiThread {
                tasks.clear()
                tasks.addAll(taskList.map { Task(it.title, it.isDone) })
                taskAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser == null) {
            // If no user is logged in, go back to LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // Finish MainActivity to prevent the user from navigating back to it
        }
    }
}
