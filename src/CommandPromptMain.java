import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class CommandPromptMain {
	
	//GLOBAL VARIABLES***********************************************************************
	
	static final int WORKDAYSECONDS = 28800;//total time of the work day in seconds (9-5)
	static final int CLOSINGTIME = 61200; //time in seconds of 5pm (0 seconds being 12:00)
	static int currentTime = 32400; //current time in seconds with 0 being 9:00:00 am
	
	static int customersServed = 0; //counts total number of customers
	static int customersInLine = 0; //counts number of people in the line
	static int maxLineCount = 0; //tracks the longest number that the line has gotten
	
	static int idleTime = 0; //idle time of employee
	static int breakTime = 0; //break time of employee
	static int maxBreakTime = 0; // represents longest break time
	
	static boolean idling = true; //flag to see if employee is serving someone
	static int serviceTimeStatus; //time the employee has spent serving a customer
	static int serviceTime; //time it takes to serve each customer 
	
	static ArrayList<Customer> allCustomers = new ArrayList<Customer>();// this list holds all the customers from the text file
	static Queue<Customer> q = new Queue<Customer>(); //represent customer q
	static ArrayList<Customer> servedCustomers = new ArrayList<Customer>();// this list holds all the customers that have left Q
	static ArrayList<Customer> unservedCustomers = new ArrayList<Customer>();// this list holds all the customers that were never served
	
	//STATIC METHODS**************************************************************************
	
	//This method performs all operations when a customer arrives in the Q
	public static void addToQ(Customer newCustomer) {
		q.add(newCustomer);
		//allCustomers.remove(newCustomer);
		customersInLine++;
		if (customersInLine > maxLineCount) {
			maxLineCount = customersInLine;
		}
	}
	
	//This method performs all operations when a customer is ready to be served
	public static void takeFromQ() {
		//Poll the customer
		Customer polledCustomer = q.poll(); //removes customer from poll
		polledCustomer.addWaitingTime(currentTime - polledCustomer.getTimeEntered()); //final update of waiting time
		servedCustomers.add(polledCustomer);
		//Employee Status
		serviceTimeStatus = serviceTime;
		idling = false;
		customersInLine--;
		if (breakTime > maxBreakTime) {
			maxBreakTime = breakTime;
		}
		breakTime = 0;
	}
	
	
	//This method performs all operations when a customer done being served
	public static void serveFinished() {
		customersServed++;
		idling = true;
	}

	
	
	
	//MAIN PROGRAM*******************************************************************************

	public static void main(String[] args) {
		
		//-------------------CUSTOMER FILE READER-------------------
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(args[0]));
			String line = reader.readLine();
			int lineNum = 1;
			while (line != null) {
				//CHECK  System.out.println("\t"+line); //PRINT1
				if (lineNum == 1) {
					serviceTime = Integer.parseInt(line);// fills in service time
					line = reader.readLine();
					lineNum++;
					continue;
				}
				else if (lineNum % 3 == 0){
					//Gathers customers id number
					String[] idNumArray = line.replaceAll("\\s", "").split(":");
					int idNum = Integer.parseInt(idNumArray[1]);
					//Immediately captures next line which is arrival time
					line = reader.readLine();
					String[] arrivalTimeArray = line.replaceAll("\\s", "").split(":");//CHECK System.out.println("hi" + Arrays.toString(arrivalTimeArray));
					int hour = Integer.parseInt(arrivalTimeArray[1]);
					if (hour < 7) { //if c comes before 7pm, 12 hours are added 
						hour += 12; 
					}
					int min = Integer.parseInt(arrivalTimeArray[2]);
					int sec = Integer.parseInt(arrivalTimeArray[3]);
					int arrivalTime = (hour * 3600) + (min * 60) + sec; //arrival time in seconds
					
					//creates and adds customer to list
					allCustomers.add(new Customer(arrivalTime,idNum)); 
					line = reader.readLine();
					lineNum += 2;
					continue;
				}
				line = reader.readLine();
				lineNum++;
				
			}
		} catch (IOException e) { System.out.println("File Not Found"); }
		
		/*This is to check if all customers have been made and added to the arrayList
		 for(int i=0; i < allCustomers.size(); i++){
         System.out.println(allCustomers.get(i).toString()); }*/
         
		
		//*******************WORK_DAY MODEL BEGINS HERE****************************
		
		//keeps track of the allCustomers and their position
		int var = 0; 
	
		//-------While Loop For Second by Second Operations of Work Day---------
		while (currentTime < CLOSINGTIME) {
			
			//goes through allCustomers and checks if its their time to enter
			while (var < allCustomers.size() && (allCustomers.get(var).getTimeEntered() <= currentTime)) {
					addToQ(allCustomers.get(var));
					var++;
			}
			
			if (q.isEmpty()) { // If Q is empty
				// 1) If Employee is free
				if (idling) { 
					idleTime++;
					breakTime++;
				}
				// 2) Employee continues work with current customer
				else serviceTimeStatus--; 
			}
			
			else {// Q has customers
				// 1) Checks if Employee is free, so they can take a customer
				if (idling) { 
					takeFromQ();
				}
				// 2) Employee continues to work with current customer
				else {
					serviceTimeStatus--;
				}
			}
			if (!idling && serviceTimeStatus == 0 ) {//Employee done with current person
				serveFinished();
				if (!q.isEmpty()) {//if another person waiting, q immediately
					takeFromQ();
				} else {
					idleTime++;//no one waiting
					breakTime++;
				}
			}
			currentTime++;
		}
		
		//----------------End of the Work day operations------------
		
		//if there is customer still being served (NOTE has already been added to servedCustomersList)
		if (!idling) {
			serveFinished();
		}
		//Dismisses people in the Q
		while (!q.isEmpty()) {
			Customer unservedCustomer = q.poll();
			unservedCustomer.addWaitingTime(currentTime-unservedCustomer.getTimeEntered());
			unservedCustomers.add(unservedCustomer);
			customersInLine--;
		}
		//Dismisses people that arrived after 5pm 
		for (int i = var; i < allCustomers.size(); i++) {
			unservedCustomers.add(allCustomers.get(i));
		}
		
		//FILE WRITER****************************************************
		try {
			FileWriter fw = new FileWriter(args[1]);
			reader = new BufferedReader(new FileReader("queriesfile.txt"));
			String line = reader.readLine();
			while (line != null) {
				//Reads all the queries line by line
				if (line.contentEquals("NUMBER-OF-CUSTOMERS-SERVED")){
					fw.write(line + ":" + customersServed + "\n");
				}
				else if (line.contentEquals("LONGEST-BREAK-LENGTH")){
					fw.write(line + ":" + maxBreakTime + "\n");
				}
				else if (line.contentEquals("TOTAL-IDLE-TIME")) {
					fw.write(line + ":" + idleTime+ "\n");
				}
				else if (line.contentEquals("MAXIMUM-NUMBER-OF-PEOPLE-IN-QUEUE-AT-ANY-TIME")) {
					fw.write(line + ":" + maxLineCount+ "\n");
				}
				else {
					String[] waiting = line.split(" ");
					int idNumber = Integer.parseInt(waiting[1]);
					int customerWait = 0; //default for unserved customers
					for (int i = 0; i < servedCustomers.size(); i++) {
						if (servedCustomers.get(i).getCustomerNum() == idNumber) {
							customerWait = servedCustomers.get(i).getWaitingTime();
						}
					}
					fw.write(line + ":" + customerWait+ "\n");
				}
				line = reader.readLine();
			}
			fw.close();
		} catch (IOException e) { System.out.println("File Not Found"); }
		
		//PRINTS VARIABLES FOR TESTING PURPOSES
		System.out.println("currentTime: "+currentTime);
		
		System.out.println("customersServed: " + customersServed);
		
		System.out.println("customersInLine: " + customersInLine);
		System.out.println("maxLineCount: " + maxLineCount);
		
		System.out.println("idleTime: " + idleTime);
		System.out.println("breakTime: " + breakTime);
		System.out.println("maxBreakTime: " + maxBreakTime);
		System.out.println();
		System.out.println("Served:");
		for (int i = 0; i<servedCustomers.size(); i++) {
			System.out.println(servedCustomers.get(i).toString());
		}
		System.out.println();
		System.out.println("Unserved:");
		for (int i = 0; i<unservedCustomers.size(); i++) {
			System.out.println(unservedCustomers.get(i).toString());
		}
		
		
		
		
	}
}


