package net.pacee.exposition;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AddExpositionFragment.NoticeDialogListener{

    List<String> list;
    RecyclerView rv;
    FloatingActionButton fab;
    MainExpostionAdapter mea;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv = (RecyclerView) findViewById(R.id.main_rv_expositionList);
        mea = new MainExpostionAdapter(this);
        myRef = database.getReference("EXPOSITION");
        //myRef.setValue("Hello, World!");
        list = new ArrayList<>();
        mea.setExpositions(list);
        rv.setAdapter(mea);
        refreshData();
        rv.setLayoutManager(new LinearLayoutManager(this));
        fab = (FloatingActionButton)findViewById(R.id.main_btn_add_exposition);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNoticeDialog();
            }
        });



    }

    //lecture de la liste des données depuis le serveur firebase
    public void refreshData()
    {
        //on ne lit qu'une fois puis on se déconnecte
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dt: dataSnapshot.getChildren())
                {
                    list.add(dt.child("title").getValue(String.class));
                }
                mea.setExpositions(list);
                mea.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void showNoticeDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new AddExpositionFragment();
        dialog.show(getSupportFragmentManager(), "AddExpositionFragment");
    }

    @Override
    public void onDialogPositiveClick(String exposition) {
        Log.i("DialogClick","OnPositive click");
        addExposition(exposition);
    }


    /**
     * add new exposition field
     * push it to firebase database
     * notify adapter
     * @param title
     */
    public void addExposition(String title)
    {
        mea.adExposition(title);
        myRef.push().child("title").setValue(title);
        mea.notifyDataSetChanged();

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }
}
