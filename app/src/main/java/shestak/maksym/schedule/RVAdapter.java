package shestak.maksym.schedule;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import shestak.maksym.schedule.db.dao.ClassDao;
import shestak.maksym.schedule.src.max.Class;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ClassViewHolder> {
    List<ClassDao> classes;


    String TIME[] = {
            "08:15-09:35",
            "09:50-11:10",
            "11:25-12:45",
            "13:25-14:45",
            "15:00-16:20",
            "16:35-17:55",
            "18:00-19:20",
            "19:25-20:45"
    };

    public RVAdapter(List<ClassDao> classes) {
        this.classes = classes;
    }

    @Override
    public void onViewRecycled(ClassViewHolder holder) {
        //Log.d("max", "onViewRecycled");
        super.onViewRecycled(holder);
    }

    @Override
    public ClassViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Log.d("max", "onCreateViewHolder");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lec, parent, false);
        return new ClassViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ClassViewHolder holder, int position) {
        //Log.d("max", "onBindViewHolder");
        //todo time
        //holder.time.setText(classes.get(position).time);
        holder.classN.setText(classes.get(position).classN);
        holder.title.setText(classes.get(position).title);


        holder.time.setText(TIME[Integer.valueOf(classes.get(position).classN) - 1]);

        if(!classes.get(position).type.isEmpty()) {
            holder.type.setText(classes.get(position).type);
            holder.type.setVisibility(View.VISIBLE);
        }
        else
            holder.type.setVisibility(View.GONE);
        //holder.type.setText(classes.get(position).type);


        if(!classes.get(position).auditorium.isEmpty()) {
            holder.auditorium.setText(classes.get(position).auditorium);
            holder.auditorium.setVisibility(View.VISIBLE);
        }
        else
            holder.auditorium.setVisibility(View.GONE);
        //holder.auditorium.setText(classes.get(position).auditorium);

        if(!classes.get(position).lecturer.isEmpty()){
            holder.lecturer.setText(classes.get(position).lecturer);
            holder.lecturer.setVisibility(View.VISIBLE);
        }
        else
            holder.lecturer.setVisibility(View.GONE);
        //holder.lecturer.setText(classes.get(position).lecturer);


        holder.group.setText(classes.get(position).group);


        if(!classes.get(position).date.isEmpty()) {
            holder.date.setText(classes.get(position).date);
            holder.date.setVisibility(View.VISIBLE);
        }
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
