package pt.ipt.dama2024.absan

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class ScoreboardActivity : AppCompatActivity() {

    private lateinit var scoreTeam1: TextView
    private lateinit var scoreTeam2: TextView
    private lateinit var team1Name: EditText
    private lateinit var team2Name: EditText

    private var score1 = 0
    private var score2 = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scoreboard)

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

        team1Button.setOnClickListener {
            score1++
            updateScores()
        }

        team2Button.setOnClickListener {
            score2++
            updateScores()
        }

        decrementTeam1.setOnClickListener {
            if (score1 > 0) {
                score1--
                updateScores()
            }
        }

        decrementTeam2.setOnClickListener {
            if (score2 > 0) {
                score2--
                updateScores()
            }
        }

        mainMenuButton.setOnClickListener {
            finish()
        }

        microphoneButton.setOnClickListener {
            val intent = Intent(this, SoundMonitorActivity::class.java)
            startActivity(intent)
        }
    }

    private fun updateScores() {
        scoreTeam1.text = score1.toString()
        scoreTeam2.text = score2.toString()
    }
}