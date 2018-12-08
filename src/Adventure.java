import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;

/**
 * Adventure class.
 */
public class Adventure extends Application {

    /**
     * intialize game board array.
     */
    private static String[][] gameBoard;

    /**
     * intialize picture vector.
     */
    private static Vector<Picture> pictureVector = new Vector<>();

    /**
     * intialize item vector.
     */
    private static Vector<Items> itemVector = new Vector<>();

    /**
     * intialize border pane.
     */
    private static BorderPane borderPane = new BorderPane();
    /**
     * max row initializer.
     */
    private static int maxRows = 0;
    /**
     * max col initializer.
     */
    private static int maxCols = 0;

    /**
     * position row initializer.
     */
    private static int positionRow = 2;

    /**
     * position col initializer.
     */
    private static int positionCol = 2;

    /**
     * item position row initializer.
     */
    private static int itemPositionRow = 2;

    /**
     * item position col initializer.
     */
    private static int itemPositionCol = 2;

    /**
     * tile width initializer.
     */
    private static int tileWidth = 0;

    /**
     * tile height initializer.
     */
    private static int tileHeight = 0;

    /**
     * item map file initializer.
     */
    private static String itemMapFile;

    /**
     * text area initializer.
     */
    private static TextArea textArea = new TextArea();

    /**
     * grid pane initializer.
     */
    private static GridPane gridPane = new GridPane();

    /**
     * image exists initializer.
     */
    private static boolean imageExists = false;

    /**
     * items carrying initializer.
     */
    private static Vector<String> itemsCarrying = new Vector<>();

    /**
     * inset value initializer.
     */
    private static final int INSET_VALUE = 20;

    /**
     * Start method.
     * @param primaryStage primaryStage
     */
    public void start(final Stage primaryStage) {
        Button openBtn = new Button("Open");
        Button saveBtn = new Button("Save");
        Button quitBtn = new Button("Quit");

        Pane pane = new Pane();

        ToolBar toolBar = new ToolBar();
        toolBar.setOrientation(Orientation.HORIZONTAL);
        borderPane.setTop(toolBar);
        TextField textField = new TextField();
        borderPane.setBottom(textField);
        borderPane.setRight(textArea);
        textArea.setDisable(true);

        openBtn.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(pane.getScene().getWindow());
            String filepath = file.getAbsolutePath();
            try {
                readMapFile(filepath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            print5x5Map();

            try {
                readItemFile(itemMapFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            textArea.appendText("Welcome to the Adventure Game!"
                    + "\nWhat would you like your "
                    + "character to do?\n\n");

            gridPane.setPadding(new Insets(INSET_VALUE,
                    INSET_VALUE, INSET_VALUE, INSET_VALUE));
            gridPane.setMaxSize(tileWidth, tileHeight);

            gridPane.setAlignment(Pos.CENTER);
            borderPane.setCenter(gridPane);
            borderPane.getChildren().add(gridPane);

        });

        quitBtn.setOnAction(event -> {
            Platform.exit();
        });

        textField.setOnAction(event -> {
            textArea.appendText("> " + textField.getText() + "\n");
            String[] text = textField.getText().split(" ");
            if (textField.getText().toUpperCase().startsWith("D")) {
                if (itemsCarrying.size() == 0) {
                textArea.appendText("You are carrying no items. "
                        + "Cannot drop.\n");
                } else {
                    for (int i = 0; i < itemsCarrying.size(); i++) {
                        String[] removeItem = textField.getText().split("a");
                        if (itemsCarrying.elementAt(i).equals(removeItem[1])) {
                            itemsCarrying.remove(i);
                        }
                    }
                }

            } else {
                String inputs = text[0].toUpperCase();
                String direction = "";
                if (text.length > 1) {
                    direction = text[1].toUpperCase();
                }

                command(inputs, direction);
            }
            textArea.appendText("\n");
        });

        textField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.UP) {
                command("GO", "NORTH");
            } else if (e.getCode() == KeyCode.DOWN) {
                command("GO", "SOUTH");
            } else if (e.getCode() == KeyCode.RIGHT) {
                command("GO", "EAST");
            } else if (e.getCode() == KeyCode.LEFT) {
                command("GO", "WEST");
            }

        });

        final int paneWidthAndHeight = 600;
        toolBar.getItems().addAll(openBtn, saveBtn, quitBtn);
        pane.getChildren().addAll(borderPane);
        Scene scene = new Scene(pane,
                paneWidthAndHeight, paneWidthAndHeight);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    /**
     * read map file function.
     * @param fileName fileName
     * @throws IOException IOException
     */
    public static void readMapFile(final String fileName) throws IOException {
        Scanner scanner = new Scanner(new File(fileName));
        maxRows = scanner.nextInt();
        maxCols = scanner.nextInt();
        gameBoard = new String[maxRows]
                [maxCols];
        for (int i = 0; i < maxRows; i++) {
            for (int j = 0; j < maxCols; ++j) {
                scanner.useDelimiter("\\n|(?<=.)");
                if (i <= maxRows && j <= maxCols) {
                    gameBoard[i][j] = scanner.next();
//                    System.out.print(gameBoard[i][j]);
                } else {
                    gameBoard[i][j] = "-";
//                    System.out.print(gameBoard[i][j]);
                }
            }
//            System.out.println();
        }

        scanner.nextLine();
        String tempValue = scanner.nextLine();

        String[] tempArray = tempValue.split(" ");

        tileHeight = Integer.parseInt(tempArray[0]);

        tileWidth = Integer.parseInt(tempArray[1]);

        itemMapFile = scanner.nextLine();

        final int counterLimit = 6;
        int counter = 0;
        while (counter <= counterLimit) {
            Picture picture = new Picture();
            scanner.useDelimiter("(?<=;)");
            picture.symbol = scanner.next().replace(";", "")
                    .replace("\n", "");
            picture.type = scanner.next().replace(";", "")
                    .replace("\n", "");
            picture.image = scanner.next().replace(";", "")
                    .replace("\n", "");

            pictureVector.add(picture);
            ++counter;
        }
    }

    /**
     * read item file function.
     * @param fileName fileName
     * @throws FileNotFoundException FileNotFoundException
     */
    public static void readItemFile(final String fileName)
            throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(fileName));
        final int itemLimit = 3;
        for (int i = 0; i < itemLimit; i++) {
            Items items = new Items();
            String[] text = scanner.nextLine().split(";");

            items.row = Integer.parseInt(text[0]);
            items.col = Integer.parseInt(text[1]);
            items.item = text[2];

            itemVector.add(items);
        }


    }

    /**
     * command function.
     * @param inputs Inputs
     * @param direction direction
     */
    public static void command(final String inputs, final String direction) {
        itemCheck();
        if (inputs.startsWith("G")) {
            if (direction.equals("NORTH") || direction.startsWith("N")) {
                if (positionRow > 2) {
                    positionRow--;
                    textArea.appendText("Moving north...\nYou are at location "
                            + getLocation() + " in terrain "
                            + gameBoard[positionRow]
                            [positionCol] + "\n");
                    print5x5Map();
                } else {
                    textArea.appendText("You can't go that far north."
                            + "\nYou are at location "
                            + getLocation() + " in terrain "
                            + gameBoard[positionRow]
                            [positionCol] + "\n");
                }
            } else if (direction.equals("SOUTH")
                    || direction.startsWith("S")) {
                if (positionRow < maxRows + 2) {
                    positionRow++;
                    textArea.appendText("Moving south...\nYou are at location "
                            + getLocation() + " in terrain "
                            + gameBoard[positionRow]
                            [positionCol] + "\n");
                    print5x5Map();
                } else {
                    textArea.appendText("You can't go that far south."
                            + "\nYou are at location "
                            + getLocation() + " in terrain "
                            + gameBoard[positionRow]
                            [positionCol] + "\n");
                }
            } else if (direction.equals("WEST")
                    || direction.startsWith("W")) {
                if (positionCol > 2) {
                    positionCol--;
                    textArea.appendText("Moving west...\nYou are at location "
                            + getLocation() + " in terrain "
                            + gameBoard[positionRow]
                            [positionCol] + "\n");
                    print5x5Map();
                } else {
                    textArea.appendText("You can't go that far west."
                            + "\nYou are at location "
                            + getLocation() + " in terrain "
                            + gameBoard[positionRow]
                            [positionCol] + "\n");
                }
            } else if (direction.equals("EAST") || direction.startsWith("E")) {
                if (positionCol < maxCols + 2) {
                    positionCol++;
                    textArea.appendText("Moving east...\nYou are at location "
                            + getLocation() + " in terrain "
                            + gameBoard[positionRow - 2][positionCol - 2]
                            + "\n");
                    print5x5Map();
                } else {
                    textArea.appendText("You can't go that far east."
                            + "\nYou are at location "
                            + getLocation() + " in terrain "
                            + gameBoard[positionRow]
                            [positionCol] + "\n");
                }
            } else {
                textArea.appendText("Invalid command: "
                        + "\nYou are at location "
                        + getLocation()
                        + " in terrain "
                        + gameBoard[positionRow]
                        [positionCol] + "\n");
            }
        } else if (inputs.startsWith("I")) {
            textArea.appendText("You are carrying: ");

            for (int i = 0; i < itemsCarrying.size(); i++) {
                textArea.appendText(itemsCarrying.elementAt(i) + ", ");
            }

            textArea.appendText("\nYou are at location "
            + getLocation() + " in terrain "
            + gameBoard[positionRow]
            [positionCol] + "\n");

        } else if (inputs.toUpperCase().startsWith("T")) {
            addItem();
        } else {
            textArea.appendText("Invalid command: "
                    + "\nYou are at location "
                    + getLocation()
                    + " in terrain "
                    + gameBoard[positionRow]
                    [positionCol] + "\n");
        }

    }

    /**
     * print 5 by 5 map function.
     */
    public static void print5x5Map() {
        final int three = 3;
        final int four = 4;
        final int five = 5;
        final int six = 6;
        int count = 0;
        for (int row = positionRow - four; row < positionRow + 1; row++) {
            for (int col = positionCol - four; col < positionCol + 1; col++) {
                Image image = null;
                if (row == positionRow - 2 && col == positionCol - 2) {
                    try {
                        image = new Image(new FileInputStream(
                                pictureVector.elementAt(six).image));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                } else if (row < 0 || col < 0) {
                    try {
                        image = new Image(new FileInputStream(
                                pictureVector.elementAt(five).image));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else if (gameBoard[row][col].equals(
                        pictureVector.elementAt(0).symbol)) {
                    try {
                        image = new Image(new FileInputStream(
                                pictureVector.elementAt(0).image));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else if (gameBoard[row][col].equals(
                        pictureVector.elementAt(1).symbol)) {
                    try {
                        image = new Image(new FileInputStream(
                                pictureVector.elementAt(1).image));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else if (gameBoard[row][col].equals(
                        pictureVector.elementAt(2).symbol)) {
                    try {
                        image = new Image(new FileInputStream(
                                pictureVector.elementAt(2).image));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else if (gameBoard[row][col].equals(
                        pictureVector.elementAt(three).symbol)) {
                    try {
                        image = new Image(new FileInputStream(
                                pictureVector.elementAt(three).image));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else if (gameBoard[row][col].equals(
                        pictureVector.elementAt(four).symbol)) {
                    try {
                        image = new Image(new FileInputStream(
                                pictureVector.elementAt(four).image));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else if (gameBoard[row][col].equals(
                        pictureVector.elementAt(five).symbol)) {
                    try {
                        image = new Image(new FileInputStream(
                                pictureVector.elementAt(five).image));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else if (gameBoard[row][col].equals(
                        pictureVector.elementAt(six).symbol)) {
                    try {
                        image = new Image(new FileInputStream(
                                pictureVector.elementAt(six).image));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                if (imageExists && count == 0) {
                    gridPane.getChildren().clear();
                    count++;
                }
                gridPane.add(new ImageView(image), col + 2, row + 2);
            }
        }
        imageExists = true;
        borderPane.getChildren().add(gridPane);
    }

    /**
     * get location function.
     * @return String
     */
    public static String getLocation() {
        int row = positionRow - 2;
        int col = positionCol - 2;
        String location = row + "," + col;
        return location;
    }

    /**
     * item in vector initializer.
     */
    private static int itemInVector = 0;

    /**
     * item check function.
     */
    public static void itemCheck() {

        String itemsAtLocation = "";
        for (int i = 0; i < itemVector.size(); i++) {
            if (positionRow + 1 == itemVector.elementAt(i)
                    .row && positionCol == itemVector.elementAt(i).col
                    || positionRow == itemVector.elementAt(i)
                    .row && positionCol + 1 == itemVector.elementAt(i).col) {
                itemsAtLocation += " " + itemVector.elementAt(i).item;
                itemInVector = i;
            }
        }
        if (!itemsAtLocation.equals("")) {
            textArea.appendText("Items Found: \n"
                    + itemsAtLocation + ". Would you like to take them?\n");
            if (textArea.getText().equals("Take")) {
                itemsCarrying.add(itemVector.elementAt(itemInVector).item);
            }
        }
    }

    /**
     * add item function.
     */
    public static void addItem() {
        if (positionRow == itemVector.elementAt(itemInVector).row
                && positionCol == itemVector.elementAt(itemInVector).col) {
            itemsCarrying.add(itemVector.elementAt(itemInVector).item);
        } else {
            textArea.appendText("\nNo item found here.\n");
        }
    }

    /**
     * main method.
     * @param args args
     */
    public static void main(final String[] args) {
        launch(args);
    }
}

/**
 * picture class.
 */
class Picture {
    /**
     * symbol initializer.
     */
    String symbol = "";

    /**
     * type initializer.
     */
    String type = "";

    /**
     * image initializer.
     */
    String image = "";
}

/**
 * items class.
 */
class Items {
    /**
     * row initializer.
     */
    int row = 0;

    /**
     * col initializer.
     */
    int col = 0;

    /**
     * item initializer.
     */
    String item = "";
}
