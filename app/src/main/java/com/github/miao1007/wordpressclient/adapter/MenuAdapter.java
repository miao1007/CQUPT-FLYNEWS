package com.github.miao1007.wordpressclient.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.miao1007.wordpressclient.R;
import com.github.miao1007.wordpressclient.info.post.Category;
import com.github.miao1007.wordpressclient.model.Model;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.HashMap;
import java.util.List;

public class MenuAdapter extends BaseAdapter {

    private static final int INCLUDE_VIEW_COUNT = 5;


    private Context context;

    private List<Category> categories;
    private HashMap<String,String> avatarandname;


    public MenuAdapter(Context context, List<Category> categories , HashMap<String,String> avatarandname) {
        this.context = context;
        this.categories = categories;
        this.avatarandname = avatarandname;
    }

    @Override
    public int getCount() {
        return categories.size() + INCLUDE_VIEW_COUNT ;
    }

    @Override
    public Category getItem(int position) {
        if ( position < INCLUDE_VIEW_COUNT ){
            return new Category();
        } else {
            return categories.get(position - INCLUDE_VIEW_COUNT);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if (position < INCLUDE_VIEW_COUNT){
            switch (position){
                case 0:view = LayoutInflater.from(context).inflate(R.layout.slidingmenu_header_user_info, null);
                    TextView textView_username = (TextView)view.findViewById(R.id.slidingmenu_header_username);
                    ImageView imageView_avatar = (ImageView)view.findViewById(R.id.slidingmenu_header_avatar);
                    textView_username.setText(avatarandname.get(Model.USER_NAME));
                    Picasso.with(context)
                            .load(avatarandname.get(Model.USER_AVATAR))
                            .placeholder(R.drawable.logo_qzone)
                            .error(R.drawable.weibo_login)
                            .resize(116,116)
                            .transform(new Transformation() {
                                float radius = 58;
                                float margin = 0;
                                @Override
                                public Bitmap transform(final Bitmap source) {
                                    //transform into round cornered Image
                                    final Paint paint = new Paint();
                                    paint.setAntiAlias(true);
                                    paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));

                                    Bitmap output = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
                                    Canvas canvas = new Canvas(output);
                                    canvas.drawRoundRect(new RectF(margin, margin, source.getWidth() - margin, source.getHeight() - margin), radius, radius, paint);

                                    if (source != output) {
                                        source.recycle();
                                    }

                                    return output;
                                }

                                @Override
                                public String key() {
                                    return "rounded";
                                }
                            })
                            .into(imageView_avatar);
                    break;
                case 1:view = LayoutInflater.from(context).inflate(R.layout.slidingmenu_choice_home, null);break;
                case 2:view = LayoutInflater.from(context).inflate(R.layout.slidingmenu_choice_search, null);break;
                case 3:view = LayoutInflater.from(context).inflate(R.layout.slidingmenu_choice_settings, null);break;
                case 4:view = LayoutInflater.from(context).inflate(R.layout.slidingmenu_choice_commit, null);break;
            }
            return view;
        } else {

            if ( position %2 == 1 ){
                view = LayoutInflater.from(context).inflate(R.layout.slidingmenu_categories_white, null);
            } else {
                view = LayoutInflater.from(context).inflate(R.layout.slidingmenu_categories, null);
            }

            TextView category_type = (TextView)view.findViewById(R.id.slidingmenu_categories_tpye);
            category_type.setText(getItem(position).getTitle());
            return view;
        }
    }

}
