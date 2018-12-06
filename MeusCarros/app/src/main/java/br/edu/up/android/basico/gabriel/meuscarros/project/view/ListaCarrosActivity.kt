package br.edu.up.android.basico.gabriel.meuscarros.project.view

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import br.edu.up.android.basico.gabriel.meuscarros.R
import br.edu.up.android.basico.gabriel.meuscarros.project.adapter.CarroListAdapter
import br.edu.up.android.basico.gabriel.meuscarros.project.createNotify
import br.edu.up.android.basico.gabriel.meuscarros.project.db.Car
import br.edu.up.android.basico.gabriel.meuscarros.project.viewmodel.CarViewModel
import kotlinx.android.synthetic.main.activity_lista_carros.*

class ListaCarrosActivity : AppCompatActivity() {

    private lateinit var carViewModel: CarViewModel
    private val requestCodeCar = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_carros)

        //Recebe a recyclerView do XML
        val recyclerView = rvListaCarros
        //Recebe o adapter para o recyclerView
        val adapter = CarroListAdapter(this)
        //Insere os dados no recyclerView
        recyclerView.adapter = adapter
        //Define o layout do recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter.onItemClick = {
            //Cria intent para a activity de detalhes
            val intent = Intent(this, DetailCarroActivity::class.java)
            //Adciona item para ir para a próxima activity
            intent.putExtra("selectedCar", it)

            //Abre uma nova activity e espera um resultado que será validado com
            //uma chave (requestCodeCar)
            startActivityForResult(intent, requestCodeCar)
        }

        //Cria um viel model
        carViewModel = ViewModelProviders.of(this).get(CarViewModel::class.java)

        //Coloca os carros vindo do view model na tela
        carViewModel.allCars.observe(this, Observer {
            cars -> cars?.let { adapter.setCarList(it) }
        })

        //Click do botão para adcionar novo carro
        fbAddCar.setOnClickListener{
            //Cria uma intent para a página de adcionar
            val intent = Intent(this@ListaCarrosActivity, AddCarroActivity::class.java)
            //Abre uma nova activity e espera um resultado que será validado com
            //uma chave (requestCodeCar)
            startActivityForResult(intent, requestCodeCar)
        }



    }

    //Pega o resultado do retorno
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //Verifica se o request code está certo e o código de retorno é OK
        if(requestCode == requestCodeCar && resultCode == Activity.RESULT_OK){
            //Executa a ação caso a variavel data não seja nula
            data.let {
                //Inserir
                try{
                    //Pega o carro serializado que foi retornado
                    val car: Car = data?.getSerializableExtra(AddCarroActivity.EXTRA_REPLY) as Car
                    //Chama a função para inserir o carro
                    carViewModel.insert(car)
                    //Cria notificação
                    createNotify(this, intent, "Carro "+car.nome+" foi adcionado",
                            "O carro "+car.nome+" foi adcionado!", R.drawable.ic_new_car_white)
                }catch (e: Exception){
                    //Update
                    try {
                        val car = data?.getSerializableExtra(DetailCarroActivity.EXTRA_UPDATE) as Car
                        carViewModel.update(car)
                    //Delete
                    }catch (e: Exception){
                        val car = data?.getSerializableExtra(DetailCarroActivity.EXTRA_DELETE) as Car
                        //Chama o metodo para deletar
                        carViewModel.delete(car)
                        //Mostra notificação
                        createNotify(this, intent, "Carro excluído",
                                "O carro "+car.nome+" foi excluído!", R.drawable.ic_close_red_24dp)
                    }
                }
            }

        //Verifica se o request code está certo e o código de retorno é CANCELED
        }else if(requestCode == requestCodeCar && resultCode == Activity.RESULT_CANCELED) {

        //Outras opções
        }else {
            //Mostra um TOAST dizendo que deu problema ao salvar
            Toast.makeText(applicationContext,
                    "Problema ao salvar, tente novamente mais tarde",
                    Toast.LENGTH_LONG).show()
        }
    }

}
