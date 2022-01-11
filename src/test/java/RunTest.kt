import de.chaos.swlnmngr.Main

fun main(args :Array<String>) {
    print("Arguments: ")
    val vArgs = readLine()
    if (vArgs != null) {
        Main.main(vArgs.split(" ").toTypedArray())
    } else {
        println("Null!")
    }
}