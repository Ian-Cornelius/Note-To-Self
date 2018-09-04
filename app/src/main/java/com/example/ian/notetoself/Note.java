package com.example.ian.notetoself;

/**
 * Created by Ian on 9/4/2017.
 */

import org.json.JSONObject;
import org.json.JSONException;

public class Note {

    private String mTitle;
    private String mDescription;
    private boolean mIdea;
    private boolean mTodo;
    private boolean mImportant;

    //some variables that we will use later in a key-value pair
    private static final String JSON_TITLE = "title";
    private static final String JSON_DESCRIPTION = "description";
    private static final String JSON_IDEA = "idea";
    private static final String JSON_TODO = "todo";
    private static final String JSON_IMPORTANT = "important";

    //add a constructor that receives a JSONObject and intializes the variables using
    //JSONObject member functions getBoolean or getString, passing the key
    // as the argument. A default constructor is needed since we have a specialized one

    public Note(JSONObject jo) throws JSONException{

        //This retrieves the data memebers of the JSON Object stored in memory, fetched into the JSONObject instance
        //jo. Data members are stored using key-value pairs, presumably defined at the saving instant
        mTitle = jo.getString(JSON_TITLE);
        mDescription = jo.getString(JSON_DESCRIPTION);
        mIdea = jo.getBoolean(JSON_IDEA);
        mTodo = jo.getBoolean(JSON_TODO);
        mImportant = jo.getBoolean(JSON_IMPORTANT);
    }

    //Empty default constructor
    public Note(){

    }

    //The following code will load the Note object's member variables into the JSONObject object's, ready to be
    //actually serialized into a single JSONObject object. We simply use the put function and the appropriate key value
    //pair. We return a JSONObject object

    public JSONObject convertToJSON() throws JSONException{

        //create an instance of the JSONObject class
        JSONObject jo = new JSONObject();

        //first argument in the put method is the key, second the value to be stored with the key
        jo.put(JSON_TITLE,mTitle);
        jo.put(JSON_DESCRIPTION,mDescription);
        jo.put(JSON_IDEA,mIdea);
        jo.put(JSON_TODO,mTodo);
        jo.put(JSON_IMPORTANT,mImportant);

        //return the JSONObject instance
        return jo;

    }

    public String getTitle(){
        return mTitle;
    }

    public void setTitle(String mTitle){
        this.mTitle = mTitle;
    }

    public String getDescription(){
        return mDescription;
    }

    public void setDescription(String mDescription){
        this.mDescription = mDescription;
    }

    public boolean isIdea(){
        return mIdea;
    }

    public void setIdea(boolean mIdea){
        this.mIdea = mIdea;
    }

    public boolean isTodo(){
        return mTodo;
    }

    public void setTodo(boolean mTodo){
        this.mTodo = mTodo;
    }

    public boolean isImportant(){
        return mImportant;
    }

    public void setImportant(boolean mImportant){
        this.mImportant = mImportant;
    }

}
