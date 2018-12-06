package br.edu.up.android.basico.gabriel.meuscarros.project.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import br.edu.up.android.basico.gabriel.meuscarros.R
import br.edu.up.android.basico.gabriel.meuscarros.project.db.Car
import br.edu.up.android.basico.gabriel.meuscarros.project.createNotify
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail_carro.*

class DetailCarroActivity : AppCompatActivity() {

    private val requestCodeEditCar = 2

    companion object {
        const val EXTRA_UPDATE = "view.UPDATE"
        const val EXTRA_DELETE = "view.DELETE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_carro)

        //Botão de voltar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Pega carro passado pela intent anterior
        val car = intent.getSerializableExtra("selectedCar") as Car

        car.let {
            populaTela(car)
        }
    }

    //Função que popula a tela
    fun populaTela(car: Car){
        //Titulo da página
        supportActionBar?.title = car.nome
        //Seta os valores em seus campos
        carNome.text = car.nome
        carPlaca.text = car.placa
        carCor.text = car.cor
        if(!car.foto.isEmpty()){
            Picasso.get().load(Uri.parse(car.foto)).fit().into(imgCar)
        }
    }

    //Coloca o menu na tela
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_view_car, menu)
        return true
    }

    //Pega a seleção do item do menu
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        //Pega o carro passado pela intent anterior
        val car = intent.getSerializableExtra("selectedCar") as Car

        //Botão de voltar
        return when {
            item?.itemId == android.R.id.home -> {
                finish()
                true
            }
            //Botão para deletar
            item?.itemId == R.id.menuItemDeleteCar -> {
                val replyIntent = Intent()
                replyIntent.putExtra(EXTRA_DELETE, car)
                setResult(Activity.RESULT_OK, replyIntent)
                //Finaliza intent
                finish()
                true
            }
            //Botão para editar
            item?.itemId == R.id.menuItemEditCar -> {
                //Cria a intent para a tela de adcionar/editar
                val intent = Intent(this@DetailCarroActivity, AddCarroActivity::class.java)
                //Passa o carro para editar para a próxima intent
                intent.putExtra("selectedCar", car)
                //Inicia a activity esperando um resultado
                startActivityForResult(intent, requestCodeEditCar)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    //Quando a activity retornar para essa tela com um resultado
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when {
            //Verifica se retornou com o mesmo código e sem erros
            requestCode == requestCodeEditCar && resultCode == Activity.RESULT_OK -> {
                //Adciona o script para inserir o objeto caso a variável data nçao esteja nula
                //Pega o carro retornado
                val car: Car = data?.getSerializableExtra(AddCarroActivity.EXTRA_REPLY) as Car
                data.let {
                    populaTela(car)
                    intent.putExtra("selectedCar", car)
                }
                val replyIntent = Intent()
                replyIntent.putExtra(EXTRA_UPDATE, car)
                setResult(Activity.RESULT_OK, replyIntent)

                //Cria notificação
                createNotify(applicationContext, intent, "Carro modificado",
                        "O carro "+car.nome+" foi modificado!", R.drawable.ic_edit_yelow_24dp)
            }
            //Verifica se retornou com o mesmo código mas foi cancelado
            requestCode == requestCodeEditCar && resultCode == Activity.RESULT_CANCELED -> {

            }
            //Outras opções
            else -> //Mostra um Toas com avisando sobre o erro para editar
                Toast.makeText(applicationContext,
                        "Problema ao editar, tente novamente mais tarde",
                        Toast.LENGTH_LONG).show()
        }
    }
}
