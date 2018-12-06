package br.edu.up.android.basico.gabriel.meuscarros.project.adapter

import android.content.Context
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import br.edu.up.android.basico.gabriel.meuscarros.R
import br.edu.up.android.basico.gabriel.meuscarros.project.db.Car
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_lista_carro.view.*

class CarroListAdapter internal constructor(context: Context)
    : RecyclerView.Adapter<CarroListAdapter.ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var carros = emptyList<Car>() //cachear os elementos
    var onItemClick: ((Car) -> Unit)? = null

    //Infla o layout do item da lista para cada componente da lista
    override fun onCreateViewHolder(holder: ViewGroup, p1: Int): ViewHolder {
        val view = inflater.inflate(R.layout.item_lista_carro, holder, false)
        return ViewHolder(view)
    }

    //Retorna o tamanho da lista
    override fun getItemCount() = carros.size

    //Colocando os itens da lista nos itens da view da lista
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //Item atual
        val current = carros[position]
        //Coloca o nome do carro no cardView
        holder.carName.text = current.nome
        if(!current.foto.isEmpty()){
            holder.carPhoto.setImageURI(Uri.parse(current.foto))

            Picasso.get().load(Uri.parse(current.foto)).fit().into(holder.carPhoto)
        }
    }

    //Classe para mapear os componentes do item da lista
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        //Instancia de elementos da tela
        val carPhoto = itemView.imgCarListPhoto!!
        val carName: TextView = itemView.txtCarListName

        init {
            //Seta o click do cardView
            itemView.setOnClickListener{
                onItemClick?.invoke(carros[adapterPosition])
            }
        }
    }

    //Coloca a lista de carros na tela
    fun setCarList(carList: List<Car>){
        this.carros = carList
        notifyDataSetChanged()
    }

}