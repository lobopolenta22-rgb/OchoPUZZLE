package bo.edu.uajms.cristianalejandro.ochopuzzle

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity()
{
    //Controls

    private lateinit var BTNTablero: Array<Button>
    private lateinit var BTNReboot: Button
    private lateinit var BTNMess: Button
    private lateinit var BTNVerify: Button
    private lateinit var Tablero: Array<Array<Int>>
    // Variables
    private val rows = 4
    private val cols = 4;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize variable
        BTNTablero = arrayOf(
                    findViewById(R.id.BTN01),
                    findViewById(R.id.BTN02),
                    findViewById(R.id.BTN03),
                    findViewById(R.id.BTN04),
                    findViewById(R.id.BTN05),
                    findViewById(R.id.BTN06),
                    findViewById(R.id.BTN07),
                    findViewById(R.id.BTN08),
                    findViewById(R.id.BTN09),
                    findViewById(R.id.BTN10),
                    findViewById(R.id.BTN11),
                    findViewById(R.id.BTN12),
                    findViewById(R.id.BTN13),
                    findViewById(R.id.BTN14),
                    findViewById(R.id.BTN15),
                    findViewById(R.id.BTN00)
        )
        BTNReboot = findViewById(R.id.BTNReboot)
        BTNMess = findViewById(R.id.BTNMess)
        BTNVerify = findViewById(R.id.BTNVerify)
        Tablero = Array(size = rows){ Array (size = cols){0} }
        //Initialize sorted board
        var num = 1
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                if (i == rows - 1 && j == cols - 1) {
                    Tablero[i][j] = 0 // empty hollow
                    BTNTablero[i * cols + j].text = ""
                } else {
                    Tablero[i][j] = num
                    BTNTablero[i * cols + j].text = num.toString()
                    num++
                }
            }
        }
        //Events
        for (i in BTNTablero.indices) {
            val row = i / rows
            val col = i % cols
            BTNTablero[i].setOnClickListener()
            {
                Log.d("Click", "Hiciste click en ($row, $col)")
                click(row,col,BTNTablero[i])
            }
        }
        BTNVerify.setOnClickListener {
            if (VerifyTablero()) {
                Toast.makeText(this, "¡Puzzle resuelto!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Todavía no está ordenado", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun click(row: Int, col: Int, button: android.widget.Button)
    {
        // Find empty position
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

        // Check if the clicked button is next to the empty space.
        val esVecino = (row == emptyRow && (col == emptyCol - 1 || col == emptyCol + 1)) ||
                (col == emptyCol && (row == emptyRow - 1 || row == emptyRow + 1))

        if (esVecino) {
            // swap matrix values
            Tablero[emptyRow][emptyCol] = Tablero[row][col]
            Tablero[row][col] = 0

            // Update button text
            BTNTablero[emptyRow * cols + emptyCol].text = Tablero[emptyRow][emptyCol].toString()
            BTNTablero[row * cols + col].text = ""

            Log.d("Movimiento", "Se movió la ficha ${Tablero[emptyRow][emptyCol]} al hueco")
        } else {
            Log.d("Movimiento", "No se puede mover, no está junto al hueco")
        }
    }
    private fun VerifyTablero(): Boolean
    {
        var num = 1
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                if (i == rows - 1 && j == cols - 1)
                {
                    if (Tablero[i][j] != 0)
                    {
                        return false // última casilla debe estar vacía
                    }
                } else {
                    if (Tablero[i][j] != num)
                    {
                        return false
                    }
                    num++
                }
            }
        }
        return true
    }
}
