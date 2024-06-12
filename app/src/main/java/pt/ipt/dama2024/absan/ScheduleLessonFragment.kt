package pt.ipt.dama2024.absan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore

class ScheduleLessonFragment : Fragment() {
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_schedule_lesson, container, false)

        val editTextTrainerName = view.findViewById<EditText>(R.id.editTextTrainerName)
        val editTextDate = view.findViewById<EditText>(R.id.editTextDate)
        val editTextTime = view.findViewById<EditText>(R.id.editTextTime)
        val buttonSchedule = view.findViewById<Button>(R.id.buttonSchedule)

        firestore = FirebaseFirestore.getInstance()

        buttonSchedule.setOnClickListener {
            val trainerName = editTextTrainerName.text.toString()
            val date = editTextDate.text.toString()
            val time = editTextTime.text.toString()

            if (trainerName.isNotEmpty() && date.isNotEmpty() && time.isNotEmpty()) {
                val lesson = hashMapOf(
                    "trainerName" to trainerName,
                    "date" to date,
                    "time" to time
                )

                firestore.collection("lessons")
                    .add(lesson)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Aula agendada com sucesso", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(context, "Erro ao agendar aula: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(context, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}
