package br.edu.up.android.basico.gabriel.meuscarros.project.db

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

//Table name
@Entity(tableName = "cars_table")
data class Car (
    //Atributes and table coluns
    @ColumnInfo(name = "nome")
    var nome: String,
    @ColumnInfo(name = "placa")
    var placa: String,
    @ColumnInfo(name = "cor")
    var cor: String = "",
    @ColumnInfo(name = "foto")
    var foto: String = ""
): Serializable{
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var cId: Int = 0
}