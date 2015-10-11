package me.rigfox.slivanswer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class EditorActivity extends AppCompatActivity {
    EditorAdapter editorAdapter;
    ListView editorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        editorAdapter = new EditorAdapter(getApplicationContext());

        editorList = (ListView) findViewById(R.id.editorList);
        editorList.setAdapter(editorAdapter);
    }

    public void addItem(View v) {
        EditText editText = (EditText) findViewById(R.id.editorEdit);
        String text = editText.getText().toString();

        if (text.length() != 0) {
            editorAdapter.addItem(text);
            editorList.setAdapter(editorAdapter);
            editText.setText("");
        } else {
            Toast.makeText(EditorActivity.this, "Пустой слив!", Toast.LENGTH_SHORT).show();
        }
    }

    public void toGameActivity(View v) {
        if (editorAdapter.objects.size() != 0) {
            Intent intent = new Intent(EditorActivity.this, GameActivity.class);
            intent.putExtra("GameWords", editorAdapter.objects);
            startActivity(intent);
        } else {
            Toast.makeText(EditorActivity.this, "Мало слива!", Toast.LENGTH_SHORT).show();
        }
    }
}

class EditorAdapter extends BaseAdapter {
    Context ctx;
    ArrayList<String> objects;
    LayoutInflater lInflater;

    EditorAdapter(Context context) {
        ctx = context;
        objects = new ArrayList<>();
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
            view = lInflater.inflate(R.layout.editor_item, parent, false);
        }

        String editorText = getEditorItem(position);

        ((TextView) view.findViewById(R.id.editorTextView)).setText(editorText);

        return view;
    }

    String getEditorItem(int position) {
        return (String) getItem(position);
    }

    public void addItem(String text) {
        objects.add(text);
    }
}

