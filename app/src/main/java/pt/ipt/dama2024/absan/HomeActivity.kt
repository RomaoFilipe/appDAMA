package pt.ipt.dama2024.absan

import android.content.Intent
import android.os.Bundle
import android.view.View // Importe a classe View
import androidx.appcompat.app.AppCompatActivity
import pt.ipt.dama2024.absan.R

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Inicialize seus elementos de interface do usuário aqui

    }

    fun openProfile(view: View) {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }
}