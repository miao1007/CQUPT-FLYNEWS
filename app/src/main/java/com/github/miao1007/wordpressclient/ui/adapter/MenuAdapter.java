package com.github.miao1007.wordpressclient.ui.adapter;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class MenuAdapter extends BaseAdapter {

    private static final int INCLUDE_VIEW_COUNT = 5;


    private Context context;
    private SharedPreferences sharedPreferences;

    public MenuAdapter(Context context,SharedPreferences sharedPreferences) {
        this.context = context;
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public int getCount() {
        return INCLUDE_VIEW_COUNT;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {


        switch (position) {
            case 0:
                view = LayoutInflater.from(context).inflate(R.layout.slidingmenu_header_user_info, null);
                TextView textView_username = (TextView) view.findViewById(R.id.slidingmenu_header_username);
                ImageView imageView_avatar = (ImageView) view.findViewById(R.id.slidingmenu_header_avatar);
                textView_username.setText(sharedPreferences.getString("name", context.getString(R.string.user_login)));
                Picasso.with(context)
                        .load(sharedPreferences.getString("avatar", context.getString(R.string.defaultavatar_link)))
                        .placeholder(R.drawable.logo_qzone)
                        .error(R.drawable.weibo_login)
                        .resize(116, 116)
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
            case 1:
                view = LayoutInflater.from(context).inflate(R.layout.slidingmenu_choice_home, null);
                break;
            case 2:
                view = LayoutInflater.from(context).inflate(R.layout.slidingmenu_choice_search, null);
                break;
            case 3:
                view = LayoutInflater.from(context).inflate(R.layout.slidingmenu_choice_settings, null);
                break;
            case 4:
                view = LayoutInflater.from(context).inflate(R.layout.slidingmenu_choice_commit, null);
                break;
        }
        return view;

    }

    @Override
    public Object getItem(int i) {
        return null;
    }
}
