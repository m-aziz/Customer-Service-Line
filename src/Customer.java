
public class Customer {

	private int timeEntered; //data field that holds for when this customer enters in seconds
	private int customerNum;//data field that holds the number indicator for each customer
	private int waitingTime;//data field that holds how long customer waited
	
	//CONSTRUCTOR
	public Customer(int timeEntered, int customerNum) {
		this.timeEntered = timeEntered;
		this.customerNum = customerNum;
		this.waitingTime = 0;
		
	}
	
	//TOSTRING
	public String toString() {
		return "ID:" + customerNum + ", Entered:" + timeEntered + ", Wait:" + waitingTime;
	}

	//GETTERS
	public int getTimeEntered() {
		return timeEntered;
	}
	public int getCustomerNum() {
		return customerNum;
	}
	public int getWaitingTime() {
		return waitingTime;
	}

	//SETTERS
	public void setTimeEntered(int timeEntered) {
		this.timeEntered = timeEntered;
	}
	public void setCustomerNum(int customerNum) {
		this.customerNum = customerNum;
	}
	public void setWaitingTime(int waitingTime) {
		this.waitingTime = waitingTime;
	}
	public void addWaitingTime(int waitingTime) {
		this.waitingTime += waitingTime;
	}
	
	
	
	
	
}
