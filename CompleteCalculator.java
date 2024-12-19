import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;
import javax.swing.*;

public class CompleteCalculator {
    

    // Method to evaluate an infix expression
    public static String evaluateExpression(String expression) {
        if (!isValidParentheses(expression)) {
            return "Error: Mismatched parentheses";
        }        
        try {
            String postfix = infixToPostfix(expression);
            int result = evaluatePostfix(postfix);
            return "Postfix: " + postfix + "\nResult: " + result;
        } catch (Exception e) {
            return "Error: Invalid Expression";
        }
    }

    // Convert infix to postfix
    public static String infixToPostfix(String expression) {
        StringBuilder result = new StringBuilder();
        Stack<Character> stack = new Stack<>();
        StringBuilder numberBuffer = new StringBuilder();
    
        for (int i = 0; i < expression.length(); i++) {
            char current = expression.charAt(i);
    
            if (Character.isDigit(current)) {
                numberBuffer.append(current);
            } else {
                if (numberBuffer.length() > 0) {
                    result.append(numberBuffer).append(" ");
                    numberBuffer.setLength(0);
                }
    
                if (current == '(') {
                    stack.push(current);
                } else if (current == ')') {
                    while (!stack.isEmpty() && stack.peek() != '(') {
                        result.append(stack.pop()).append(" ");
                    }
                    stack.pop();
                } else {
                    while (!stack.isEmpty() && precedence(stack.peek()) >= precedence(current)) {
                        result.append(stack.pop()).append(" ");
                    }
                    stack.push(current);
                }
            }
        }
    
        if (numberBuffer.length() > 0) {
            result.append(numberBuffer).append(" ");
        }
    
        while (!stack.isEmpty()) {
            result.append(stack.pop()).append(" ");
        }
    
        return result.toString().trim();
    }
    
    public static int evaluatePostfix(String postfix) {
        Stack<Integer> stack = new Stack<>();
        String[] tokens = postfix.split(" ");
    
        for (String token : tokens) {
            if (token.matches("\\d+")) {
                stack.push(Integer.parseInt(token));
            } else {
                int b = stack.pop();
                int a = stack.pop();
                switch (token) {
                    case "+": stack.push(a + b); break;
                    case "-": stack.push(a - b); break;
                    case "*": stack.push(a * b); break;
                    case "/": stack.push(a / b); break;
                }
            }
        }
    
        return stack.pop();
    }
    public static boolean isValidParentheses(String expression) {
    Stack<Character> stack = new Stack<>();
    for (char c : expression.toCharArray()) {
        if (c == '(') {
            stack.push(c);
        } else if (c == ')') {
            if (stack.isEmpty()) return false;
            stack.pop();
        }
    }
    return stack.isEmpty();
}


    // Define precedence of operators
    public static int precedence(char operator) {
        switch (operator) {
            case '+': case '-': return 1;
            case '*': case '/': return 2;
            default: return -1;
        }
    }
    
    // Main GUI
    public static void main(String[] args) {
        JFrame frame = new JFrame("Complete Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 600);

        JTextField inputField = new JTextField();
        inputField.setFont(new Font("Arial", Font.PLAIN, 24));
        inputField.setHorizontalAlignment(SwingConstants.RIGHT);

        JTextArea outputArea = new JTextArea();
        outputArea.setFont(new Font("Arial", Font.PLAIN, 18));
        outputArea.setEditable(false);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 4, 10, 10));

        String[] buttons = {"7", "8", "9", "+",
                            "4", "5", "6", "-",
                            "1", "2", "3", "*",
                            "Undo", "0", "=", "/", "C"};
        Stack<String> history = new Stack<>();

        for (String text : buttons) {
            JButton button = new JButton(text);
            button.setFont(new Font("Arial", Font.BOLD, 20));
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String command = e.getActionCommand();
            
                    if (command.equals("=")) {
                        String expression = inputField.getText();
                        if (!expression.isEmpty()) {
                            history.push(expression); 
                        }
                        String result = evaluateExpression(expression);
                        outputArea.setText(result);
                    } else if (command.equals("C")) {
                        inputField.setText("");
                        outputArea.setText("");
                        history.clear(); 
                    } else if (command.equals("Undo")) {
                        if (!history.isEmpty()) {
                            inputField.setText(history.pop()); 
                        }
                    } else {
                        if (!command.equals("Undo") && !command.equals("=")) {
                            history.push(inputField.getText()); 
                        }
                        inputField.setText(inputField.getText() + command);
                    }
                }
            });
            buttonPanel.add(button);
        }
        frame.setLayout(new BorderLayout());
        frame.add(inputField, BorderLayout.NORTH);
        frame.add(new JScrollPane(outputArea), BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    
}
}