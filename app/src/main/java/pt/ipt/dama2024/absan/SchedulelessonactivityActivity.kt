package pt.ipt.dama2024.absan

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class ScheduleLessonActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var selectedTrainer: Trainer
    private lateinit var editTextDate: EditText
    private lateinit var editTextTime: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedulelessonactivity)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Verificar se o usuário está autenticado
        val currentUser = auth.currentUser
        if (currentUser == null) {
            // Redirecionar para a tela de login se o usuário não estiver autenticado
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        val recyclerViewTrainers = findViewById<RecyclerView>(R.id.recyclerViewTrainers)
        editTextDate = findViewById(R.id.editTextDate)
        editTextTime = findViewById(R.id.editTextTime)
        val buttonSchedule = findViewById<Button>(R.id.buttonSchedule)
        val buttonBackToHome = findViewById<ImageButton>(R.id.buttonBackToHome)

        // Configurar o RecyclerView
        val trainers = listOf(
            Trainer("Jack Lisowski", R.drawable.trainer1),
            Trainer("Ronnie O'Sullivan", R.drawable.trainer2)
        )
        val adapter = TrainerAdapter(trainers) { trainer ->
            selectedTrainer = trainer
        }
        recyclerViewTrainers.layoutManager = LinearLayoutManager(this)
        recyclerViewTrainers.adapter = adapter

        editTextDate.setOnClickListener { showDatePickerDialog() }
        editTextTime.setOnClickListener { showTimePickerDialog() }

        buttonSchedule.setOnClickListener {
            val date = editTextDate.text.toString()
            val time = editTextTime.text.toString()
            val user = auth.currentUser?.email ?: "unknown"

            if (::selectedTrainer.isInitialized && date.isNotEmpty() && time.isNotEmpty()) {
                val lesson = hashMapOf(
                    "trainerName" to selectedTrainer.name,
                    "date" to date,
                    "time" to time,
                    "user" to user
                )

                // Adicionar log para verificar os dados
                Log.d("ScheduleLesson", "Agendamento: $lesson")

                firestore.collection("lessons")
                    .add(lesson)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Aula agendada com sucesso", Toast.LENGTH_SHORT).show()
                        Log.d("ScheduleLesson", "Agendamento salvo com sucesso")
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Erro ao agendar aula: ${e.message}", Toast.LENGTH_SHORT).show()
                        Log.e("ScheduleLesson", "Erro ao salvar agendamento", e)
                    }
            } else {
                Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
        }

        buttonBackToHome.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                editTextDate.setText("$selectedDay/${selectedMonth + 1}/$selectedYear")
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            { _, selectedHour, selectedMinute ->
                editTextTime.setText(String.format("%02d:%02d", selectedHour, selectedMinute))
            },
            hour, minute, true
        )
        timePickerDialog.show()
    }
}