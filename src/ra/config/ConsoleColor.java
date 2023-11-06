package ra.config;

public class ConsoleColor {
    public static final String RESET = "default";
    public static final String BLACK = "black";
    public static final String RED = "red";
    public static final String GREEN = "green";
    public static final String YELLOW = "yellow";
    public static final String BLUE = "blue";
    public static final String PURPLE = "purple";
    public static final String CYAN = "cyan";
    public static final String WHITE = "white";


    public static void print(String color) {
        switch (color) {
            case "default":

                System.out.print("\u001B[0m");
                break;
            case "black":

                System.out.print("\u001B[30m");
                break;
            case "red":

                System.out.print("\u001B[31m");
                break;
            case "green":

                System.out.print("\u001B[32m");
                break;
            case "yellow":

                System.out.print("\u001B[33m");
                break;
            case "blue":

                System.out.print("\u001B[34m");
                break;
            case "purple":

                System.out.print("\u001B[35m");
                break;
            case "cyan":
                System.out.print("\u001B[36m");
                break;
            case "white":

                System.out.print("\u001B[37m");
                break;
            default:
                break;
        }
    }

    public static void printFinish() {

        System.out.print("\u001B[0m");

    }

    public static void sout(String color, String text) {
        switch (color) {
            case "default":
                System.out.println("\u001B[0m" + text + "\u001B[0m");
                break;
            case "black":
                System.out.println("\u001B[30m" + text + "\u001B[0m");
                break;
            case "red":
                System.out.println("\u001B[31m" + text + "\u001B[0m");
                break;
            case "green":
                System.out.println("\u001B[32m" + text + "\u001B[0m");
                break;
            case "yellow":
                System.out.println("\u001B[33m" + text + "\u001B[0m");
                break;
            case "blue":
                System.out.println("\u001B[34m" + text + "\u001B[0m");
                break;
            case "purple":
                System.out.println("\u001B[35m" + text + "\u001B[0m");
                break;
            case "cyan":
                System.out.println("\u001B[36m" + text + "\u001B[0m");
                break;
            case "white":
                System.out.println("\u001B[37m" + text + "\u001B[0m");
                break;
            default:
                break;
        }
    }

    public static void printlnError(String text) {

        System.out.println("\u001B[31m" + text + "\u001B[0m");

    }

    public static void printlnSuccess(String text) {
        System.out.println("\u001B[32m" + text + "\u001B[0m");
    }
    public static void printlnMess(String text) {

        System.out.println("\u001B[34m" + text + "\u001B[0m");

    }

}
