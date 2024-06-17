package pt.ipt.dama2024.absan

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    /* Declaração das variáveis dbHelper e auth */
    private lateinit var dbHelper: DBHelper
    private lateinit var auth: FirebaseAuth

    /* Método chamado quando a atividade é criada */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        /* Inicializa dbHelper e auth */
        dbHelper = DBHelper(this)
        auth = FirebaseAuth.getInstance()

        /* Referências para os elementos da interface do utilizador */
        val etUsernameLogin = findViewById<EditText>(R.id.etUsernameLogin)
        val etPasswordLogin = findViewById<EditText>(R.id.etPasswordLogin)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvRegister = findViewById<Button>(R.id.tvRegister)
        val languageSpinner = findViewById<Spinner>(R.id.spinnerLanguage)

        /* Define o comportamento do botão de login */
        btnLogin.setOnClickListener {
            val username = etUsernameLogin.text.toString()
            val password = etPasswordLogin.text.toString()

            /* Verifica se os campos de nome de utilizador e senha não estão vazios */
            if (username.isNotEmpty() && password.isNotEmpty()) {
                val email = dbHelper.getUserEmailByUsername(username)
                if (email != null) {
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, getString(R.string.login_successful), Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, HomeActivity::class.java) // <-- Redireciona para HomeActivity
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this, getString(R.string.invalid_credentials), Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(this, getString(R.string.invalid_credentials), Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, getString(R.string.empty_fields), Toast.LENGTH_SHORT).show()
            }
        }

        /* Define o comportamento do botão de registo */
        tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        /* Define o comportamento do spinner de seleção de idioma */
        languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedLanguage = when (position) {
                    0 -> "en" // English
                    1 -> "pt" // Portuguese
                    else -> "en"
                }

                /* Altera o idioma apenas se for diferente do idioma atual */
                if (selectedLanguage != LocaleHelper.getLanguage(this@LoginActivity)) {
                    LocaleHelper.setLocale(this@LoginActivity, selectedLanguage)
                    recreate() // Recria a Activity para aplicar a mudança de idioma
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }
}