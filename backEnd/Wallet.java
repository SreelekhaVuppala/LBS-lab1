package backEnd;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;




public class Wallet {
    /**
     * The RandomAccessFile of the wallet file
     */  
    private RandomAccessFile file;


     private FileChannel channel;
    

    /**
     * Creates a Wallet object
     *
     * A Wallet object interfaces with the wallet RandomAccessFile
     */
    public Wallet () throws Exception {
	this.file = new RandomAccessFile(new File("backEnd/wallet.txt"), "rw");
    this.channel = this.file.getChannel();
    
    }

    /**
     * Gets the wallet balance. 
     *
     * @return                   The content of the wallet file as an integer
     */
    public int getBalance() throws IOException {
        
    this.file.seek(0);
    return Integer.parseInt(this.file.readLine());
    }

    /**
     * Sets a new balance in the wallet
     *
     * @param  newBalance          new balance to write in the wallet
     */
    public void setBalance(int newBalance) throws Exception {
	this.file.setLength(0);
	String str = Integer.valueOf(newBalance).toString()+'\n'; 
	this.file.writeBytes(str); 
    }

    /**
     * Safely withdraws the specified amount from the wallet.
     * This method uses synchronization to prevent race conditions.
     *
     * @param valueToWithdraw The amount to withdraw.
     * @return true if the withdrawal was successful, false if there were insufficient funds.
     */
    public  boolean safeWithdraw(int valueToWithdraw) throws Exception {
        FileLock lock = null;
        try{
            lock = channel.lock();
            //read current balance
           int currentBalance = getBalance();
        
            // Check if the balance is enough for the withdrawal
            if (currentBalance >= valueToWithdraw) {
                // Deduct the amount from the balance
                int newBalance = currentBalance - valueToWithdraw;
                setBalance(newBalance);
                return true;
            } else {
                // Insufficient funds, return false
                return false;
            }
        }
        finally{
            if(lock != null && lock.isValid())
            {
            lock.release();
            }
        }
    }

    /**
     * Closes the RandomAccessFile in this.file
     */
    public void close() throws Exception {
        if(this.channel.isOpen())
        {
            this.channel.close();
        }    
	    this.file.close();
    }
}
