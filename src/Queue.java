

public class Queue<T> {
	
		
	//INSTANCE VARIABLES
	private int size;
	private Node<T> head;
	private Node<T> tail;
		
	//CONSTRUCTOR
	public Queue() {
		this.head = null;
		this.tail = null;
		size = 0;
	}
	
	//Method adds to the end of the Queue
	public void add(T data) {
		Node<T> newNode = new Node<T>(data);
		if (this.head == null) {
			this.head = newNode;
			this.tail = newNode;
			size++;
		} 
		else {
			this.tail.next = newNode;
			this.tail = newNode;
			size++;
		}	
	}
	
	//Method removes from beginning of the Queue
	public T poll() {
		if (isEmpty()) {
			return null;
		}
		else {
			Node<T> removedNode = this.head;
			this.head = this.head.next;
			if (this.head == null) {
				this.tail = null;
			}
			size--;
			return removedNode.data;
		}
	}
	
	
	//Method sees if anything is in Queue
	public boolean isEmpty() {
		return this.head == null;
	}
	
	
	//Method shows the Node in the beginning of the Queue
	public Node<T> peek() {
		return this.head;
	}
	
	public int getQsize() {
		return this.size;
	}
	
	
	
}



