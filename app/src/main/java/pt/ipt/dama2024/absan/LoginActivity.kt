package pt.ipt.dama2024.absan

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class LoginActivity : AppCompatActivity() {
    private lateinit var dbHelper: DBHelper
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        dbHelper = DBHelper(this)
        auth = FirebaseAuth.getInstance()

        val etUsernameLogin = findViewById<EditText>(R.id.etUsernameLogin)
        val etPasswordLogin = findViewById<EditText>(R.id.etPasswordLogin)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvRegister = findViewById<Button>(R.id.tvRegister)
        val languageSpinner = findViewById<Spinner>(R.id.spinnerLanguage)
        val btnInfo = findViewById<ImageButton>(R.id.btnInfo)
        val btnDemoLogin = findViewById<Button>(R.id.btnDemoLogin)

        // Create demo user if it doesn't exist
        createDemoUser()

        btnLogin.setOnClickListener {
            val username = etUsernameLogin.text.toString()
            val password = etPasswordLogin.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                if (username == "demo" && password == "demo") {
                    loginDemoUser()
                } else {
                    val email = dbHelper.getUserEmailByUsername(username)
                    if (email != null) {
                        auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this) { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(this, getString(R.string.login_successful), Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this, HomeActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    Toast.makeText(this, getString(R.string.invalid_credentials), Toast.LENGTH_SHORT).show()
                                }
                            }
                    } else {
                        Toast.makeText(this, getString(R.string.invalid_credentials), Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, getString(R.string.empty_fields), Toast.LENGTH_SHORT).show()
            }
        }

        tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedLanguage = when (position) {
                    0 -> "en"
                    1 -> "pt"
                    else -> "en"
                }

                if (selectedLanguage != LocaleHelper.getLanguage(this@LoginActivity)) {
                    LocaleHelper.setLocale(this@LoginActivity, selectedLanguage)
                    recreate()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        btnInfo.setOnClickListener {
            val intent = Intent(this, InfoActivity::class.java)
            startActivity(intent)
        }

        btnDemoLogin.setOnClickListener {
            loginDemoUser()
        }
    }

    private fun createDemoUser() {
        val demoUsername = "demo"
        val demoPassword = "demo"
        val demoEmail = "demo@example.com"
        val demoFullName = "Demo User"
        val demoPhone = "123456789"

        if (!dbHelper.checkUserExists(demoUsername)) {
            auth.createUserWithEmailAndPassword(demoEmail, demoPassword)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setDisplayName(demoFullName)
                            .build()

                        user?.updateProfile(profileUpdates)
                            ?.addOnCompleteListener { profileUpdateTask ->
                                if (profileUpdateTask.isSuccessful) {
                                    dbHelper.addUser(demoFullName, demoEmail, demoPhone, demoUsername, demoPassword)
                                }
                            }
                    }
                }
        }
    }

    private fun loginDemoUser() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}
