package pt.ipt.dama2024.absan

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import pt.ipt.dama2024.absan.LocaleHelper

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocaleHelper.onAttach(this)
    }
}