package br.edu.up.android.basico.gabriel.meuscarros.project.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

@Dao
interface CarDAO {

    //Função de inserir no banco de dados
    @Insert
    fun insert(car: Car)

    //Função de deletar tudo do banco de dados
    @Query("DELETE FROM cars_table")
    fun deleteAll()

    //Função para deletar um único carro do banco de dados
    @Delete
    fun delete(car: Car)

    //Função para atualizar um único carro do banco de dados
    @Update
    fun update(car: Car)

    //Função para receber todos os carros do banco de dados
    @Query("SELECT * FROM cars_table")
    fun getAll():LiveData<List<Car>>

}