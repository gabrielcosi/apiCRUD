package com.example.clase05

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    lateinit var vacanteRecyclerAdapter: VacanteRecyclerAdapter
    private var recyclerView: RecyclerView? = null
    private lateinit  var linearLayoutManager: LinearLayoutManager
    private var fab: FloatingActionButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        fab = findViewById(R.id.fab)
        recyclerView = findViewById(R.id.recycler_view)
        vacanteRecyclerAdapter = VacanteRecyclerAdapter(this)
        linearLayoutManager = LinearLayoutManager(applicationContext)
        (recyclerView as RecyclerView).layoutManager = linearLayoutManager
        recyclerView!!.adapter = vacanteRecyclerAdapter
        fetchData()
        swipe.setProgressBackgroundColorSchemeColor(Color.CYAN)
        swipe.setColorSchemeColors(Color.WHITE)
        swipe.setOnRefreshListener {
            fetchData()
        }
        fab?.setOnClickListener {
            val i = Intent(applicationContext, AddOrEditActivity::class.java)
            i.putExtra("Mode", "A")
            startActivity(i)
        }
    }

    private fun fetchData() {
        val request = ServiceBuilder.buildService(VacantesDao::class.java)
        val call = request.getVacantes()
        call.enqueue(object : Callback<List<Vacante>>{
            override fun onResponse(call: Call<List<Vacante>>, response: Response<List<Vacante>>) {
                if (response.isSuccessful){
                    recyclerView?.apply {
                        vacanteRecyclerAdapter.setVacanteList(response.body()!!)
                    }
                }
            }
            override fun onFailure(call: Call<List<Vacante>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                Log.d("error",t.message)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_delete) {
            val dialog = AlertDialog.Builder(this).setTitle("Info").setMessage("Click en 'SI' borrara todos los alumnos")
                .setPositiveButton("SI") { dialog, _ ->

                    dialog.dismiss()
                }
                .setNegativeButton("NO") { dialog, _ ->
                    dialog.dismiss()
                }
            dialog.show()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        fetchData()
    }
}