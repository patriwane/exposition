package net.pacee.exposition;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import net.pacee.exposition.domain.Emotion;

import java.util.ArrayList;
import java.util.List;

public class ExpositionPlanning extends AppCompatActivity {

    RecyclerView rv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_planning_layout);
        rv = (RecyclerView) findViewById(R.id.details_rv_listPlannings);
        PlanningExpostionListAdapter pela = new PlanningExpostionListAdapter(this);
        List<Emotion> el = new ArrayList<>();
        pela.setExpositions(el);

        rv.setAdapter(pela);
        rv.setLayoutManager(new LinearLayoutManager(this));

        pela.setListener(new PlanningExpostionListAdapter.MainExpostionListener() {
            @Override
            public void getPosition(int pos) {
                Log.i("Main","yolo");
            }
        });
    }


}
