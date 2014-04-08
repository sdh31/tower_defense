package main.java.data.datahandler;

import java.io.File;
import java.io.Serializable;

import main.java.schema.GameBlueprint;


/**
 * DataBundles store two objects. One object is a GameBlueprint,
 * which contains data relevant to the authoring environment.
 * The other object is a GameState, which contains data
 * relevant to the engine. There are public getter and setter methods
 * for both objects so that data can be properly saved and retrieved
 * to/from the data bundle.
 * 
 * @author In-Young Jo
 * 
 */

@SuppressWarnings("serial")
public class DataBundle implements Serializable {

    private GameBlueprint myGameBlueprint;
    private File myZippedResources;

    public DataBundle () {
    }

    public DataBundle (GameBlueprint gameBlueprintInit) {
        myGameBlueprint = gameBlueprintInit;
    }

    public GameBlueprint getBlueprint () {
        return myGameBlueprint;
    }
    
    public void setResourcesFile(File resourceFolderToSave){
    	myZippedResources = resourceFolderToSave;
    }

    public void setBlueprint (GameBlueprint gameBlueprint) {
        myGameBlueprint = gameBlueprint;
    }

}
