package gof.gui;

import gof.core.Board;
import gof.core.Cell;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class PresetHandler {
    //private static Pagination presetsPagination;

    

    private static String[] presets;
    private static int presetCount = 0;
    
    public static AnchorPane loadPresets(FlowPane base) {
        
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

        Pagination presetsPagination = new Pagination(presets.length, 0);
        //pagination.setStyle("-fx-border-color:red;");
        presetsPagination.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer pageIndex) {
                if (pageIndex >= presets.length) {
                    return null;
                } else {
                    return createPresetPage(pageIndex, base);
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
    
    private static VBox createPresetPage(int pageIndex, FlowPane base) {
        VBox box = new VBox(5);
        for (int i = pageIndex; i < pageIndex + 1; i++) {
            TextArea text = new TextArea(presets[i]);
            text.setWrapText(true);
            String presetName = Character.toUpperCase(presets[i].charAt(0)) + presets[i].substring(1);
            Label l = new Label(presetName);
            Button openPresetButton = new Button("Open");
            int ii = i;
            openPresetButton.setOnAction(event -> openPreset(presets[ii], base));
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
    
    private static void openPreset(String presetName, FlowPane base) {

        File selectedFile = new File ("Presets/"+presetName+".gofb");   
        try {
            Scanner s = new Scanner(selectedFile);
            int rows = 0;
            int cols = 0;
            String input = "";
            int sz = 1;
            while(s.hasNextLine()){
                String line = s.nextLine();
                if (cols == 0){
                    cols = line.length();
                }
                line.replaceAll("\\s+","");
                input+=line;
                rows++;
                sz = line.length();
            }
            s.close();

            Cell[][] g = new Cell[sz][sz];
            int pos = 0;
            for (int i = 0; i < sz; i++) {
                for (int j = 0; j < sz; j++) {
                    boolean state = (input.charAt(pos) =='1');
                    g[i][j] = new Cell(state);
                    pos++;
                }
            }

            Board board = new Board(g);

            JavaFXDisplayDriver display = new JavaFXDisplayDriver(sz, 30, board);

            base.getChildren().clear();
            base.getChildren().add(new Group(display.getPane()));
            //createBoard(rows,cols, 0);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }       
    }

}
