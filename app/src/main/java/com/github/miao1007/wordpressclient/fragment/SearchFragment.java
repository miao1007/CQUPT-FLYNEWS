package com.github.miao1007.wordpressclient.fragment;

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
public class SearchFragment extends CategoryFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View searchView = inflater.inflate(R.layout.frag_setting,null);
        final EditText editText_query = (EditText)searchView.findViewById(R.id.frag_search_edittext_content);
        searchView.findViewById(R.id.frag_search_dosearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = editText_query.getText().toString();
                if ( !query.isEmpty()){
                    setResult(query);
                } else {
                    Toast.makeText(getActivity(),"请输入内容",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return searchView;
    }

    private void setResult(String query){
//        //start activity for result
//        Intent mIntent = getActivity().getIntent();
//        int resultcode = 3;
//        mIntent.putExtra("query", query);
//        // 设置结果，并进行传送
//        getActivity().setResult(resultcode, mIntent);
        CategoryFragment fragment = new CategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putString("query", query);
        fragment.setArguments(bundle);

    }

}
