package com.example.clase05

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface VacantesDao {
    @GET("/vacantes")
    fun getVacantes(): Call<List<Vacante>>

    @GET("/vacantes/{id}")
    fun getVacante(@Path("id") id: String): Call<Vacante>

    @POST("/vacantes")
    fun addVacante(@Body vacante: Vacante): Call<Vacante>

    @PUT("/vacantes/{id}")
    fun updateVacante(@Path("id") id: String, @Body vacante: Vacante): Call<ResponseBody>

    @DELETE("/vacantes/{id}")
    fun deleteVacante(@Path("id") id: String): Call<ResponseBody>
}