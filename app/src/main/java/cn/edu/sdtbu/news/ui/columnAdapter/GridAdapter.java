package cn.edu.sdtbu.news.ui.columnAdapter;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import cn.edu.sdtbu.news.R;
import cn.edu.sdtbu.news.entity.ChoseItem;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.GridViewHolder> {
    private static final String TAG = GridAdapter.class.getSimpleName();
    HashSet<String> choseItemsNameSet = new HashSet<>();
    private Context choseItemsContext;
    private ArrayList<ChoseItem> choseItemsList = new ArrayList<>();

    public GridAdapter(Context context) {
        choseItemsContext = context;
    }
    public HashSet<String> getmNameSet(){
        return choseItemsNameSet;
    }
    public void setGridDataList(ArrayList list) {
        choseItemsList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(choseItemsContext).inflate(R.layout.grid_recycle_item, parent, false);
        return new GridViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final GridViewHolder holder, final int position) {
//        holder.tvNum.setText(position + 1 + "");
        holder.btn_chose.setText(choseItemsList.get(position).getName());
        holder.btn_chose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChose = choseItemsList.get(position).isChose();
                if (!isChose) {
                    if (choseItemsNameSet.size() == 10) {
                        Toast.makeText(choseItemsContext, "最多选择十个", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    choseItemsList.get(position).setChose(true);
                    choseItemsNameSet.add(choseItemsList.get(position).getName());
                } else {
                    choseItemsList.get(position).setChose(false);
                    choseItemsNameSet.remove(choseItemsList.get(position).getName());
                }
                updateStatus(holder.btn_chose, isChose, choseItemsNameSet);
                // mChoseEvent.setNameSet(mNameSet);
                // EventBus.getDefault().post(mChoseEvent);
            }
        });
    }

    private void updateStatus(Button textView, boolean isChose, Set<String> set) {
        if (!isChose) {
            if (set.size() < 10) {
                textView.setTextColor(Color.WHITE);
                textView.setBackgroundResource(R.drawable.btn_cycle_tinge);
            }
        } else {
            textView.setTextColor(Color.parseColor("#323f8a"));
            textView.setBackgroundResource(R.drawable.btn_cycle_white);
        }
    }


   @Override
    public int getItemCount() {
        return choseItemsList == null ? 0 : choseItemsList.size();
    }
    public class GridViewHolder extends RecyclerView.ViewHolder {
        Button btn_chose;
        public GridViewHolder(View itemView) {
            super(itemView);
            btn_chose = itemView.findViewById(R.id.button);
        }
    }

    /*public static class ChoseEvent {
        public HashSet<String> NameSet;
        public void setNameSet(HashSet<String> nameSet) {
            NameSet = nameSet;
        }
        public HashSet<String> getNameSet() {
            return NameSet;
        }
        private ChoseEvent() {
        }
    }*/
}
