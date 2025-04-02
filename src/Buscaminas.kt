class Buscaminas (val filas:Int, val columnas:Int, val Num_Minas:Int){

    private data class Celda(
        var tienemina: Boolean = false,
        var destapada:Boolean = false,
        var bandera:Boolean = false,
        var minasAdyacentes:Int=0
    )

    private val tablero = Array(filas){
        Array(columnas){
            Celda()
        }
    }
    private var juegoTerminado= false
    private var juegoGanado=false
    private var celdasDestapadas = 0
    private var minasColocadas=false

    init {
        require(filas > 0 && columnas > 0) { "El tablero debe tener al menos 1 fila y 1 columna" }
        require(Num_Minas < filas * columnas) { "Demasiadas minas para el tamaño del tablero" }
    }
    fun estaTerminado() = juegoTerminado
    fun esGanado() = juegoGanado

    fun destapar(fila: Int, columna: Int) {
        if (juegoTerminado || !esPosicionValida(fila, columna)) return

        if (!minasColocadas) {
            colocarMinas(fila, columna)
            minasColocadas = true
        }

        val celda = tablero[fila][columna]
        if (celda.destapada || celda.bandera) return

        if (celda.tienemina) {
            terminarJuego(ganado = false)
            return
        }

        destaparCelda(fila, columna)

        if (celdasDestapadas == filas * columnas - Num_Minas) {
            terminarJuego(ganado = true)
        }
    }

    fun alternarBandera(fila: Int, columna: Int) {
        if (juegoTerminado || !esPosicionValida(fila, columna)) return
        val celda = tablero[fila][columna]
        if (!celda.destapada) {
            celda.bandera = !celda.bandera
        }
    }

    fun obtenerEstadoCelda(fila: Int, columna: Int): Char {
        if (!esPosicionValida(fila, columna)) return ' '
        val celda = tablero[fila][columna]

        return when {
            juegoTerminado && celda.tienemina -> '*'
            celda.bandera -> 'B'
            !celda.destapada -> '.'
            celda.tienemina -> '*' // No debería ocurrir
            celda.minasAdyacentes > 0 -> '0' + celda.minasAdyacentes
            else -> ' '
        }
    }

    private fun esPosicionValida(fila: Int, columna: Int) =
        fila in 0 until filas && columna in 0 until columnas

    private fun colocarMinas(filaExcluida: Int, columnaExcluida: Int) {
        val posiciones = mutableListOf<Pair<Int, Int>>().apply {
            for (f in 0 until filas) {
                for (c in 0 until columnas) {
                    if (f != filaExcluida || c != columnaExcluida) {
                        add(f to c)
                    }
                }
            }
        }

        posiciones.shuffle()

        repeat(Num_Minas) { i ->
            if (i >= posiciones.size) return@repeat
            val (f, c) = posiciones[i]
            tablero[f][c].tienemina = true
            actualizarMinasAdyacentes(f, c)
        }
    }

    private fun actualizarMinasAdyacentes(fila: Int, columna: Int) { //rarete
        for (f in (fila - 1).coerceAtLeast(0)..(fila + 1).coerceAtMost(filas - 1)) {
            for (c in (columna - 1).coerceAtLeast(0)..(columna + 1).coerceAtMost(columnas - 1)) {
                if (f != fila || c != columna) {
                    tablero[f][c].minasAdyacentes++
                }
            }
        }
    }

    private fun destaparCelda(fila: Int, columna: Int) {
        if (!esPosicionValida(fila, columna)) return
        val celda = tablero[fila][columna]
        if (celda.destapada || celda.bandera || celda.tienemina) return

        celda.destapada = true
        celdasDestapadas++

        if (celda.minasAdyacentes == 0) {
            destaparCeldasAdyacentes(fila, columna)
        }
    }

    private fun destaparCeldasAdyacentes(fila: Int, columna: Int) { //cambiar
        for (f in (fila - 1).coerceAtLeast(0)..(fila + 1).coerceAtMost(filas - 1)) {
            for (c in (columna - 1).coerceAtLeast(0)..(columna + 1).coerceAtMost(columnas - 1)) {
                destaparCelda(f, c)
            }
        }
    }

    private fun terminarJuego(ganado: Boolean) {
        juegoTerminado = true
        juegoGanado = ganado
        if (!ganado) revelarMinas()
    }

    private fun revelarMinas() {
        for (fila in tablero) {
            for (celda in fila) {
                if (celda.tienemina) celda.destapada = true
            }
        }
    }
}