package cn.edu.sdtbu.news.ui.bookmarksAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.edu.sdtbu.news.R;
import cn.edu.sdtbu.news.activity.NewsDetailActivity;
import cn.edu.sdtbu.news.entity.FavoriteListData;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.NewsListViewHolder> {
    List<FavoriteListData> newsList = new ArrayList<>();
    private Context bookMarks;

    public void setContext(Context context) {
        bookMarks = context;
    }

    public NewsListAdapter(List<FavoriteListData> newsList, Context context) {
        this.newsList = newsList;
        this.bookMarks = context;
    }

    @NonNull
    @Override
    public NewsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.book_marks,parent,false);
        //
        return new NewsListViewHolder(itemView);
    }

    /**
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull NewsListViewHolder holder, int position) {
        FavoriteListData news = newsList.get(position);
        holder.tvNewsTitle.setText(news.getNewsTitle());
        holder.tvCreateTime.setText(news.getCreatTime());
        holder.tvTag.setText(news.getTagName());
        holder.tvNewsTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("张海龙", String.valueOf(news.getNewsid()));
                NewsDetailActivity.actionStart(bookMarks,news.getNewsid(),news.getNewsTitle());
            }
        });
    }
    @Override
    public int getItemCount() {
        return newsList.size();
    }
    static class NewsListViewHolder extends RecyclerView.ViewHolder{
        TextView tvNewsTitle,tvCreateTime,tvTag;
        public NewsListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNewsTitle = itemView.findViewById(R.id.tv_news_title);
            tvCreateTime = itemView.findViewById(R.id.tv_create_time);
            tvTag = itemView.findViewById(R.id.tv_tag);
        }
    }

}
