package com.example.todoapp

import androidx.room.*
import java.util.concurrent.Executors

@Dao
interface TaskDao {
    @Insert
    fun insert(task: TaskEntity)

    @Update
    fun update(task: TaskEntity)

    @Delete
    fun delete(task: TaskEntity)

    @Query("SELECT * FROM tasks")
    fun getAllTasks(): List<TaskEntity>
}

class TaskRepository(private val taskDao: TaskDao) {

    private val executor = Executors.newSingleThreadExecutor()

    fun insertTask(task: TaskEntity) {
        executor.execute {
            taskDao.insert(task)
        }
    }

    fun getTasks(): List<TaskEntity> {
        return taskDao.getAllTasks()
    }
}
