package com.example.ian.notetoself;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView;
import android.content.Intent;
//import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private NoteAdapter mNoteAdapter; //declaring object of NoteAdapter type

    //These variables are needed to load the user's saved settings
    private boolean mSound;
    private int mAnimOption;
    private SharedPreferences mPrefs;

    //declaring objects to handle our animations
    Animation mAnimFlash;
    Animation mFadeIn;

    //Now, we add member variables who will help us with playing sound files
    int mIdBeep = -1;
    SoundPool mSp;


    //Now, we override the onResume method, where we will instantiate mPrefs and read the user's stored values. We will use this data to
    //add UI and sound FX
    //onResume has been overriden below onCreate, for programmer readability sakes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //intializing the mNoteAdapter object, getting a reference to listview and binding them together
        mNoteAdapter = new NoteAdapter();
        ListView listNote = (ListView) findViewById(R.id.listView);//nashuku shida hapa. Just change in font. OK. l looking like L
        //binding adapter to listView
        listNote.setAdapter(mNoteAdapter);

        //allow it to be long clickable (responsive to long clicks)
        listNote.setLongClickable(true);

        //Now to detect long clicks and delete the note
        listNote.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){

            public boolean onItemLongClick(AdapterView<?> adapter,View view,int whichItem,long id){

                //Ask noteAdapter to delete this note using deleteNote method we defined
                //I want to add a confirmation dialog box here..all control for delete passed there (calling the method basically).
                //code left here is for showing delete dialog box, plus passing the whichItem variable to use in delete method
                //mNoteAdapter.deleteNote(whichItem);

                //Create the DialogDeleteNoteInstance
                DialogDeleteNote deleteNote = new DialogDeleteNote();

                //send whichItem
                deleteNote.receiveItemAndAdapter(whichItem,mNoteAdapter);

                //show the dialog box
                deleteNote.show(getFragmentManager(),"");

                return true;
            }
        });


        //Initialize our SoundPool class, version specific
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){

            //Create and initialize an AudioAttributes instance. Note USAGE_MEDIA...what's the difference? Learn more
            //See the use of builder/build. Seems like they build an object or widget like dialog box.
            //Similar concept being seen
            AudioAttributes audioAttributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).
                    setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).
                    build();

            //initialize the SoundPool instance
            mSp = new SoundPool.Builder().setMaxStreams(5).setAudioAttributes(audioAttributes).build();
        }

        //load the sound file
        try{

            //create AssetManager and AssetFileDescriptor objects
            AssetManager assetManager = this.getAssets();
            AssetFileDescriptor descriptor;

            //Looks like the folder is deliberately called assets because there is an inbuilt method of the AssetManager class
            //that supposedly performs read/write operations on this folder.

            //load our fx in memory, ready to use.
            //It seems like AssetManager performs read/write on the assets folder, descriptor is an object that stores the decompressed
            //audio file

            //By initializing our descriptor object, we are loading the sound file into memory
            descriptor = assetManager.openFd("fx2.ogg");

            //get the soundFile's Id. Happens as we load the sound file into the SoundPool object, where we will be able to play it
            mIdBeep = mSp.load(descriptor,0);
        }catch(IOException e){

            //print an error message to the console
            Toast.makeText(this,"Error loading sound files",Toast.LENGTH_LONG).show();
        }

        //We will open a note, when it is clicked on in the list view

        //handle clicks on the listview. Seems like the AdapterView.OnItemClickListener() is the anonymous class designed to handle
        //clicks in adapters
        listNote.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int whichItem, long id){

                //Play the beep sound when note is clicked
                //done here since we are in the onItemClick method, where we do certain actions when note is clicked
                if (mSound){

                    //play sound
                    //No need to worry about setting mSound since this was done before
                    mSp.play(mIdBeep,1,1,0,0,1);
                }
                else{

                }
                /*
                create a temporary note which is a reference to the note that has just been clicked.
                use the getItem method
                 */
                Note tempNote = mNoteAdapter.getItem(whichItem);

                //Create a new dialog window
                DialogShowNote dialog = new DialogShowNote();
                //send the reference for the note to be shown
                dialog.sendNoteSelected(tempNote);

                //show the dialog window with the note in it. I think it first invokes onCreateDialog
                dialog.show(getFragmentManager(),"");

            }
        });

    }

    @Override
    protected void onResume(){

        //call the original method
        super.onResume();

        //instantiate mPrefs and load data
        mPrefs = getSharedPreferences("Note to Self",MODE_PRIVATE);
        mSound = mPrefs.getBoolean("sound",true);
        mAnimOption = mPrefs.getInt("anim option",settingsActivity.FAST);//last argument is the static variable of settingsActivity

        //Now, instantiate the Animation class objects. Done on onResume method because this is where we load the user's saved settings
        //data. This is of particular importance especially when the user has changed the animation speed settings, for the flash
        mAnimFlash = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.flash);
        mFadeIn = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);

        //Just debugging manenos
        //Toast.makeText(this,"Sound = " + mSound,Toast.LENGTH_LONG).show();

        //set the rate of flash based on settings
        if(mAnimOption == settingsActivity.FAST){

            mAnimFlash.setDuration(100);
            //Toast.makeText(this,"anim = " + mAnimOption,Toast.LENGTH_SHORT).show();
        }
        else if(mAnimOption == settingsActivity.SLOW){
            mAnimFlash.setDuration(1000);
            //Toast.makeText(this,"anim = " + mAnimOption,Toast.LENGTH_SHORT).show();
        }

        //Why this code? Ama ni kuupdate the widgets in the listadapter? Maybe
        mNoteAdapter.notifyDataSetChanged();
    }

    //Where we invoke user data saving
    @Override
    protected void onPause(){
        super.onPause();
        mNoteAdapter.saveNotes();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.id.action_add){
            DialogNewNote dialog = new DialogNewNote();
            dialog.show(getFragmentManager(),"");
        }
        if(item.getItemId()==R.id.action_settings){

            //The first argument in the constructor is the source activity, the second argument is the target activity, qualified by .class
            Intent intent = new Intent(this,settingsActivity.class);

            //Opens the activity we want to go to (target activity)
            startActivity(intent);
        }
        return true;
    }


    //This method will allow us to receive a new note from
    //DialogNewNote class
    public void createNewNote(Note n){

        mNoteAdapter.addNote(n);//the note is now passed to addNote for the adapter class to update listview and noteList ArrayList
    }

    //inner class to handle listview and list items
    public class NoteAdapter extends BaseAdapter{

        //Since we can now save and load data using the JSONSerializer class, let us create an instance of it here, which we will use
        //within to do all those functions. Remember we don't import because IT IS DECLARED AND DEFINED WITHIN THE SAME PACKAGE
        //AS MAINACTIVITY CLASS.

        private JSONSerializer mSerializer;
        //create our ArrayList, of type Note. Notice this List isht? Kwani its a subclass?
        List<Note> noteList = new ArrayList<Note>();

        //Now, we need a new constructor that will initialize the JSONSerializer object, and attempt loading saved notes to
        //the noteList ArrayList. If it fails, just redeclare the noteList (why???) (try avoiding it and see), and display an error
        //that you failed to load the saved noted
        public NoteAdapter (){

            //initialize the JSONSerializer object. Remember the first argument is the filename, second is the context
            mSerializer = new JSONSerializer("NoteToSelf.json",MainActivity.this.getApplicationContext());
            //this MainActivity.this code though. I think we use it to get access to an instance method, without necessarily
            //declaring and initializing an instance of the class MainActivity, since this will be treated as an instance by default (its
            //the main class)

            try{

                //load notes
                noteList = mSerializer.load();
                //Then I have noticed that since the load() method handles an exception, wherever it returns to must also handle an
                //exception as presumed by my own understanding before
            }
            catch(Exception e){

                noteList = new ArrayList<Note>(); //Is this code necessary? Just have it for now. EXPERIMENT WHEN DONE WITH APP
                Toast.makeText(new MainActivity(),"Error Loading Saved Notes",Toast.LENGTH_SHORT).show(); //Will this also work? Try changing the first object to NoteAdapter type if not work
            }
        }

        //A new method that we can call to save our user's data. We have already loaded it using the constructor
        //It will be invoked in the overriden onPause()
        public void saveNotes(){

            //need a try catch finally combo, to handle exceptions and avoid the app crashing unexpectedly
            try{
                mSerializer.save(noteList);
            }
            catch(Exception e){
                Toast.makeText(MainActivity.this.getApplicationContext(),"Error saving notes",Toast.LENGTH_SHORT).show(); //Will this change do?
            }
        }

        // A method to delete notes, by removing it from the array list
        public void deleteNote(int n){

            //Remove the list from noteList arrayList, using the index passed
            //when notes are resaved, overwriting previously stored, using JSONSerializer, the removed note won't be there
            noteList.remove(n);

            //inform adapter to update list
            notifyDataSetChanged();
        }


        //returns the size of ArrayList
        @Override
        public int getCount(){
            return noteList.size();
        }

        //returns item from ArrayList
        @Override
        public Note getItem(int whichItem){
            return noteList.get(whichItem);
        }

        //returns the internal Id of the item in the list the BaseAdapter, not that given by the programmer
        @Override
        public long getItemId(int whichItem){
            return whichItem;
        }

        //Key method. Has the view object of the View class as a parameter. This is the List Item which needs to be prepared for display
        //to the user. We prepare the list item here using techniques of handling layouts and widget, starting with the inflater.
        //view is actually an instance of the list item layout (remember its an object at runtime). Which item indexes our Note object
        //in the ArrayList which needs to be displayed in the list item and (making a guess), viewGroup will reference the listview
        //we will send the list item layout to, to show.
        /*
        All we thus need to do in the code below is write code that will transfer the data held in the note object into the widgets
        of the listitem.xml layout, for display.
         */
        @Override
        public View getView(int whichItem, View view, ViewGroup viewGroup){
            //implement this method next
            //Check if view has been inflated. If not, inflate
            if (view==null){

                //if not, do so here. Is there a problem if I use getInflater() here?
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                //now instantiate view using inflater.inflate, using the listitem layout
                view = inflater.inflate(R.layout.listitem,viewGroup,false);

                //The false parameter is necessary because of how we will use list item. (Should not be attached to ViewGroup)

            }//end if

            //Grab a reference to all our textview and imageview widgets
            TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            TextView txtDescription = (TextView) view.findViewById(R.id.txtDescription);
            ImageView ivImportant = (ImageView) view.findViewById(R.id.imageViewImportant);
            ImageView ivTodo = (ImageView) view.findViewById(R.id.imageViewToDo);
            ImageView ivIdea = (ImageView) view.findViewById(R.id.imageViewIdea);

            //hide any imageview widgets that are not relevant. Using the Note object that holds this data
            Note tempNote = noteList.get(whichItem);

            //To animate or not to animate
            if(tempNote.isImportant() && mAnimOption != settingsActivity.NONE){
                view.setAnimation(mAnimFlash);
                //Why use setAnimation and not start animation? Coz we are using the larger class View??
                //Does this mean that the whole layout will flash? Let us build and see...YES. THE WHOLE LAYOUT (OBJECT IN VIEW) WILL FLASH
                //So, it looks like to a whole view/layout, we set animation? Maybe.......
            }

            else{
                view.setAnimation(mFadeIn);
            }

            if(!tempNote.isImportant()){
                ivImportant.setVisibility(View.GONE);
            }

            if (!tempNote.isTodo()){
                ivTodo.setVisibility(View.GONE);
            }

            if (!tempNote.isIdea()){
                ivIdea.setVisibility(View.GONE);
            }

            //add the text to the heading and description
            txtTitle.setText(tempNote.getTitle());
            txtDescription.setText(tempNote.getDescription());

            return view;
        }

        //add a method of our own, which will be called when we want to add a note object to the notelist
        public void addNote(Note n){

            noteList.add(n);
            notifyDataSetChanged();

            /*
            notifyDataSetChanged tells the NoteAdapter class that the noteList has changed and it thus needs to update the listview layout
             */
        }
    }

}

/*
That's it. We can now run the app and add as many notes as we like.
ArrayList will store them all in our running app, BaseAdapter will manage displaying them in ListView, and now
JSON will take care of loading them from disk and saving them back as well.
 */