package com.example.ian.notetoself;

/**
 * Created by Ian on 9/5/2017.
 */

import android.app.DialogFragment;
import android.app.Dialog;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.CheckBox;
import android.widget.Button;

public class DialogNewNote extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //Object of class LayoutInflater, which we will use to inflate our xml layout,
        //That is change it from the xml file to a java object
        LayoutInflater inflater = getActivity().getLayoutInflater();

        /* Now, having the LayoutInflater object inflater, use its method inflate(), to
        inflate the layout (change it from xml to a java object). The method returns the
        created object (its reference that is), thus we create a new object of the
        View class to hold that reference. This is apt because, the class View is actually
        a superclass of all the widgets we will have in the layout object. Note that this reference
        points to an array of objects, since each widget is an object on its own. This will be
        seen in the preceding lines of code (after the one hinted above), where we will use
        the findViewById method, which dialog view has. This further solidifies the use of
        a View object to get the object passed from inflate(), since being a superclass of
        all the widget objects, it is then presumably designed to handle all widget objects it holds
        within one of its objects, and find their real references.
         */
        View dialogView = inflater.inflate(R.layout.dialog_new_note,null);
        final EditText editTitle = (EditText)dialogView.findViewById(R.id.editTitle);
        final EditText editDescription = (EditText) dialogView.findViewById(R.id.editDescription);
        final CheckBox checkBoxIdea = (CheckBox) dialogView.findViewById(R.id.checkBoxIdea);
        final CheckBox checkBoxTodo = (CheckBox) dialogView.findViewById(R.id.checkBoxTodo);
        final CheckBox checkBoxImportant = (CheckBox) dialogView.findViewById(R.id.checkBoxImportant);
        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        Button btnCancel = (Button) dialogView.findViewById(R.id.btnCancel);

        //Now, using the builder object to set the view, passing the object of the layout
        //(dialogView), in its argument.
        builder.setView(dialogView).setMessage("Add a new note");
        /*Important to note that, another reason qualifying the use of View to hold the object of
        the layout is because, the overloaded setView method of AlertDialog.Builder takes an object,
        of the View class. That is another reason, more qualified, why we use it. This is shown by
        the android studio hint given when typig the code
         */

        //Handle the cancel button. Funny enough we never used final with this one. And btnOk
        btnCancel.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                dismiss();
                //The above method dismisses the dialog box
            }
        });

        //Handle the OK button
        btnOk.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){

                //create a newNote object of the Note class,
                //purposely for transferring details on the form to
                //the mvariables of the Note class we created
                Note newNote = new Note();

                //Set the variables to match the user's entry on the form.
                //This therefore means we get the entries on the widgets
                newNote.setTitle(editTitle.getText().toString());
                newNote.setDescription(editDescription.getText().toString());
                newNote.setIdea(checkBoxIdea.isChecked());
                newNote.setTodo(checkBoxTodo.isChecked());
                newNote.setImportant(checkBoxImportant.isChecked());

                //Get a reference to MainActivity. Remember this is a class,
                //the main class on which the main code runs
                MainActivity callingActivity = (MainActivity) getActivity();

                //Pass newNote to MainActivity
                callingActivity.createNewNote(newNote);
                //quit the dialog
                dismiss();
            }
        });
        return builder.create();
    }
}
