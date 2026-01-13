import java.util.Scanner;

public class Array1 {
    public static void main(String[] args) {
        int[] number = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

        for (int i = 0; i < 10; i++) {
            System.out.println(number[i]);

        }
        Scanner input = new Scanner(System.in);

        System.out.print("Enter an index (0-9) to retrieve the corresponding number: ");
        int index = input.nextInt();
        
        for (int x = 0; x < 10; x++){
            if (  number[x] == index ){
                System.out.println(x);
            }
        }
    }
}