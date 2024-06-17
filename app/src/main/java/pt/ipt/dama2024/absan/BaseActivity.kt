package pt.ipt.dama2024.absan

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import pt.ipt.dama2024.absan.LocaleHelper

// BaseActivity é uma classe aberta que herda de AppCompatActivity
open class BaseActivity : AppCompatActivity() {

    // Método chamado quando a atividade é criada
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Anexa a configuração de localidade (idioma) à atividade
        LocaleHelper.onAttach(this)
    }
}