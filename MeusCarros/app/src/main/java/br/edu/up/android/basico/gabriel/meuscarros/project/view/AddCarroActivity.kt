package br.edu.up.android.basico.gabriel.meuscarros.project.view

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import br.edu.up.android.basico.gabriel.meuscarros.R
import br.edu.up.android.basico.gabriel.meuscarros.project.db.Car
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_add_carro.*

class AddCarroActivity : AppCompatActivity() {

    //Caminho da imagem
    private var imageUri : Uri? = null
    private var mCurrentPhotoPath: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_carro)

        try {
            //Carro recebido caso esteja editando
            val car = intent.getSerializableExtra("selectedCar") as Car

            //Verifica se esta editando e muda a titulo da intent, caso esteja editando atribui
            //os valores aos campos
            supportActionBar?.title = "Novo Carro"
            car.let {
                supportActionBar?.title = "Editar Carro"
                carNome.setText(car.nome)
                carPlaca.setText(car.placa)
                carCor.setText(car.cor)
                if(!car.foto.isEmpty()){
                    Picasso.get().load(Uri.parse(car.foto)).fit().into(imgNewCar)
                }
            }
        }catch(e: Exception){}

        //Botão de voltar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Click do botão de adcionar foto ao carro
        fbAddCarPhoto.setOnClickListener {
            Toast.makeText(this,
                    "Mantenha pressionado para escolher a foto.",
                    Toast.LENGTH_LONG).show()
        }

        //Apertar e segurar o botão para adcionar foto ao carro
        fbAddCarPhoto.setOnCreateContextMenuListener { menu, v, menuInfo ->
            menu.add(Menu.NONE, 1, 1, "Escolher foto")
            menu.add(Menu.NONE, 2, 2, "Tirar foto")
        }

        //
    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            1 -> getPermissionImageFromGallery()
            2 -> getPermissionTakePicture()
        }
        return super.onContextItemSelected(item)
    }

    private fun getPermissionImageFromGallery(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED) {
                // permission denied
                val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(permission, REQUEST_IMAGE_GARELLY)
            } else {
                // permission granted
                pickImageFromGallery()
            }
        }
        else{
            // system < M
            pickImageFromGallery()
        }
    }

    private fun getPermissionTakePicture(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED) {
                // permission denied
                val permission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                requestPermissions(permission, REQUEST_IMAGE_CAPTURE)
            } else {
                // permission granted
                takePicture()
            }
        }
        else{
            // system < M
            takePicture()
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_IMAGE_GARELLY)
    }

    private fun takePicture() {
        //Organiza o conjunto de informações para ser usado posteriormente
        val values = ContentValues()
        //Adciona o titulo na imagem
        values.put(MediaStore.Images.Media.TITLE, "nova imagem")
        //Adciona a descrição na imagem
        values.put(MediaStore.Images.Media.DESCRIPTION, "imagem da camera")
        //Adciona o tipo da imagem
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")

        //Define a URI para setar os dados da imagem
        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values)
        //Define a Intent para tirar a foto
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if(intent.resolveActivity(packageManager) != null) {
            //Adciona no caminho da imagem o caminho uri da imagem
            mCurrentPhotoPath = imageUri.toString()
            //Envia para a intent do tipo extra output s imageUri
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            //Adciona as flags de verificação de permissão
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                    or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        }

        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }

    companion object {
        // image pick code
        private const val REQUEST_IMAGE_GARELLY = 1000
        private const val REQUEST_IMAGE_CAPTURE = 2000
        const val EXTRA_REPLY = "view.REPLY"

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            REQUEST_IMAGE_GARELLY -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) pickImageFromGallery()
                else Toast.makeText(this, "Permissão negada", Toast.LENGTH_SHORT).show()
            }
            REQUEST_IMAGE_CAPTURE ->{
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) takePicture()
                else Toast.makeText(this, "Permissão negada", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //Verifica se o retorno esta OK e é o retorno de pegar imagem ca galeria
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_GARELLY){
            imageUri = data?.data
            imgNewCar.setImageURI(data?.data)
        }

        //Verifica se o retorno esta OK e é o retorno de tirar foto
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE){
            imgNewCar.setImageURI(imageUri)
        }

        imgNewCar.visibility = View.VISIBLE

    }

    //Coloca o menu na tela
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_car, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when {
            //Botão de voltar para a home
            item?.itemId == android.R.id.home -> {
                //Intent para inserir os dados de resposta
                val replyIntent = Intent()
                //Enviando os dados
                setResult(Activity.RESULT_CANCELED, replyIntent)
                finish()
                true
            }
            //Botão de salvar carro
            item?.itemId == R.id.menu_car_save -> {
                when {
                    //Verifica se foi preenchido o nome
                    carNome.text.isNullOrEmpty() -> Toast.makeText(this,
                            "Insira o nome do carro",
                            Toast.LENGTH_LONG).show()
                    //Verifica se foi preenchido a placa
                    carPlaca.text.isNullOrEmpty() -> Toast.makeText(this,
                            "Insira a placa do carro",
                            Toast.LENGTH_LONG).show()
                    //Se todos os campos obrigatórios forma preenchidos
                    else -> {
                        //Instancia um novo carro para retornar e atribui a ele nome e placa
                        val car = Car(nome = carNome.text.toString(), placa = carPlaca.text.toString())
                        try {
                            //Pega o carro passado caso esteja editando
                            val carRef = intent.getSerializableExtra("selectedCar") as Car
                            //Verifica se esta em editando e se tiver atribui o ID
                            carRef.let{
                                car.cId = carRef.cId
                            }
                        }catch(e: Exception){}
                        //Verifica se foi informado cor e atribui ao objeto
                        if(!carCor.text.isNullOrEmpty()){
                            car.cor = carCor.text.toString()
                        }
                        //Verifica se foi informado foto e atribui ao objeto
                        if(imageUri != null){
                            car.foto = imageUri.toString()
                        }
                        //Intent para inserir os dados de resposta
                        val replyIntent = Intent()
                        //Inserindo na intent a chave (EXTRA_REPLY) e os dados (car)
                        replyIntent.putExtra(EXTRA_REPLY, car)
                        //Enviando os dados
                        setResult(Activity.RESULT_OK, replyIntent)
                        //Finaliza intent
                        finish()
                    }
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
