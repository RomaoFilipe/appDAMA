package pt.ipt.dama2024.absan

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            // Depois de 3 segundos, redirecionar para a tela de login
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, 3000) // 3000 milissegundos = 3 segundos
    }
}