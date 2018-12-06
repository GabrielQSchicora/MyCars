package br.edu.up.android.basico.gabriel.meuscarros.project.repository

import android.arch.lifecycle.LiveData
import br.edu.up.android.basico.gabriel.meuscarros.project.db.Car
import br.edu.up.android.basico.gabriel.meuscarros.project.db.CarDAO

class CarRepository(private val carDAO: CarDAO) {

    //Variavel que reccebe todos os carros
    val allCars: LiveData<List<Car>> = carDAO.getAll()

    //Função que chama o inserir da classe DAO
    fun insert(car: Car){
        carDAO.insert(car)
    }

    //Função que chama o deletar da classe DAO
    fun delete(car: Car){
        carDAO.delete(car)
    }

    //Função que chama o update da classe DAO
    fun update(car: Car){
        carDAO.update(car)
    }

}