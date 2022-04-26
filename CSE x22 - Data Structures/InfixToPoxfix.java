import java.util.*;
import java.lang.Math;

interface IExpressionEvaluator {
    public String infixToPostfix(String expression);
    public int evaluate(String expression, int a, int b, int c);
}

interface IStack {
    public Object pop();
    public Object peek();
    public void push(Object element);
    public boolean isEmpty();
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


public class Evaluator implements IExpressionEvaluator {

    int getPriority(char c){
        if (c == '-' || c == '+') 
            return 1;
        else if (c == '*' || c == '/')
            return 2;
        else if (c == '^')
            return 3;
        else
            return 0;
    }

    public String infixToPostfix(String expression){
        String postfix = "";
        StackImplementation Stack = new StackImplementation();
        
        for (int i=0; i<expression.length(); i++){
            char CHARACTER = expression.charAt(i);
            int ASCIvalue = (int)CHARACTER;
            
            if (CHARACTER == ' '){
                continue;

            } else if (Character.isDigit(CHARACTER) || Character.isAlphabetic(CHARACTER)){
                postfix = postfix.concat(String.valueOf(CHARACTER));

            } else if (CHARACTER == '('){
                Stack.push(ASCIvalue);
            
            } else if (CHARACTER == ')'){
                while (!Stack.isEmpty()){
                    int val = (int)Stack.pop();
                    if (val == 40) break;
                    postfix = postfix.concat(String.valueOf((char)val));
                }
            }

            else{
                while (!Stack.isEmpty() && (getPriority(CHARACTER) <= getPriority((char)(int)Stack.peek()))){
                    postfix = postfix.concat(String.valueOf((char)(int)Stack.pop()));
                }

                Stack.push(ASCIvalue);
            }
        }
        while (!Stack.isEmpty()){
            int value = (int)Stack.pop();
            if (value != 40){
                postfix = postfix.concat(String.valueOf((char)value));
            }
        }
        return postfix;
    }

    public int evaluate(String expression, int a, int b, int c){
        StackImplementation Stack = new StackImplementation();

        for (int i=0; i<expression.length(); i++){
            char CHARACTER = expression.charAt(i);

            if (Character.isDigit(CHARACTER)){
                Stack.push(CHARACTER - '0');
            } else if (Character.isAlphabetic(CHARACTER)){
                if (CHARACTER == 'a'){
                    Stack.push(a);
                } else if (CHARACTER == 'b'){
                    Stack.push(b);
                } else if (CHARACTER == 'c'){
                    Stack.push(c);
                }
            } else {
                int operand_2 = (int)Stack.pop();
                int operand_1 = 0;
                if (!Stack.isEmpty()){
                    operand_1 = (int)Stack.pop();
                }
                int result = 0;
                if (CHARACTER == '+'){
                    result = operand_1 + operand_2;
                } else if (CHARACTER == '-'){
                    result = operand_1 - operand_2;
                } else if (CHARACTER == '*'){
                    result = operand_1 * operand_2;
                } else if (CHARACTER == '/'){
                    result = operand_1 / operand_2;
                } else if (CHARACTER == '^'){
                    result = (int) Math.pow(operand_1, operand_2);
                }
                Stack.push(result);
            }
        }

        return (int)Stack.pop();
    }

    public static void main(String[] args) throws IOException {
        Evaluator elevator = new Evaluator();

        // Parse User Input
        Scanner in = new Scanner(System.in);
        String user_input_infix = in.nextLine();
        int a = Integer.parseInt(in.nextLine().split("=")[1]);
        int b = Integer.parseInt(in.nextLine().split("=")[1]);
        int c = Integer.parseInt(in.nextLine().split("=")[1]);

        if (user_input_infix.charAt(user_input_infix.length()-1) == '+' || user_input_infix.charAt(user_input_infix.length()-1) == '-'  || user_input_infix.charAt(user_input_infix.length()-1) == '*' 
            || user_input_infix.charAt(user_input_infix.length()-1) == '/' || user_input_infix.charAt(user_input_infix.length()-1) == '^' || user_input_infix.charAt(0) == '*' 
            || user_input_infix.charAt(0) == '/' || user_input_infix.charAt(0) == '^'){
            System.out.println("Error");
            System.exit(0);
        }

        while (user_input_infix.contains("--")){
            user_input_infix = user_input_infix.replace("/--", "/");
            user_input_infix = user_input_infix.replace("*--", "*");
            user_input_infix = user_input_infix.replace("^--", "^");
            user_input_infix = user_input_infix.replace(")--", ")");
            user_input_infix = user_input_infix.replace("--", "+");
        }
        while (user_input_infix.charAt(0) == '+'){
            user_input_infix = user_input_infix.substring(1);
        }

        int OPEN = 0;
        int CLOSE = 0;
        for (int i=0; i<user_input_infix.length(); i++){
            if (user_input_infix.charAt(i) == ')'){
                OPEN += 1;
            } else if (user_input_infix.charAt(i) == '('){
                CLOSE += 1;
            }
        }


        if (user_input_infix.contains("**") || user_input_infix.contains("a(") || user_input_infix.contains("b(") || user_input_infix.contains("c(") || user_input_infix.contains(")a")
         || user_input_infix.contains(")b") || user_input_infix.contains(")c") || (OPEN != CLOSE) || user_input_infix.contains("*-") || user_input_infix.contains("^+")){
            System.out.println("Error");
            System.exit(0);
         }
        
        // Call Functions
        String postfix = elevator.infixToPostfix(user_input_infix);
        int result = elevator.evaluate(postfix, a, b, c);
        // Print results
        System.out.println(postfix);
        System.out.println(result);
        in.close();
    }
}