package net.pacee.exposition;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;

import net.pacee.exposition.R;
import net.pacee.exposition.domain.Emotion;

/**
 * Created by mupac_000 on 23-04-17.
 */

public class AddEmotionFragment extends DialogFragment {
    public interface NoticeDialogListener {
        public void onDialogPositiveClick(Emotion emotion);
        public void onDialogNegativeClick(DialogFragment dialog);
    }



    NoticeDialogListener mListener;
    EditText name;
    EditText desctiption;
    SeekBar cotation;


    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view= inflater.inflate(R.layout.expo_planning_fragment, null);
        name = (EditText)view.findViewById(R.id.et_expo_planning_fragment_name);
        cotation = (SeekBar) view.findViewById(R.id.et_expo_planning_fragment_cotation);
        desctiption = (EditText) view.findViewById(R.id.et_expo_planning_fragment_description);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton("Ajouter une émotions", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user .
                        String title= name.getText().toString();
                        String infos= desctiption.getText().toString();
                        int rating= cotation.getProgress();
                        if(!title.isEmpty() && !infos.isEmpty())
                        {
                            mListener.onDialogPositiveClick(new Emotion(null,title,rating,infos));
                        }

                    }
                })
                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

}
