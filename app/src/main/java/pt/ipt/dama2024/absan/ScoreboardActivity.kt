package pt.ipt.dama2024.absan

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class ScoreboardActivity : AppCompatActivity() {

    /* Declaração das variáveis de pontuação e nome das equipas */
    private lateinit var scoreTeam1: TextView
    private lateinit var scoreTeam2: TextView
    private lateinit var team1Name: EditText
    private lateinit var team2Name: EditText

    /* Variáveis para armazenar a pontuação das equipas */
    private var score1 = 0
    private var score2 = 0

    /* Método chamado quando a atividade é criada */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scoreboard)

        /* Inicializa as variáveis com os elementos da interface do utilizador */
        scoreTeam1 = findViewById(R.id.scoreTeam1)
        scoreTeam2 = findViewById(R.id.scoreTeam2)
        team1Name = findViewById(R.id.team1Name)
        team2Name = findViewById(R.id.team2Name)

        val team1Button = findViewById<MaterialButton>(R.id.team1Button)
        val team2Button = findViewById<MaterialButton>(R.id.team2Button)
        val decrementTeam1 = findViewById<MaterialButton>(R.id.decrementTeam1)
        val decrementTeam2 = findViewById<MaterialButton>(R.id.decrementTeam2)
        val mainMenuButton = findViewById<MaterialButton>(R.id.mainMenuButton)
        val microphoneButton = findViewById<MaterialButton>(R.id.microphoneButton)

        /* Configura o botão de incremento da equipa 1 */
        team1Button.setOnClickListener {
            score1++
            updateScores()
        }

        /* Configura o botão de incremento da equipa 2 */
        team2Button.setOnClickListener {
            score2++
            updateScores()
        }

        /* Configura o botão de decremento da equipa 1 */
        decrementTeam1.setOnClickListener {
            if (score1 > 0) {
                score1--
                updateScores()
            }
        }

        /* Configura o botão de decremento da equipa 2 */
        decrementTeam2.setOnClickListener {
            if (score2 > 0) {
                score2--
                updateScores()
            }
        }

        /* Configura o botão para voltar ao menu principal */
        mainMenuButton.setOnClickListener {
            finish()
        }

        /* Configura o botão do microfone para iniciar a SoundMonitorActivity */
        microphoneButton.setOnClickListener {
            val intent = Intent(this, SoundMonitorActivity::class.java)
            startActivity(intent)
        }
    }

    /* Método para atualizar as pontuações na interface do utilizador */
    private fun updateScores() {
        scoreTeam1.text = score1.toString()
        scoreTeam2.text = score2.toString()
    }
}