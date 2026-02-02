package practice.week3;
import java.util.*;

public class ExeptionalHanling2 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter a number: ");

        int number;

        try {
            number = sc.nextInt();
            int sum = number + 5;

        } catch (Exception e) {
            System.out.println("Invalid Number");
        }

        System.out.println("Try another number");
        number = inputNumber();

        System.out.println("Thank for using the program. Good bye.");
        sc.close();

    }

    public static int inputNumber() {
        int number = 0;

        Scanner input = new Scanner(System.in);
        for (;;) {  //infint loop
            try {
                number = inputNumber();
                return number;
                
            } catch (Exception e) {
                input.nextLine();
                System.out.println("Invalid number");

            }
        }

    }
}
