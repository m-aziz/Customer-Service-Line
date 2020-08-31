
public class Node<T> {

	//VARIABLES
	T data;
	Node<T> next;
	
	//CONSTRUCTORS
	public Node(T data, Node<T> next){
		this.setData(data);
		this.next = next;
	}
	
	public Node(T data){
		this.setData(data);
		this.next = null;
	}
	
	public Node(){
		this.setData(null);
		this.next = null;
	}
	
	//GETTERS
	public T getData() {
		return data;
	}
	public Node<T> getNext() {
		return next;
	}
	
	//SETTERS
	public void setData(T data) {
		this.data = data;
	}
	public void setNext(Node<T> next) {
		this.next = next;
	}
	
	
	
	
	

	
}
