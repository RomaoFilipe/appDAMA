package pt.ipt.dama2024.absan

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ScoreboardActivity : AppCompatActivity() {

    private lateinit var scoreTeam1: TextView
    private lateinit var scoreTeam2: TextView

    private var score1 = 0
    private var score2 = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scoreboard)

        scoreTeam1 = findViewById(R.id.scoreTeam1)
        scoreTeam2 = findViewById(R.id.scoreTeam2)

        val team1Button = findViewById<Button>(R.id.team1Button)
        val team2Button = findViewById<Button>(R.id.team2Button)

        val decrementTeam1 = findViewById<Button>(R.id.decrementTeam1)
        val decrementTeam2 = findViewById<Button>(R.id.decrementTeam2)

        val mainMenuButton = findViewById<Button>(R.id.mainMenuButton)

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
    }

    private fun updateScores() {
        scoreTeam1.text = score1.toString()
        scoreTeam2.text = score2.toString()
    }
}
