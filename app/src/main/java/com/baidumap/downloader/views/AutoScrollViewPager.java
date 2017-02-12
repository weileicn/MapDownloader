package com.baidumap.downloader.views;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

/**
 * Created by weilei on 16/8/15.
 */
public class AutoScrollViewPager extends ViewPager implements Runnable {

    private static final String TAG = AutoScrollViewPager.class.getSimpleName();
    private PagerAdapter pagerAdapter;

    private static final int POST_DELAYED_TIME = 1000 * 4;

    private static final int MAX_COUNT = 1000;

    private boolean touching;
    private boolean mIsAttached;
    // 更新数据需要获得myPagerAdapter
    private PagerAdapter myPagerAdapter;

    private Scroller mScroller;
    public AutoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context, new DecelerateInterpolator());
        setScroller(mScroller);
    }

    // 对setAdapter的数据进行包装
    private class MyPagerAdapter extends PagerAdapter {

        private PagerAdapter pa;

        public MyPagerAdapter(PagerAdapter pa) {
            this.pa = pa;
        }

        @Override
        // 关键之一:修改Count长度
        public int getCount() {
            return MAX_COUNT;
        }

        @Override
        // 这里是关键之二:修改索引(如果不考虑内容问题可以全部加载进数组然后操作更简单)
        public Object instantiateItem(ViewGroup container, int position) {

            if (pa.getCount() == 0)
                return null;
            return pa.instantiateItem(container, getRealPosition(position, pa.getCount()));
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            pa.destroyItem(container, position, object);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return pa.isViewFromObject(arg0, arg1);
        }
    }

    private int getRealPosition(int position, int realCount) {
        if (position == 0) {
            position = MAX_COUNT - 1;
        } else if (position == MAX_COUNT - 1) {
            position = 0;
        }
        return ((position - MAX_COUNT / 2) % realCount + realCount) % realCount;
    }

    // 包装setOnPageChangeListener的数据
    private class MyOnPageChangeListener implements OnPageChangeListener {

        private OnPageChangeListener listener;

        // 是否已经提前触发了OnPageSelected事件
        private boolean alreadyTriggerOnPageSelected;

        public MyOnPageChangeListener(OnPageChangeListener listener) {
            this.listener = listener;
        }

        @Override
        // 关键之三:
        public void onPageScrollStateChanged(int arg0) {
            listener.onPageScrollStateChanged(arg0);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            listener.onPageScrolled(arg0, arg1, arg2);
        }

        @Override
        // 关键四:
        public void onPageSelected(int arg0) {
            if (pagerAdapter != null) {
                listener.onPageSelected(getRealPosition(arg0, pagerAdapter.getCount()));
            }
        }
    }

    protected OnPageChangeListener mOnPageChangeListener = null;

    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        mOnPageChangeListener = listener == null ? null
                : new MyOnPageChangeListener(listener);
        super.setOnPageChangeListener(mOnPageChangeListener);
    }

    @Override
    public void setAdapter(PagerAdapter arg0) {

        int old_pos = MAX_COUNT / 2;
        if (myPagerAdapter != null)
            old_pos = getCurrentItem();
        myPagerAdapter = arg0 == null ? null : new MyPagerAdapter(arg0);
        super.setAdapter(myPagerAdapter);
        setCurrentItem(old_pos, false);
        this.pagerAdapter = arg0;
    }

    @Override
    // 兼容PageIndicator
    public PagerAdapter getAdapter() {
        return pagerAdapter;
    }

    public PagerAdapter getMyPagerAdapter() {
        return myPagerAdapter;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mIsAttached = true;

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mIsAttached = false;

    }

    private boolean mIsPaused = false;
    public void onPause() {
        mIsPaused = true;
        removeCallbacks(this);
    }
    public void onResume() {
        mIsPaused = false;
        postDelayed(this, POST_DELAYED_TIME);
    }

    @Override
    // 自动滚动关键
    public void run() {
        if (getAdapter() != null && getAdapter().getCount() > 1 && !touching && mScroller.isFinished()) {
            setCurrentItem(getCurrentItem() + 1, true);
        }
        postDelayed(this, POST_DELAYED_TIME);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN ) {
            setTouch(true);
            removeCallbacks(this);
        } else if (event.getAction() == MotionEvent.ACTION_UP
                || event.getAction() == MotionEvent.ACTION_CANCEL) {
            setTouch(false);
            if (!mIsPaused) {
                postDelayed(this, POST_DELAYED_TIME);
            }
        }
        return super.dispatchTouchEvent(event);
    }

    private void setTouch(boolean touch) {
        if (touch != touching) {
            touching = touch;
            if (onTouchListener != null) {
                onTouchListener.onTouchStateChanged(touch);
            }
        }
    }

    public static interface OnTouchStateListener {
        public void onTouchStateChanged(boolean touching);
    }

    public OnTouchStateListener onTouchListener = null;
    public void setOnTouchStateListener(OnTouchStateListener listener) {
        onTouchListener = listener;
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercept = super.onInterceptTouchEvent(ev);
        if (ev.getAction() == MotionEvent.ACTION_UP
                || ev.getAction() == MotionEvent.ACTION_CANCEL) {
            if (getParent() != null) {
                getParent().requestDisallowInterceptTouchEvent(false);
            }
        } else {
            if (intercept) {
                if (getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
            }
        }
        return intercept;
    }

}

