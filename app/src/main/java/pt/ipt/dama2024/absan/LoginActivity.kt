package pt.ipt.dama2024.absan

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import pt.ipt.dama2024.absan.R


class LoginActivity : BaseActivity() {

    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        dbHelper = DBHelper(this)

        val etUsernameLogin = findViewById<EditText>(R.id.etUsernameLogin)
        val etPasswordLogin = findViewById<EditText>(R.id.etPasswordLogin)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvRegister = findViewById<Button>(R.id.tvRegister)
        val languageSpinner = findViewById<Spinner>(R.id.spinnerLanguage)

        // Initialize Spinner with current language
        val currentLanguage = LocaleHelper.getLanguage(this)
        val languagePosition = when (currentLanguage) {
            "en" -> 0
            "pt" -> 1
            else -> 0
        }
        languageSpinner.setSelection(languagePosition)

        btnLogin.setOnClickListener {
            val username = etUsernameLogin.text.toString()
            val password = etPasswordLogin.text.toString()

            if (dbHelper.isValidLogin(username, password)) {
                Toast.makeText(this, getString(R.string.login_successful), Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, getString(R.string.invalid_credentials), Toast.LENGTH_SHORT).show()
            }
        }

        tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedLanguage = when (position) {
                    0 -> "en" // English
                    1 -> "pt" // Portuguese
                    else -> "en"
                }

                // Only change the language if it is different from the current language
                if (selectedLanguage != LocaleHelper.getLanguage(this@LoginActivity)) {
                    LocaleHelper.setLocale(this@LoginActivity, selectedLanguage)
                    recreate() // Recria a Activity para aplicar a mudança de idioma
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }
}
