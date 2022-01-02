import de.chaos.swlnmngr.Main;

import java.util.Scanner;

public class RunWithArgs {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String arg = scanner.nextLine();
        Main.main(arg.split(" "));
    }
}
