package gof.gui;

import gof.console.ConsoleDriver;
import gof.core.Board;
import gof.core.Cell;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

public class Controller implements Initializable {
    
    private final int    DEFAULT_SIZE = 15;
    private final double DEFAULT_PROB = 0.3;

    @FXML
    private FlowPane base;
    @FXML
    private Label countLabel;
    @FXML
    private Slider countSlider;
    @FXML
    private HBox presetBox = new HBox();
    @FXML
    private Button openButton, saveButton;
    @FXML
    private Button runButton, stopButton, randomizeButton, clearButton;

    private Board board;

    private JavaFXDisplayDriver display;

    private ConsoleDriver console = null;

    private Timeline loop = null;
    
    private Pagination presetsPagination;

    private int presetCount = 0;

    private static String[] presets;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AnchorPane anchor = loadPresets();
        presetBox.getChildren().add(anchor);

        createBoard(DEFAULT_SIZE, DEFAULT_PROB);
    }

    @FXML
    private void onRun(Event evt) {
        toggleButtons(false);

        loop = new Timeline(new KeyFrame(Duration.millis(300), e -> {
            board.update();
            display.displayBoard(board);
            if (console != null) {
                console.displayBoard(board);
            }
        }));

        loop.setCycleCount(100);
        loop.play();
    }

    @FXML
    private void onStop(Event evt) {
        toggleButtons(true);
        loop.stop();
    }

    @FXML
    private void onClear(Event evt) {
        createBoard(DEFAULT_SIZE, 0);
    }

    @FXML
    private void onRandomize(Event evt) {
        createBoard(DEFAULT_SIZE, (double) countSlider.getValue()/100);
    }

    /**
     * TODO: check if valid file (correct number of cells for rectangle shaped board)
     */
    @FXML
    private void onOpen(Event evt) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Game of Life Board File");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("GOFB files (*.gofb)", "*.gofb"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile == null) {
            return;
        }
        
        try (Scanner s = new Scanner(selectedFile)) {
            String input = "";
            int sz = DEFAULT_SIZE;
            while (s.hasNextLine()) {
                String line = s.nextLine().replaceAll("\\s+","");
                input += line;
                
                sz = line.length();
            }

            int pos = 0;
            Cell[][] g = new Cell[sz][sz];
            for (int i = 0; i < sz; i++) {
                for (int j = 0; j < sz; j++) {
                    boolean state = (input.charAt(pos) =='1');
                    g[i][j] = new Cell(state);
                    pos++;
                }
            }

            board = new Board(g);
            display = new JavaFXDisplayDriver(sz, 30, board);

            base.getChildren().clear();
            base.getChildren().add(new Group(display.getPane()));

        } catch (FileNotFoundException e) {
            // will never happen since we return on null file
            e.printStackTrace();
        }
    }

    @FXML
    private void onSave(Event evt) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("GOFB files (*.gofb)", "*.gofb");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(new Stage());
        
        if (file == null) {
            return;
        }
        
        String output = ""; // string of numbers from board
        Cell[][] g = board.getGrid();
        for (int i = 0; i < g.length; i++) {
            for (int j = 0; j < g[0].length; j++) {
                output+= g[i][j].getState() ? 1 : 0;
            }
            if (i != g.length-1){
                output+="\n";
            }
        }

        try (FileWriter fileWriter = new FileWriter(file + ".gofb")) {
            fileWriter.write(output);
            fileWriter.close();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }


    @FXML
    private void onSlide(Event evt) {
        countSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                countLabel.setText(newValue.intValue()+"%");
                createBoard(DEFAULT_SIZE, (double) newValue.intValue()/100);
            }
        });
    }


    @FXML
    private void onAbout(Event evt) {
        // TEXT //
        Text text1 = new Text("Conway's Game of Life\n");
        text1.setFont(Font.font(30));
        Text text2 = new Text(
                "\nThe Game of Life, also known simply as Life, is a cellular automaton devised by the British mathematician John Horton Conway in 1970.\n"
                        + "The game is a zero-player game, meaning that its evolution is determined by its initial state, requiring no further input. One interacts with the Game of Life by creating an initial configuration and observing how it evolves or, for advanced players, by creating patterns with particular properties."
                );
        Text text3 = new Text("\n\nRules\n");
        text3.setFont(Font.font(20));
        Text text4 = new Text(
                "\nThe universe of the Game of Life is a two-dimensional orthogonal grid of square cells, each of which is in one of two possible states, alive or dead. Every cell interacts with its eight neighbours, which are the cells that are horizontally, vertically, or diagonally adjacent. At each step in time, the following transitions occur:\n"
                        +"\n1) Any live cell with fewer than two live neighbours dies, as if caused by under-population.\n"
                        +"2) Any live cell with two or three live neighbours lives on to the next generation.\n"
                        +"3) Any live cell with more than three live neighbours dies, as if by overcrowding.\n"
                        +"4) Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.\n\nMore on Wikipedia:\n"
                );

        Hyperlink link = new Hyperlink("http://en.wikipedia.org/wiki/Conway%27s_Game_of_Life <-------not working");
        TextFlow tf = new TextFlow(text1,text2,text3,text4,link);
        tf.setPadding(new Insets(10, 10, 10, 10));
        tf.setTextAlignment(TextAlignment.JUSTIFY);
        // END TEXT, START WINDOW //
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(new Stage());
        VBox dialogVbox = new VBox(20);
        dialogVbox.getChildren().add(tf);
        Scene dialogScene = new Scene(dialogVbox, 450, 500);
        dialog.setScene(dialogScene);
        dialog.show();
        // END WINDOW //
    }
    
    private AnchorPane loadPresets() {
        File dir = new File("Presets");
        File[] selectedFiles = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (pathname.getName().endsWith(".gofb")){
                    presetCount++;
                    return true;}
                return false;
            }
        });
        int pr = 0;
        presets = new String[presetCount];
        for (File selectedFile : selectedFiles) {
            presets[pr] = selectedFile.getName().substring(0, selectedFile.getName().length() - 5);
            pr++;
        }

        presetsPagination = new Pagination(presets.length, 0);
        //pagination.setStyle("-fx-border-color:red;");
        presetsPagination.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer pageIndex) {
                if (pageIndex >= presets.length) {
                    return null;
                } else {
                    return createPresetPage(pageIndex);
                }
            }
        });

        AnchorPane anchor = new AnchorPane();
        AnchorPane.setTopAnchor(presetsPagination, 10.0);
        AnchorPane.setRightAnchor(presetsPagination, 10.0);
        AnchorPane.setBottomAnchor(presetsPagination, 10.0);
        AnchorPane.setLeftAnchor(presetsPagination, 10.0);
        anchor.getChildren().addAll(presetsPagination);
        
        return anchor;
    }

    private VBox createPresetPage(int pageIndex) {
        VBox box = new VBox(5);
        for (int i = pageIndex; i < pageIndex + 1; i++) {
            TextArea text = new TextArea(presets[i]);
            text.setWrapText(true);
            String presetName = Character.toUpperCase(presets[i].charAt(0)) + presets[i].substring(1);
            Label l = new Label(presetName);
            Button openPresetButton = new Button("Open");
            int ii = i;
            openPresetButton.setOnAction(event -> openPreset(presets[ii]));
            HBox nameAndOpen = new HBox(5);
            nameAndOpen.getChildren().addAll(l, openPresetButton);
            File f1 = new File("Presets/"+presets[i]+".png");
            if(f1.exists() && !f1.isDirectory()) { 
                Image myPreset = new Image("file:Presets/"+presets[i]+".png");
                ImageView myPresetView = new ImageView();
                myPresetView.setImage(myPreset);
                box.getChildren().add(myPresetView);
            } else {
                File f = new File("Presets/nopreview.png");
                if(f.exists() && !f.isDirectory()) { 
                    Image noprevImg = new Image("file:Presets/nopreview.png"); //new Image("Presets/nopreview.png");
                    ImageView noprev = new ImageView();
                    noprev.setImage(noprevImg);
                    box.getChildren().add(noprev);
                } else {
                    System.out.println("nopreview.png not found");
                }
            }


            box.getChildren().add(nameAndOpen);
        }
        return box;
    }

    private void openPreset(String presetName) {

        File selectedFile = new File ("Presets/"+presetName+".gofb");   
        try {
            Scanner s = new Scanner(selectedFile);
            int rows = 0;
            int cols = 0;
            String input = "";
            while(s.hasNextLine()){
                String line = s.nextLine();
                if (cols == 0){
                    cols = line.length();
                }
                line.replaceAll("\\s+","");
                input+=line;
                rows++;
            }
            s.close();

            int pos = 0;
            createBoard(rows,0);
            Cell[][] g = board.getGrid();
            for (int i = 0; i < g.length; i++) {
                for (int j = 0; j < g[0].length; j++) {
                    char c = input.charAt(pos);
                    //boolean state = (int) c == 1 ? true : false;
                    boolean state;
                    if (c =='1'){
                        state = true;
                    } else {
                        state = false;
                    }
                    g[i][j].setNewState(state);
                    g[i][j].updateState();
                    pos++;
                }
            }

            board = new Board(g);

            display = new JavaFXDisplayDriver(DEFAULT_SIZE, 30, board);

            base.getChildren().clear();
            base.getChildren().add(new Group(display.getPane()));
            //createBoard(rows,cols, 0);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }       
    }
    
    private void toggleButtons(boolean enable) {
        countSlider.setDisable(!enable);
        presetBox.setDisable(!enable);
        openButton.setDisable(!enable);
        saveButton.setDisable(!enable);
        runButton.setDisable(!enable);
        clearButton.setDisable(!enable);
        randomizeButton.setDisable(!enable);

        stopButton.setDisable(enable);
    }

    private void createBoard(int size, double prob) {
        board = new Board(size, size, prob);

        // for debugging
        // console = new ConsoleDriver();
        // console.displayBoard(board);

        display = new JavaFXDisplayDriver(size, 30, board);

        base.getChildren().clear();
        base.getChildren().add(new Group(display.getPane()));
    }
}