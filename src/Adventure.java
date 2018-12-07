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

    BorderPane borderPane = new BorderPane();
    /**
     * max row initializer.
     */
    static int maxRows = 0;
    /**
     * max col initializer.
     */
    static int maxCols = 0;

    static int positionRow = 1;

    static int positionCol = 1;

    static int tileWidth = 0;

    static int tileHeight = 0;

    static String itemMapFile;

    static TextArea textArea = new TextArea();

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

            GridPane gridPane = new GridPane();
            gridPane.setPadding(new Insets(20, 20, 20, 20));
            gridPane.setMaxSize(60,60);
            for (int row = 0; row < maxRows + 2; row++) {
                ImageView imageView;
                for (int col = 0; col < maxCols + 2; col++) {
                    Image image = null;
                    if (row == 1 && col == 1) {
                        try {
                            image = new Image(new FileInputStream(pictureVector.elementAt(6).image));
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
                    gridPane.add(new ImageView(image), col, row);
                }
            }
            gridPane.setAlignment(Pos.CENTER);
            borderPane.setCenter(gridPane);
        });

        quitBtn.setOnAction(event -> {
            Platform.exit();
        });

        textField.setOnAction(event -> {
            textArea.appendText("> " + textField.getText() + "\n");
            String[] text = textField.getText().split(" ");
            String inputs = text[0].toUpperCase();
            String direction = "";
            if (text.length > 0) {
                direction = text[1].toUpperCase();
            }

            command(inputs, direction, text[0], text[1]);
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
        gameBoard = new String[Adventure.maxRows + 2]
                [Adventure.maxCols + 2];
        for (int i = 0; i < maxRows + 2; i++) {
            for (int j = 0; j < maxCols + 2; ++j) {
                scanner.useDelimiter("\\n|(?<=.)");
                if (i >= 1 && j >= 1 && i <= maxRows
                        && j <= maxCols) {
                    gameBoard[i][j] = scanner.next();
//                    System.out.print(Adventure.gameBoard[i][j]);
                } else {
                    gameBoard[i][j] = "-";
//                    System.out.print(Adventure.gameBoard[i][j]);
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
                if (Adventure.positionRow > 1) {
                    Adventure.positionRow--;
                    textArea.appendText("Moving north...\nYou are at location "
                            + getLocation() + " in terrain "
                            + Adventure.gameBoard[Adventure.positionRow]
                            [Adventure.positionCol] + "\n");
//                    print3by3map();
                } else {
                    textArea.appendText("You can't go that far north."
                            + "\nYou are at location "
                            + getLocation() + " in terrain "
                            + Adventure.gameBoard[Adventure.positionRow]
                            [Adventure.positionCol] + "\n");
                }
            } else if (direction.equals("SOUTH")
                    || direction.startsWith("S")) {
                if (Adventure.positionRow < Adventure.maxRows) {
                    Adventure.positionRow++;
                    textArea.appendText("Moving south...\nYou are at location "
                            + getLocation() + " in terrain "
                            + Adventure.gameBoard[Adventure.positionRow]
                            [Adventure.positionCol] + "\n");
//                    print3by3map();
                } else {
                    textArea.appendText("You can't go that far south."
                            + "\nYou are at location "
                            + getLocation() + " in terrain "
                            + Adventure.gameBoard[Adventure.positionRow]
                            [Adventure.positionCol] + "\n");
                }
            } else if (direction.equals("WEST")
                    || direction.startsWith("W")) {
                if (Adventure.positionCol > 1) {
                    Adventure.positionCol--;
                    textArea.appendText("Moving west...\nYou are at location "
                            + getLocation() + " in terrain "
                            + Adventure.gameBoard[Adventure.positionRow]
                            [Adventure.positionCol] + "\n");
//                    print3by3map();
                } else {
                    textArea.appendText("You can't go that far west."
                            + "\nYou are at location "
                            + getLocation() + " in terrain "
                            + Adventure.gameBoard[Adventure.positionRow]
                            [Adventure.positionCol] + "\n");
                }
            } else if (direction.equals("EAST") || direction.startsWith("E")) {
                if (Adventure.positionCol < Adventure.maxCols) {
                    Adventure.positionCol++;
                    textArea.appendText("Moving east...\nYou are at location "
                            + getLocation() + " in terrain "
                            + Adventure.gameBoard[Adventure.positionRow]
                            [Adventure.positionCol] + "\n");
//                    print3by3map();
                } else {
                    textArea.appendText("You can't go that far east."
                            + "\nYou are at location "
                            + getLocation() + " in terrain "
                            + Adventure.gameBoard[Adventure.positionRow]
                            [Adventure.positionCol] + "\n");
                }
            } else {
                textArea.appendText("Invalid command: " + commandOriginal
                        + " " + directionOriginal
                        + "\nYou are at location "
                        + getLocation()
                        + " in terrain "
                        + Adventure.gameBoard[Adventure.positionRow]
                        [Adventure.positionCol] + "\n");
            }
        } else if (inputs.startsWith("I")) {
            textArea.appendText("You are carrying:\nbrass lantern\nrope\n"
                    + "rations\nstaff\nYou are at location "
                    + getLocation() + " in terrain "
                    + Adventure.gameBoard[Adventure.positionRow]
                    [Adventure.positionCol] + "\n");
        } else {
            textArea.appendText("Invalid command: " + commandOriginal
                    + " " + directionOriginal
                    + "\nYou are at location "
                    + getLocation()
                    + " in terrain "
                    + Adventure.gameBoard[Adventure.positionRow]
                    [Adventure.positionCol] + "\n");
        }
    }

    public static String getLocation() {
        int row = Adventure.positionRow - 1;
        int col = Adventure.positionCol - 1;
        String location = row + "," + col;
        return location;
    }



    public static void main(String[] args) {
//        Scanner input = new Scanner(System.in);
//        Scanner input1 = new Scanner(System.in);
//        String command = " ";
//        String fileName = " ";
//
//        if (args.length > 0) {
//            fileName = args[0];
//        } else {
//            fileName = input.next();
//        }
//        try {
//            readTxt(fileName);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        launch(args);
    }
}

class Picture {
    String symbol = "";
    String type = "";
    String image = "";
}