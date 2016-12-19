package com.example.adnan.panachatfragment.Adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adnan.panachatfragment.R;
import com.example.adnan.panachatfragment.Signatures.CallHistory;
import com.example.adnan.panachatfragment.Signatures.Frient_Req_Signature;
import com.firebase.client.Firebase;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Adnan on 8/29/2016.
 */
public class CallAdaptor extends BaseAdapter {

    ArrayList<CallHistory> list;
    Context con;

    public CallAdaptor(Context con, ArrayList<CallHistory> list) {
        this.con = con;
        this.list = list;
        inflater = LayoutInflater.from(con);
    }

    LayoutInflater inflater;

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        View v = inflater.inflate(R.layout.layout_history, parent, false);
        CircularImageView imageView = (CircularImageView) v.findViewById(R.id.Historycircular);
        imageView.setBorderWidth(0);
        TextView name = (TextView) v.findViewById(R.id.historyName);
        TextView date = (TextView) v.findViewById(R.id.historyDate);
        TextView status = (TextView) v.findViewById(R.id.historyStatus);
        ImageButton btn = (ImageButton) v.findViewById(R.id.imageButtonCall);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(con, "Currently Not Working", Toast.LENGTH_SHORT).show();
            }
        });
        Picasso.with(con).load(list.get(position).getPicUrl()).placeholder(R.drawable.userdefaul).error(R.drawable.userdefaul).into(imageView);
        name.setText(list.get(position).getName());
        date.setText(list.get(position).getDate());
        status.setText(list.get(position).getStatus());

        return v;
    }
}
