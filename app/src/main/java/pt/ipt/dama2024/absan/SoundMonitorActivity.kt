package pt.ipt.dama2024.absan

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Handler
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton

class SoundMonitorActivity : AppCompatActivity() {

    private lateinit var mediaRecorder: MediaRecorder
    private lateinit var textViewSoundLevel: TextView
    private lateinit var progressBarSoundLevel: ProgressBar
    private lateinit var buttonStop: MaterialButton
    private lateinit var buttonBack: ImageButton
    private var isRecording = false
    private val handler = Handler()

    private val PERMISSION_REQUEST_CODE = 1
    private val REQUIRED_PERMISSIONS = arrayOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sound_monitor)

        textViewSoundLevel = findViewById(R.id.textViewSoundLevel)
        progressBarSoundLevel = findViewById(R.id.progressBarSoundLevel)
        buttonStop = findViewById(R.id.buttonStop)
        buttonBack = findViewById(R.id.buttonBack)

        // Verificar se as permissões necessárias foram concedidas
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSION_REQUEST_CODE)
        } else {
            startRecording()
        }

        buttonStop.setOnClickListener {
            stopRecording()
            Toast.makeText(this, "Gravação Parada", Toast.LENGTH_SHORT).show()
        }

        buttonBack.setOnClickListener {
            stopRecording()
            finish() // Finaliza a atividade atual e retorna à atividade anterior
        }
    }

    private fun allPermissionsGranted(): Boolean {
        return REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun startRecording() {
        try {
            mediaRecorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                setOutputFile("/dev/null")
                prepare()
                start()
            }
            isRecording = true
            monitorSoundLevel()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Erro ao iniciar a gravação: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun stopRecording() {
        if (isRecording) {
            mediaRecorder.stop()
            mediaRecorder.release()
            isRecording = false
        }
    }

    private fun monitorSoundLevel() {
        if (isRecording) {
            val maxAmplitude = mediaRecorder.maxAmplitude
            textViewSoundLevel.text = "Nível de Som: $maxAmplitude"
            progressBarSoundLevel.progress = maxAmplitude
            if (maxAmplitude > 10000) { // Ajuste o valor conforme necessário
                Toast.makeText(this, "Tacada forte detectada!", Toast.LENGTH_SHORT).show()
            }
            handler.postDelayed({ monitorSoundLevel() }, 100)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                startRecording()
            } else {
                Toast.makeText(this, "Todas as permissões são necessárias", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopRecording()
    }
}