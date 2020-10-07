package com.example.clase05

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView


import java.util.ArrayList

class VacanteRecyclerAdapter(private var context: Context) : RecyclerView.Adapter<VacanteRecyclerAdapter.VacanteViewHolder>() {

    private var vacantes: List<Vacante> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacanteViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        return VacanteViewHolder(view)
    }

    override fun onBindViewHolder(holder: VacanteViewHolder, position: Int) {
        val vacantes = vacantes[position]
        holder.carrera.text = vacantes.carrera
        holder.cupo.text = "Cupos: " + vacantes.cupo
        holder.requisitos.text = "Requisitos: "+vacantes.requisitos

        holder.itemView.setOnClickListener {
            val i = Intent(context, AddOrEditActivity::class.java)
            i.putExtra("Mode", "E")
            i.putExtra("Id", vacantes._id)
            Log.d("id",vacantes._id)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(i)
        }
    }

    fun setVacanteList(vacantes: List<Vacante>){
        this.vacantes = vacantes;
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return vacantes.size
    }

    inner class VacanteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var carrera: TextView = view.findViewById(R.id.tvCarrera) as TextView
        var cupo: TextView = view.findViewById(R.id.tvCupo) as TextView
        var requisitos: TextView = view.findViewById(R.id.tvRequisitos) as TextView
        var list_item: LinearLayout = view.findViewById(R.id.list_item) as LinearLayout
    }

}