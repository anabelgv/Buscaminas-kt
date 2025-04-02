fun main() {
    val scanner = java.util.Scanner(System.`in`)

    println("=== BUSCAMINAS ===")
    val (filas, columnas, minas) = pedirConfiguracion(scanner)

    try {
        val juego = Buscaminas(filas, columnas, minas)
        var primeraJugada = true

        while (!juego.estaTerminado()) {
            imprimirTablero(juego)

            when (pedirAccion(scanner)) {
                1 -> procesarDestapar(juego, scanner, primeraJugada).also { primeraJugada = false }
                2 -> procesarBandera(juego, scanner)
            }
        }

        imprimirTablero(juego)
        println(if (juego.esGanado()) "¡GANASTE!" else "¡PERDISTE!")
    } catch (e: IllegalArgumentException) {
        println("Error: ${e.message}")
    }
}

private fun pedirConfiguracion(scanner: java.util.Scanner): Triple<Int, Int, Int> {
    fun leerNumero(mensaje: String, default: Int): Int {
        print("$mensaje (default $default): ")
        return scanner.nextLine().toIntOrNull() ?: default
    }

    val filas = leerNumero("Filas", 8)
    val columnas = leerNumero("Columnas", 8)
    val minas = leerNumero("Minas", 10)

    return Triple(filas, columnas, minas)
}

private fun imprimirTablero(juego: Buscaminas) {
    print("  ")
    for (c in 0 until juego.columnas) print("$c ")
    println()

    for (f in 0 until juego.filas) {
        print("$f ")
        for (c in 0 until juego.columnas) {
            print("${juego.obtenerEstadoCelda(f, c)} ")
        }
        println()
    }
}

private fun pedirAccion(scanner: java.util.Scanner): Int {
    println("\n1. Destapar")
    println("2. Bandera")
    print("Elige: ")
    return scanner.nextLine().toIntOrNull() ?: 1
}

private fun procesarDestapar(juego: Buscaminas, scanner: java.util.Scanner, primeraJugada: Boolean) {
    val (f, c) = pedirCoordenadas(scanner, "Destapar (fila columna): ", juego.filas, juego.columnas)
    juego.destapar(f, c)
    if (primeraJugada) println("¡Primer movimiento! Minas colocadas.")
}

private fun procesarBandera(juego: Buscaminas, scanner: java.util.Scanner) {
    val (f, c) = pedirCoordenadas(scanner, "Bandera (fila columna): ", juego.filas, juego.columnas)
    juego.alternarBandera(f, c)
}

private fun pedirCoordenadas(
    scanner: java.util.Scanner,
    mensaje: String,
    maxFilas: Int,
    maxColumnas: Int
): Pair<Int, Int> {
    while (true) {
        print(mensaje)
        val input = scanner.nextLine().split(" ").mapNotNull { it.toIntOrNull() }
        if (input.size == 2 && input[0] in 0 until maxFilas && input[1] in 0 until maxColumnas) {
            return input[0] to input[1]
        }
        println("Coordenadas inválidas. Usa: fila (0-${maxFilas - 1}) columna (0-${maxColumnas - 1})")
    }
}