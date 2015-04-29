package gof.gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import gof.core.Board;
import gof.core.Cell;

public class FileHandler {

    public static Board openFromFile(int defaultSize) {
        File file = askForOpenFile();
        if (file == null) {
            return null;
        }

        String input = "";
        int sz = defaultSize;
        try (Scanner s = new Scanner(file)) {
            while (s.hasNextLine()) {
                String line = s.nextLine().replaceAll("\\s+","");
                input += line;
                
                sz = line.length();
            }
        } catch (FileNotFoundException e) {
            // should never happen since we return on null file
            // so if we end up here it's something really bad 
            // and so we let it blow up to runtime
            throw new RuntimeException(e);
        }

        Cell[][] g = new Cell[sz][sz];

        int pos = 0;
        for (int i = 0; i < sz; i++) {
            for (int j = 0; j < sz; j++) {
                boolean state = (input.charAt(pos) =='1');
                g[i][j] = new Cell(state);
                pos++;
            }
        }

        return new Board(g);
    }
    
    public static void saveToFile(Board board) {
        File file = askForSaveFile();
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

        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(output);
            fileWriter.close();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    private static File askForSaveFile() {
        return getFileChooser().showSaveDialog(new Stage());
    }
    
    private static File askForOpenFile() {
        return getFileChooser().showOpenDialog(new Stage());
    }
    
    private static FileChooser getFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Game of Life Board File");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("GOFB files (*.gofb)", "*.gofb"));
        
        return fileChooser;
    }
}
