import java.util.Scanner

fun main() {
    val buscaminas = InterfaceBuscaminas(10, 10, 10)
    buscaminas.jugar()
}

class InterfaceBuscaminas(private val filas: Int, private val columnas: Int, private val numMinas: Int) {
    private val juego: Buscaminas = Buscaminas(filas, columnas, numMinas)
    private val scanner: Scanner = Scanner(System.`in`)

    fun jugar() {
        println("¡Bienvenido al Buscaminas!")
        println("Instrucciones facilitas:")
        println("- Ingresa las coordenadas de la C que quieres destapar")
        println("- Las filas y columnas van de 0 a ${filas-1}")
        println("- ¡Cuidado con las *!\n")

        while (!juego.isJuegoTerminado()) {
            mostrarTablero(juego.getTableroDestapado(), juego.getTableroNum())

            try {
                print("Fila (0-${filas-1}): ")
                val fila = scanner.nextInt()
                print("Columna (0-${columnas-1}): ")
                val columna = scanner.nextInt()

                if (fila !in 0 until filas || columna !in 0 until columnas) {
                    println("No vale, pon algo válido.")
                    continue
                }

                juego.destaparCasilla(fila, columna)
            } catch (e: Exception) {
                println("No vale, pon algo válido.")
                scanner.nextLine() // Limpiar
            }
        }

        mostrarTableroFinal()

        if (juego.isJuegoGanado()) {
            println("Has ganado :D!.")
        } else {
            println("¡Boom! Chao pescao")
        }
    }

    private fun mostrarTablero(tableroDestapado: Array<BooleanArray>, tableroNum: Array<IntArray>) {
        println("\nTablero:")
        print("   ")
        for (j in 0 until columnas) print("$j ")
        println()

        for (i in 0 until filas) {
            print("$i ")
            for (j in 0 until columnas) {
                if (tableroDestapado[i][j]) {
                    when (tableroNum[i][j]) {
                        -1 -> print("*") // Mina (solo se muestra al final)
                        0 -> print("-")  // Casilla vacía
                        else -> print(" ${tableroNum[i][j]}") // Número de minas adyacentes
                    }
                } else {
                    print("C") // Casilla no destapada
                }
                print(" ")
            }
            println()
        }
    }

    private fun mostrarTableroFinal() {
        val tableroDestapado = juego.getTableroDestapado()
        val tableroNum = juego.getTableroNum()

        println("\nTablero Final:")
        print("   ")
        for (j in 0 until columnas) print("$j ")
        println()

        for (i in 0 until filas) {
            print("$i ")
            for (j in 0 until columnas) {
                if (tableroNum[i][j] == -1) {
                    print("*") // Mostrar todas las minas al final
                } else if (tableroDestapado[i][j]) {
                    when (tableroNum[i][j]) {
                        0 -> print("-")
                        else -> print(" ${tableroNum[i][j]}")
                    }
                } else {
                    print("C")
                }
                print(" ")
            }
            println()
        }
    }
}