package br.edu.up.android.basico.gabriel.meuscarros.project.db

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.IO
import kotlinx.coroutines.experimental.launch

@Database(entities = [Car::class], version = 3)
abstract class CarDatabase: RoomDatabase(){

    abstract fun carDAO():CarDAO

    //Objeto que pode ser acessado por outra classe sem instância
    companion object {

        //Recebe a database
        @Volatile
        private var INSTANCE: CarDatabase? = null

        //Cria ou gera a database
        fun getDatabase(context: Context, scope: CoroutineScope):CarDatabase{
            return INSTANCE ?: synchronized(this){
                //Cria banco de dados
                val intance = Room.databaseBuilder(
                        context.applicationContext,
                        CarDatabase::class.java,
                        "cars-database"
                )
                        .fallbackToDestructiveMigration()
                        .addCallback(carDatabaseCalback(scope))
                        .build()
                        INSTANCE = intance
                        intance
            }
        }

        //Calback do database
        private class carDatabaseCalback(
                private val scope: CoroutineScope
        ): RoomDatabase.Callback(){
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCE?.let {
                    database ->
                    scope.launch(Dispatchers.IO) {
                        populaTabela(database.carDAO())
                    }
                }
            }
        }

        //Função para popular tabela automaticamente (Testes)
        fun populaTabela(carDAO: CarDAO){
            /*carDAO.deleteAll()
            carDAO.insert(Car(id=1, nome="Astra", placa="lpk-9987"))
            carDAO.insert(Car(id=2, nome="Opala", placa="avc-4198"))
            carDAO.insert(Car(id=3, nome="Corola", placa="bjv-2075"))*/
        }

    }

}