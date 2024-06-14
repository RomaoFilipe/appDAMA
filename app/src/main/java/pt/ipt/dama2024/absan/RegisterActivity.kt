package pt.ipt.dama2024.absan

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        dbHelper = DBHelper(this)
        auth = FirebaseAuth.getInstance()

        val etFullName = findViewById<EditText>(R.id.etFullName)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPhone = findViewById<EditText>(R.id.etPhone)
        val etUsernameRegister = findViewById<EditText>(R.id.etUsernameRegister)
        val etPasswordRegister = findViewById<EditText>(R.id.etPasswordRegister)
        val etConfirmPasswordRegister = findViewById<EditText>(R.id.etConfirmPasswordRegister)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        btnRegister.setOnClickListener {
            val fullName = etFullName.text.toString()
            val email = etEmail.text.toString()
            val phone = etPhone.text.toString()
            val username = etUsernameRegister.text.toString()
            val password = etPasswordRegister.text.toString()
            val confirmPassword = etConfirmPasswordRegister.text.toString()

            Log.d("RegisterActivity", "Full Name: $fullName")
            Log.d("RegisterActivity", "Email: $email")
            Log.d("RegisterActivity", "Phone: $phone")
            Log.d("RegisterActivity", "Username: $username")
            Log.d("RegisterActivity", "Password: $password")
            Log.d("RegisterActivity", "Confirm Password: $confirmPassword")

            if (fullName.isEmpty() || email.isEmpty() || phone.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, getString(R.string.empty_fields), Toast.LENGTH_SHORT).show()
                Log.d("RegisterActivity", "Empty fields detected")
            } else if (password != confirmPassword) {
                Toast.makeText(this, getString(R.string.passwords_do_not_match), Toast.LENGTH_SHORT).show()
                Log.d("RegisterActivity", "Passwords do not match")
            } else if (dbHelper.checkUserExists(username)) {
                Toast.makeText(this, getString(R.string.user_already_exists), Toast.LENGTH_SHORT).show()
                Log.d("RegisterActivity", "User already exists: $username")
            } else {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
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
                            Toast.makeText(this, getString(R.string.firebase_registration_failed), Toast.LENGTH_SHORT).show()
                            Log.d("RegisterActivity", "Firebase registration failed: ${task.exception?.message}")
                        }
                    }
            }
        }
    }
}
