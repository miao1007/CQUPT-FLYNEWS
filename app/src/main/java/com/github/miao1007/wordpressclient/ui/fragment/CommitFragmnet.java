package com.github.miao1007.wordpressclient.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.miao1007.wordpressclient.R;
import com.github.miao1007.wordpressclient.info.comment.CommentResult;
import com.github.miao1007.wordpressclient.model.Model;
import com.github.miao1007.wordpressclient.utils.UIutils;
import com.github.miao1007.wordpressclient.utils.WordPressUtils;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by leon on 14/10/6.
 */
public class CommitFragmnet extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getActionBar().setTitle(R.string.commit_message);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        //http://leondemac.jd-app.com/api/submit_comment/?post_id=177&name=123&email=6621@qq.com&content=qawedsad
        View view = inflater.inflate(R.layout.frag_commit, null);
        TextView content_textview = (TextView) view.findViewById(R.id.frag_commit_textview_content);
        final EditText editText_name = (EditText) view.findViewById(R.id.frag_commit_editText_name);
        final EditText editText_email = (EditText) view.findViewById(R.id.frag_commit_editText_email);
        final EditText editText_content = (EditText) view.findViewById(R.id.frag_commit_editText_content);
        view.findViewById(R.id.frag_commit_commit).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String name = editText_name.getText().toString();
                String email = editText_email.getText().toString();
                String content = editText_content.getText().toString();
                if (name.isEmpty() || email.isEmpty() || content.isEmpty()) {
                    Toast.makeText(getActivity(), "请正确填写", Toast.LENGTH_SHORT).show();
                } else {

                    WordPressUtils.commitComment(Model.FEED_COMMIT_POST_ID, name, email, content, new Callback<CommentResult>() {
                        @Override
                        public void success(CommentResult commentResult, Response response) {
                            UIutils.disSucc(getActivity(),response);
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            UIutils.disErr(getActivity(), error);
                        }
                    });
                }
            }
        });
        return view;
    }
}

