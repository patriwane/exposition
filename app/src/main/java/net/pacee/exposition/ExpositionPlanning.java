package net.pacee.exposition;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.pacee.exposition.domain.Emotion;

import java.util.ArrayList;
import java.util.List;

public class ExpositionPlanning extends AppCompatActivity implements AddEmotionFragment.NoticeDialogListener{

    FloatingActionButton fab;
    List<Emotion> emotions;
    RecyclerView rv;
    PlanningExpostionListAdapter pela;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    private String key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null)
            key = bundle.getString("ID_PARENT");

        myRef = database.getReference("EXPOSITION_EMOTIONS").child(key);
        setContentView(R.layout.details_planning_layout);
        rv = (RecyclerView) findViewById(R.id.details_rv_listPlannings);
        pela = new PlanningExpostionListAdapter(this);
        emotions = new ArrayList<>();
        pela.setExpositions(emotions);
        fab = (FloatingActionButton)findViewById(R.id.details_fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNoticeDialog();
            }
        });
        rv.setAdapter(pela);
        rv.setLayoutManager(new LinearLayoutManager(this));

        pela.setListener(new PlanningExpostionListAdapter.MainExpostionListener() {
            @Override
            public void getPosition(int pos) {
                Toast.makeText(ExpositionPlanning.this,"Selected emotion:"+emotions.get(pos).getName(),Toast.LENGTH_SHORT);
            }
        });
        refreshData();
    }

    public void refreshData()
    {
        Log.i("ExpositionPlanning","Data refreshed for key:"+key);
        //on ne lit qu'une fois puis on se déconnecte
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            List<Emotion> refreshedList = new ArrayList<>();
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dt: dataSnapshot.getChildren())
                {
                    Log.i("ExpositionPlanning","Dnas onDataChange de firebase");
                    Emotion em = dt.getValue(Emotion.class);
                    //tester du retour de firebase 26-04-17
                    Log.i("ExpositionPlanning",em.toString());
                    refreshedList.add(em);
                }
                emotions = refreshedList;
                pela.setExpositions(refreshedList);
                pela.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("ExpositionPlanning","error");
            }
        });
    }
    public void showNoticeDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new AddEmotionFragment();
        dialog.show(getSupportFragmentManager(), "AddEmotionFragment");
    }

    @Override
    public void onDialogPositiveClick(Emotion emotion) {
        Log.i("ExpositionPlanning", emotion.toString());
        pela.adExposition(emotion);
        DatabaseReference ref = myRef.push();

        emotion.setParent(key);
        ref.setValue(emotion);
        pela.notifyDataSetChanged();
        refreshData();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        Log.i("ExpositionPlanning","ok");
    }
}
