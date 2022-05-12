import java.util.*;
import java.io.FileNotFoundException;

interface IMazeSolver{
    /* Find path with BFS (Breadth First Search) algorithm */
    public int[][] solveBFS(java.io.File maze);
    /* Find path with DFS (Depth First Search) algorithm */
    public int[][] solveDFS(java.io.File maze);
}

interface IStack {
    /* Removes an item from Stack and returns the item as Object */
    public Object pop();
    /* Retrieves the first item in Stack as Object */
    public Object peek();
    /* Inserts a new item to the stack */
    public void push(Object element);
    /* Checks if the Stack is empty */
    public boolean isEmpty();
    /* Returns the number of items in a Stack */
    public int size();
}

interface IQueue {
	/* Inserts an item at the queue front.*/
    public void enqueue(Object item);
    /* Removes the object at the queue rear and returnsit.*/
    public Object dequeue();
    /* Tests if this queue is empty.*/
    public boolean isEmpty();
    /* Returns the number of elements in the queue*/
    public int size();
}
 
class StackImplementation implements IStack {
    private static List<Integer> values = new ArrayList<>();

    public void push(Object element){
        values.add(0, (int) element);
    }

    public Object peek(){
        if (values.size() == 0) {
            return "";
        }
        return values.get(0);
    }

    public boolean isEmpty(){
        if (values.size() == 0){
            return true;
        } else {
            return false;
        }
    }
    
    public int size(){
        return values.size();
    }

    public Object pop(){
        if (values.size() == 0) {
            return "";
        }
        Object pop_value = values.get(0);
        values.remove(0);
        return pop_value;
    }
}

class ArrayQueue implements IQueue {
	public static class node{
        Object data;
        public node(Object elem){
            data = elem;
        }
    }
	public int[] array= new int[500];
	public int front = -1;
	public int rear = -1;
	ArrayQueue(){
        front = -1;
        rear = -1;
        array = new int[500];
    }
    public void enqueue(Object item){ 
        if(rear == 499){
             System.out.println("Error");
        }
        else if(front == -1 && rear == -1){
        	front = rear = 0;
        }else {
        	rear++;
        }
        array[rear] = (int) item;
    }
    public Object dequeue(){
    	int temp;
        if (front == -1 && rear == -1) {
        	System.out.println("Error");
        	return null;
        }else if(front == rear) {
        	temp = array[rear];
        	front = rear = -1;
        }else {
        	temp = array[front];
        	front ++;
        }
        return (Object)temp;
    }
    public boolean isEmpty(){
        return (front == -1 && rear == -1) ;
    }
    public int size(){
        if(front == -1 && rear == -1) {
        	return 0;
        }else {
        	return (rear - front + 1);
        }
    }
}


public class MazeSolver implements IMazeSolver {
    
    public int[][] solveBFS(java.io.File maze){
        /* Parsing Text File */
        try{
            Scanner myReader = new Scanner(maze);
            
            // PARSE THE TEXT FILE HERE

            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("!!!!FILE NOT FOUND!!!!!");
            e.printStackTrace();
        }

        int[][] something_until_later = new int[2][2];
        return something_until_later;
    }

    public int[][] solveDFS(java.io.File maze){
        /* Parsing Text File */
        try{
            Scanner myReader = new Scanner(maze);
            
            // PARSE THE TEXT FILE HERE

            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("!!!!FILE NOT FOUND!!!!!");
            e.printStackTrace();
        }
        
        int[][] something_until_later = new int[2][2];
        return something_until_later;
    }

    public static void main(String[] args) {
        java.io.File myFile = new java.io.File("map.txt");
    }
}