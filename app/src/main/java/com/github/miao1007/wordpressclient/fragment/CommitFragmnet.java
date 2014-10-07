package com.github.miao1007.wordpressclient.fragment;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.github.miao1007.wordpressclient.R;
import com.github.miao1007.wordpressclient.model.comment.CommentResult;
import com.github.miao1007.wordpressclient.utils.WordPressUtils;

/**
 * Created by leon on 14/10/6.
 */
public class CommitFragmnet extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        //http://leondemac.jd-app.com/api/submit_comment/?post_id=177&name=123&email=6621@qq.com&content=qawedsad
        View view = inflater.inflate(R.layout.frag_commit, null);
        final EditText editText_name = (EditText)view.findViewById(R.id.frag_commit_editText_name);
        final EditText editText_email = (EditText)view.findViewById(R.id.frag_commit_editText_content);
        final EditText editText_content = (EditText)view.findViewById(R.id.frag_commit_editText_content);
        view.findViewById(R.id.frag_commit_commit).setOnClickListener(new View.OnClickListener() {
            CommentResult commentResult;
            //post id = 2
            //http://leondemac.jd-app.com/wp-admin/post.php?post=2&action=edit

            @Override
            public void onClick(View view) {
                String name = editText_name.getText().toString();
                String email = editText_email.getText().toString();
                String content = editText_content.getText().toString();
                if (name.isEmpty() || email.isEmpty() || content.isEmpty()) {
                    Toast.makeText(getActivity(), "请正确填写", Toast.LENGTH_SHORT).show();
                } else {

                    new AsyncTask<String, Void, CommentResult>() {
                        @Override
                        protected CommentResult doInBackground(String... strings) {
                            return WordPressUtils.commitComment("2", strings[0], strings[1], strings[2]);
                        }

                        @Override
                        protected void onPostExecute(CommentResult commentResult) {
                            super.onPostExecute(commentResult);
                            if (commentResult != null) {
                                if (commentResult.getStatus().equals("pending")) {
                                    Toast.makeText(getActivity(), "Server:非常感谢,我们将及时回复您", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getActivity(), "Server:" + commentResult.getStatus(), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Server:检测到重复数据", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }.execute(name, email, content);
                }
            }
        });
        return view;
    }
}
