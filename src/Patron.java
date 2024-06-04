//M. M. Kuttel 2024 mkuttel@gmail.com

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

/*
 This is the basicclass, representing the patrons at the bar
 */

public class Patron extends Thread {
	
   private Random random = new Random();// for variation in Patron behaviour

   private CountDownLatch startSignal; //all start at once, actually shared
   private Barman theBarman; //the Barman is actually shared though

   private int ID; //thread ID 
   private int lengthOfOrder;
   private static long startTime, endTime; //for all the metrics
   private static int patronsServed = 0; //Number of patron served
	
   public static FileWriter fileW;
   public static FileWriter waitingTimeW ;
   public static FileWriter responseTimeW ;


   private DrinkOrder [] drinksOrder;
	
   Patron( int ID,  CountDownLatch startSignal, Barman aBarman) {
      this.ID=ID;
      this.startSignal=startSignal;
      this.theBarman=aBarman;
      this.lengthOfOrder=random.nextInt(5)+1;//between 1 and 5 drinks
      drinksOrder=new DrinkOrder[lengthOfOrder];
   }

   public static int getPatronsServed() {
      return patronsServed;
  }
	
   public  void writeToFile(String data) throws IOException {
      synchronized (fileW) {
         fileW.write(data);
      }
   }

   public  void waitingTime(String data) throws IOException { //waitingTimeFile
      synchronized (waitingTimeW) {
         waitingTimeW.write(data);
      }
   }

   public  void responseTime(String data) throws IOException { //responseTimeFile
      synchronized (responseTimeW) {
         responseTimeW.write(data);
      }
   }
	
   private static synchronized void updateMatrics(){
      patronsServed++;
   }

   public static synchronized double calThroughput(long durationInSecs){
      return (double) patronsServed/durationInSecs ;
   }

   public static synchronized void setStartTime(){
      startTime = System.currentTimeMillis() ;
   }

   public static synchronized void setEndTime(){
      endTime = System.currentTimeMillis() ;
   }



   public void run() {
      try {
      	//Do NOT change the block of code below - this is the arrival times
         startSignal.countDown(); //this patron is ready
         startSignal.await(); //wait till everyone is ready
         int arrivalTime = random.nextInt(300)+ID*100;  // patrons arrive gradually later
         sleep(arrivalTime);// Patrons arrive at staggered  times depending on ID 
         System.out.println("thirsty Patron "+ this.ID +" arriveed");
      	//END do not change
      	
         //create drinks order
         int burstTime = 0 ;
         for(int i=0;i<lengthOfOrder;i++) {
            drinksOrder[i]=new DrinkOrder(this.ID);
            burstTime += drinksOrder[i].getExecutionTime();
           	
         }

         System.out.println("Patron "+ this.ID + " submitting order of " + lengthOfOrder +" drinks"); //output in standard format  - do not change this
         startTime = System.currentTimeMillis();//started placing orders
         for(int i=0;i<lengthOfOrder;i++) {
            System.out.println("Order placed by " + drinksOrder[i].toString());
            theBarman.placeDrinkOrder(drinksOrder[i]);
            updateMatrics(); //Patron's order being served
         }
         for(int i=0;i<lengthOfOrder;i++) {
            drinksOrder[i].waitForOrder();
         }

         // Calculate the response time (time when the barman started preparing the drink - arrival time)
         long responseTime = drinksOrder[0].getStartTime() - startTime; 
      
         endTime = System.currentTimeMillis();
         long totalTime = endTime - startTime; //turnaroundTime

         writeToFile( String.format("%d\n",totalTime));
         System.out.println("Patron "+ this.ID + " got order in " + totalTime);

         long waitingTime = totalTime - burstTime ;

         waitingTime( String.format("%d\n",waitingTime)); //print waiting time
         System.out.println("Patron "+ this.ID + " got order in " + totalTime);

         responseTime( String.format("%d\n",responseTime)); //print response time
         System.out.println("Patron "+ this.ID + " got order in " + totalTime);

         // synchronized(this){ patronsServed++; }  //increment patrons served
      	
      } catch (InterruptedException e1) {  //do nothing
      } catch (IOException e) {
      	//  Auto-generated catch block
         e.printStackTrace();
      }
   }

   public static void printThroughput(){
      long durationInSecs = (endTime - startTime) / 1000 ;
      double throughput =calThroughput(durationInSecs) ;
      System.out.println("Throughput: " + throughput + " patrons/second");
   }
}
	

