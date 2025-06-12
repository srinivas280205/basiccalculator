import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SimpleCalculatorFX extends Application {

    private TextField display;
    private String currentNumber = ""; // Stores the number being typed
    private double firstOperand = 0;   // Stores the first number for calculation
    private String operator = "";      // Stores the selected operator (+, -, *, /)
    private boolean startNewNumber = true; // True if the next digit starts a new number

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Simple Calculator");

        // Main layout container (vertical box)
        VBox root = new VBox(10);
        root.setPadding(new Insets(15));
        root.setAlignment(Pos.TOP_CENTER);

        // Display field for numbers and results
        display = new TextField("0");
        display.setPrefHeight(50);
        display.setEditable(false); // User can't type directly
        display.setAlignment(Pos.CENTER_RIGHT);
        display.setStyle("-fx-font-size: 20px; -fx-background-color: #f0f0f0;");

        // Grid for calculator buttons
        GridPane buttonGrid = new GridPane();
        buttonGrid.setHgap(10); // Horizontal gap between buttons
        buttonGrid.setVgap(10); // Vertical gap between buttons
        buttonGrid.setAlignment(Pos.CENTER);

        // Define button labels for the grid
        String[][] buttonLabels = {
                {"C", "/", "*", "-"}, // Clear, Divide, Multiply, Subtract
                {"7", "8", "9", "+"}, // Numbers and Add
                {"4", "5", "6", ""},  // Numbers (empty space for layout)
                {"1", "2", "3", ""},  // Numbers (empty space for layout)
                {"0", ".", "="}       // Zero, Decimal, Equals
        };

        // Create buttons and add them to the grid
        int rowIdx = 0;
        for (String[] row : buttonLabels) {
            int colIdx = 0;
            for (String label : row) {
                if (label.isEmpty()) continue; // Skip empty labels for layout

                Button button = new Button(label);
                button.setPrefSize(60, 50); // Set button size
                button.setStyle("-fx-font-size: 16px;");

                // Special styling for operators and equals button
                if ("+-*/=".contains(label)) {
                    button.setStyle("-fx-font-size: 16px; -fx-background-color: #FFC107;"); // Orange for operators
                } else if ("C".equals(label)) {
                    button.setStyle("-fx-font-size: 16px; -fx-background-color: #F44336; -fx-text-fill: white;"); // Red for Clear
                } else if ("=".equals(label)) {
                    button.setStyle("-fx-font-size: 16px; -fx-background-color: #4CAF50; -fx-text-fill: white;"); // Green for Equals
                    // Make the equals button span two rows if needed for layout
                    GridPane.setRowSpan(button, 2);
                }


                // Set what happens when a button is clicked
                button.setOnAction(e -> handleButtonClick(label));
                buttonGrid.add(button, colIdx, rowIdx);
                colIdx++;
            }
            rowIdx++;
        }

        // Add the special buttons for multiplication and subtraction to occupy the "empty" spaces
        // Adjust column and row spans as necessary
        // This part needs careful placement based on the desired layout
        // For example, if you want operators to fill the last column:
        Button plusButton = (Button) buttonGrid.getChildren().get(7); // Assuming '7' is the index of '+' button
        GridPane.setRowSpan(plusButton, 2); // Make plus span two rows

        // Add display and button grid to the main layout
        root.getChildren().addAll(display, buttonGrid);

        // Set up the scene and show the window
        Scene scene = new Scene(root, 280, 380); // Width, Height
        primaryStage.setScene(scene);
        primaryStage.setResizable(false); // Don't allow resizing
        primaryStage.show();
    }

    // Handles logic when any button is clicked
    private void handleButtonClick(String label) {
        if ("0123456789.".contains(label)) { // If a digit or decimal is pressed
            if (startNewNumber) {
                currentNumber = label; // Start new number
                startNewNumber = false;
            } else if (".".equals(label) && currentNumber.contains(".")) {
                // Do nothing if decimal already exists
            } else {
                currentNumber += label; // Append to current number
            }
            display.setText(currentNumber);
        } else if ("+-*/".contains(label)) { // If an operator is pressed
            firstOperand = Double.parseDouble(currentNumber); // Save current number as first operand
            operator = label; // Save the operator
            startNewNumber = true; // Next digit will start a new number
        } else if ("=".equals(label)) { // If equals is pressed
            if (operator.isEmpty()) return; // Nothing to calculate yet

            double secondOperand = Double.parseDouble(currentNumber);
            double result = 0;

            switch (operator) {
                case "+":
                    result = firstOperand + secondOperand;
                    break;
                case "-":
                    result = firstOperand - secondOperand;
                    break;
                case "*":
                    result = firstOperand * secondOperand;
                    break;
                case "/":
                    if (secondOperand == 0) {
                        display.setText("Error: Div by 0");
                        resetCalculator();
                        return;
                    }
                    result = firstOperand / secondOperand;
                    break;
            }
            display.setText(String.valueOf(result)); // Show result
            currentNumber = String.valueOf(result); // Result becomes new current number
            operator = ""; // Clear operator
            startNewNumber = true; // Next digit starts new
        } else if ("C".equals(label)) { // If Clear button is pressed
            resetCalculator();
        }
    }

    // Resets the calculator to its initial state
    private void resetCalculator() {
        currentNumber = "";
        firstOperand = 0;
        operator = "";
        startNewNumber = true;
        display.setText("0");
    }

    public static void main(String[] args) {
        launch(args); // Starts the JavaFX application
    }
}
