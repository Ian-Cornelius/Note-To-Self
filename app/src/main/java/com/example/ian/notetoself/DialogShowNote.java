package com.example.ian.notetoself;

/**
 * Created by Ian on 9/5/2017.
 */
import android.app.DialogFragment;
import android.app.Dialog;
import android.os.Bundle;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;

public class DialogShowNote extends DialogFragment {

    private Note mNote;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        /* Okay. I see something in the above code. We get a reference to the activity we are
        currently in, then use the getLayoutInflater method of that activity (present in its superclass
        from which it extends), where it returns the reference to the inflater object instantiated
         in that activity
         */
        View dialogView = inflater.inflate(R.layout.dialog_show_note,null);
        TextView txtTitle = (TextView) dialogView.findViewById(R.id.txtTitle);
        TextView txtDescription = (TextView) dialogView.findViewById(R.id.txtDescription);

        //Set the values, using what is in the member variables of the note object
        txtTitle.setText(mNote.getTitle());
        txtDescription.setText(mNote.getDescription());

        //Extract image view references in layout
        ImageView ivImportant = (ImageView) dialogView.findViewById(R.id.imageViewImportant);
        ImageView ivTodo = (ImageView) dialogView.findViewById(R.id.imageViewTodo);
        ImageView ivIdea = (ImageView) dialogView.findViewById(R.id.imageViewIdea);

        /*These next lines of code check whether the note is an idea, important or To-do and
        thus hides or shows the appropriate icon.
         */
        if (!mNote.isImportant()){
            ivImportant.setVisibility(View.GONE);
        }
        if (!mNote.isTodo()){
            ivTodo.setVisibility(View.GONE);
        }
        if (!mNote.isIdea()){
            ivIdea.setVisibility(View.GONE);
        }

        //Handling the OK button, which will simply dismiss the dialog
        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);

        builder.setView(dialogView).setMessage("Your note");

        btnOk.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                dismiss();
            }
        });
        return builder.create();

    }

    //Receive a note object from MainActivity that the user has clicked on
    public void sendNoteSelected(Note noteSelected){
        mNote = noteSelected;
    }
}
