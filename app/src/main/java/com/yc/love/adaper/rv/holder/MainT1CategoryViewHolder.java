package com.yc.love.adaper.rv.holder;

import android.content.Context;
import android.support.v7.widget.SearchView;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yc.love.R;
import com.yc.love.adaper.rv.base.RecyclerViewItemListener;
import com.yc.love.model.bean.LoveHealBean;


public class MainT1CategoryViewHolder extends BaseViewHolder<LoveHealBean> {

    private final Context context;
    private OnClickTitleIconListener onClickTitleIconListener;

    public MainT1CategoryViewHolder(Context context, RecyclerViewItemListener listener, ViewGroup parent) {
        super(context, parent, R.layout.recycler_view_item_t1category, listener);   //一个类对应一个布局文件
        this.context = context;
    }

    @Override
    public void bindData(LoveHealBean exampListsBean) {

        final SearchView searchView = itemView.findViewById(R.id.item_t1category_share_view);
        final ImageView ivIconShare = itemView.findViewById(R.id.item_t1category_iv_icon_share);


        final ImageView searchIcon = searchView.findViewById(android.support.v7.appcompat.R.id.search_mag_icon);
        searchView.post(new Runnable() {
            @Override
            public void run() {
                searchIcon.setImageDrawable(null);
                searchIcon.setVisibility(View.GONE);
            }
        });
        //修改键入的文字字体大小、颜色和hint的字体颜色
        final EditText editText = searchView.findViewById(R.id.search_src_text);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources()
                .getDimension(R.dimen.size_16));
//        editText.setTextColor(ContextCompat.getColor(this,R.color.nb_text_primary));

        //监听关闭按钮点击事件
        ImageView mCloseButton = searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        final TextView textView = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        if (mCloseButton.isClickable()) {
            mCloseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //清除搜索框并加载默认数据
//                    hindShareItemShowInfo();
                    textView.setText(null);
                }
            });
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { //搜索按钮回调
                onClickTitleIconListener.clickIconShare(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) { //输入变化回调
//                ShareActivity.this.shareTextString = newText;
                return false;
            }
        });

        ivIconShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickTitleIconListener.clickIconShare(searchView.getQuery().toString().trim());
            }
        });

    }

    public interface OnClickTitleIconListener {
        void clickIconShare(String keyword);
    }

    public void setOnClickTitleIconListener(OnClickTitleIconListener onClickTitleIconListener) {
        this.onClickTitleIconListener = onClickTitleIconListener;
    }



}