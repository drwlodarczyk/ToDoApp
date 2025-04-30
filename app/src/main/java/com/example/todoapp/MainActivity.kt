package com.example.todoapp  // upewnij się, że package masz taki sam

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerViewTasks: RecyclerView
    private lateinit var editTextTask: EditText
    private lateinit var buttonAddTask: Button
    private lateinit var taskAdapter: TaskAdapter
    private val tasks = mutableListOf<Task>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerViewTasks = findViewById(R.id.recyclerViewTasks)
        editTextTask = findViewById(R.id.editTextTask)
        buttonAddTask = findViewById(R.id.buttonAddTask)

        // Przykładowe zadania na start
        tasks.add(Task("Wynieść śmieci"))
        tasks.add(Task("Zrobić zakupy"))
        tasks.add(Task("Napisać projekt"))
        tasks.add(Task("Pójść pobiegać"))

        taskAdapter = TaskAdapter(tasks)
        recyclerViewTasks.layoutManager = LinearLayoutManager(this)
        recyclerViewTasks.adapter = taskAdapter

        buttonAddTask.setOnClickListener {
            val taskTitle = editTextTask.text.toString()
            if (taskTitle.isNotBlank()) {
                val newTask = Task(taskTitle)
                tasks.add(newTask)
                taskAdapter.notifyItemInserted(tasks.size - 1)
                editTextTask.text.clear()
            }
        }
    }
}
