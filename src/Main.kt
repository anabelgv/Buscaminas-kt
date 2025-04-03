import java.util.Random

//parte lógica

class Buscaminas(private val filas: Int, private val columnas: Int, private val numMinas: Int) {
    private val tableroNum: Array<IntArray> = Array(filas) { IntArray(columnas) }
    private val tableroDestapado: Array<BooleanArray> = Array(filas) { BooleanArray(columnas) }
    private var juegoTerminado: Boolean = false
    private var juegoGanado: Boolean = false
    private var casillasDestapadas: Int = 0

    init {
        inicializarTablero()
        colocarMinasYActualizar()
    }

    private fun inicializarTablero() {
        for (i in 0 until filas) {
            for (j in 0 until columnas) {
                tableroNum[i][j] = 0
                tableroDestapado[i][j] = false
            }
        }
    }

    private fun colocarMinasYActualizar() {
        val random = Random()
        var minasColocadas = 0

        while (minasColocadas < numMinas) {
            val fila = random.nextInt(filas)
            val columna = random.nextInt(columnas)

            if (tableroNum[fila][columna] != -1) {
                tableroNum[fila][columna] = -1 // -1 representa una mina
                minasColocadas++

                // Actualizar celdas vecinas
                for (i in maxOf(0, fila - 1)..minOf(filas - 1, fila + 1)) {
                    for (j in maxOf(0, columna - 1)..minOf(columnas - 1, columna + 1)) {
                        if (tableroNum[i][j] != -1) {
                            tableroNum[i][j]++
                        }
                    }
                }
            }
        }
    }

    fun destaparCasilla(fila: Int, columna: Int) {
        if (!juegoTerminado && !tableroDestapado[fila][columna]) {
            tableroDestapado[fila][columna] = true

            if (tableroNum[fila][columna] == 0) {
                destaparVecinos(fila, columna)
            }

            if (tableroNum[fila][columna] == -1) {
                juegoTerminado = true
            } else {
                casillasDestapadas++
                if (casillasDestapadas == (filas * columnas) - numMinas) {
                    juegoTerminado = true
                    juegoGanado = true
                }
            }
        }
    }

    private fun destaparVecinos(fila: Int, columna: Int) {
        for (i in maxOf(0, fila - 1)..minOf(filas - 1, fila + 1)) {
            for (j in maxOf(0, columna - 1)..minOf(columnas - 1, columna + 1)) {
                if (!tableroDestapado[i][j]) {
                    tableroDestapado[i][j] = true
                    if (tableroNum[i][j] == 0) {
                        destaparVecinos(i, j)
                    }
                }
            }
        }
    }

    fun isJuegoTerminado(): Boolean = juegoTerminado
    fun isJuegoGanado(): Boolean = juegoGanado
    fun getTableroDestapado(): Array<BooleanArray> = tableroDestapado
    fun getTableroNum(): Array<IntArray> = tableroNum
}