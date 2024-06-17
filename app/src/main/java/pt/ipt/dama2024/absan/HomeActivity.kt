package pt.ipt.dama2024.absan

import android.content.Intent
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat

/* HomeActivity é uma atividade que representa a tela principal do aplicativo */
class HomeActivity : AppCompatActivity() {
    /* Método chamado quando a atividade é criada */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        /* Obtém as referências dos CardViews do layout */
        val cardViewProfile = findViewById<CardView>(R.id.cardViewProfile)
        val cardViewScheduleLesson = findViewById<CardView>(R.id.cardViewScheduleLesson)
        val cardViewScoreboard = findViewById<CardView>(R.id.cardViewScoreboard)
        /* Define o fundo circular para cada CardView com cores diferentes */
        setCircleBackground(cardViewProfile, R.color.red)
        setCircleBackground(cardViewScheduleLesson, R.color.yellow)
        setCircleBackground(cardViewScoreboard, R.color.blue)
        /* Define ações de clique para cada CardView, abrindo uma nova atividade correspondente */
        cardViewProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        cardViewScheduleLesson.setOnClickListener {
            val intent = Intent(this, ScheduleLessonActivity::class.java)
            startActivity(intent)
        }

        cardViewScoreboard.setOnClickListener {
            val intent = Intent(this, ScoreboardActivity::class.java)
            startActivity(intent)
        }
    }

    /* Método para definir o fundo circular de um CardView com uma cor específica */
    private fun setCircleBackground(cardView: CardView, colorRes: Int) {
        val drawable = ShapeDrawable(OvalShape()).apply {
            paint.color = ContextCompat.getColor(this@HomeActivity, colorRes)
        }
        cardView.background = drawable
    }
}
