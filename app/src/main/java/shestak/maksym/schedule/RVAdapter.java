package shestak.maksym.schedule;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ClassViewHolder> {
    List<Class> classes;

    public RVAdapter(List<Class> classes) {
        this.classes = classes;
    }

    @Override
    public ClassViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lec, parent, false);
        return new ClassViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ClassViewHolder holder, int position) {
        holder.time.setText(classes.get(position).time);
        holder.classN.setText(classes.get(position).classN);
        holder.title.setText(classes.get(position).title);
        holder.type.setText(classes.get(position).type);
        holder.auditorium.setText(classes.get(position).auditorium);
        holder.lecturer.setText(classes.get(position).lecturer);
        holder.group.setText(classes.get(position).group);
        if(!classes.get(position).date.isEmpty())
            holder.date.setText(classes.get(position).date);
        else
            holder.date.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return classes.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    class ClassViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView time;
        TextView classN;
        TextView title;
        TextView type;
        TextView auditorium;
        TextView lecturer;
        TextView group;
        TextView date;
        public ClassViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            time = (TextView) itemView.findViewById(R.id.time);
            classN = (TextView) itemView.findViewById(R.id.classN);
            title = (TextView) itemView.findViewById(R.id.title);
            type = (TextView) itemView.findViewById(R.id.type);
            auditorium = (TextView) itemView.findViewById(R.id.auditorium);
            lecturer = (TextView) itemView.findViewById(R.id.lecturer);
            group = (TextView) itemView.findViewById(R.id.group);
            date = (TextView) itemView.findViewById(R.id.date);
        }
    }
}
