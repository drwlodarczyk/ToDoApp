package com.example.todoapp.com.example.todoapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.app.AlertDialog
import android.widget.EditText
import com.example.todoapp.R

class TaskAdapter(private val tasks: MutableList<Task>) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkBoxTask: CheckBox = itemView.findViewById(R.id.checkBoxTask)
        val buttonEdit: ImageButton = itemView.findViewById(R.id.buttonEdit)
        val buttonDelete: ImageButton = itemView.findViewById(R.id.buttonDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.checkBoxTask.text = task.title
        holder.checkBoxTask.isChecked = task.isDone

        holder.checkBoxTask.setOnClickListener {
            task.isDone = !task.isDone
            holder.checkBoxTask.isChecked = task.isDone
        }

        holder.buttonEdit.setOnClickListener {
            val editText = EditText(holder.itemView.context)
            editText.setText(task.title)

            AlertDialog.Builder(holder.itemView.context)
                .setTitle("Edit Task")
                .setView(editText)
                .setPositiveButton("Save") { _, _ ->
                    task.title = editText.text.toString()
                    notifyItemChanged(position)
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        holder.buttonDelete.setOnClickListener {
            AlertDialog.Builder(holder.itemView.context)
                .setTitle("Delete Task")
                .setMessage("Are you sure you want to delete this task?")
                .setPositiveButton("Yes") { _, _ ->
                    tasks.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, tasks.size)
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    override fun getItemCount(): Int {
        return tasks.size
    }
}
