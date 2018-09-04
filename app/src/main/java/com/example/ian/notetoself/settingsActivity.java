package com.example.ian.notetoself;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class settingsActivity extends AppCompatActivity {

    //private so that they cannot be used by external code to read or write data, I guess.
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mEditor;

    //These will store the user's selection at settings
    private boolean mSound;

    public static final int FAST = 0;
    public static final int SLOW = 1;
    public static final int NONE = 2;
    /*
    Used static and final so that they are not changed and persist despite several instances.Also, can be
    accessed without the instance of the classes it is visible to.
     */

    private int mAnimOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //initializing mPrefs and mEditor
        mPrefs = getSharedPreferences("Note to Self",MODE_PRIVATE);
        //The first argument provides a string name associated with all the data stored (read and written) while the second
        //argument specifies the mode of access, with the specifically used mode stating that only
        //classes of the app can access the data

        //mEditor initialized using edit() method of mPrefs. Editor inner class writes data, SharedPreferences reads data
        mEditor = mPrefs.edit();

        /*
        Now, we retrieve data held in memory using mPrefs. The first argument is the key of the value stored,
        the second argument being the default value if the value of this key is found to be null
         */
        //Specifically, we look for the user's last selection that was stored in memory, for the sound checkbox.
        //We later use this value to check or uncheck the checkbox. Remember this is happening as the activity is being launched (onCreate)
        mSound = mPrefs.getBoolean("sound",true);

        //getting a reference to the sound checkbox
        CheckBox checkBoxSound = (CheckBox) findViewById(R.id.checkBoxSound);

        //checking or unchecking the checkbox
        if(mSound){
            //check the checkbox
            checkBoxSound.setChecked(true);
        }
        else
        {
            checkBoxSound.setChecked(false);
        }

        //Now listen to clicks on the checkbox. If any, the value of the checkbox has been changed from what we had previously put or had been loaded
        //Thus, change the value of mSound
        checkBoxSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){

                //since the value has been changed, change mSound
                mSound = !mSound;
                //save this change. Not yet committed to permanent memory though. We use the key-value pair
                mEditor.putBoolean("sound",mSound);
            }
        }); //Wait, I think now I get it. The anonymous class here (with no name), is implemented in the inner class OnCheckedChangeListener
        //of the class CompoundButton

        //Now for the radio buttons
        //Load what was previously stored, with the default as second argument if nothing is found
        mAnimOption = mPrefs.getInt("anim option",FAST);

        //get reference to the radio group. No need for radio buttons reference
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        //deselect all buttons
        radioGroup.clearCheck();

        //Decide the radio button to be selected, based on the fetched data
        switch(mAnimOption){
            case FAST:
                radioGroup.check(R.id.radioFast);
                break;
            case SLOW:
                radioGroup.check(R.id.radioSlow);
                break;
            case NONE:
                radioGroup.check(R.id.radioNone);
                break;
        }

        //Now, we handle clicks on the radio button
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId){

                //now create an instance of the radio button. //Get the reference to the actual radio button clicked
                RadioButton rb = (RadioButton) group.findViewById(checkedId);

                //ensure rb and checkedId are not null and <0 respectively
                if (null != rb && checkedId > -1){

                    //switch-case statement to change the checked radiobutton
                    switch(rb.getId()){
                        case R.id.radioFast:
                            mAnimOption = FAST;
                            break;
                        case R.id.radioSlow:
                            mAnimOption = SLOW;
                            break;
                        case R.id.radioNone:
                            mAnimOption = NONE;
                            break;
                    }//end switch block

                    //write the data
                    mEditor.putInt("anim option",mAnimOption);
                }

            }
        });
    }

    ///Now, we will commit the data (save it to permanent memory), when the activity has been paused. Thus, override the onPause method
    @Override
    protected void onPause(){

        //call its original function
        super.onPause();

        //save settings here. No parameters are taken
        mEditor.commit();
        //Now, we need to load these settings in the mainActivity when the app starts or the user switches back from the settings activity
    }
}
