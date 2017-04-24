package net.pacee.exposition;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by mupac_000 on 22-04-17.
 */

public class MainExpostionAdapter extends RecyclerView.Adapter<MainExpostionAdapter.ListExpostionViewHolder> {
    Context context;
    List<String> expositions;

    MainExpostionListener listener;
    public interface MainExpostionListener
    {
        public void getPosition(int pos);
    }

    public void setListener(MainExpostionListener mel)
    {
        listener = mel;
    }

    public MainExpostionAdapter(Context ctx)
    {
        this.context = ctx;
    }
    @Override
    public ListExpostionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.expostion_title_layout,parent,false);
        return new ListExpostionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListExpostionViewHolder holder, int position) {
        String txt = expositions.get(position);
        holder.title.setText(txt);
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return expositions.size();
    }

    public void setExpositions(List<String> exp)
    {
        expositions = exp;
    }

    public void adExposition(String txt)
    {
        expositions.add(txt);
        this.notifyDataSetChanged();
    }

    public class ListExpostionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView title;
        public ListExpostionViewHolder(View v)
        {
            super(v);
            title = (TextView)v.findViewById(R.id.main_rv_expostionTitle);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
           // Log.i("Liste element Clicked", "position clicked:"+position+" / value:"+expositions.get(position));
            listener.getPosition(position);
        }
    }
}
