package com.example.todoapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import android.widget.ImageButton

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
                .setTitle("Edytuj zadanie")
                .setView(editText)
                .setPositiveButton("Zapisz") { _, _ ->
                    task.title = editText.text.toString()
                    notifyItemChanged(position)
                }
                .setNegativeButton("Anuluj", null)
                .show()
        }

        holder.buttonDelete.setOnClickListener {
            AlertDialog.Builder(holder.itemView.context)
                .setTitle("Usuń zadanie")
                .setMessage("Czy na pewno chcesz usunąć to zadanie?")
                .setPositiveButton("Tak") { _, _ ->
                    tasks.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, tasks.size)
                }
                .setNegativeButton("Anuluj", null)
                .show()
        }

    }


    override fun getItemCount(): Int {
        return tasks.size
    }
}
