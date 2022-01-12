fun main(args :Array<String>) {
    print("Arguments: ")
    val vArgs = readLine()
    if (vArgs != null) {
        de.chaos.swlnmngr.main(vArgs.split(" ").toTypedArray())
    } else {
        println("Null!")
    }
}