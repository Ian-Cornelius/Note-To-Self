package com.example.ian.notetoself;

/**
 * Created by Ian on 11/1/2017.
 */
import android.app.DialogFragment;
import android.app.Dialog;
import android.os.Bundle;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class DialogDeleteNote extends DialogFragment {

    //I don't need a Note instance since I won't be manipulating notes data to show...
    //I only need the NoteAdapter object, so as to call the delete method and whichItem int variable to hold the NoteObject id selected
    //Pass the object we have in other place, so as to use it aptly, as with the data it has, especially the listNote. New objector will
    //have nill listNote entries. That is why I had an error before.
    private int whichItem;
    private MainActivity.NoteAdapter mNoteAdapter;

    //Now Override onCreateDialog
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        //get a Builder inner class instance. We will use it to build the dialog box
        //I think this getActivity() argument tells the builder in what activity the dialog box is to be built
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //LayoutInflater object to help us with inflating the layout of the dialog box. Created and initialized
        LayoutInflater inflater = getActivity().getLayoutInflater();

        //Our View object to hold the inflated layout
        View dialogDelete = inflater.inflate(R.layout.dialog_delete_note,null);

        //get the specific widgets reference. Don't need textview coz we are not manipulating it
        //TextView alertText = (TextView) dialogDelete.findViewById(R.id.alertText);
        Button buttonCancel = (Button) dialogDelete.findViewById(R.id.buttonCancel);
        Button buttonOk = (Button) dialogDelete.findViewById(R.id.buttonOk);

        //no need to set values of widget such as text since we are not interested in note object contents

        //setting the view
        builder.setView(dialogDelete).setMessage("Confirm Delete");

        //Handling the Ok button that will delete the note
        buttonOk.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                mNoteAdapter.deleteNote(whichItem);

                //quit the dialog
                dismiss();
            }
        });

        //Handling the cancel button
        buttonCancel.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                dismiss();
            }
        });
        return builder.create();
    }

    //now the method which gets the selected note item in the noteList arrayList
    public void receiveItemAndAdapter(int item, MainActivity.NoteAdapter adapter){

        mNoteAdapter = adapter;
        whichItem = item;
    }
}
