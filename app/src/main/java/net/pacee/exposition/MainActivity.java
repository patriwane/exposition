package net.pacee.exposition;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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
    List<String> listKeys;
    RecyclerView rv;
    FloatingActionButton fab;
    MainExpostionAdapter mea;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    private Paint p = new Paint();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv = (RecyclerView) findViewById(R.id.main_rv_expositionList);
        mea = new MainExpostionAdapter(this);
        myRef = database.getReference("EXPOSITION");
        //myRef.setValue("Hello, World!");
        list = new ArrayList<>();
        listKeys= new ArrayList<>();
        mea.setExpositions(list);
        //when it's clicked we need to get the position then we need to retrieve positionKey
        mea.setListener(new MainExpostionAdapter.MainExpostionListener() {
            @Override
            public void getPosition(int pos) {
                Log.i("MainActivity_showKey",listKeys.get(pos));
                Intent i = new Intent(MainActivity.this,ExpositionPlanning.class);
                startActivity(i);

            }
        });
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


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

                return false;
            }

            /**
             * Suppression lors du swype
             * @param viewHolder
             * @param direction
             */
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {

                boolean notDeleted = false;
                Snackbar snackbar = Snackbar
                        .make(rv, "EVENT REMOVED", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                mea.notifyDataSetChanged();
                            }
                        });
                snackbar.addCallback(new Snackbar.Callback() {
                    //quand la personne annuler la suppression on remet les chose dans leurs états initiales
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        if (event == DISMISS_EVENT_TIMEOUT) {
                            String key;
                            if((key=viewHolder.itemView.getTag().toString())!=null) {
                                Log.v("Main_swipeDeletable","deletation of element at position:"+key);
                                //delete value

                                myRef.child(listKeys.get(Integer.valueOf(key))).removeValue();
                               // viewHolder.itemView.getTag();
                                refreshData();
                            }
                            //
                        }
                    }
                });
                snackbar.show();


            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {


                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if (dX > 0) {
                        p.setColor(Color.argb(255, 170, 255, 0));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                        c.drawRect(background, p);

                    } else {

                        int tmpColor = Math.round((int) ((dX / -2000.0) * 100));
                        p.setColor(Color.argb(255, 172 + tmpColor, 162 - tmpColor, 162 - tmpColor));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, p);

                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }


        }).attachToRecyclerView(rv);


    }

    //lecture de la liste des données depuis le serveur firebase
    public void refreshData()
    {
        //on ne lit qu'une fois puis on se déconnecte
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            List<String> refreshedList = new ArrayList<String>();
            List<String> refreshedListKey = new ArrayList<String>();
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dt: dataSnapshot.getChildren())
                {

                    refreshedListKey.add(dt.getKey());
                 //   Log.i("MainActivity_Loading","key:"+dt.getKey());
                    refreshedList.add(dt.child("title").getValue(String.class));
                }
                listKeys = refreshedListKey;
                list = refreshedList;
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
