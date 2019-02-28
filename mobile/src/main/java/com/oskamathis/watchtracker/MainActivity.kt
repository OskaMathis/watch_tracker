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

        val taskNames = getTaskNames()

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = RecyclerAdapter(this, taskNames)
    }

    private fun getTaskNames() : List<String>{
        val taskNames = mutableListOf<String>()
        val docRef = db.collection("Tasks")
//        val docRef = db.collection("Tasks").document("PM業務説明")

        docRef.get().addOnCompleteListener { task->
//            if (task.isSuccessful) {
//                val document = task.result ?: return@addOnCompleteListener
//                if (document.exists()) {
//                    Log.d("MainActivity", "DocumentSnapshot data: " + document.data)
//                } else {
//                    Log.d("MainActivity", "No such document");
//                }
//            } else {
////                Log.d("MainActivity", "get failed with ", task.getException());
//            }
            if (task.isSuccessful) {
                val result = task.result ?: return@addOnCompleteListener
                for (document in result) {
                    val taskName = document.data["name"]
                    Log.d("MainActivity", document.id + " => " + document.data["name"])
                    taskNames.add(taskName.toString())
                }
            } else {
                Log.d("MainActivity", "Error getting documents: ", task.exception)
            }
        }
        return taskNames
    }




}

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val textView: TextView = itemView.findViewById(R.id.textView)
}

class RecyclerAdapter(context: Context, val data: List<String>) : RecyclerView.Adapter<ViewHolder>() {
    val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // ここでViewHolderを作る
        return ViewHolder(inflater.inflate(R.layout.list_item, parent, false))
    }

    override fun getItemCount(): Int {
        // データの要素数を返す
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // ViewHolderを通してデータをViewに設定する
        holder.textView.text = data[position]
    }
}

data class Tasks(val tasks:List<Task>)
data class Task(val name:String, val times:List<Time>)
data class Time(val started_at:Date, val stopped_at:Date)
