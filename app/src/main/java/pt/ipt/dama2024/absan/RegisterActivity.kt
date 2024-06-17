package pt.ipt.dama2024.absan

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class RegisterActivity : AppCompatActivity() {

    /* Declaração das variáveis dbHelper e auth */
    private lateinit var dbHelper: DBHelper
    private lateinit var auth: FirebaseAuth

    /* Método chamado quando a atividade é criada */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        dbHelper = DBHelper(this)
        auth = FirebaseAuth.getInstance()

        /* Referências para os elementos da interface do utilizador */
        val etFullName = findViewById<EditText>(R.id.etFullName)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPhone = findViewById<EditText>(R.id.etPhone)
        val etUsernameRegister = findViewById<EditText>(R.id.etUsernameRegister)
        val etPasswordRegister = findViewById<EditText>(R.id.etPasswordRegister)
        val etConfirmPasswordRegister = findViewById<EditText>(R.id.etConfirmPasswordRegister)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        /* Configura o comportamento do botão de registo */
        btnRegister.setOnClickListener {
            val fullName = etFullName.text.toString()
            val email = etEmail.text.toString()
            val phone = etPhone.text.toString()
            val username = etUsernameRegister.text.toString()
            val password = etPasswordRegister.text.toString()
            val confirmPassword = etConfirmPasswordRegister.text.toString()

            /* Log para depuração */
            Log.d("RegisterActivity", "Full Name: $fullName")
            Log.d("RegisterActivity", "Email: $email")
            Log.d("RegisterActivity", "Phone: $phone")
            Log.d("RegisterActivity", "Username: $username")
            Log.d("RegisterActivity", "Password: $password")
            Log.d("RegisterActivity", "Confirm Password: $confirmPassword")

            /* Verifica se os campos estão vazios */
            if (fullName.isEmpty() || email.isEmpty() || phone.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, getString(R.string.empty_fields), Toast.LENGTH_SHORT).show()
                Log.d("RegisterActivity", "Empty fields detected")
            } else if (password != confirmPassword) {
                /* Verifica se as senhas coincidem */
                Toast.makeText(this, getString(R.string.passwords_do_not_match), Toast.LENGTH_SHORT).show()
                Log.d("RegisterActivity", "Passwords do not match")
            } else if (dbHelper.checkUserExists(username)) {
                /* Verifica se o utilizador já existe */
                Toast.makeText(this, getString(R.string.user_already_exists), Toast.LENGTH_SHORT).show()
                Log.d("RegisterActivity", "User already exists: $username")
            } else {
                /* Cria uma nova conta no Firebase */
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Atualizar o perfil do utilizador com o nome completo
                            val user = auth.currentUser
                            val profileUpdates = UserProfileChangeRequest.Builder()
                                .setDisplayName(fullName)
                                .build()

                            user?.updateProfile(profileUpdates)
                                ?.addOnCompleteListener { profileUpdateTask ->
                                    if (profileUpdateTask.isSuccessful) {
                                        val success = dbHelper.addUser(fullName, email, phone, username, password)
                                        if (success) {
                                            Toast.makeText(this, getString(R.string.registration_successful), Toast.LENGTH_SHORT).show()
                                            Log.d("RegisterActivity", "Registration successful for user: $username")
                                            startActivity(Intent(this, LoginActivity::class.java))
                                            finish()
                                        } else {
                                            Toast.makeText(this, getString(R.string.registration_failed), Toast.LENGTH_SHORT).show()
                                            Log.d("RegisterActivity", "Registration failed for user: $username")
                                        }
                                    } else {
                                        Toast.makeText(this, getString(R.string.profile_update_failed), Toast.LENGTH_SHORT).show()
                                        Log.d("RegisterActivity", "Profile update failed: ${profileUpdateTask.exception?.message}")
                                    }
                                }
                        } else {
                            Toast.makeText(this, getString(R.string.firebase_registration_failed), Toast.LENGTH_SHORT).show()
                            Log.d("RegisterActivity", "Firebase registration failed: ${task.exception?.message}")
                        }
                    }
            }
        }
    }
}