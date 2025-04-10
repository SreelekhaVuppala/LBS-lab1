import backEnd.*;
import java.util.Scanner;

public class ShoppingCart {
    private static void print(Wallet wallet, Pocket pocket) throws Exception {
        System.out.println("Your current balance is: " + wallet.getBalance() + " credits.");
        System.out.println(Store.asString());
        System.out.println("Your current pocket is:\n" + pocket.getPocket());
    }

    private static String scan(Scanner scanner) throws Exception {
        System.out.print("What do you want to buy? (type quit to stop) ");
        return scanner.nextLine();
    }

    public static void main(String[] args) throws Exception {
        Wallet wallet = new Wallet();
        Pocket pocket = new Pocket();
        Scanner scanner = new Scanner(System.in);

        print(wallet, pocket);
        String product = scan(scanner);

        while(!product.equals("quit")) {
            /* TODO:
               - check if the amount of credits is enough, if not stop the execution.
               - otherwise, withdraw the price of the product from the wallet.
               - add the name of the product to the pocket file.
               - print the new balance.
            */

           //Exploit race condition.

            int price = Store.getProductPrice(product);
        //    int currentbalance = wallet.getBalance();
        //     if (price == -1) {
        //         System.out.println("Invalid product.");
        //     } else if ( currentbalance < price) {
        //         System.out.println("Not enough credits.");
        //         break;
        //     } else {
        //         // Artificial delay to make race condition easier to exploit
        //         System.out.println("Checking balance... (delaying for race condition)");
        //         Thread.sleep(5000); // 5 second delay for presentation purpose.
        //         wallet.setBalance(currentbalance-price);
        //         pocket.addProduct(product);
        //         System.out.println("You bought: " + product);
        //     }

            // to fix API 

            if (price == -1) {
                System.out.println("Invalid product.");
            } else {
                Thread.sleep(5000);// for presentation purpose delay is added.
                // Use safeWithdraw instead of withdraw
                boolean success = wallet.safeWithdraw(price);
                
                if (success) {
                    pocket.addProduct(product);
                    System.out.println("You bought: " + product);
                } else {
                    System.out.println("Not enough credits.");
                    break; // Exit if there are insufficient funds
                }
            }
            // Just to print everything again...
            print(wallet, pocket);
            product = scan(scanner);
        }
    }
}
