package com.github.miao1007.wordpressclient.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.github.miao1007.wordpressclient.R;

/**
 * Created by leon on 14/10/6.
 */
public class SearchFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_search , null);
        final EditText editText_query = (EditText)view.findViewById(R.id.frag_search_edittext_content);
        view.findViewById(R.id.frag_search_dosearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = editText_query.getText().toString();
                if ( !query.isEmpty()){
                   //todo

                } else {
                    Toast.makeText(getActivity(),"请输入内容",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }


}
