package cn.edu.sdtbu.news.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cn.edu.sdtbu.news.R;
import cn.edu.sdtbu.news.entity.NewsVO;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.NewsListViewHolder> {
    List<NewsVO> newsList = new ArrayList<>();


    public NewsListAdapter(List<NewsVO> newsList) {
        this.newsList = newsList;
    }

    @NonNull
    @Override
    public NewsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.list_item,parent,false);
        //
        return new NewsListViewHolder(itemView);
    }

    /**
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull NewsListViewHolder holder, int position) {
        NewsVO news = newsList.get(position);
        holder.tvNewsTitle.setText(news.getTitle());
        holder.tvNewsBrief.setText(news.getContent());
        holder.tvCreateTime.setText(news.getCreateTime());
        holder.tvTag.setText(news.getTagName());
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    static class NewsListViewHolder extends RecyclerView.ViewHolder{
        TextView tvNewsTitle,tvNewsBrief,tvCreateTime,tvTag;

        public NewsListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNewsTitle = itemView.findViewById(R.id.tv_news_title);
            tvNewsBrief = itemView.findViewById(R.id.tv_news_brief);
            tvCreateTime = itemView.findViewById(R.id.tv_create_time);
            tvTag = itemView.findViewById(R.id.tv_tag);
        }
    }

}
