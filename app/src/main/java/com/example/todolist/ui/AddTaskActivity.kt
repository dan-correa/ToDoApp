package com.example.todolist.ui

import android.app.Activity
import android.nfc.Tag
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import com.example.todolist.dataSource.TaskDataSource
import com.example.todolist.databinding.ActivityAddTaskBinding
import com.example.todolist.extensions.format
import com.example.todolist.extensions.text
import com.example.todolist.model.Task
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat

import java.util.*

class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityAddTaskBinding.inflate(layoutInflater)


        setContentView(binding.root)

        if(intent.hasExtra(TASK_id)){

            val taskId = intent.getIntExtra(TASK_id, 0)
            TaskDataSource.findById(taskId)?.let{
                binding.tilTitle.text = it.title
                binding.tilDate.text = it.date
                binding.tilHour.text = it.hour

            }
        }

        insertListeners()

    }

    private fun insertListeners() {
        binding.tilDate.editText?.setOnClickListener{
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.addOnPositiveButtonClickListener {
                val timeZone = TimeZone.getDefault()
                val offset = timeZone.getOffset(Date().time) * -1
                binding.tilDate.text = Date(it + offset).format()
            }
            datePicker.show(supportFragmentManager, "DATE_PIKER_TAG")
        }

        binding.tilHour.editText?.setOnClickListener {
           val timePiker = MaterialTimePicker.Builder()
               .setTimeFormat(TimeFormat.CLOCK_24H)
                   .setTimeFormat(TimeFormat.CLOCK_24H)
                   .build()
            timePiker.addOnPositiveButtonClickListener {
                val minute = if (timePiker.minute in 0..9) "0${timePiker.minute}" else timePiker.minute
                val hour = if (timePiker.hour in 0..9) "0${timePiker.hour}" else timePiker.hour

                binding.tilHour.text="$hour : $minute"
           }
            timePiker.show(supportFragmentManager,null)

        }

        binding.btnCancel.setOnClickListener{
            finish()
        }

        binding.btnNewTask.setOnClickListener{
            val task = Task(
                title = binding.tilTitle.text,
                date = binding.tilDate.text,
                hour = binding.tilHour.text,
                id = intent.getIntExtra(TASK_id, 0)
            )

            TaskDataSource.insertTask(task)

            setResult(Activity.RESULT_OK)

            finish()

        }
    }

    companion object {
        const val TASK_id = "task_id"
    }
}