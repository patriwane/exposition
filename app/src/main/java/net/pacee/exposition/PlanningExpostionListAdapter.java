package net.pacee.exposition;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.pacee.exposition.domain.Emotion;

import java.util.List;

/**
 * Created by mupac_000 on 22-04-17.
 */

public class PlanningExpostionListAdapter extends RecyclerView.Adapter<PlanningExpostionListAdapter.ListExpostionViewHolder> {
    Context context;
    List<Emotion> expositions;

    MainExpostionListener listener;
    public interface MainExpostionListener
    {
        public void getPosition(int pos);
    }

    public void setListener(MainExpostionListener mel)
    {
        listener = mel;
    }

    public PlanningExpostionListAdapter(Context ctx)
    {
        this.context = ctx;
    }
    @Override
    public ListExpostionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.expoplannings_list,parent,false);
        return new ListExpostionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListExpostionViewHolder holder, int position) {
        Emotion ep = expositions.get(position);
        holder.title.setText(ep.getName());
        holder.cotation.setText(ep.getCotation());
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return expositions.size();
    }

    public void setExpositions(List<Emotion> exp)
    {
        expositions = exp;
    }

    public void adExposition(Emotion txt)
    {
        expositions.add(txt);
        this.notifyDataSetChanged();
    }

    public class ListExpostionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView title;
        TextView cotation;
        public ListExpostionViewHolder(View v)
        {
            super(v);
            title = (TextView)v.findViewById(R.id.expoplanning_list_title);
            cotation = (TextView)v.findViewById(R.id.expoplanning_list_cotation);
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
