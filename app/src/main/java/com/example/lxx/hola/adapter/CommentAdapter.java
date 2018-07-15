package com.example.lxx.hola.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.view.View;
import android.widget.TextView;

import com.example.lxx.hola.R;
import com.example.lxx.hola.model.Comment;

import org.w3c.dom.Text;

import java.util.List;




public class CommentAdapter extends BaseAdapter{
    Context context;
    List<Comment> data;

    public CommentAdapter(Context c, List<Comment> data){
        this.context = c;
        this.data = data;
    }

    @Override
    public int getCount(){
        return data.size();
    }

    @Override
    public Object getItem(int i){
        return data.get(i);
    }

    @Override
    public long getItemId(int i){
        return  i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup){
        ViewHolder holder;
        //重用convertView
        if(convertView==null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_comment,null);
            holder.comment_name = (TextView) convertView.findViewById(R.id.comment_name);
            holder.comment_content = (TextView) convertView.findViewById(R.id.comment_content);
            holder.comment_time = (TextView) convertView.findViewById(R.id.comment_time);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        // 适配数据
        holder.comment_name.setText(data.get(i).getName());
        holder.comment_content.setText(data.get(i).getContent());
        holder.comment_time.setText(data.get(i).getTime());

        return convertView;
        }
    /**
     * 添加一条评论,刷新列表
     * @param comment
     */
    public void addComment(Comment comment){
        data.add(comment);
        notifyDataSetChanged();
    }

    /**
     * 静态类，便于GC回收
     */
    public static class ViewHolder{
        TextView comment_name;
        TextView comment_content;
        TextView comment_time;
    }
}