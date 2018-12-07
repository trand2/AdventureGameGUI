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

public class Adventure extends Application {

    /**
     * intialize game board array.
     */
    static String[][] gameBoard;

    static Vector<Picture> pictureVector = new Vector<>();

    static BorderPane borderPane = new BorderPane();
    /**
     * max row initializer.
     */
    static int maxRows = 0;
    /**
     * max col initializer.
     */
    static int maxCols = 0;

    static int positionRow = 2;

    static int positionCol = 2;

    static int tileWidth = 0;

    static int tileHeight = 0;

    static String itemMapFile;

    static TextArea textArea = new TextArea();

    static GridPane gridPane = new GridPane();

    static boolean imageExists = false;

    public void start(Stage primaryStage) throws FileNotFoundException {
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
                readTxt(filepath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            print5x5Map();

            gridPane.setPadding(new Insets(20, 20, 20, 20));
            gridPane.setMaxSize(60,60);

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
            String inputs = text[0].toUpperCase();
            String direction = "";
            if (text.length > 1) {
                direction = text[1].toUpperCase();
            }

            command(inputs, direction, text[0], text[1]);
            textArea.appendText("\n");
        });


        toolBar.getItems().addAll(openBtn, saveBtn, quitBtn);
        pane.getChildren().addAll(borderPane);
        Scene scene = new Scene(pane, 600, 600);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    public static void readTxt(final String fileName) throws IOException {
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

        int counter = 0;
        while (counter <= 6) {
            Picture picture = new Picture();
            scanner.useDelimiter("(?<=;)");
            picture.symbol = scanner.next().replace(";", "").replace("\n", "");
            picture.type = scanner.next().replace(";", "").replace("\n", "");
            picture.image = scanner.next().replace(";", "").replace("\n", "");

            pictureVector.add(picture);
            ++counter;
        }
    }

    public static void command(final String inputs, final String direction, final String commandOriginal, final String directionOriginal) {
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
                            + gameBoard[positionRow]
                            [positionCol] + "\n");
                    print5x5Map();
                } else {
                    textArea.appendText("You can't go that far east."
                            + "\nYou are at location "
                            + getLocation() + " in terrain "
                            + gameBoard[positionRow]
                            [positionCol] + "\n");
                }
            } else {
                textArea.appendText("Invalid command: " + commandOriginal
                        + " " + directionOriginal
                        + "\nYou are at location "
                        + getLocation()
                        + " in terrain "
                        + gameBoard[positionRow]
                        [positionCol] + "\n");
            }
        } else if (inputs.startsWith("I")) {
            textArea.appendText("You are carrying:\nbrass lantern\nrope\n"
                    + "rations\nstaff\nYou are at location "
                    + getLocation() + " in terrain "
                    + gameBoard[positionRow]
                    [positionCol] + "\n");
        } else {
            textArea.appendText("Invalid command: " + commandOriginal
                    + " " + directionOriginal
                    + "\nYou are at location "
                    + getLocation()
                    + " in terrain "
                    + gameBoard[positionRow]
                    [positionCol] + "\n");
        }
    }

    public static void print5x5Map() {
        int count = 0;
        for (int row = positionRow - 4; row < positionRow + 1; row++) {
            for (int col = positionCol - 4; col < positionCol + 1; col++) {
                Image image = null;
                if (row == positionRow - 2 && col == positionCol - 2) {
                    try {
                        image = new Image(new FileInputStream(pictureVector.elementAt(6).image));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                } else if (row < 0 || col < 0) {
                    try {
                        image = new Image(new FileInputStream(pictureVector.elementAt(5).image));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else if (gameBoard[row][col].equals(pictureVector.elementAt(0).symbol)) {
                    try {
                        image = new Image(new FileInputStream(pictureVector.elementAt(0).image));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else if (gameBoard[row][col].equals(pictureVector.elementAt(1).symbol)) {
                    try {
                        image = new Image(new FileInputStream(pictureVector.elementAt(1).image));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else if (gameBoard[row][col].equals(pictureVector.elementAt(2).symbol)) {
                    try {
                        image = new Image(new FileInputStream(pictureVector.elementAt(2).image));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else if (gameBoard[row][col].equals(pictureVector.elementAt(3).symbol)) {
                    try {
                        image = new Image(new FileInputStream(pictureVector.elementAt(3).image));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else if (gameBoard[row][col].equals(pictureVector.elementAt(4).symbol)) {
                    try {
                        image = new Image(new FileInputStream(pictureVector.elementAt(4).image));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                else if (gameBoard[row][col].equals(pictureVector.elementAt(5).symbol)) {
                    try {
                        image = new Image(new FileInputStream(pictureVector.elementAt(5).image));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else if (gameBoard[row][col].equals(pictureVector.elementAt(6).symbol)) {
                    try {
                        image = new Image(new FileInputStream(pictureVector.elementAt(6).image));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                if (imageExists == true && count == 0) {
                    gridPane.getChildren().clear();
                    count++;
                }
                gridPane.add(new ImageView(image), col + 2, row + 2);
            }
        }
        imageExists = true;
        borderPane.getChildren().add(gridPane);
    }

    public static String getLocation() {
        int row = positionRow - 2 ;
        int col = positionCol - 2;
        String location = row + "," + col;
        return location;
    }



    public static void main(String[] args) {
        launch(args);
    }
}

class Picture {
    String symbol = "";
    String type = "";
    String image = "";
}