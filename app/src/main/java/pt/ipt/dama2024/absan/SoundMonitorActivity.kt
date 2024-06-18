package pt.ipt.dama2024.absan

import android.Manifest
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import kotlin.math.log10

class SoundMonitorActivity : AppCompatActivity() {

    private lateinit var textViewSoundLevel: TextView
    private lateinit var progressBarSoundLevel: ProgressBar
    private lateinit var buttonStop: MaterialButton
    private lateinit var buttonRepeat: MaterialButton
    private lateinit var buttonBack: ImageButton
    private var audioRecord: AudioRecord? = null
    private var isRecording = false
    private val handler = Handler(Looper.getMainLooper())
    private var maxAmplitude = 0

    private val PERMISSION_REQUEST_CODE = 1
    private val REQUIRED_PERMISSIONS = arrayOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sound_monitor)

        textViewSoundLevel = findViewById(R.id.textViewSoundLevel)
        progressBarSoundLevel = findViewById(R.id.progressBarSoundLevel)
        buttonStop = findViewById(R.id.buttonStop)
        buttonRepeat = findViewById(R.id.buttonRepeat)
        buttonBack = findViewById(R.id.buttonBack)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSION_REQUEST_CODE)
        } else {
            startRecording()
        }

        buttonStop.setOnClickListener {
            stopRecording()
            Toast.makeText(this, "Gravação Parada", Toast.LENGTH_SHORT).show()
        }

        buttonRepeat.setOnClickListener {
            stopRecording()
            startRecording()
        }

        buttonBack.setOnClickListener {
            stopRecording()
            finish()
        }
    }

    private fun allPermissionsGranted(): Boolean {
        return REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun startRecording() {
        val bufferSize = AudioRecord.getMinBufferSize(
            44100,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )

        audioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            44100,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            bufferSize
        )

        audioRecord?.startRecording()
        isRecording = true
        maxAmplitude = 0
        handler.postDelayed(stopRecordingRunnable, 10000) // Stop recording after 10 seconds
        monitorSoundLevel()
    }

    private fun stopRecording() {
        if (isRecording) {
            audioRecord?.stop()
            audioRecord?.release()
            audioRecord = null
            isRecording = false
            handler.removeCallbacks(stopRecordingRunnable) // Ensure callback is removed
            textViewSoundLevel.text = "Highest Sound Level: $maxAmplitude dB"
            Toast.makeText(this, "Highest Sound Level: $maxAmplitude dB", Toast.LENGTH_LONG).show()
        }
    }

    private fun monitorSoundLevel() {
        if (isRecording) {
            val buffer = ShortArray(1024)
            audioRecord?.read(buffer, 0, buffer.size)
            val currentAmplitude = calculateDecibels(buffer)
            if (currentAmplitude > maxAmplitude) {
                maxAmplitude = currentAmplitude
            }
            textViewSoundLevel.text = "Nível de Som: $currentAmplitude dB"
            progressBarSoundLevel.progress = currentAmplitude
            handler.postDelayed({ monitorSoundLevel() }, 100)
        }
    }

    private fun calculateDecibels(buffer: ShortArray): Int {
        var sum = 0.0
        for (sample in buffer) {
            sum += sample * sample
        }
        val rms = Math.sqrt(sum / buffer.size)
        return (20 * log10(rms)).toInt()
    }

    private val stopRecordingRunnable = Runnable {
        stopRecording()
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
