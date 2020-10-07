package com.example.clase05

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_add_or_edit.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddOrEditActivity : AppCompatActivity() {

    private var isEditMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_or_edit)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        btn_delete.visibility = View.INVISIBLE
        if (intent != null && intent.getStringExtra("Mode") == "E") {
            isEditMode = true
            val request = ServiceBuilder.buildService(VacantesDao::class.java)
            val call = request.getVacante(intent.getStringExtra("Id"))
            call.enqueue(object : Callback<Vacante>{
                override fun onResponse(call: Call<Vacante>, response: Response<Vacante>) {
                    if (response.isSuccessful){
                        val vacante: Vacante = response.body()!!
                        input_career.setText(vacante.carrera)
                        input_quota.setText(vacante.cupo)
                        input_requirements.setText(vacante.requisitos)
                        btn_delete.visibility = View.VISIBLE
                    }
                }
                override fun onFailure(call: Call<Vacante>, t: Throwable) {
                    Toast.makeText(this@AddOrEditActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                    Log.d("error",t.message)
                }
            })

        }

        btn_save.setOnClickListener {
            if (!isEditMode) {
                val vacante: Vacante = Vacante(
                    "",
                input_career.text.toString(),
                input_quota.text.toString(),
                input_requirements.text.toString())
                val request = ServiceBuilder.buildService(VacantesDao::class.java)
                val call = request.addVacante(vacante)
                call.enqueue(object : Callback<Vacante>{
                    override fun onResponse(call: Call<Vacante>, response: Response<Vacante>) {
                        if (response.isSuccessful){
                            finish()
                        }
                    }
                    override fun onFailure(call: Call<Vacante>, t: Throwable) {
                        Toast.makeText(this@AddOrEditActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                        Log.d("error",t.message)
                    }
                })
            } else {
                val vacante: Vacante = Vacante(
                "",
                input_career.text.toString(),
                input_quota.text.toString(),
                input_requirements.text.toString())
                val request = ServiceBuilder.buildService(VacantesDao::class.java)
                val call = request.updateVacante(intent.getStringExtra("Id"),vacante)
                call.enqueue(object : Callback<ResponseBody>{
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.isSuccessful){
                            finish()
                        }
                    }
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Toast.makeText(this@AddOrEditActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                        Log.d("error",t.message)
                    }
                })
            }


        }

        btn_delete.setOnClickListener {
            val dialog = AlertDialog.Builder(this).setTitle("Info").setMessage("Click en 'SI' para eliminar la vacante")
                .setPositiveButton("SI") { dialog, _ ->
                    val request = ServiceBuilder.buildService(VacantesDao::class.java)
                    val call = request.deleteVacante(intent.getStringExtra("Id"))

                    call.enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                            if (response.isSuccessful){
                                finish()
                                dialog.dismiss()                            }
                        }
                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            Toast.makeText(this@AddOrEditActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                            Log.d("error",t.message)
                        }
                    })
                }
                .setNegativeButton("NO") { dialog, _ ->
                    dialog.dismiss()
                }
            dialog.show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}