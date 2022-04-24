import java.util.*;

interface IStack {
   public Object pop();
   public Object peek();
   public void push(Object element);
   public boolean isEmpty();
   public int size();
}


public class StackImplementation implements IStack {
    private static List<Integer> values = new ArrayList<>();

    public void push(Object element){
        values.add(0, (int) element);
    }

    public Object peek(){
        if (values.size() == 0) {
            System.out.println("Error");
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
            System.out.println("Error");
            return "";
        }
        Object pop_value = values.get(0);
        values.remove(0);
        return pop_value;
    }


    public static void main(String[] args) {
        StackImplementation Stack = new StackImplementation();
        // Read Input From User
        Scanner in = new Scanner(System.in);
        String user_input_list = in.nextLine();
        String user_input_action = in.nextLine();
        int user_input_value = 0;
        if (user_input_action.equals("push")) {
            user_input_value = in.nextInt();
        }

        // Processing
        if (!user_input_list.equals("[]")){
            String[] temp_array = user_input_list.replace("[", "").replace("]", "").split(", ");
            for (String val: temp_array){
                values.add(Integer.parseInt(val));
            }
        }

        // Functions
        switch(user_input_action){
            case "push":
                Stack.push(user_input_value);
                System.out.println(values);
                break;
            case "pop":
                if (Stack.size() == 0){
                    System.out.println("Error");
                    break;
                }
                Stack.pop();
                System.out.println(values);
                break;
            case "peek":
                if (Stack.size() == 0){
                    System.out.println("Error");
                    break;
                }
                Object value = Stack.peek();
                System.out.println(value);
                break;
            case "isEmpty":
                if (Stack.size() == 0) 
                    System.out.println("True");
                else
                    System.out.println("False");
                break;
            case "size":
                System.out.println(Stack.size());
        }
        in.close();
    }
}