//M. M. Kuttel 2024 mkuttel@gmail.com
// the main class, starts all threads


import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;


public class SchedulingSimulation {
   static int noPatrons=100; //number of customers - default value if not provided on command line
   static int sched=0; //which scheduling algorithm, 0= FCFS
			
   static CountDownLatch startSignal;

   static Patron[] patrons; // array for customer threads
   static Barman Andre;
   static FileWriter writer;
   static FileWriter writerWaiting;
   static FileWriter writerResponse;

   public  void writeToFile(String data) throws IOException {
      synchronized (writer) {
         writer.write(data);
      }
   }

   public  void waitingTime(String data) throws IOException { //waitingWrt
      synchronized (writerWaiting) {
         writerWaiting.write(data);
      }
   }

   public  void responseTime(String data) throws IOException { //responseWrt
      synchronized (writerResponse) {
         writerResponse.write(data);
      }
   }


   public static void main(String[] args) throws InterruptedException, IOException {
   	
   	
   
   	//deal with command line arguments if provided
      if (args.length==1) {
         noPatrons=Integer.parseInt(args[0]);  //total people to enter room
      } else if (args.length==2) {
         noPatrons=Integer.parseInt(args[0]);  //total people to enter room
         sched=Integer.parseInt(args[1]); 
      }
   	
      writer = new FileWriter("turnaround_time_"+Integer.toString(sched)+".txt", false);
      Patron.fileW=writer;

      writerWaiting = new FileWriter("waiting_time_"+Integer.toString(sched)+".txt", false); //waitingFile
      Patron.waitingTimeW=writerWaiting;

      writerResponse = new FileWriter("response_time_"+Integer.toString(sched)+".txt", false); //responseFile
      Patron.responseTimeW=writerResponse;
   
      startSignal= new CountDownLatch(noPatrons+2);//Barman and patrons and main method must be raeady
   	
   	//create barman
      Andre= new Barman(startSignal,sched); 
      Andre.start();
   
       //create all the patrons, who all need access to Andre
      patrons = new Patron[noPatrons];
      for (int i=0;i<noPatrons;i++) {
         patrons[i] = new Patron(i,startSignal,Andre);
         patrons[i].start();
      }
   	
      System.out.println("------Andre the Barman Scheduling Simulation------");
      System.out.println("-------------- with "+ Integer.toString(noPatrons) + " patrons---------------");
   
      startSignal.countDown(); //main method ready
      	
      //wait till all patrons done, otherwise race condition on the file closing!
      for (int i=0;i<noPatrons;i++) {
         patrons[i].join();
      }

      Patron.setEndTime();// get the endTime of the simulation
      System.out.println();
      Patron.printThroughput();// printing throughput


      System.out.println("------Waiting for Andre------");
      Andre.interrupt();   //tell Andre to close up
      Andre.join(); //wait till he has
      writer.close(); //all done, can close file
      writerWaiting.close();
      writerResponse.close();
      System.out.println("------Bar closed------");
   }

}
