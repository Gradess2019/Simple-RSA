import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        RSA rsa = new RSA(5,5001);
        System.out.println("Информация о RSA-объекте: \n" + rsa + "\n==============");
        Scanner scanner = new Scanner(System.in);
        String msg = null;
        do {
            System.out.print("Исходное сообщение: ");
            //Hello! My name is Stepan!  I'm a programmer! Am I coding now? Yes, I am
            msg = scanner.nextLine();
            long[] encryptMsg = rsa.encrypt(rsa.getPublicKey(), msg);
            String decryptMsg = rsa.decrypt(rsa.getPrivateKey(), encryptMsg);


            System.out.println("Зашифрованное сообщение: " + Arrays.toString(encryptMsg));
            System.out.println("Расшифрованное сообщение: " + decryptMsg);

            System.out.println("\nПродолжить? (да/нет)");
            msg = scanner.nextLine();
        } while (msg != null && !msg.equals("да"));

    }
}
