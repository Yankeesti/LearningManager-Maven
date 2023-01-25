package com.heinsberg.LearningManager.Gui;

import com.google.gson.GsonBuilder;
import com.heinsberg.LearningManager.Gui.controller.FileResult;
import com.heinsberg.LearningManager.Gui.treeItems.SemesterTreeItem;
import com.heinsberg.LearningManager.Gui.treeItems.StudyTreeItem;
import com.heinsberg.LearningManager.Gui.treeItems.SubjectTreeItem;
import com.heinsberg.LearningManagerProjekt.BackGround.Study;
import com.heinsberg.LearningManagerProjekt.BackGround.TimeClasses.Semester;
import com.heinsberg.LearningManagerProjekt.BackGround.subject.Subject;
import com.heinsberg.LearningManagerProjekt.BackGround.typeAdapters.StudyTypeAdapter;
import javafx.scene.control.TreeItem;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

/**
 * The StudyManager class is responsible for creating and managing a Study object and its associated TreeView in the GUI.
 */
public class StudyManager {
    private Study study;
    private TreeItem<String> foldersRoot = new TreeItem<String>(""); //

    /**
     * Creates a new Study object with the given name and sets up the TreeView object to display the study information.
     *
     * @param studyName the name of the study.
     */
    public void createStudy(String studyName) {
        study = new Study(studyName);
        setUpTreeView();
        System.out.println("study Created");
    }

    /**
     * sets up the TreeView object to display the study information.
     */
    private void setUpTreeView() {
        //Set up Study root
        TreeItem<String> studyTreeItem = new StudyTreeItem(study);
        foldersRoot.getChildren().add(studyTreeItem);
    }

    /**
     * Returns the root of the TreeView object which is used to display the study information in the GUI.
     *
     * @return the root of the TreeView object.
     */
    public TreeItem<String> getFoldersRoot() {
        return foldersRoot;
    }

    /**
     * Returns the Study object managed by this class.
     *
     * @return the Study object.
     */
    public Study getStudy() {
        return study;
    }

    public void studyToJson(File file){
        GsonBuilder gsonBuilder = new GsonBuilder().registerTypeAdapter(Study.class, new StudyTypeAdapter());
        //Write in File
        if (file != null) {
            try {
                FileWriter writer = new FileWriter(file);
                writer.write(gsonBuilder.create().toJson(study));
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else{
            System.err.println("File null");
        }
    }

    /**
     * loads a study from json
     * @param file
     * @return true when succes and false when not
     */
    public FileResult studyFromJson(File file){
        GsonBuilder gsonBuilder = new GsonBuilder().registerTypeAdapter(Study.class, new StudyTypeAdapter());
        if(file == null){
            return FileResult.FILE_NOT_FOUND;
        }
        try {
            FileReader simpleReader = new FileReader(file);
            BufferedReader reader = new BufferedReader(simpleReader);
            study = gsonBuilder.create().fromJson(reader.readLine(),Study.class);
            setUpTreeView();
            return FileResult.SUCCESS;
        } catch (FileNotFoundException e) {
            return FileResult.FILE_NOT_FOUND;
        } catch (IOException e) {
            return FileResult.UNEXPECTED_ERROR;
        }
    }
}
