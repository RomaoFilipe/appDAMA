package pt.ipt.dama2024.absan

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.github.dhaval2404.imagepicker.ImagePicker
import pt.ipt.dama2024.absan.R
import pt.ipt.dama2024.absan.databinding.ActivityProfileBinding
import java.io.File
import java.io.FileInputStream
import java.io.IOException

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var dbHelper: DBHelper
    private var selectedImageUri: Uri? = null
    private var imageFilePath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)

        dbHelper = DBHelper(this)

        val username = intent.getStringExtra("username")

        if (username != null) {
            val user = dbHelper.getUserByUsername(username)
            user?.let {
                it.fullName.also { binding.textView.text = it }
                it.email.also { binding.textView2.text = it }
                // Aqui você pode exibir outros detalhes do usuário, se necessário
            }
        } else {
            // Trate o caso em que o nome de usuário é nulo
        }

        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.BLUE))

        binding.floatingActionButton.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start()
        }

        // Carregar a imagem do perfil, se houver
        loadImageFromInternalStorage()?.let {
            binding.imageView.setImageURI(it)
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
                imageFilePath = getRealPathFromURI(uri)
                // Salvar a imagem no armazenamento interno
                imageFilePath?.let { filePath ->
                    saveImageToInternalStorage(filePath)
                }
            }
        }
    }

    private fun getRealPathFromURI(uri: Uri): String? {
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.moveToFirst()
        val columnIndex = cursor?.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
        val filePath = cursor?.getString(columnIndex ?: -1)
        cursor?.close()
        return filePath
    }

    private fun saveImageToInternalStorage(filePath: String) {
        try {
            val inputStream = FileInputStream(File(filePath))
            val outputStream = openFileOutput("profile_image.jpg", Context.MODE_PRIVATE)
            inputStream.copyTo(outputStream)
            inputStream.close()
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun loadImageFromInternalStorage(): Uri? {
        val file = File(filesDir, "profile_image.jpg")
        return if (file.exists()) {
            Uri.fromFile(file)
        } else {
            null
        }
    }
}
