package cn.edu.sdtbu.news.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import cn.edu.sdtbu.news.R;
import cn.edu.sdtbu.news.entity.lishengqi.CommentDetail;
import cn.edu.sdtbu.news.entity.lishengqi.CommentReplyDetail;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Author: lishengqi
 * Date:  2020/7/1
 * Describe: 评论与回复列表的适配器
 */

public class MyExpandAdapter extends BaseExpandableListAdapter {
    private static final String TAG = "CommentExpandAdapter";
    private List<CommentDetail> commentBeanList;
    private List<CommentReplyDetail> replyBeanList;
    private Context context;

    public MyExpandAdapter(Context context, List<CommentDetail> commentBeanList) {
        this.context = context;
        this.commentBeanList = commentBeanList;
    }
    //获得父列表的数目
    @Override
    public int getGroupCount() {
        return commentBeanList.size();
    }

    //获得子列表项的数目
    @Override
    public int getChildrenCount(int i) {
        if(commentBeanList.get(i).getReplyList() == null){
            return 0;
        }else {
            return commentBeanList.get(i).getReplyList().size()>0 ? commentBeanList.get(i).getReplyList().size():0;
        }

    }

    //获得父列表项
    @Override
    public Object getGroup(int i) {
        return commentBeanList.get(i);
    }


    //获取子列表项对应的Item
    @Override
    public Object getChild(int i, int i1) {
        return commentBeanList.get(i).getReplyList().get(i1);
    }

    //获得父列表项的Id
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
    //获得子列表项的Id
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return getCombinedChildId(groupPosition, childPosition);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
    boolean isLike = false;

    //获得父列表项，与getChildView方法类似
    @Override
    public View getGroupView(final int groupPosition, boolean isExpand, View convertView, ViewGroup viewGroup) {
        final GroupHolder groupHolder;

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.comment_item_layout, viewGroup, false);
            groupHolder = new GroupHolder(convertView);
            convertView.setTag(groupHolder);
        }else {
            groupHolder = (GroupHolder) convertView.getTag();
        }

        Glide.with (context).load(commentBeanList.get(groupPosition).getUserLogo()).into(groupHolder.logo) ;
        groupHolder.tv_name.setText(commentBeanList.get(groupPosition).getNickName());
        groupHolder.tv_time.setText(commentBeanList.get(groupPosition).getCreateDate());
        groupHolder.tv_content.setText(commentBeanList.get(groupPosition).getContent());


        return convertView;
    }

    //获得子列表项，比较重要、难理解的一个方法
    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean b, View convertView, ViewGroup viewGroup) {
        final ChildHolder childHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.comment_reply_item_layout,viewGroup, false);
            childHolder = new ChildHolder(convertView);
            convertView.setTag(childHolder);
        }
        else {
            childHolder = (ChildHolder) convertView.getTag();
        }

        String replyUser = commentBeanList.get(groupPosition).getReplyList().get(childPosition).getNickName();
        if(!TextUtils.isEmpty(replyUser)){
            childHolder.tv_name.setText(replyUser + ":");
        }else {
            childHolder.tv_name.setText("无名"+":");
        }

        Glide.with (context).load(commentBeanList.get(groupPosition).getReplyList().get(childPosition).getUserLogo()).into(ChildHolder.replyLogo) ;
        childHolder.tv_content.setText(commentBeanList.get(groupPosition).getReplyList().get(childPosition).getContent());
        childHolder.tv_time.setText(commentBeanList.get(groupPosition).getReplyList().get(childPosition).getCreateDate());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    private class GroupHolder{
        private CircleImageView logo;
        private TextView tv_name, tv_content, tv_time;
        public GroupHolder(View view) {
            logo =  view.findViewById(R.id.comment_item_logo);
            tv_content =  view.findViewById(R.id.comment_item_content);
            tv_name =  view.findViewById(R.id.comment_item_userName);
            tv_time =  view.findViewById(R.id.comment_item_time);
        }
    }

    private static class ChildHolder{
        private static CircleImageView replyLogo;
        private TextView tv_name, tv_content, tv_time;
        public ChildHolder(View view) {
            replyLogo = view.findViewById(R.id.reply_item_logo);
            tv_name =  view.findViewById(R.id.reply_item_user);
            tv_content =  view.findViewById(R.id.reply_item_content);
            tv_time =  view.findViewById(R.id.reply_item_content_time);
        }
    }


    /**
     * 评论成功后插入一条数据
     * @param CommentDetail
     */
    public void addTheCommentData(CommentDetail CommentDetail){
        if(CommentDetail!=null){

            commentBeanList.add(CommentDetail);
            notifyDataSetChanged();
        }else {
            throw new IllegalArgumentException("评论数据为空!");
        }
    }

    /**
     * 回复成功后插入一条数据
     * @param CommentReplyDetail
     * @param groupPosition
     */
    public void addTheReplyData(CommentReplyDetail CommentReplyDetail, int groupPosition){
        if(CommentReplyDetail!=null){
            Log.e(TAG, "addTheReplyData: >>>>该刷新回复列表了:"+CommentReplyDetail.toString() );
            if(commentBeanList.get(groupPosition).getReplyList() != null ){
                commentBeanList.get(groupPosition).getReplyList().add(CommentReplyDetail);
            }else {
                List<CommentReplyDetail> replyList = new ArrayList<>();
                replyList.add(CommentReplyDetail);
                commentBeanList.get(groupPosition).setReplyList(replyList);
            }
            notifyDataSetChanged();
        }else {
            throw new IllegalArgumentException("回复数据为空!");
        }

    }

    /**
     * 添加和展示所有回复
     * @param replyBeanList 所有回复数据
     * @param groupPosition 当前的评论
     */
    private void addReplyList(List<CommentReplyDetail> replyBeanList, int groupPosition){
        if(commentBeanList.get(groupPosition).getReplyList() != null ){
            commentBeanList.get(groupPosition).getReplyList().clear();
            commentBeanList.get(groupPosition).getReplyList().addAll(replyBeanList);
        }else {

            commentBeanList.get(groupPosition).setReplyList(replyBeanList);
        }
        notifyDataSetChanged();
    }

}
