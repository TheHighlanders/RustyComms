package org.usfirst.frc.team6201.robot.dataLogger;

public class Queue <T> {
	private Node rear;
	private Node front;

	private class Node {
		Node nextNode;
		T data;
		
		Node (T message) {
			data = message;
		}
	}

	public Queue () {
		rear = new Node(null);
		rear.nextNode = null;
		front = rear;
	}
	
	/**
	 * adds an object to the end of the queue
	 * @param s the Object to add to the queue
	 */
	public void add (T s) {
		Node tmp = new Node (s);
		tmp.nextNode  = rear;
		rear = tmp;
	}
	
	/**
	 * @return true if the queue is empty, false if not.
	 */
	public boolean isEmpty (){
		return (front == rear);
	}
	
	/**
	 * returns the front of the queue.
	 * @return if the queue is empty, returns null else returns the front element of the queue.
	 */
	public T removeFrontElement () {
		if (isEmpty()){
			return null;
		}
		else {
			Node tmp = front;
			front = front.nextNode;
			return tmp.data;
		}
	}

}
