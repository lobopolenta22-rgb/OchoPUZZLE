package bo.edu.uajms.cristianalejandro.ochopuzzle

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    // Controls
    private lateinit var BTNTablero: Array<Button>
    private lateinit var BTNRestart: Button
    private lateinit var BTNDisorder: Button
    private lateinit var BTNVerify: Button
    private lateinit var TXVMessage: TextView
    private lateinit var Tablero: Array<Array<Int>>

    // Variables
    private val rows = 4
    private val cols = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar controles
        BTNTablero = arrayOf(
            findViewById(R.id.BTN01), findViewById(R.id.BTN02),
            findViewById(R.id.BTN03), findViewById(R.id.BTN04),
            findViewById(R.id.BTN05), findViewById(R.id.BTN06),
            findViewById(R.id.BTN07), findViewById(R.id.BTN08),
            findViewById(R.id.BTN09), findViewById(R.id.BTN10),
            findViewById(R.id.BTN11), findViewById(R.id.BTN12),
            findViewById(R.id.BTN13), findViewById(R.id.BTN14),
            findViewById(R.id.BTN15), findViewById(R.id.BTN00)
        )
        BTNRestart = findViewById(R.id.BTNReboot)
        BTNDisorder = findViewById(R.id.BTNMess)
        BTNVerify = findViewById(R.id.BTNVerify)
        TXVMessage = findViewById(R.id.TXVMessage)

        // Inicializar tablero
        Tablero = Array(rows) { Array(cols) { 0 } }
        RebootTableroEspiral()

        // Eventos
        BTNRestart.setOnClickListener { RebootTableroEspiral() }
        BTNDisorder.setOnClickListener { MessTablero() }
        BTNVerify.setOnClickListener {
            if (VerifyTableroEspiral()) {
                Toast.makeText(this, "¡Puzzle resuelto!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Todavía no está ordenado", Toast.LENGTH_SHORT).show()
            }
        }

        for (i in BTNTablero.indices) {
            val row = i / rows
            val col = i % cols
            BTNTablero[i].setOnClickListener {
                click(row, col, BTNTablero[i])
            }
        }
    }

    private fun click(row: Int, col: Int, button: Button) {
        var emptyRow = -1
        var emptyCol = -1
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                if (Tablero[i][j] == 0) {
                    emptyRow = i
                    emptyCol = j
                }
            }
        }

        val esVecino = (row == emptyRow && (col == emptyCol - 1 || col == emptyCol + 1)) ||
                (col == emptyCol && (row == emptyRow - 1 || row == emptyRow + 1))

        if (esVecino) {
            Tablero[emptyRow][emptyCol] = Tablero[row][col]
            Tablero[row][col] = 0

            BTNTablero[emptyRow * cols + emptyCol].text = Tablero[emptyRow][emptyCol].toString()
            BTNTablero[row * cols + col].text = ""
        }
    }

    // ✅ Verificar tablero en espiral
    private fun VerifyTableroEspiral(): Boolean {
        val ideal = generarEspiralCorrecto()
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                if (Tablero[i][j] != ideal[i][j]) {
                    TXVMessage.text = "Juego Desordenado"
                    return false
                }
            }
        }
        TXVMessage.text = "Juego Ordenado"
        return true
    }

    // ✅ Desordenar tablero
    private fun MessTablero() {
        val numeros = (1..15).toMutableList()
        numeros.shuffle()
        var index = 0
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                if (i == 2 && j == 1) { // Hueco en [2][1]
                    Tablero[i][j] = 0
                    BTNTablero[i * cols + j].text = ""
                } else {
                    Tablero[i][j] = numeros[index]
                    BTNTablero[i * cols + j].text = numeros[index].toString()
                    index++
                }
            }
        }
        TXVMessage.text = "Completado"
    }

    // ✅ Reiniciar tablero en espiral
    private fun RebootTableroEspiral() {
        val ideal = generarEspiralCorrecto()
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                val value = ideal[i][j]
                Tablero[i][j] = value
                BTNTablero[i * cols + j].text = if (value == 0) "" else value.toString()
            }
        }
        TXVMessage.text = "Juego Reiniciado"
    }

    // 🔑 Generar espiral correcto con hueco en [2][1]
    private fun generarEspiralCorrecto(): Array<Array<Int>> {
        val matriz = Array(rows) { Array(cols) { 0 } }
        val orden = listOf(
            1, 2, 3, 4,
            12, 13, 14, 5,
            11, 0, 15, 6,
            10, 9, 8, 7
        )
        var index = 0
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                matriz[i][j] = orden[index++]
            }
        }
        return matriz
    }
}
