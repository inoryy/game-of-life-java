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
    private String[] presets;
    private int presetCount = 0;
    private String currentPreset;
    
    public AnchorPane loadPresets(FlowPane base) {
        
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
    
    public Board openCurrentPreset(int defaultSize) {
        File selectedFile = new File ("Presets/"+currentPreset.toLowerCase()+".gofb");   
        return FileHandler.loadFromFile(selectedFile, defaultSize);
    }
    
    private VBox createPresetPage(int pageIndex, FlowPane base) {
        VBox box = new VBox(5);
        for (int i = pageIndex; i < pageIndex + 1; i++) {
            TextArea text = new TextArea(presets[i]);
            text.setWrapText(true);
            currentPreset = Character.toUpperCase(presets[i].charAt(0)) + presets[i].substring(1);
            Label l = new Label(currentPreset);

            HBox nameAndOpen = new HBox(5);
            nameAndOpen.getChildren().addAll(l);
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
}
