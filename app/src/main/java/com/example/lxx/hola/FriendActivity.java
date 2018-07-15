package com.example.lxx.hola;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by echo on 2017/9/3.
 */

public class FriendActivity extends ListActivity {
    private List<Map<String, Object>> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mData = getData();
        MyAdapter adapter = new MyAdapter(this);
        setListAdapter(adapter);
    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("friend_name", "Tiffany Simmons");
        map.put("friend_sign", "Retro occupy organic, stumptown …");
        map.put("friend_head", R.drawable.port1);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("friend_name", "Gerald Beck");
        map.put("friend_sign", "Fixie tote bag ethnic keytar. Neutra …");
        map.put("friend_head", R.drawable.port2);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("friend_name", "Juan Barrett");
        map.put("friend_sign", "Bushwick meh Blue Bottle pork belly …");
        map.put("friend_head", R.drawable.head3);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("friend_name", "Samantha Romero");
        map.put("friend_sign", "Synth polaroid bitters chillwave pickled…");
        map.put("friend_head", R.drawable.head4);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("friend_name", "Crystal Welch");
        map.put("friend_sign", "Synth polaroid bitters chillwave pickled…");
        map.put("friend_head", R.drawable.head5);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("friend_name", "Anna Armstrong");
        map.put("friend_sign", "Keytar McSweeney's Williamsburg, rea…");
        map.put("friend_head", R.drawable.head6);
        list.add(map);

        return list;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Intent intent = new Intent(FriendActivity.this,FriendInfoActivity.class);
        intent.putExtra("friend_head", (Integer) mData.get(position).get("friend_head"));
        intent.putExtra("friend_name", (String) mData.get(position).get("friend_name"));
        intent.putExtra("friend_sign", (String) mData.get(position).get("friend_sign"));
        startActivity(intent);
        finish();
    }

    public final class ViewHolder{
        public ImageView friend_head;
        public TextView friend_name;
        public TextView friend_sign;
    }


    public class MyAdapter extends BaseAdapter{

        private LayoutInflater mInflater;

        public MyAdapter(Context context){
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            if(convertView == null){

                holder = new ViewHolder();

                convertView = mInflater.inflate(R.layout.item_friend,null);
                holder.friend_head = (ImageView)convertView.findViewById(R.id.friend_head);
                holder.friend_name = (TextView)convertView.findViewById(R.id.friend_name);
                holder.friend_sign = (TextView)convertView.findViewById(R.id.friend_sign);
                convertView.setTag(holder);
            }
            else{

                holder = (ViewHolder)convertView.getTag();
            }

            holder.friend_head.setBackgroundResource((Integer)mData.get(position).get("friend_head"));
            holder.friend_name.setText((String)mData.get(position).get("friend_name"));
            holder.friend_sign.setText((String)mData.get(position).get("friend_sign"));

            return convertView;
        }
    }






















}
