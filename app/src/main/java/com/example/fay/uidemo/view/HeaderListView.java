package com.example.fay.uidemo.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.fay.uidemo.R;
import com.example.fay.uidemo.constant.SP;
import com.example.fay.uidemo.utils.SpUtils;

import java.util.Date;

/**
 * Created by xiaofei9 on 2018/4/27.
 * 封装HeaderListView对象，实现headerView的四种展示状态和下拉刷新的功能
 * 封装下拉刷新数据的接口，将headerView的展示和刷新数据内容解藕，接口方法由headerView的实例实现
 */
public class HeaderListView extends ListView implements AbsListView.OnScrollListener {
    View header;
    int headerHeight;

    int startY;
    boolean isRemark;
    int firstVisibleItem;

    int state;
    final int NONE = 0;      //header处于正常状态
    final int PULL = 1;      //header处于下拉可以刷新状态
    final int RELEASE = 2;   //header处于松开可以刷新状态
    final int REFRESHING = 3;//header处于刷新状态

    String lastUpdate_time;  //上次更新时间
    Context mContext;

    IRefreshListener refreshListener;

    public HeaderListView(Context context) {
        super(context);
        initView(context);
        this.mContext = context.getApplicationContext();
    }

    public HeaderListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        this.mContext = context.getApplicationContext();
    }

    public HeaderListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        this.mContext = context.getApplicationContext();
    }

    /**
     * 初始化headerView，流程如下：
     * 1、通过LayoutInflater解析资源文件转换成headerView对象
     * 2、通知父布局headerView的宽度和高度
     * 3、通过指定topPadding的方式来隐藏headerView
     * 4、将headerView添加到view
     * 5、view添加滑动监听器
     *
     * @param context
     */
    private void initView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        header = inflater.inflate(R.layout.header_listview, null);
        measureView(header);
        headerHeight = header.getMeasuredHeight();
        topPadding(-headerHeight);
        this.addHeaderView(header);
        this.setOnScrollListener(this);
    }

    /**
     * 通知父布局 headerView的宽高
     *
     * @param view
     */
    private void measureView(View view) {
        ViewGroup.LayoutParams p = view.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int width = ViewGroup.getChildMeasureSpec(0, 0, p.width);
        int height;
        if (p.height > 0) {
            height = MeasureSpec.makeMeasureSpec(p.height, MeasureSpec.EXACTLY);
        } else {
            height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        view.measure(width, height);
    }

    /**
     * 设置headerView的顶部位置
     *
     * @param paddingHeight
     */
    private void topPadding(int paddingHeight) {
        header.setPadding(header.getPaddingLeft(), paddingHeight, header.getPaddingRight(), header.getPaddingBottom());
        header.invalidate();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.firstVisibleItem = firstVisibleItem;
    }

    /**
     * 重写touch事件 监听按下、滑动、松开行为
     * 1、按下时获取初使位置
     * 2、滑动时调用滑动视图处理方法
     * 3、松开时根据header的状态选择更新数据和更新view 并重置view的显示状态
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (firstVisibleItem == 0) {
                    startY = (int) ev.getY();
                    isRemark = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                onMove(ev);
                break;
            case MotionEvent.ACTION_UP:
                if (state == RELEASE) {
                    state = REFRESHING;
                    refreshView();
                    if(refreshListener != null) {
                        refreshListener.onRefresh();
                    }
                }else if (state == PULL) {
                    state = NONE;
                    isRemark = false;
                    refreshView();
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 滑动视图处理方法，根据滑动距离判断header应该展示的四种状态，并更新view
     * @param ev
     */
    private void onMove(MotionEvent ev) {
        if (!isRemark) return;
        int space = (int) ev.getY() - startY;
        int topPadding = space - headerHeight;

        switch (state) {
            case NONE:
                if (space > 0) {
                    state = PULL;
                }
                break;
            case PULL:
                topPadding(topPadding);
                if (space >= headerHeight +30) {
                    state = RELEASE;
                }
                refreshView();
                break;
            case RELEASE:
                topPadding(topPadding);
                if (space < headerHeight +30) {
                    state = PULL;
                } else if (space <= 0) {
                    state = NONE;
                    isRemark = false;
                }
                refreshView();
                break;
        }
    }

    /**
     * 更新header的展示高度设置以下四种展示状态：
     * 1、正常状态：header隐藏
     * 2、下拉状态：文字提示为下拉刷新
     * 3、松开刷新状态
     * 4、正在刷新状态
     */
    private void refreshView() {
        TextView tip = header.findViewById(R.id.tip);
        TextView time = header.findViewById(R.id.last_update_time);
        ImageView arrow = header.findViewById(R.id.arrow);
        ProgressBar progress = header.findViewById(R.id.progress);

        RotateAnimation anim1 = new RotateAnimation(0, 180,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        anim1.setDuration(500);
        anim1.setFillAfter(true);

        RotateAnimation anim2 = new RotateAnimation(180,0,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        anim1.setDuration(500);
        anim1.setFillAfter(true);

        switch (state) {
            case NONE:
                topPadding(-headerHeight);
                arrow.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                arrow.clearAnimation();
                tip.setText("下拉可以刷新");
                break;
            case PULL:
                tip.setText("下拉可以刷新");
                arrow.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                arrow.clearAnimation();
                arrow.setAnimation(anim1);
                break;
            case RELEASE:
                tip.setText("松开可以刷新");
                arrow.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                arrow.clearAnimation();
                arrow.setAnimation(anim2);
                break;
            case REFRESHING:
                topPadding(30);
                getCurrentTime();
                arrow.clearAnimation();
                tip.setText("正在刷新......");
                time.setText("上次刷新时间 " + lastUpdate_time);
                arrow.setVisibility(View.GONE);
                progress.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * 获取当前时间并转换为指定日期格式
     */
    @TargetApi(Build.VERSION_CODES.N)
    private void getCurrentTime(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        lastUpdate_time = format.format(date);
        SpUtils.saveToSP(mContext, SP.KEY_LAST_UPDATE_TIME, lastUpdate_time);
    }

    /**
     * 数据刷新完成后 将状态设置为隐藏状态并更新view
     */
    public void refreshComplete(){
        state = NONE;
        isRemark = false;
        refreshView();
    }

    /**
     * 设置数据刷新实例
     * @param refreshListener
     */
    public void setRefreshListener(IRefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }

    /**
     * 刷新数据的接口类
     */
    public interface IRefreshListener {
        void onRefresh();
    }

}
