package com.example.splitorstealgame

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private var playerScore = 0
    private var cpuScore = 0

    private lateinit var mpClick: MediaPlayer
    private lateinit var mpWin: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            playerScore = savedInstanceState.getInt("playerScore")
            cpuScore = savedInstanceState.getInt("cpuScore")
        }

        setContentView(R.layout.activity_main)

        mpClick = MediaPlayer.create(this, R.raw.click)
        mpWin = MediaPlayer.create(this, R.raw.win)

        val btnStart = findViewById<Button>(R.id.btnStart)
        btnStart.setOnClickListener {
            setContentView(R.layout.game_layout)
            setupGameScreen()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("playerScore", playerScore)
        outState.putInt("cpuScore", cpuScore)
    }

    private fun setupGameScreen() {
        val scoreText = findViewById<TextView>(R.id.scoreText)
        val resultText = findViewById<TextView>(R.id.resultText)
        val btnSplit = findViewById<Button>(R.id.btnSplit)
        val btnSteal = findViewById<Button>(R.id.btnSteal)

        fun updateScoreDisplay() {
            scoreText.text = getString(R.string.score_display, playerScore, cpuScore)
        }

        fun checkForWinner() {
            if (playerScore >= 15) {
                resultText.text = getString(R.string.you_win)
                mpWin.start()
                btnSplit.isEnabled = false
                btnSteal.isEnabled = false
            } else if (cpuScore >= 15) {
                resultText.text = getString(R.string.cpu_win)
                mpWin.start()
                btnSplit.isEnabled = false
                btnSteal.isEnabled = false
            }
        }

        fun handlePlayerChoice(playerChoice: String) {
            val cpuChoice = if (Random.nextBoolean()) "Split" else "Steal"
            val message = when {
                playerChoice == "Split" && cpuChoice == "Split" -> {
                    playerScore += 2
                    cpuScore += 2
                    getString(R.string.split_split)
                }
                playerChoice == "Split" && cpuChoice == "Steal" -> {
                    cpuScore += 3
                    getString(R.string.split_steal)
                }
                playerChoice == "Steal" && cpuChoice == "Split" -> {
                    playerScore += 3
                    getString(R.string.steal_split)
                }
                else -> {
                    getString(R.string.steal_steal)
                }
            }

            mpClick.start()
            Log.d("Game", "Player: $playerChoice, CPU: $cpuChoice → $message")
            resultText.text = message
            updateScoreDisplay()
            checkForWinner()
        }

        btnSplit.setOnClickListener { handlePlayerChoice("Split") }
        btnSteal.setOnClickListener { handlePlayerChoice("Steal") }

        updateScoreDisplay()
    }
}
