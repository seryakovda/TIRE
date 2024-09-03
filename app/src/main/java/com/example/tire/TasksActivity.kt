package com.example.tire

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tire.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TasksActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tasks)
    }
}