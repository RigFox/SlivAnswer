package me.rigfox.slivanswer;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ResultAdapter extends BaseAdapter {
    Context ctx;
    ArrayList<ResultItem> objects;
    LayoutInflater lInflater;

    ResultAdapter(Context context, ArrayList<ResultItem> resultList) {
        ctx = context;
        objects = resultList;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.result_item, parent, false);
        }

        ResultItem resultItem = getResultItem(position);

        ((TextView) view.findViewById(R.id.resultText)).setText(resultItem.word);

        if (resultItem.status) {
            view.findViewById(R.id.bodyResultItem).setBackgroundColor(Color.GREEN);
        } else {
            view.findViewById(R.id.bodyResultItem).setBackgroundColor(Color.RED);
        }

        return view;
    }

    ResultItem getResultItem(int position) {
        return ((ResultItem) getItem(position));
    }
}

class ResultItem {
    String word;
    boolean status;

    ResultItem(String getword, boolean getstatus) {
        word = getword;
        status = getstatus;
    }
}
