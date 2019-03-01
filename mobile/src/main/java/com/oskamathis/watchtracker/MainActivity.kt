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

//import org.junit.experimental.results.ResultMatchers.isSuccessful


class MainActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView.layoutManager = LinearLayoutManager(this)
        getTaskNames()
    }

    private fun getTaskNames() {
        val taskNames = mutableListOf<String>()
        val docRef = db.collection("tasks")
        docRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val result = task.result ?: return@addOnCompleteListener
                for (document in result) {
                    val taskName = document.data["name"].toString()
                    Log.d("MainActivity", document.id + " => " + document.data["name"])
                    taskNames.add(taskName)
                    recyclerView.adapter = RecyclerAdapter(this, taskNames)
                }
            } else {
                Log.d("MainActivity", "Error getting documents: ", task.exception)
            }
        }
    }
}
