package com.oskamathis.watchtracker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.common.collect.ImmutableList
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.math.log
//import jdk.nashorn.internal.runtime.ECMAException.getException
//import androidx.test.orchestrator.junit.BundleJUnitUtils.getResult
import com.google.firebase.firestore.QueryDocumentSnapshot
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout


//import org.junit.experimental.results.ResultMatchers.isSuccessful


class MainActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(itemDecoration)
        reloadTasks()
        showTasks()
//        getTimes()
    }

    private fun reloadTasks() {
        db.collection("tasks").addSnapshotListener { value, e ->
            if (e != null) {
                Log.w("MainActivity", "Listen failed.", e)
                return@addSnapshotListener
            }
            val tasks = mutableListOf<String>()

            if (value != null) {
                for (doc in value) {
                    if (doc.get("name") != null) {
                        tasks.add(doc.getString("name")!!)
                    }
                }
            }
            Log.d("MainActivity", "Current tasks: $tasks")
        }
    }

    private fun showTasks() {
        db.collection("tasks").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val result = task.result ?: return@addOnCompleteListener
                val taskNames = mutableListOf<String>()
                for (document in result) {
                    val taskName = document.data["name"].toString()
                    Log.d("MainActivity", "${document.data}")
                    taskNames.add(taskName)
                    recyclerView.adapter = RecyclerAdapter(this, taskNames)
                }
            } else {
                Log.d("MainActivity", "Error getting documents: ", task.exception)
            }
        }
    }

    private fun getTimes() {
        db.collection("tasks").get().addOnCompleteListener {
            if (it.isSuccessful) {
                val result = it.result ?: return@addOnCompleteListener
                for (task in result) {
                    db.collection("tasks").document(task.id).collection("times").get().addOnCompleteListener { it2 ->
                        val result2 = it2.result ?: return@addOnCompleteListener
                        for (time in result2) {
                            val started_at = java.util.Date(time.data["started_at"].toString())
                            val stopped_at = java.util.Date(time.data["stopped_at"].toString())
//                            val duration = stopped_at - started_at
                        }
                    }
                }
            }
        }
    }
}
