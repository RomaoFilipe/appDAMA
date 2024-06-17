package pt.ipt.dama2024.absan

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import pt.ipt.dama2024.absan.databinding.ActivityProfileBinding
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

class ProfileActivity : AppCompatActivity() {

    /* Declaração das variáveis */
    private lateinit var binding: ActivityProfileBinding
    private lateinit var dbHelper: DBHelper
    private var selectedImageUri: Uri? = null

    /* Método chamado quando a atividade é criada */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)

        dbHelper = DBHelper(this)

        /* Carregar dados do utilizador autenticado do Firebase */
        loadUserData()

        /* Define a cor de fundo da ActionBar */
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.BLUE))

        /* Configura o comportamento do botão flutuante para selecionar uma imagem */
        binding.floatingActionButton.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start()
        }


        /* Carregar a imagem do perfil, se houver */
        loadImageFromInternalStorage()?.let {
            Log.d("ProfileActivity", "Image loaded from internal storage: $it")
            binding.imageView.setImageURI(it)
        }

        /* Configura o botão "Back to Main Page" */
        binding.buttonBackToMainPage.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        /* Configura o botão "Logout" */
        binding.buttonLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == ImagePicker.REQUEST_CODE) {
            val uri = data?.data
            binding.imageView.setImageURI(uri)
            uri?.let {
                selectedImageUri = it
                /* Salvar a imagem no armazenamento específico do aplicativo */
                saveImageToInternalStorage(it)
            }
        }
    }

    /* Método para salvar a imagem no armazenamento interno */
    private fun saveImageToInternalStorage(uri: Uri) {
        try {
            val inputStream = contentResolver.openInputStream(uri)
            val file = File(filesDir, "profile_image.jpg")
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            Log.d("ProfileActivity", "Image saved to internal storage: ${file.absolutePath}")
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("ProfileActivity", "Error saving image to internal storage: ${e.message}")
        }
    }

    /* Método para carregar a imagem do armazenamento interno */
    private fun loadImageFromInternalStorage(): Uri? {
        val file = File(filesDir, "profile_image.jpg")
        return if (file.exists()) {
            Uri.fromFile(file)
        } else {
            Log.d("ProfileActivity", "No image found in internal storage")
            null
        }
    }

    /* Método para carregar os dados do utilizador autenticado */
    private fun loadUserData() {
        val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        user?.let {
            val name = it.displayName ?: "Nome do Usuário"
            val email = it.email ?: "Email do Usuário"
            binding.textView.text = name
            binding.textView2.text = email
        }
    }
}