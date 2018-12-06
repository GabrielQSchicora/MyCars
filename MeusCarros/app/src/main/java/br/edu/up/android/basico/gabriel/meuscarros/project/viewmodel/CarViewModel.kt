package br.edu.up.android.basico.gabriel.meuscarros.project.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import br.edu.up.android.basico.gabriel.meuscarros.project.db.Car
import br.edu.up.android.basico.gabriel.meuscarros.project.db.CarDatabase
import br.edu.up.android.basico.gabriel.meuscarros.project.repository.CarRepository
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.Main
import kotlin.coroutines.experimental.CoroutineContext
import kotlin.coroutines.experimental.coroutineContext

class CarViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CarRepository
    val allCars: LiveData<List<Car>>
    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
    get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    init{
        //Pega a instancia do banco de dados
        val carDao = CarDatabase.getDatabase(application, scope).carDAO()
        //instancia o repositório
        repository = CarRepository(carDao)
        //Coloca todos os carros numa lista
        allCars = repository.allCars
    }

    //Função que chama o inserir do repositório
    fun insert(car: Car) = scope.launch(Dispatchers.IO){
        repository.insert(car)
    }

    //Função que chama o delete do repositório
    fun delete(car: Car) = scope.launch(Dispatchers.IO){
        repository.delete(car)
    }

    //Função que chama o update do repositório
    fun update(car: Car) = scope.launch(Dispatchers.IO){
        repository.update(car)
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

}