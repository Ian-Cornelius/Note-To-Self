package com.example.ian.notetoself;

/**
 * Created by Ian on 10/18/2017.
 */

/*
We will use this class for the actual serialization and deserialization of the objects (JSONObject's instance that is)
 */

import android.content.Context;
import java.util.List;
import java.io.IOException;
import org.json.JSONException;
import org.json.JSONArray;
import org.json.JSONTokener;

import java.io.Writer;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;

public class JSONSerializer {

    //Two important member variables
    //This one will be used to store the filename where the data is stored
    private String mFilename;
    //This one is a Context objectm=, necessary in Android when writing data into a file
    private Context mContext;

    //the constructor, where the two member variables above are initialized, having their values passed in as arguments
    public JSONSerializer(String fn, Context con){

        mFilename = fn;
        mContext = con;
    }

    //save method where most of the work is done. Basically, we do serialization here
    public void save(List<Note> notes) throws IOException, JSONException{

        //Make an array in JSON format, using an instance of JSONArray class. It creates an array list (in object type),suitable
        //for storing JSONObject instances
        JSONArray jArray = new JSONArray();

        //Load it with the Note objects, passed in as an ArrayList
        for (Note n: notes)
            jArray.put(n.convertToJSON());

        //Now we write it to the private disk space of our app
        Writer writer = null;

        //Use try, catch, finally combination so as to handle any exceptions
        try{
            //declare and initialize an object of OutputStream class
            //mContext, instance of Context class, used here to invoke the
            //openFileOutput method
            OutputStream out = mContext.openFileOutput(mFilename,mContext.MODE_PRIVATE);

            //initialize the Writer object
            writer = new OutputStreamWriter(out);
            writer.write(jArray.toString());
        } finally{
            if(writer!=null)
                writer.close();
        }
    }

    //Now for de-serialization. We use the load method (user-defined), to handle all this task
    public ArrayList<Note> load() throws IOException, JSONException{

        //create a new ArrayList that will hold Note objects, extracted from the file
        ArrayList<Note> noteList = new ArrayList<Note>();
        BufferedReader reader = null;

        /*
        something interesting about the code above. In saving data (serialization), we used Writer, and first had it initalized to null.
        In loading data, we are using BufferedReader and similarly initializing it to null
         */

        //In likened fashion, we go to the try, catch, finally sequence of statements
        try{

            //declare intialize and inputStream instance (in saving it was outputStream), using the mContext method openFileInput
            //in saving it was openFileOutput
            InputStream in = mContext.openFileInput(mFilename);
            //Just needed the filename as the argument, unlike in saving where we used filename, Context instance variable MODE_PRIVATE

            //now initializing reader, a little bit different from the saving methodology
            reader = new BufferedReader(new InputStreamReader(in));

            //create a string using the StringBuilder class' instance. Append the all the data in the file to it
            //It's in java.lang package which is imported by default in the program (all java programs)
            StringBuilder jsonString = new StringBuilder();

            //a String type variable which we will use in the while loop, as a conditional tester to append data to
            //the StringBuilder instance
            String line = null;

            //while loop for appending data to StringBuilder instance
            while ((line = reader.readLine())!= null){
                jsonString.append(line);
            }

            //What this code does, I DON'T KNOW
            /*
            Okay, I think now I know what it does. We initialize a JSONArray object, which as we saw before, is a special ArrayList
            designed to hold JSONObjects instances.

            Speaking of which, remember that ALL NOTE OBJECTS ARE HELD IN AN ARRAYLIST WHICH WE CONVERT EACH OF ITS ELEMENTS
            (NOTE OBJECTS) INTO JSONOBJECTS THEN SAVE ALL THIS CLUSTER IN DISK. SINCE THE SINGLE FILE WILL CONTAIN SEVERAL SERIALIZED
            OBJECTS, DE-SERIALIZING INVOLVES FISHING OUT THESE OBJECTS INTO SINGULAR COMPONENTS, WHICH IS ESSENTIALLY WHAT WE DO HERE.

            Now, back to the code analysis, what the code below does, as per my understanding so far, in continuation with the
            first statement, is type cast an object of JSONTokener class to a JSONArray class type thus assign it to a JSONArray,
            instance, with the JSONTokener instance having being passed to the StringBuilder instance jsonString, but in String type,
           (since it is an object), thus the toString() instance method. Then, nextValue(), which appears to be of the JSONTokener
            instance method is used to convert each String token to the corresponding JSON object, feeding it to the JSONArray object,
            every time. Remember the JSONArray instance is operated on just like a normal ArrayList. This may suggest that nextValue()
            a recursive function, thus fulfilling the iteration required to feed all the JSON objects into the JSONArray instance, as
            was the requisite in the serialization method, save().
             */
            JSONArray jArray = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();

            //Now, having the JSON objects, construct the Note type using the new Note constructor (the one that takes a JSONObject instance
            //as an argument). Having constructed the Note object, add it to the noteList ArrayList we had declared before.
            for (int i=0;i<jArray.length();i++){

                //The statement below is where it all happens. We use the add() method to add an element to the noteList ArrayList.
                //Since it expects a Note object as the argument, we use the new keyword, with the Note constructor that takes
                //a JSONObject as an instance. To get this JSONObject instance, which is stored in the JSONArray instance (a special
                //ArrayList for JSONObjects), we use the JSONArray instance method getJSONObject(int index), with the index passed as
                //an argument, to access the specific JSONObject
                noteList.add(new Note(jArray.getJSONObject(i)));
            }
            //incase we have an expection

        }
        catch(FileNotFoundException e){

            //We will ignore this since it will happen when we first open the app
            //You can output something in the log maybe.
            //Personal suggestion; use later for an access to already saved data, to show corrupt file or lost. I stand to be corrected
        }
        finally{

            //This will always run
            if (reader !=null)
                reader.close();

            //close the reader object that extracted the data
        }

        //now return the ArrayList noteList. Which will be used to show data once the app is opened.
        return noteList;

        //Now, we make our class work with the main activity class specifically, the NoteAdapter class (where all this happens)
        //head to mainActivity class, NoteAdapter inner class
        //and it turns out a catch will lead to no return. Thus an error that has to be processed by the new NoteAdapter constructor
        //using a similar try catch finally sequence
    }
}
