package com.baidumap.downloader.views;
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.KeyEventCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.view.accessibility.AccessibilityRecordCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * Created by weilei on 16/8/15.
 */
public class ViewPager extends ViewGroup {
    private static final String TAG = "ViewPager";
    private static final boolean DEBUG = false;
    private static final boolean USE_CACHE = false;
    private static final int DEFAULT_OFFSCREEN_PAGES = 1;
    private static final int MAX_SETTLE_DURATION = 600;
    private static final int MIN_DISTANCE_FOR_FLING = 25;
    private static final int DEFAULT_GUTTER_SIZE = 16;
    private static final int MIN_FLING_VELOCITY = 400;
    private static final int[] LAYOUT_ATTRS = new int[]{16842931};
    private int mExpectedAdapterCount;
    private static final Comparator<ItemInfo> COMPARATOR = new Comparator<ItemInfo>() {
        public int compare(ViewPager.ItemInfo lhs, ViewPager.ItemInfo rhs) {
            return lhs.position - rhs.position;
        }
    };
    private static final Interpolator sInterpolator = new Interpolator() {
        public float getInterpolation(float t) {
            --t;
            return t * t * t * t * t + 1.0F;
        }
    };
    private final ArrayList<ItemInfo> mItems = new ArrayList();
    private final ViewPager.ItemInfo mTempItem = new ViewPager.ItemInfo();
    private final Rect mTempRect = new Rect();
    private PagerAdapter mAdapter;
    private int mCurItem;
    private int mRestoredCurItem = -1;
    private Parcelable mRestoredAdapterState = null;
    private ClassLoader mRestoredClassLoader = null;
    private Scroller mScroller;
    private ViewPager.PagerObserver mObserver;
    private int mPageMargin;
    private Drawable mMarginDrawable;
    private int mTopPageBounds;
    private int mBottomPageBounds;
    private float mFirstOffset = -3.4028235E38F;
    private float mLastOffset = 3.4028235E38F;
    private int mChildWidthMeasureSpec;
    private int mChildHeightMeasureSpec;
    private boolean mInLayout;
    private boolean mScrollingCacheEnabled;
    private boolean mPopulatePending;
    private int mOffscreenPageLimit = 1;
    private boolean mIsBeingDragged;
    private boolean mIsUnableToDrag;
    private boolean mIgnoreGutter;
    private int mDefaultGutterSize;
    private int mGutterSize;
    private int mTouchSlop;
    private float mLastMotionX;
    private float mLastMotionY;
    private float mInitialMotionX;
    private float mInitialMotionY;
    private int mActivePointerId = -1;
    private static final int INVALID_POINTER = -1;
    private VelocityTracker mVelocityTracker;
    private int mMinimumVelocity;
    private int mMaximumVelocity;
    private int mFlingDistance;
    private int mCloseEnough;
    private int mOverscrollDistance;
    private int mOverflingDistance;
    private static final int CLOSE_ENOUGH = 2;
    private boolean mFakeDragging;
    private long mFakeDragBeginTime;
    private boolean mFirstLayout = true;
    private boolean mNeedCalculatePageOffsets = false;
    private boolean mCalledSuper;
    private int mDecorChildCount;
    private ViewPager.OnPageChangeListener mOnPageChangeListener;
    private ViewPager.OnPageChangeListener mInternalPageChangeListener;
    private ViewPager.OnAdapterChangeListener mAdapterChangeListener;
    private ViewPager.PageTransformer mPageTransformer;
    private Method mSetChildrenDrawingOrderEnabled;
    private static final int DRAW_ORDER_DEFAULT = 0;
    private static final int DRAW_ORDER_FORWARD = 1;
    private static final int DRAW_ORDER_REVERSE = 2;
    private int mDrawingOrder;
    private ArrayList<View> mDrawingOrderedChildren;
    private static final ViewPager.ViewPositionComparator sPositionComparator = new ViewPager.ViewPositionComparator();
    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_DRAGGING = 1;
    public static final int SCROLL_STATE_SETTLING = 2;
    private final Runnable mEndScrollRunnable = new Runnable() {
        public void run() {
            ViewPager.this.setScrollState(0);
            ViewPager.this.populate();
        }
    };
    private int mScrollState = 0;

    public ViewPager(Context context) {
        super(context);
        this.initViewPager();
    }

    public ViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initViewPager();
    }

    void initViewPager() {
        this.setWillNotDraw(false);
        this.setDescendantFocusability(262144);
        this.setFocusable(true);
        Context context = this.getContext();
        this.mScroller = new Scroller(context, sInterpolator);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        float density = context.getResources().getDisplayMetrics().density;
        this.mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
        this.mMinimumVelocity = (int)(400.0F * density);
        this.mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        this.mFlingDistance = (int)(25.0F * density);
        this.mCloseEnough = (int)(2.0F * density);
        this.mDefaultGutterSize = (int)(16.0F * density);
        ViewCompat.setAccessibilityDelegate(this, new ViewPager.MyAccessibilityDelegate());
        if(ViewCompat.getImportantForAccessibility(this) == 0) {
            ViewCompat.setImportantForAccessibility(this, 1);
        }

    }

    public void setScroller(Scroller scroller) {
        this.mScroller = scroller;
    }

    protected void onDetachedFromWindow() {
        this.removeCallbacks(this.mEndScrollRunnable);
        super.onDetachedFromWindow();
    }

    private void setScrollState(int newState) {
        if(this.mScrollState != newState) {
            this.mScrollState = newState;
            if(this.mPageTransformer != null) {
                this.enableLayers(newState != 0);
            }

            if(this.mOnPageChangeListener != null) {
                this.mOnPageChangeListener.onPageScrollStateChanged(newState);
            }

        }
    }

    public void setAdapter(PagerAdapter adapter) {
        if(this.mAdapter != null) {
            this.mAdapter.unregisterDataSetObserver(this.mObserver);
            this.mAdapter.startUpdate(this);

            for(int oldAdapter = 0; oldAdapter < this.mItems.size(); ++oldAdapter) {
                ViewPager.ItemInfo wasFirstLayout = (ViewPager.ItemInfo)this.mItems.get(oldAdapter);
                this.mAdapter.destroyItem(this, wasFirstLayout.position, wasFirstLayout.object);
            }

            this.mAdapter.finishUpdate(this);
            this.mItems.clear();
            this.removeNonDecorViews();
            this.mCurItem = 0;
            this.scrollTo(0, 0);
        }

        PagerAdapter var4 = this.mAdapter;
        this.mAdapter = adapter;
        this.mExpectedAdapterCount = 0;
        if(this.mAdapter != null) {
            if(this.mObserver == null) {
                this.mObserver = new ViewPager.PagerObserver();
            }

            this.mAdapter.registerDataSetObserver(this.mObserver);
            this.mPopulatePending = false;
            boolean var5 = this.mFirstLayout;
            this.mFirstLayout = true;
            this.mExpectedAdapterCount = this.mAdapter.getCount();
            if(this.mRestoredCurItem >= 0) {
                this.mAdapter.restoreState(this.mRestoredAdapterState, this.mRestoredClassLoader);
                this.setCurrentItemInternal(this.mRestoredCurItem, false, true);
                this.mRestoredCurItem = -1;
                this.mRestoredAdapterState = null;
                this.mRestoredClassLoader = null;
            } else if(!var5) {
                this.populate();
            } else {
                this.requestLayout();
            }
        }

        if(this.mAdapterChangeListener != null && var4 != adapter) {
            this.mAdapterChangeListener.onAdapterChanged(var4, adapter);
        }

    }

    private void removeNonDecorViews() {
        for(int i = 0; i < this.getChildCount(); ++i) {
            View child = this.getChildAt(i);
            ViewPager.LayoutParams lp = (ViewPager.LayoutParams)child.getLayoutParams();
            if(!lp.isDecor) {
                this.removeViewAt(i);
                --i;
            }
        }

    }

    public PagerAdapter getAdapter() {
        return this.mAdapter;
    }

    void setOnAdapterChangeListener(ViewPager.OnAdapterChangeListener listener) {
        this.mAdapterChangeListener = listener;
    }

    private int getClientWidth() {
        return this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight();
    }

    public void setCurrentItem(int item) {
        this.mPopulatePending = false;
        this.setCurrentItemInternal(item, !this.mFirstLayout, false);
    }

    public void setCurrentItem(int item, boolean smoothScroll) {
        this.mPopulatePending = false;
        this.setCurrentItemInternal(item, smoothScroll, false);
    }

    public int getCurrentItem() {
        return this.mCurItem;
    }

    void setCurrentItemInternal(int item, boolean smoothScroll, boolean always) {
        this.setCurrentItemInternal(item, smoothScroll, always, 0);
    }

    void setCurrentItemInternal(int item, boolean smoothScroll, boolean always, int velocity) {
        if(this.mAdapter != null && this.mAdapter.getCount() > 0) {
            if(!always && this.mCurItem == item && this.mItems.size() != 0) {
                this.setScrollingCacheEnabled(false);
            } else {
                if(item < 0) {
                    item = 0;
                } else if(item >= this.mAdapter.getCount()) {
                    item = this.mAdapter.getCount() - 1;
                }

                int pageLimit = this.mOffscreenPageLimit;
                if(item > this.mCurItem + pageLimit || item < this.mCurItem - pageLimit) {
                    for(int dispatchSelected = 0; dispatchSelected < this.mItems.size(); ++dispatchSelected) {
                        ((ViewPager.ItemInfo)this.mItems.get(dispatchSelected)).scrolling = true;
                    }
                }

                boolean var7 = this.mCurItem != item;
                if(this.mFirstLayout) {
                    this.mCurItem = item;
                    if(var7 && this.mOnPageChangeListener != null) {
                        this.mOnPageChangeListener.onPageSelected(item);
                    }

                    if(var7 && this.mInternalPageChangeListener != null) {
                        this.mInternalPageChangeListener.onPageSelected(item);
                    }

                    this.requestLayout();
                } else {
                    this.populate(item);
                    this.scrollToItem(item, smoothScroll, velocity, var7);
                }

            }
        } else {
            this.setScrollingCacheEnabled(false);
        }
    }

    private void scrollToItem(int item, boolean smoothScroll, int velocity, boolean dispatchSelected) {
        ViewPager.ItemInfo curInfo = this.infoForPosition(item);
        int destX = 0;
        if(curInfo != null) {
            int width = this.getClientWidth();
            destX = (int)((float)width * Math.max(this.mFirstOffset, Math.min(curInfo.offset, this.mLastOffset)));
        }

        if(smoothScroll) {
            this.smoothScrollTo(destX, 0, velocity);
            if(dispatchSelected && this.mOnPageChangeListener != null) {
                this.mOnPageChangeListener.onPageSelected(item);
            }

            if(dispatchSelected && this.mInternalPageChangeListener != null) {
                this.mInternalPageChangeListener.onPageSelected(item);
            }
        } else {
            if(dispatchSelected && this.mOnPageChangeListener != null) {
                this.mOnPageChangeListener.onPageSelected(item);
            }

            if(dispatchSelected && this.mInternalPageChangeListener != null) {
                this.mInternalPageChangeListener.onPageSelected(item);
            }

            this.completeScroll(false);
            this.scrollTo(destX, 0);
            this.pageScrolled(destX);
        }

    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        this.mOnPageChangeListener = listener;
    }

    public void setPageTransformer(boolean reverseDrawingOrder, ViewPager.PageTransformer transformer) {
        if(Build.VERSION.SDK_INT >= 11) {
            boolean hasTransformer = transformer != null;
            boolean needsPopulate = hasTransformer != (this.mPageTransformer != null);
            this.mPageTransformer = transformer;
            this.setChildrenDrawingOrderEnabledCompat(hasTransformer);
            if(hasTransformer) {
                this.mDrawingOrder = reverseDrawingOrder?2:1;
            } else {
                this.mDrawingOrder = 0;
            }

            if(needsPopulate) {
                this.populate();
            }
        }

    }

    void setChildrenDrawingOrderEnabledCompat(boolean enable) {
        if(Build.VERSION.SDK_INT >= 7) {
            if(this.mSetChildrenDrawingOrderEnabled == null) {
                try {
                    this.mSetChildrenDrawingOrderEnabled = ViewGroup.class.getDeclaredMethod("setChildrenDrawingOrderEnabled", new Class[]{


                            Boolean.TYPE});
                } catch (NoSuchMethodException var4) {
                    Log.e("ViewPager", "Can\'t find setChildrenDrawingOrderEnabled", var4);
                }
            }

            try {
                this.mSetChildrenDrawingOrderEnabled.invoke(this, new Object[]{Boolean.valueOf(enable)});
            } catch (Exception var3) {
                Log.e("ViewPager", "Error changing children drawing order", var3);
            }
        }

    }

    protected int getChildDrawingOrder(int childCount, int i) {
        int index = this.mDrawingOrder == 2?childCount - 1 - i:i;
        int result = ((ViewPager.LayoutParams)((View)this.mDrawingOrderedChildren.get(index)).getLayoutParams()).childIndex;
        return result;
    }

    ViewPager.OnPageChangeListener setInternalPageChangeListener(ViewPager.OnPageChangeListener listener) {
        ViewPager.OnPageChangeListener oldListener = this.mInternalPageChangeListener;
        this.mInternalPageChangeListener = listener;
        return oldListener;
    }

    public int getOffscreenPageLimit() {
        return this.mOffscreenPageLimit;
    }

    public void setOffscreenPageLimit(int limit) {
        if(limit < 1) {
            Log.w("ViewPager", "Requested offscreen page limit " + limit + " too small; defaulting to " + 1);
            limit = 1;
        }

        if(limit != this.mOffscreenPageLimit) {
            this.mOffscreenPageLimit = limit;
            this.populate();
        }

    }

    public void setPageMargin(int marginPixels) {
        int oldMargin = this.mPageMargin;
        this.mPageMargin = marginPixels;
        int width = this.getWidth();
        this.recomputeScrollPosition(width, width, marginPixels, oldMargin);
        this.requestLayout();
    }

    public int getPageMargin() {
        return this.mPageMargin;
    }

    public void setPageMarginDrawable(Drawable d) {
        this.mMarginDrawable = d;
        if(d != null) {
            this.refreshDrawableState();
        }

        this.setWillNotDraw(d == null);
        this.invalidate();
    }

    public void setPageMarginDrawable(int resId) {
        this.setPageMarginDrawable(this.getContext().getResources().getDrawable(resId));
    }

    protected boolean verifyDrawable(Drawable who) {
        return super.verifyDrawable(who) || who == this.mMarginDrawable;
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        Drawable d = this.mMarginDrawable;
        if(d != null && d.isStateful()) {
            d.setState(this.getDrawableState());
        }

    }

    float distanceInfluenceForSnapDuration(float f) {
        f -= 0.5F;
        f = (float)((double)f * 0.4712389167638204D);
        return (float) Math.sin((double)f);
    }

    void smoothScrollTo(int x, int y) {
        this.smoothScrollTo(x, y, 0);
    }

    void smoothScrollTo(int x, int y, int velocity) {
        if(this.getChildCount() == 0) {
            this.setScrollingCacheEnabled(false);
        } else {
            int sx = this.getScrollX();
            int sy = this.getScrollY();
            int dx = x - sx;
            int dy = y - sy;
            if(dx == 0 && dy == 0) {
                this.completeScroll(false);
                this.populate();
                this.setScrollState(0);
            } else {
                this.setScrollingCacheEnabled(true);
                this.setScrollState(2);
                int width = this.getClientWidth();
                int halfWidth = width / 2;
                float distanceRatio = Math.min(1.0F, 1.0F * (float) Math.abs(dx) / (float)width);
                float distance = (float)halfWidth + (float)halfWidth * this.distanceInfluenceForSnapDuration(distanceRatio);
                boolean duration = false;
                velocity = Math.abs(velocity);
                int duration1;
                if(velocity > 0) {
                    duration1 = 4 * Math.round(1000.0F * Math.abs(distance / (float)velocity));
                } else {
                    float pageWidth = (float)width * this.mAdapter.getPageWidth(this.mCurItem);
                    float pageDelta = (float) Math.abs(dx) / (pageWidth + (float)this.mPageMargin);
                    duration1 = (int)((pageDelta + 1.0F) * 200.0F);
                }

                duration1 = Math.min(duration1, 600);
                this.mScroller.startScroll(sx, sy, dx, dy, duration1);
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }
    }

    ViewPager.ItemInfo addNewItem(int position, int index) {
        ViewPager.ItemInfo ii = new ViewPager.ItemInfo();
        ii.position = position;
        ii.object = this.mAdapter.instantiateItem(this, position);
        ii.widthFactor = this.mAdapter.getPageWidth(position);
        if(index >= 0 && index < this.mItems.size()) {
            this.mItems.add(index, ii);
        } else {
            this.mItems.add(ii);
        }

        return ii;
    }

    void dataSetChanged() {
        int adapterCount = this.mAdapter.getCount();
        this.mExpectedAdapterCount = adapterCount;
        boolean needPopulate = this.mItems.size() < this.mOffscreenPageLimit * 2 + 1 && this.mItems.size() < adapterCount;
        int newCurrItem = this.mCurItem;
        boolean isUpdating = false;

        int childCount;
        for(childCount = 0; childCount < this.mItems.size(); ++childCount) {
            ViewPager.ItemInfo i = (ViewPager.ItemInfo)this.mItems.get(childCount);
            int child = this.mAdapter.getItemPosition(i.object);
            if(child != -1) {
                if(child == -2) {
                    this.mItems.remove(childCount);
                    --childCount;
                    if(!isUpdating) {
                        this.mAdapter.startUpdate(this);
                        isUpdating = true;
                    }

                    this.mAdapter.destroyItem(this, i.position, i.object);
                    needPopulate = true;
                    if(this.mCurItem == i.position) {
                        newCurrItem = Math.max(0, Math.min(this.mCurItem, adapterCount - 1));
                        needPopulate = true;
                    }
                } else if(i.position != child) {
                    if(i.position == this.mCurItem) {
                        newCurrItem = child;
                    }

                    i.position = child;
                    needPopulate = true;
                }
            }
        }

        if(isUpdating) {
            this.mAdapter.finishUpdate(this);
        }

        Collections.sort(this.mItems, COMPARATOR);
        if(needPopulate) {
            childCount = this.getChildCount();

            for(int var9 = 0; var9 < childCount; ++var9) {
                View var10 = this.getChildAt(var9);
                ViewPager.LayoutParams lp = (ViewPager.LayoutParams)var10.getLayoutParams();
                if(!lp.isDecor) {
                    lp.widthFactor = 0.0F;
                }
            }

            this.setCurrentItemInternal(newCurrItem, false, true);
            this.requestLayout();
        }

    }

    void populate() {
        this.populate(this.mCurItem);
    }

    void populate(int newCurrentItem) {
        ViewPager.ItemInfo oldCurInfo = null;
        int focusDirection = 2;
        if(this.mCurItem != newCurrentItem) {
            focusDirection = this.mCurItem < newCurrentItem?66:17;
            oldCurInfo = this.infoForPosition(this.mCurItem);
            this.mCurItem = newCurrentItem;
        }

        if(this.mAdapter == null) {
            this.sortChildDrawingOrder();
        } else if(this.mPopulatePending) {
            this.sortChildDrawingOrder();
        } else if(this.getWindowToken() != null) {
            this.mAdapter.startUpdate(this);
            int pageLimit = this.mOffscreenPageLimit;
            int startPos = Math.max(0, this.mCurItem - pageLimit);
            int N = this.mAdapter.getCount();
            int endPos = Math.min(N - 1, this.mCurItem + pageLimit);
            if(N != this.mExpectedAdapterCount) {
                String var20;
                try {
                    var20 = this.getResources().getResourceName(this.getId());
                } catch (Resources.NotFoundException var18) {
                    var20 = Integer.toHexString(this.getId());
                }

                throw new IllegalStateException("The application\'s PagerAdapter changed the adapter\'s contents without calling PagerAdapter#notifyDataSetChanged! Expected adapter item count: " + this.mExpectedAdapterCount + ", found: " + N + " Pager id: " + var20 + " Pager class: " + this.getClass() + " Problematic adapter: " + this.mAdapter.getClass());
            } else {
                boolean curIndex = true;
                ViewPager.ItemInfo curItem = null;

                int var19;
                for(var19 = 0; var19 < this.mItems.size(); ++var19) {
                    ViewPager.ItemInfo childCount = (ViewPager.ItemInfo)this.mItems.get(var19);
                    if(childCount.position >= this.mCurItem) {
                        if(childCount.position == this.mCurItem) {
                            curItem = childCount;
                        }
                        break;
                    }
                }

                if(curItem == null && N > 0) {
                    curItem = this.addNewItem(this.mCurItem, var19);
                }

                int currentFocused;
                ViewPager.ItemInfo ii;
                int i;
                if(curItem != null) {
                    float var22 = 0.0F;
                    currentFocused = var19 - 1;
                    ii = currentFocused >= 0?(ViewPager.ItemInfo)this.mItems.get(currentFocused):null;
                    i = this.getClientWidth();
                    float child = i <= 0?0.0F:2.0F - curItem.widthFactor + (float)this.getPaddingLeft() / (float)i;

                    for(int extraWidthRight = this.mCurItem - 1; extraWidthRight >= 0; --extraWidthRight) {
                        if(var22 >= child && extraWidthRight < startPos) {
                            if(ii == null) {
                                break;
                            }

                            if(extraWidthRight == ii.position && !ii.scrolling) {
                                this.mItems.remove(currentFocused);
                                this.mAdapter.destroyItem(this, extraWidthRight, ii.object);
                                --currentFocused;
                                --var19;
                                ii = currentFocused >= 0?(ViewPager.ItemInfo)this.mItems.get(currentFocused):null;
                            }
                        } else if(ii != null && extraWidthRight == ii.position) {
                            var22 += ii.widthFactor;
                            --currentFocused;
                            ii = currentFocused >= 0?(ViewPager.ItemInfo)this.mItems.get(currentFocused):null;
                        } else {
                            ii = this.addNewItem(extraWidthRight, currentFocused + 1);
                            var22 += ii.widthFactor;
                            ++var19;
                            ii = currentFocused >= 0?(ViewPager.ItemInfo)this.mItems.get(currentFocused):null;
                        }
                    }

                    float var28 = curItem.widthFactor;
                    currentFocused = var19 + 1;
                    if(var28 < 2.0F) {
                        ii = currentFocused < this.mItems.size()?(ViewPager.ItemInfo)this.mItems.get(currentFocused):null;
                        float rightWidthNeeded = i <= 0?0.0F:(float)this.getPaddingRight() / (float)i + 2.0F;

                        for(int pos = this.mCurItem + 1; pos < N; ++pos) {
                            if(var28 >= rightWidthNeeded && pos > endPos) {
                                if(ii == null) {
                                    break;
                                }

                                if(pos == ii.position && !ii.scrolling) {
                                    this.mItems.remove(currentFocused);
                                    this.mAdapter.destroyItem(this, pos, ii.object);
                                    ii = currentFocused < this.mItems.size()?(ViewPager.ItemInfo)this.mItems.get(currentFocused):null;
                                }
                            } else if(ii != null && pos == ii.position) {
                                var28 += ii.widthFactor;
                                ++currentFocused;
                                ii = currentFocused < this.mItems.size()?(ViewPager.ItemInfo)this.mItems.get(currentFocused):null;
                            } else {
                                ii = this.addNewItem(pos, currentFocused);
                                ++currentFocused;
                                var28 += ii.widthFactor;
                                ii = currentFocused < this.mItems.size()?(ViewPager.ItemInfo)this.mItems.get(currentFocused):null;
                            }
                        }
                    }

                    this.calculatePageOffsets(curItem, var19, oldCurInfo);
                }

                this.mAdapter.setPrimaryItem(this, this.mCurItem, curItem != null?curItem.object:null);
                this.mAdapter.finishUpdate(this);
                int var21 = this.getChildCount();

                for(currentFocused = 0; currentFocused < var21; ++currentFocused) {
                    View var25 = this.getChildAt(currentFocused);
                    ViewPager.LayoutParams var24 = (ViewPager.LayoutParams)var25.getLayoutParams();
                    var24.childIndex = currentFocused;
                    if(!var24.isDecor && var24.widthFactor == 0.0F) {
                        ViewPager.ItemInfo var26 = this.infoForChild(var25);
                        if(var26 != null) {
                            var24.widthFactor = var26.widthFactor;
                            var24.position = var26.position;
                        }
                    }
                }

                this.sortChildDrawingOrder();
                if(this.hasFocus()) {
                    View var23 = this.findFocus();
                    ii = var23 != null?this.infoForAnyChild(var23):null;
                    if(ii == null || ii.position != this.mCurItem) {
                        for(i = 0; i < this.getChildCount(); ++i) {
                            View var27 = this.getChildAt(i);
                            ii = this.infoForChild(var27);
                            if(ii != null && ii.position == this.mCurItem && var27.requestFocus(focusDirection)) {
                                break;
                            }
                        }
                    }
                }

            }
        }
    }

    private void sortChildDrawingOrder() {
        if(this.mDrawingOrder != 0) {
            if(this.mDrawingOrderedChildren == null) {
                this.mDrawingOrderedChildren = new ArrayList();
            } else {
                this.mDrawingOrderedChildren.clear();
            }

            int childCount = this.getChildCount();

            for(int i = 0; i < childCount; ++i) {
                View child = this.getChildAt(i);
                this.mDrawingOrderedChildren.add(child);
            }

            Collections.sort(this.mDrawingOrderedChildren, sPositionComparator);
        }

    }

    private void calculatePageOffsets(ViewPager.ItemInfo curItem, int curIndex, ViewPager.ItemInfo oldCurInfo) {
        int N = this.mAdapter.getCount();
        int width = this.getClientWidth();
        float marginOffset = width > 0?(float)this.mPageMargin / (float)width:0.0F;
        int itemCount;
        if(oldCurInfo != null) {
            itemCount = oldCurInfo.position;
            int offset;
            ViewPager.ItemInfo pos;
            float i;
            int ii;
            if(itemCount < curItem.position) {
                offset = 0;
                pos = null;
                i = oldCurInfo.offset + oldCurInfo.widthFactor + marginOffset;

                for(ii = itemCount + 1; ii <= curItem.position && offset < this.mItems.size(); ++ii) {
                    for(pos = (ViewPager.ItemInfo)this.mItems.get(offset); ii > pos.position && offset < this.mItems.size() - 1; pos = (ViewPager.ItemInfo)this.mItems.get(offset)) {
                        ++offset;
                    }

                    while(ii < pos.position) {
                        i += this.mAdapter.getPageWidth(ii) + marginOffset;
                        ++ii;
                    }

                    pos.offset = i;
                    i += pos.widthFactor + marginOffset;
                }
            } else if(itemCount > curItem.position) {
                offset = this.mItems.size() - 1;
                pos = null;
                i = oldCurInfo.offset;

                for(ii = itemCount - 1; ii >= curItem.position && offset >= 0; --ii) {
                    for(pos = (ViewPager.ItemInfo)this.mItems.get(offset); ii < pos.position && offset > 0; pos = (ViewPager.ItemInfo)this.mItems.get(offset)) {
                        --offset;
                    }

                    while(ii > pos.position) {
                        i -= this.mAdapter.getPageWidth(ii) + marginOffset;
                        --ii;
                    }

                    i -= pos.widthFactor + marginOffset;
                    pos.offset = i;
                }
            }
        }

        itemCount = this.mItems.size();
        float var12 = curItem.offset;
        int var13 = curItem.position - 1;
        this.mFirstOffset = curItem.position == 0?curItem.offset:-3.4028235E38F;
        this.mLastOffset = curItem.position == N - 1?curItem.offset + curItem.widthFactor - 1.0F:3.4028235E38F;

        int var14;
        ViewPager.ItemInfo var15;
        for(var14 = curIndex - 1; var14 >= 0; --var13) {
            for(var15 = (ViewPager.ItemInfo)this.mItems.get(var14); var13 > var15.position; var12 -= this.mAdapter.getPageWidth(var13--) + marginOffset) {
                ;
            }

            var12 -= var15.widthFactor + marginOffset;
            var15.offset = var12;
            if(var15.position == 0) {
                this.mFirstOffset = var12;
            }

            --var14;
        }

        var12 = curItem.offset + curItem.widthFactor + marginOffset;
        var13 = curItem.position + 1;

        for(var14 = curIndex + 1; var14 < itemCount; ++var13) {
            for(var15 = (ViewPager.ItemInfo)this.mItems.get(var14); var13 < var15.position; var12 += this.mAdapter.getPageWidth(var13++) + marginOffset) {
                ;
            }

            if(var15.position == N - 1) {
                this.mLastOffset = var12 + var15.widthFactor - 1.0F;
            }

            var15.offset = var12;
            var12 += var15.widthFactor + marginOffset;
            ++var14;
        }

        this.mNeedCalculatePageOffsets = false;
    }

    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        ViewPager.SavedState ss = new ViewPager.SavedState(superState);
        ss.position = this.mCurItem;
        if(this.mAdapter != null) {
            ss.adapterState = this.mAdapter.saveState();
        }

        return ss;
    }

    public void onRestoreInstanceState(Parcelable state) {
        if(!(state instanceof ViewPager.SavedState)) {
            super.onRestoreInstanceState(state);
        } else {
            ViewPager.SavedState ss = (ViewPager.SavedState)state;
            super.onRestoreInstanceState(ss.getSuperState());
            if(this.mAdapter != null) {
                this.mAdapter.restoreState(ss.adapterState, ss.loader);
                this.setCurrentItemInternal(ss.position, false, true);
            } else {
                this.mRestoredCurItem = ss.position;
                this.mRestoredAdapterState = ss.adapterState;
                this.mRestoredClassLoader = ss.loader;
            }

        }
    }

    public void addView(View child, int index, android.view.ViewGroup.LayoutParams params) {
        if(!this.checkLayoutParams(params)) {
            params = this.generateLayoutParams(params);
        }

        ViewPager.LayoutParams lp = (ViewPager.LayoutParams)params;
        lp.isDecor |= child instanceof ViewPager.Decor;
        if(this.mInLayout) {
            if(lp != null && lp.isDecor) {
                throw new IllegalStateException("Cannot add pager decor view during layout");
            }

            lp.needsMeasure = true;
            this.addViewInLayout(child, index, params);
        } else {
            super.addView(child, index, params);
        }

    }

    public void removeView(View view) {
        if(this.mInLayout) {
            this.removeViewInLayout(view);
        } else {
            super.removeView(view);
        }

    }

    ViewPager.ItemInfo infoForChild(View child) {
        for(int i = 0; i < this.mItems.size(); ++i) {
            ViewPager.ItemInfo ii = (ViewPager.ItemInfo)this.mItems.get(i);
            if(this.mAdapter.isViewFromObject(child, ii.object)) {
                return ii;
            }
        }

        return null;
    }

    ViewPager.ItemInfo infoForAnyChild(View child) {
        while(true) {
            ViewParent parent;
            if((parent = child.getParent()) != this) {
                if(parent != null && parent instanceof View) {
                    child = (View)parent;
                    continue;
                }

                return null;
            }

            return this.infoForChild(child);
        }
    }

    ViewPager.ItemInfo infoForPosition(int position) {
        for(int i = 0; i < this.mItems.size(); ++i) {
            ViewPager.ItemInfo ii = (ViewPager.ItemInfo)this.mItems.get(i);
            if(ii.position == position) {
                return ii;
            }
        }

        return null;
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mFirstLayout = true;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        int measuredWidth = this.getMeasuredWidth();
        int maxGutterSize = measuredWidth / 10;
        this.mGutterSize = Math.min(maxGutterSize, this.mDefaultGutterSize);
        int childWidthSize = measuredWidth - this.getPaddingLeft() - this.getPaddingRight();
        int childHeightSize = this.getMeasuredHeight() - this.getPaddingTop() - this.getPaddingBottom();
        int size = this.getChildCount();

        int i;
        View child;
        ViewPager.LayoutParams lp;
        int widthSpec;
        for(i = 0; i < size; ++i) {
            child = this.getChildAt(i);
            if(child.getVisibility() != GONE) {
                lp = (ViewPager.LayoutParams)child.getLayoutParams();
                if(lp != null && lp.isDecor) {
                    widthSpec = lp.gravity & 7;
                    int vgrav = lp.gravity & 112;
                    int widthMode = -2147483648;
                    int heightMode = -2147483648;
                    boolean consumeVertical = vgrav == 48 || vgrav == 80;
                    boolean consumeHorizontal = widthSpec == 3 || widthSpec == 5;
                    if(consumeVertical) {
                        widthMode = 1073741824;
                    } else if(consumeHorizontal) {
                        heightMode = 1073741824;
                    }

                    int widthSize = childWidthSize;
                    int heightSize = childHeightSize;
                    if(lp.width != -2) {
                        widthMode = 1073741824;
                        if(lp.width != -1) {
                            widthSize = lp.width;
                        }
                    }

                    if(lp.height != -2) {
                        heightMode = 1073741824;
                        if(lp.height != -1) {
                            heightSize = lp.height;
                        }
                    }

                    int widthSpec1 = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
                    int heightSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
                    child.measure(widthSpec1, heightSpec);
                    if(consumeVertical) {
                        childHeightSize -= child.getMeasuredHeight();
                    } else if(consumeHorizontal) {
                        childWidthSize -= child.getMeasuredWidth();
                    }
                }
            }
        }

        this.mChildWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
        this.mChildHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeightSize, MeasureSpec.EXACTLY);
        this.mInLayout = true;
        this.populate();
        this.mInLayout = false;
        size = this.getChildCount();

        for(i = 0; i < size; ++i) {
            child = this.getChildAt(i);
            if(child.getVisibility() != GONE) {
                lp = (ViewPager.LayoutParams)child.getLayoutParams();
                if(lp == null || !lp.isDecor) {
                    widthSpec = MeasureSpec.makeMeasureSpec((int)((float)childWidthSize * lp.widthFactor), MeasureSpec.EXACTLY);
                    child.measure(widthSpec, this.mChildHeightMeasureSpec);
                }
            }
        }

    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(w != oldw) {
            this.recomputeScrollPosition(w, oldw, this.mPageMargin, this.mPageMargin);
        }

    }

    private void recomputeScrollPosition(int width, int oldWidth, int margin, int oldMargin) {
        int scrollPos;
        if(oldWidth > 0 && !this.mItems.isEmpty()) {
            int ii1 = width - this.getPaddingLeft() - this.getPaddingRight() + margin;
            int scrollOffset1 = oldWidth - this.getPaddingLeft() - this.getPaddingRight() + oldMargin;
            scrollPos = this.getScrollX();
            float pageOffset = (float)scrollPos / (float)scrollOffset1;
            int newOffsetPixels = (int)(pageOffset * (float)ii1);
            this.scrollTo(newOffsetPixels, this.getScrollY());
            if(!this.mScroller.isFinished()) {
                int newDuration = this.mScroller.getDuration() - this.mScroller.timePassed();
                ViewPager.ItemInfo targetInfo = this.infoForPosition(this.mCurItem);
                this.mScroller.startScroll(newOffsetPixels, 0, (int)(targetInfo.offset * (float)width), 0, newDuration);
            }
        } else {
            ViewPager.ItemInfo ii = this.infoForPosition(this.mCurItem);
            float scrollOffset = ii != null? Math.min(ii.offset, this.mLastOffset):0.0F;
            scrollPos = (int)(scrollOffset * (float)(width - this.getPaddingLeft() - this.getPaddingRight()));
            if(scrollPos != this.getScrollX()) {
                this.completeScroll(false);
                this.scrollTo(scrollPos, this.getScrollY());
            }
        }

    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = this.getChildCount();
        int width = r - l;
        int height = b - t;
        int paddingLeft = this.getPaddingLeft();
        int paddingTop = this.getPaddingTop();
        int paddingRight = this.getPaddingRight();
        int paddingBottom = this.getPaddingBottom();
        int scrollX = this.getScrollX();
        int decorCount = 0;

        int childWidth;
        int childLeft;
        int loff;
        for(childWidth = 0; childWidth < count; ++childWidth) {
            View i = this.getChildAt(childWidth);
            if(i.getVisibility() != GONE) {
                ViewPager.LayoutParams child = (ViewPager.LayoutParams)i.getLayoutParams();
                boolean lp = false;
                boolean ii = false;
                if(child.isDecor) {
                    loff = child.gravity & 7;
                    childLeft = child.gravity & 112;
                    int var28;
                    switch(loff) {
                        case 1:
                            var28 = Math.max((width - i.getMeasuredWidth()) / 2, paddingLeft);
                            break;
                        case 2:
                        case 4:
                        default:
                            var28 = paddingLeft;
                            break;
                        case 3:
                            var28 = paddingLeft;
                            paddingLeft += i.getMeasuredWidth();
                            break;
                        case 5:
                            var28 = width - paddingRight - i.getMeasuredWidth();
                            paddingRight += i.getMeasuredWidth();
                    }

                    int var27;
                    switch(childLeft) {
                        case 16:
                            var27 = Math.max((height - i.getMeasuredHeight()) / 2, paddingTop);
                            break;
                        case 48:
                            var27 = paddingTop;
                            paddingTop += i.getMeasuredHeight();
                            break;
                        case 80:
                            var27 = height - paddingBottom - i.getMeasuredHeight();
                            paddingBottom += i.getMeasuredHeight();
                            break;
                        default:
                            var27 = paddingTop;
                    }

                    var28 += scrollX;
                    i.layout(var28, var27, var28 + i.getMeasuredWidth(), var27 + i.getMeasuredHeight());
                    ++decorCount;
                }
            }
        }

        childWidth = width - paddingLeft - paddingRight;

        for(int var25 = 0; var25 < count; ++var25) {
            View var26 = this.getChildAt(var25);
            if(var26.getVisibility() != GONE) {
                ViewPager.LayoutParams var29 = (ViewPager.LayoutParams)var26.getLayoutParams();
                ViewPager.ItemInfo var30;
                if(!var29.isDecor && (var30 = this.infoForChild(var26)) != null) {
                    loff = (int)((float)childWidth * var30.offset);
                    childLeft = paddingLeft + loff;
                    if(var29.needsMeasure) {
                        var29.needsMeasure = false;
                        int widthSpec = MeasureSpec
                                .makeMeasureSpec((int)((float)childWidth * var29.widthFactor), MeasureSpec.EXACTLY);
                        int heightSpec = MeasureSpec.makeMeasureSpec(height - paddingTop - paddingBottom, MeasureSpec.EXACTLY);
                        var26.measure(widthSpec, heightSpec);
                    }

                    var26.layout(childLeft, paddingTop, childLeft + var26.getMeasuredWidth(), paddingTop + var26.getMeasuredHeight());
                }
            }
        }

        this.mTopPageBounds = paddingTop;
        this.mBottomPageBounds = height - paddingBottom;
        this.mDecorChildCount = decorCount;
        if(this.mFirstLayout) {
            this.scrollToItem(this.mCurItem, false, 0, false);
        }

        this.mFirstLayout = false;
        this.mOverscrollDistance = r - l;
        this.mOverflingDistance = this.mOverscrollDistance / 2;
    }

    public void computeScroll() {
        if(!this.mScroller.isFinished() && this.mScroller.computeScrollOffset()) {
            int oldX = this.getScrollX();
            int oldY = this.getScrollY();
            int x = this.mScroller.getCurrX();
            int y = this.mScroller.getCurrY();
            if(oldX != x || oldY != y) {
                this.scrollTo(x, y);
                if(!this.pageScrolled(x)) {
                    this.mScroller.abortAnimation();
                    this.scrollTo(0, y);
                }
            }

            ViewCompat.postInvalidateOnAnimation(this);
        } else {
            this.completeScroll(true);
        }
    }

    private boolean pageScrolled(int xpos) {
        if(this.mItems.size() == 0) {
            this.mCalledSuper = false;
            this.onPageScrolled(0, 0.0F, 0);
            if(!this.mCalledSuper) {
                throw new IllegalStateException("onPageScrolled did not call superclass implementation");
            } else {
                return false;
            }
        } else {
            ViewPager.ItemInfo ii = this.infoForCurrentScrollPosition();
            int width = this.getClientWidth();
            int widthWithMargin = width + this.mPageMargin;
            float marginOffset = (float)this.mPageMargin / (float)width;
            int currentPage = ii.position;
            float pageOffset = ((float)xpos / (float)width - ii.offset) / (ii.widthFactor + marginOffset);
            int offsetPixels = (int)(pageOffset * (float)widthWithMargin);
            this.mCalledSuper = false;
            this.onPageScrolled(currentPage, pageOffset, offsetPixels);
            if(!this.mCalledSuper) {
                throw new IllegalStateException("onPageScrolled did not call superclass implementation");
            } else {
                return true;
            }
        }
    }

    protected void onPageScrolled(int position, float offset, int offsetPixels) {
        int scrollX;
        int childCount;
        int i;
        if(this.mDecorChildCount > 0) {
            scrollX = this.getScrollX();
            childCount = this.getPaddingLeft();
            i = this.getPaddingRight();
            int child = this.getWidth();
            int lp = this.getChildCount();

            for(int transformPos = 0; transformPos < lp; ++transformPos) {
                View child1 = this.getChildAt(transformPos);
                ViewPager.LayoutParams lp1 = (ViewPager.LayoutParams)child1.getLayoutParams();
                if(lp1.isDecor) {
                    int hgrav = lp1.gravity & 7;
                    boolean childLeft = false;
                    int var18;
                    switch(hgrav) {
                        case 1:
                            var18 = Math.max((child - child1.getMeasuredWidth()) / 2, childCount);
                            break;
                        case 2:
                        case 4:
                        default:
                            var18 = childCount;
                            break;
                        case 3:
                            var18 = childCount;
                            childCount += child1.getWidth();
                            break;
                        case 5:
                            var18 = child - i - child1.getMeasuredWidth();
                            i += child1.getMeasuredWidth();
                    }

                    var18 += scrollX;
                    int childOffset = var18 - child1.getLeft();
                    if(childOffset != 0) {
                        child1.offsetLeftAndRight(childOffset);
                    }
                }
            }
        }

        if(this.mOnPageChangeListener != null) {
            this.mOnPageChangeListener.onPageScrolled(position, offset, offsetPixels);
        }

        if(this.mInternalPageChangeListener != null) {
            this.mInternalPageChangeListener.onPageScrolled(position, offset, offsetPixels);
        }

        if(this.mPageTransformer != null) {
            scrollX = this.getScrollX();
            childCount = this.getChildCount();

            for(i = 0; i < childCount; ++i) {
                View var15 = this.getChildAt(i);
                ViewPager.LayoutParams var16 = (ViewPager.LayoutParams)var15.getLayoutParams();
                if(!var16.isDecor) {
                    float var17 = (float)(var15.getLeft() - scrollX) / (float)this.getClientWidth();
                    this.mPageTransformer.transformPage(var15, var17);
                }
            }
        }

        this.mCalledSuper = true;
    }

    private void completeScroll(boolean postEvents) {
        boolean needPopulate = this.mScrollState == 2;
        int i;
        if(needPopulate) {
            this.setScrollingCacheEnabled(false);
            this.mScroller.abortAnimation();
            i = this.getScrollX();
            int ii = this.getScrollY();
            int x = this.mScroller.getCurrX();
            int y = this.mScroller.getCurrY();
            if(i != x || ii != y) {
                this.scrollTo(x, y);
            }
        }

        this.mPopulatePending = false;

        for(i = 0; i < this.mItems.size(); ++i) {
            ViewPager.ItemInfo var7 = (ViewPager.ItemInfo)this.mItems.get(i);
            if(var7.scrolling) {
                needPopulate = true;
                var7.scrolling = false;
            }
        }

        if(needPopulate) {
            if(postEvents) {
                ViewCompat.postOnAnimation(this, this.mEndScrollRunnable);
            } else {
                this.mEndScrollRunnable.run();
            }
        }

    }

    private boolean isGutterDrag(float x, float dx) {
        return x < (float)this.mGutterSize && dx > 0.0F || x > (float)(this.getWidth() - this.mGutterSize) && dx < 0.0F;
    }

    private void enableLayers(boolean enable) {
        int childCount = this.getChildCount();

        for(int i = 0; i < childCount; ++i) {
            int layerType = enable?2:0;
            ViewCompat.setLayerType(this.getChildAt(i), layerType, (Paint)null);
        }

    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction() & 255;
        if(action != 3 && action != 1) {
            if(action != 0) {
                if(this.mIsBeingDragged) {
                    return true;
                }

                if(this.mIsUnableToDrag) {
                    return false;
                }
            }

            switch(action) {
                case 0:
                    this.mLastMotionX = this.mInitialMotionX = ev.getX();
                    this.mLastMotionY = this.mInitialMotionY = ev.getY();
                    this.mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                    this.mIsUnableToDrag = false;
                    this.mScroller.computeScrollOffset();
                    if(this.mScrollState == 2 && Math.abs(this.mScroller.getFinalX() - this.mScroller.getCurrX()) > this.mCloseEnough) {
                        this.mScroller.abortAnimation();
                        this.mPopulatePending = false;
                        this.populate();
                        this.mIsBeingDragged = true;
                        this.requestParentDisallowInterceptTouchEvent(true);
                        this.setScrollState(1);
                    } else {
                        this.completeScroll(false);
                        this.mIsBeingDragged = false;
                    }
                    break;
                case 2:
                    int activePointerId = this.mActivePointerId;
                    if(activePointerId != -1) {
                        int pointerIndex = MotionEventCompat.findPointerIndex(ev, activePointerId);
                        float x = MotionEventCompat.getX(ev, pointerIndex);
                        float dx = x - this.mLastMotionX;
                        float xDiff = Math.abs(dx);
                        float y = MotionEventCompat.getY(ev, pointerIndex);
                        float yDiff = Math.abs(y - this.mInitialMotionY);
                        if(dx != 0.0F && !this.isGutterDrag(this.mLastMotionX, dx) && this.canScroll(this, false, (int)dx, (int)x, (int)y)) {
                            this.mLastMotionX = x;
                            this.mLastMotionY = y;
                            this.mIsUnableToDrag = true;
                            return false;
                        }

                        if(xDiff > (float)this.mTouchSlop && xDiff * 0.5F > yDiff) {
                            this.mIsBeingDragged = true;
                            this.requestParentDisallowInterceptTouchEvent(true);
                            this.setScrollState(1);
                            this.mLastMotionX = dx > 0.0F?this.mInitialMotionX + (float)this.mTouchSlop:this.mInitialMotionX - (float)this.mTouchSlop;
                            this.mLastMotionY = y;
                            this.setScrollingCacheEnabled(true);
                        } else if(yDiff > (float)this.mTouchSlop) {
                            this.mIsUnableToDrag = true;
                        }

                        if(this.mIsBeingDragged && this.performDrag(x)) {
                            ViewCompat.postInvalidateOnAnimation(this);
                        }
                    }
                    break;
                case 6:
                    this.onSecondaryPointerUp(ev);
            }

            if(this.mVelocityTracker == null) {
                this.mVelocityTracker = VelocityTracker.obtain();
            }

            this.mVelocityTracker.addMovement(ev);
            return this.mIsBeingDragged;
        } else {
            this.mIsBeingDragged = false;
            this.mIsUnableToDrag = false;
            this.mActivePointerId = -1;
            if(this.mVelocityTracker != null) {
                this.mVelocityTracker.recycle();
                this.mVelocityTracker = null;
            }

            return false;
        }
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if(this.mFakeDragging) {
            return true;
        } else if(ev.getAction() == 0 && ev.getEdgeFlags() != 0) {
            return false;
        } else if(this.mAdapter != null && this.mAdapter.getCount() != 0) {
            if(this.mVelocityTracker == null) {
                this.mVelocityTracker = VelocityTracker.obtain();
            }

            this.mVelocityTracker.addMovement(ev);
            int action = ev.getAction();
            boolean needsInvalidate = false;
            int index;
            float x;
            switch(action & 255) {
                case 0:
                    this.mScroller.abortAnimation();
                    this.mPopulatePending = false;
                    this.populate();
                    this.mLastMotionX = this.mInitialMotionX = ev.getX();
                    this.mLastMotionY = this.mInitialMotionY = ev.getY();
                    this.mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                    break;
                case 1:
                    if(this.mIsBeingDragged) {
                        VelocityTracker index1 = this.mVelocityTracker;
                        index1.computeCurrentVelocity(1000, (float)this.mMaximumVelocity);
                        int x2 = (int) VelocityTrackerCompat.getXVelocity(index1, this.mActivePointerId);
                        this.mPopulatePending = true;
                        int width1 = this.getClientWidth();
                        int scrollX1 = this.getScrollX();
                        ViewPager.ItemInfo ii1 = this.infoForCurrentScrollPosition();
                        int currentPage1 = ii1.position;
                        float pageOffset = ((float)scrollX1 / (float)width1 - ii1.offset) / ii1.widthFactor;
                        int activePointerIndex = MotionEventCompat.findPointerIndex(ev, this.mActivePointerId);
                        if(activePointerIndex >= 0) {
                            float x1 = MotionEventCompat.getX(ev, activePointerIndex);
                            int totalDelta = (int)(x1 - this.mInitialMotionX);
                            int nextPage = this.determineTargetPage(currentPage1, pageOffset, x2, totalDelta);
                            this.setCurrentItemInternal(nextPage, true, true, x2);
                        } else {
                            Log.e("ViewPager", "Invalid pointerId=" + this.mActivePointerId + " in onTouchEvent while being dragged with ACTION_UP");
                            this.scrollToItem(this.mCurItem, true, 0, false);
                        }

                        this.mActivePointerId = -1;
                        this.endDrag();
                    }
                    break;
                case 2:
                    if(!this.mIsBeingDragged) {
                        index = MotionEventCompat.findPointerIndex(ev, this.mActivePointerId);
                        if(index < 0) {
                            Log.e("ViewPager", "Invalid pointerId=" + this.mActivePointerId + " in onTouchEvent while"
                                    + " not being dragged");
                            break;
                        }

                        x = MotionEventCompat.getX(ev, index);
                        float width = Math.abs(x - this.mLastMotionX);
                        float scrollX = MotionEventCompat.getY(ev, index);
                        float ii = Math.abs(scrollX - this.mLastMotionY);
                        if(width > (float)this.mTouchSlop && width > ii) {
                            this.mIsBeingDragged = true;
                            this.requestParentDisallowInterceptTouchEvent(true);
                            this.mLastMotionX = x - this.mInitialMotionX > 0.0F?this.mInitialMotionX + (float)this.mTouchSlop:this.mInitialMotionX - (float)this.mTouchSlop;
                            this.mLastMotionY = scrollX;
                            this.setScrollState(1);
                            this.setScrollingCacheEnabled(true);
                            ViewParent currentPage = this.getParent();
                            if(currentPage != null) {
                                currentPage.requestDisallowInterceptTouchEvent(true);
                            }
                        }
                    }

                    if(this.mIsBeingDragged) {
                        index = MotionEventCompat.findPointerIndex(ev, this.mActivePointerId);
                        if(index < 0) {
                            Log.e("ViewPager", "Invalid pointerId=" + this.mActivePointerId + " in onTouchEvent while being dragged");
                        } else {
                            x = MotionEventCompat.getX(ev, index);
                            needsInvalidate |= this.performDrag(x);
                        }
                    }
                    break;
                case 3:
                    if(this.mIsBeingDragged) {
                        this.scrollToItem(this.mCurItem, true, 0, false);
                        this.mActivePointerId = -1;
                        this.endDrag();
                    }
                case 4:
                default:
                    break;
                case 5:
                    index = MotionEventCompat.getActionIndex(ev);
                    x = MotionEventCompat.getX(ev, index);
                    this.mLastMotionX = x;
                    this.mActivePointerId = MotionEventCompat.getPointerId(ev, index);
                    break;
                case 6:
                    this.onSecondaryPointerUp(ev);
                    this.mLastMotionX = MotionEventCompat.getX(ev, MotionEventCompat.findPointerIndex(ev, this.mActivePointerId));
            }

            if(needsInvalidate) {
                ViewCompat.postInvalidateOnAnimation(this);
            }

            return true;
        } else {
            return false;
        }
    }

    private void requestParentDisallowInterceptTouchEvent(boolean disallowIntercept) {
        ViewParent parent = this.getParent();
        if(parent != null) {
            parent.requestDisallowInterceptTouchEvent(disallowIntercept);
        }

    }

    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        if(this.getScrollX() != scrollX) {
            this.onScrollChanged(scrollX, this.getScrollY(), this.getScrollX(), this.getScrollY());
            this.scrollTo(scrollX, this.getScrollY());
        }

    }

    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        int overScrollMode = ViewCompat.getOverScrollMode(this);
        boolean overScrollHorizontal = overScrollMode == 0 || overScrollMode == 1;
        boolean overScrollVertical = overScrollMode == 0 || overScrollMode == 1;
        int newScrollX = scrollX + deltaX;
        boolean isOverScrollX;
        if(deltaX > 0) {
            isOverScrollX = newScrollX > scrollRangeX;
        } else if(deltaX < 0) {
            isOverScrollX = newScrollX < scrollRangeX;
        } else {
            isOverScrollX = false;
        }

        int newScrollY;
        if(isTouchEvent && isOverScrollX) {
            newScrollY = Math.abs(scrollX - scrollRangeX);
            float isOverScrollY = 0.0F;
            if((double)newScrollY < 0.5D * (double)maxOverScrollX) {
                isOverScrollY = (float)maxOverScrollX / (float)(newScrollY * 12 + maxOverScrollX) * (float)deltaX;
            } else if((double)newScrollY < 0.67D * (double)maxOverScrollX) {
                isOverScrollY = (float)maxOverScrollX / (float)(newScrollY * 20 + maxOverScrollX) * (float)deltaX;
            } else if(Math.abs(deltaX) > 20) {
                isOverScrollY = deltaX > 0?1.0F:-1.0F;
            } else {
                isOverScrollY = 0.0F;
            }

            newScrollX = scrollX + (int)isOverScrollY;
        }

        if(!overScrollHorizontal) {
            maxOverScrollX = 0;
        }

        newScrollY = scrollY + deltaY;
        boolean isOverScrollY1;
        if(deltaY > 0) {
            isOverScrollY1 = newScrollY > scrollRangeY;
        } else if(deltaY < 0) {
            isOverScrollY1 = newScrollY < scrollRangeY;
        } else {
            isOverScrollY1 = false;
        }

        int left;
        if(isTouchEvent && isOverScrollY1) {
            left = Math.abs(scrollY - scrollRangeY);
            float right = 0.0F;
            if((double)left < 0.5D * (double)maxOverScrollY) {
                right = (float)maxOverScrollY / (float)(left * 12 + maxOverScrollY) * (float)deltaY;
            } else if((double)left < 0.67D * (double)maxOverScrollY) {
                right = (float)maxOverScrollY / (float)(left * 20 + maxOverScrollY) * (float)deltaY;
            } else if(Math.abs(deltaY) > 20) {
                right = deltaY > 0?1.0F:-1.0F;
            } else {
                right = 0.0F;
            }

            newScrollY = scrollY + (int)right;
        }

        if(!overScrollVertical) {
            maxOverScrollY = 0;
        }

        left = -(Math.abs(maxOverScrollX) + Math.abs(scrollRangeX));
        int right1 = maxOverScrollX + scrollRangeX;
        int top = -(Math.abs(maxOverScrollY) + Math.abs(scrollRangeY));
        int bottom = maxOverScrollY + scrollRangeY;
        boolean clampedX = false;
        if(newScrollX > right1 && newScrollX > scrollX) {
            newScrollX = right1;
            clampedX = true;
        } else if(newScrollX < left && newScrollX < scrollX) {
            newScrollX = left;
            clampedX = true;
        }

        boolean clampedY = false;
        if(newScrollY > bottom && newScrollY > scrollY) {
            newScrollY = bottom;
            clampedY = true;
        } else if(newScrollY < top && newScrollY < scrollY) {
            newScrollY = top;
            clampedY = true;
        }

        this.onOverScrolled(newScrollX, newScrollY, clampedX, clampedY);
        return clampedX || clampedY;
    }

    private boolean performDrag(float x) {
        boolean needsInvalidate = false;
        float deltaX = this.mLastMotionX - x;
        this.mLastMotionX = x;
        float oldScrollX = (float)this.getScrollX();
        float scrollX = oldScrollX + deltaX;
        int width = this.getClientWidth();
        float leftBound = (float)width * this.mFirstOffset;
        float rightBound = (float)width * this.mLastOffset;
        boolean leftAbsolute = true;
        boolean rightAbsolute = true;
        ViewPager.ItemInfo firstItem = (ViewPager.ItemInfo)this.mItems.get(0);
        ViewPager.ItemInfo lastItem = (ViewPager.ItemInfo)this.mItems.get(this.mItems.size() - 1);
        if(firstItem.position != 0) {
            leftAbsolute = false;
            leftBound = firstItem.offset * (float)width;
        }

        if(lastItem.position != this.mAdapter.getCount() - 1) {
            rightAbsolute = false;
            rightBound = lastItem.offset * (float)width;
        }

        boolean isOverScroll = false;
        if(scrollX < leftBound) {
            if(leftAbsolute) {
                this.overScrollBy((int)deltaX, 0, this.getScrollX(), this.getScrollY(), (int)leftBound, 0, this.mOverscrollDistance, 0, true);
                isOverScroll = true;
            }
        } else if(scrollX > rightBound && rightAbsolute) {
            this.overScrollBy((int)deltaX, 0, this.getScrollX(), this.getScrollY(), (int)rightBound, 0, this.mOverscrollDistance, 0, true);
            isOverScroll = true;
        }

        if(!isOverScroll) {
            this.mLastMotionX += scrollX - (float)((int)scrollX);
            this.scrollTo((int)scrollX, this.getScrollY());
            this.pageScrolled((int)scrollX);
        }

        return needsInvalidate;
    }

    private ViewPager.ItemInfo infoForCurrentScrollPosition() {
        int width = this.getClientWidth();
        float scrollOffset = width > 0?(float)this.getScrollX() / (float)width:0.0F;
        float marginOffset = width > 0?(float)this.mPageMargin / (float)width:0.0F;
        int lastPos = -1;
        float lastOffset = 0.0F;
        float lastWidth = 0.0F;
        boolean first = true;
        ViewPager.ItemInfo lastItem = null;

        for(int i = 0; i < this.mItems.size(); ++i) {
            ViewPager.ItemInfo ii = (ViewPager.ItemInfo)this.mItems.get(i);
            if(!first && ii.position != lastPos + 1) {
                ii = this.mTempItem;
                ii.offset = lastOffset + lastWidth + marginOffset;
                ii.position = lastPos + 1;
                ii.widthFactor = this.mAdapter.getPageWidth(ii.position);
                --i;
            }

            float offset = ii.offset;
            float rightBound = offset + ii.widthFactor + marginOffset;
            if(!first && scrollOffset < offset) {
                return lastItem;
            }

            if(scrollOffset < rightBound || i == this.mItems.size() - 1) {
                return ii;
            }

            first = false;
            lastPos = ii.position;
            lastOffset = offset;
            lastWidth = ii.widthFactor;
            lastItem = ii;
        }

        return lastItem;
    }

    private int determineTargetPage(int currentPage, float pageOffset, int velocity, int deltaX) {
        int targetPage;
        if(Math.abs(deltaX) > this.mFlingDistance && Math.abs(velocity) > this.mMinimumVelocity) {
            targetPage = velocity > 0?currentPage:currentPage + 1;
        } else {
            float firstItem = currentPage >= this.mCurItem?0.4F:0.6F;
            targetPage = (int)((float)currentPage + pageOffset + firstItem);
        }

        if(this.mItems.size() > 0) {
            ViewPager.ItemInfo firstItem1 = (ViewPager.ItemInfo)this.mItems.get(0);
            ViewPager.ItemInfo lastItem = (ViewPager.ItemInfo)this.mItems.get(this.mItems.size() - 1);
            targetPage = Math.max(firstItem1.position, Math.min(targetPage, lastItem.position));
        }

        return targetPage;
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(this.mPageMargin > 0 && this.mMarginDrawable != null && this.mItems.size() > 0 && this.mAdapter != null) {
            int scrollX = this.getScrollX();
            int width = this.getWidth();
            float marginOffset = (float)this.mPageMargin / (float)width;
            int itemIndex = 0;
            ViewPager.ItemInfo ii = (ViewPager.ItemInfo)this.mItems.get(0);
            float offset = ii.offset;
            int itemCount = this.mItems.size();
            int firstPos = ii.position;
            int lastPos = ((ViewPager.ItemInfo)this.mItems.get(itemCount - 1)).position;

            for(int pos = firstPos; pos < lastPos; ++pos) {
                while(pos > ii.position && itemIndex < itemCount) {
                    ++itemIndex;
                    ii = (ViewPager.ItemInfo)this.mItems.get(itemIndex);
                }

                float drawAt;
                if(pos == ii.position) {
                    drawAt = (ii.offset + ii.widthFactor) * (float)width;
                    offset = ii.offset + ii.widthFactor + marginOffset;
                } else {
                    float widthFactor = this.mAdapter.getPageWidth(pos);
                    drawAt = (offset + widthFactor) * (float)width;
                    offset += widthFactor + marginOffset;
                }

                if(drawAt + (float)this.mPageMargin > (float)scrollX) {
                    this.mMarginDrawable.setBounds((int)drawAt, this.mTopPageBounds, (int)(drawAt + (float)this.mPageMargin + 0.5F), this.mBottomPageBounds);
                    this.mMarginDrawable.draw(canvas);
                }

                if(drawAt > (float)(scrollX + width)) {
                    break;
                }
            }
        }

    }

    public boolean beginFakeDrag() {
        if(this.mIsBeingDragged) {
            return false;
        } else {
            this.mFakeDragging = true;
            this.setScrollState(1);
            this.mInitialMotionX = this.mLastMotionX = 0.0F;
            if(this.mVelocityTracker == null) {
                this.mVelocityTracker = VelocityTracker.obtain();
            } else {
                this.mVelocityTracker.clear();
            }

            long time = SystemClock.uptimeMillis();
            MotionEvent ev = MotionEvent.obtain(time, time, 0, 0.0F, 0.0F, 0);
            this.mVelocityTracker.addMovement(ev);
            ev.recycle();
            this.mFakeDragBeginTime = time;
            return true;
        }
    }

    public void endFakeDrag() {
        if(!this.mFakeDragging) {
            throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
        } else {
            VelocityTracker velocityTracker = this.mVelocityTracker;
            velocityTracker.computeCurrentVelocity(1000, (float)this.mMaximumVelocity);
            int initialVelocity = (int) VelocityTrackerCompat.getXVelocity(velocityTracker, this.mActivePointerId);
            this.mPopulatePending = true;
            int width = this.getClientWidth();
            int scrollX = this.getScrollX();
            ViewPager.ItemInfo ii = this.infoForCurrentScrollPosition();
            int currentPage = ii.position;
            float pageOffset = ((float)scrollX / (float)width - ii.offset) / ii.widthFactor;
            int totalDelta = (int)(this.mLastMotionX - this.mInitialMotionX);
            int nextPage = this.determineTargetPage(currentPage, pageOffset, initialVelocity, totalDelta);
            this.setCurrentItemInternal(nextPage, true, true, initialVelocity);
            this.endDrag();
            this.mFakeDragging = false;
        }
    }

    public void fakeDragBy(float xOffset) {
        if(!this.mFakeDragging) {
            throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
        } else {
            this.mLastMotionX += xOffset;
            float oldScrollX = (float)this.getScrollX();
            float scrollX = oldScrollX - xOffset;
            int width = this.getClientWidth();
            float leftBound = (float)width * this.mFirstOffset;
            float rightBound = (float)width * this.mLastOffset;
            ViewPager.ItemInfo firstItem = (ViewPager.ItemInfo)this.mItems.get(0);
            ViewPager.ItemInfo lastItem = (ViewPager.ItemInfo)this.mItems.get(this.mItems.size() - 1);
            if(firstItem.position != 0) {
                leftBound = firstItem.offset * (float)width;
            }

            if(lastItem.position != this.mAdapter.getCount() - 1) {
                rightBound = lastItem.offset * (float)width;
            }

            if(scrollX < leftBound) {
                scrollX = leftBound;
            } else if(scrollX > rightBound) {
                scrollX = rightBound;
            }

            this.mLastMotionX += scrollX - (float)((int)scrollX);
            this.scrollTo((int)scrollX, this.getScrollY());
            this.pageScrolled((int)scrollX);
            long time = SystemClock.uptimeMillis();
            MotionEvent ev = MotionEvent.obtain(this.mFakeDragBeginTime, time, 2, this.mLastMotionX, 0.0F, 0);
            this.mVelocityTracker.addMovement(ev);
            ev.recycle();
        }
    }

    public boolean isFakeDragging() {
        return this.mFakeDragging;
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        int pointerIndex = MotionEventCompat.getActionIndex(ev);
        int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
        if(pointerId == this.mActivePointerId) {
            int newPointerIndex = pointerIndex == 0?1:0;
            this.mLastMotionX = MotionEventCompat.getX(ev, newPointerIndex);
            this.mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
            if(this.mVelocityTracker != null) {
                this.mVelocityTracker.clear();
            }
        }

    }

    private void endDrag() {
        this.mIsBeingDragged = false;
        this.mIsUnableToDrag = false;
        if(this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }

    }

    private void setScrollingCacheEnabled(boolean enabled) {
        if(this.mScrollingCacheEnabled != enabled) {
            this.mScrollingCacheEnabled = enabled;
        }

    }

    public boolean canScrollHorizontally(int direction) {
        if(this.mAdapter == null) {
            return false;
        } else {
            int width = this.getClientWidth();
            int scrollX = this.getScrollX();
            return direction < 0?scrollX > (int)((float)width * this.mFirstOffset):(direction > 0?scrollX < (int)((float)width * this.mLastOffset):false);
        }
    }

    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if(v instanceof ViewGroup) {
            ViewGroup group = (ViewGroup)v;
            int scrollX = v.getScrollX();
            int scrollY = v.getScrollY();
            int count = group.getChildCount();

            for(int i = count - 1; i >= 0; --i) {
                View child = group.getChildAt(i);
                if(x + scrollX >= child.getLeft() && x + scrollX < child.getRight() && y + scrollY >= child.getTop() && y + scrollY < child.getBottom() && this.canScroll(child, true, dx, x + scrollX - child.getLeft(), y + scrollY - child.getTop())) {
                    return true;
                }
            }
        }

        return checkV && ViewCompat.canScrollHorizontally(v, -dx);
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        return super.dispatchKeyEvent(event) || this.executeKeyEvent(event);
    }

    public boolean executeKeyEvent(KeyEvent event) {
        boolean handled = false;
        if(event.getAction() == 0) {
            switch(event.getKeyCode()) {
                case 21:
                    handled = this.arrowScroll(17);
                    break;
                case 22:
                    handled = this.arrowScroll(66);
                    break;
                case 61:
                    if(Build.VERSION.SDK_INT >= 11) {
                        if(KeyEventCompat.hasNoModifiers(event)) {
                            handled = this.arrowScroll(2);
                        } else if(KeyEventCompat.hasModifiers(event, 1)) {
                            handled = this.arrowScroll(1);
                        }
                    }
            }
        }

        return handled;
    }

    public boolean arrowScroll(int direction) {
        View currentFocused = this.findFocus();
        boolean handled;
        if(currentFocused == this) {
            currentFocused = null;
        } else if(currentFocused != null) {
            handled = false;

            for(ViewParent nextFocused = currentFocused.getParent(); nextFocused instanceof ViewGroup; nextFocused = nextFocused.getParent()) {
                if(nextFocused == this) {
                    handled = true;
                    break;
                }
            }

            if(!handled) {
                StringBuilder nextFocused1 = new StringBuilder();
                nextFocused1.append(currentFocused.getClass().getSimpleName());

                for(ViewParent nextLeft = currentFocused.getParent(); nextLeft instanceof ViewGroup; nextLeft = nextLeft.getParent()) {
                    nextFocused1.append(" => ").append(nextLeft.getClass().getSimpleName());
                }

                Log.e("ViewPager", "arrowScroll tried to find focus based on non-child current focused view " + nextFocused1.toString());
                currentFocused = null;
            }
        }

        handled = false;
        View nextFocused2 = FocusFinder.getInstance().findNextFocus(this, currentFocused, direction);
        if(nextFocused2 != null && nextFocused2 != currentFocused) {
            int currLeft;
            int nextLeft1;
            if(direction == 17) {
                nextLeft1 = this.getChildRectInPagerCoordinates(this.mTempRect, nextFocused2).left;
                currLeft = this.getChildRectInPagerCoordinates(this.mTempRect, currentFocused).left;
                if(currentFocused != null && nextLeft1 >= currLeft) {
                    handled = this.pageLeft();
                } else {
                    handled = nextFocused2.requestFocus();
                }
            } else if(direction == 66) {
                nextLeft1 = this.getChildRectInPagerCoordinates(this.mTempRect, nextFocused2).left;
                currLeft = this.getChildRectInPagerCoordinates(this.mTempRect, currentFocused).left;
                if(currentFocused != null && nextLeft1 <= currLeft) {
                    handled = this.pageRight();
                } else {
                    handled = nextFocused2.requestFocus();
                }
            }
        } else if(direction != 17 && direction != 1) {
            if(direction == 66 || direction == 2) {
                handled = this.pageRight();
            }
        } else {
            handled = this.pageLeft();
        }

        if(handled) {
            this.playSoundEffect(SoundEffectConstants.getContantForFocusDirection(direction));
        }

        return handled;
    }

    private Rect getChildRectInPagerCoordinates(Rect outRect, View child) {
        if(outRect == null) {
            outRect = new Rect();
        }

        if(child == null) {
            outRect.set(0, 0, 0, 0);
            return outRect;
        } else {
            outRect.left = child.getLeft();
            outRect.right = child.getRight();
            outRect.top = child.getTop();
            outRect.bottom = child.getBottom();

            ViewGroup group;
            for(ViewParent parent = child.getParent(); parent instanceof ViewGroup && parent != this; parent = group.getParent()) {
                group = (ViewGroup)parent;
                outRect.left += group.getLeft();
                outRect.right += group.getRight();
                outRect.top += group.getTop();
                outRect.bottom += group.getBottom();
            }

            return outRect;
        }
    }

    boolean pageLeft() {
        if(this.mCurItem > 0) {
            this.setCurrentItem(this.mCurItem - 1, true);
            return true;
        } else {
            return false;
        }
    }

    boolean pageRight() {
        if(this.mAdapter != null && this.mCurItem < this.mAdapter.getCount() - 1) {
            this.setCurrentItem(this.mCurItem + 1, true);
            return true;
        } else {
            return false;
        }
    }

    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        int focusableCount = views.size();
        int descendantFocusability = this.getDescendantFocusability();
        if(descendantFocusability != 393216) {
            for(int i = 0; i < this.getChildCount(); ++i) {
                View child = this.getChildAt(i);
                if(child.getVisibility() == VISIBLE) {
                    ViewPager.ItemInfo ii = this.infoForChild(child);
                    if(ii != null && ii.position == this.mCurItem) {
                        child.addFocusables(views, direction, focusableMode);
                    }
                }
            }
        }

        if(descendantFocusability != 262144 || focusableCount == views.size()) {
            if(!this.isFocusable()) {
                return;
            }

            if((focusableMode & 1) == 1 && this.isInTouchMode() && !this.isFocusableInTouchMode()) {
                return;
            }

            if(views != null) {
                views.add(this);
            }
        }

    }

    public void addTouchables(ArrayList<View> views) {
        for(int i = 0; i < this.getChildCount(); ++i) {
            View child = this.getChildAt(i);
            if(child.getVisibility() == VISIBLE) {
                ViewPager.ItemInfo ii = this.infoForChild(child);
                if(ii != null && ii.position == this.mCurItem) {
                    child.addTouchables(views);
                }
            }
        }

    }

    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        int count = this.getChildCount();
        int index;
        byte increment;
        int end;
        if((direction & 2) != 0) {
            index = 0;
            increment = 1;
            end = count;
        } else {
            index = count - 1;
            increment = -1;
            end = -1;
        }

        for(int i = index; i != end; i += increment) {
            View child = this.getChildAt(i);
            if(child.getVisibility() == VISIBLE) {
                ViewPager.ItemInfo ii = this.infoForChild(child);
                if(ii != null && ii.position == this.mCurItem && child.requestFocus(direction, previouslyFocusedRect)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        if(event.getEventType() == 4096) {
            return super.dispatchPopulateAccessibilityEvent(event);
        } else {
            int childCount = this.getChildCount();

            for(int i = 0; i < childCount; ++i) {
                View child = this.getChildAt(i);
                if(child.getVisibility() == VISIBLE) {
                    ViewPager.ItemInfo ii = this.infoForChild(child);
                    if(ii != null && ii.position == this.mCurItem && child.dispatchPopulateAccessibilityEvent(event)) {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    protected android.view.ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new ViewPager.LayoutParams();
    }

    protected android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return this.generateDefaultLayoutParams();
    }

    protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return p instanceof ViewPager.LayoutParams && super.checkLayoutParams(p);
    }

    public android.view.ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new ViewPager.LayoutParams(this.getContext(), attrs);
    }

    static class ViewPositionComparator implements Comparator<View> {
        ViewPositionComparator() {
        }

        public int compare(View lhs, View rhs) {
            ViewPager.LayoutParams llp = (ViewPager.LayoutParams)lhs.getLayoutParams();
            ViewPager.LayoutParams rlp = (ViewPager.LayoutParams)rhs.getLayoutParams();
            return llp.isDecor != rlp.isDecor?(llp.isDecor?1:-1):llp.position - rlp.position;
        }
    }

    public static class LayoutParams extends android.view.ViewGroup.LayoutParams {
        public boolean isDecor;
        public int gravity;
        float widthFactor = 0.0F;
        boolean needsMeasure;
        int position;
        int childIndex;

        public LayoutParams() {
            super(-1, -1);
        }

        public LayoutParams(Context context, AttributeSet attrs) {
            super(context, attrs);
            TypedArray a = context.obtainStyledAttributes(attrs, ViewPager.LAYOUT_ATTRS);
            this.gravity = a.getInteger(0, 48);
            a.recycle();
        }
    }

    private class PagerObserver extends DataSetObserver {
        private PagerObserver() {
        }

        public void onChanged() {
            ViewPager.this.dataSetChanged();
        }

        public void onInvalidated() {
            ViewPager.this.dataSetChanged();
        }
    }

    class MyAccessibilityDelegate extends AccessibilityDelegateCompat {
        MyAccessibilityDelegate() {
        }

        public void onInitializeAccessibilityEvent(View host, AccessibilityEvent event) {
            super.onInitializeAccessibilityEvent(host, event);
            event.setClassName(ViewPager.class.getName());
            AccessibilityRecordCompat recordCompat = AccessibilityRecordCompat.obtain();
            recordCompat.setScrollable(this.canScroll());
            if(event.getEventType() == 4096 && ViewPager.this.mAdapter != null) {
                recordCompat.setItemCount(ViewPager.this.mAdapter.getCount());
                recordCompat.setFromIndex(ViewPager.this.mCurItem);
                recordCompat.setToIndex(ViewPager.this.mCurItem);
            }

        }

        public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfoCompat info) {
            super.onInitializeAccessibilityNodeInfo(host, info);
            info.setClassName(ViewPager.class.getName());
            info.setScrollable(this.canScroll());
            if(ViewPager.this.canScrollHorizontally(1)) {
                info.addAction(4096);
            }

            if(ViewPager.this.canScrollHorizontally(-1)) {
                info.addAction(8192);
            }

        }

        public boolean performAccessibilityAction(View host, int action, Bundle args) {
            if(super.performAccessibilityAction(host, action, args)) {
                return true;
            } else {
                switch(action) {
                    case 4096:
                        if(ViewPager.this.canScrollHorizontally(1)) {
                            ViewPager.this.setCurrentItem(ViewPager.this.mCurItem + 1);
                            return true;
                        }

                        return false;
                    case 8192:
                        if(ViewPager.this.canScrollHorizontally(-1)) {
                            ViewPager.this.setCurrentItem(ViewPager.this.mCurItem - 1);
                            return true;
                        }

                        return false;
                    default:
                        return false;
                }
            }
        }

        private boolean canScroll() {
            return ViewPager.this.mAdapter != null && ViewPager.this.mAdapter.getCount() > 1;
        }
    }

    public static class SavedState extends BaseSavedState {
        int position;
        Parcelable adapterState;
        ClassLoader loader;
        public static final Creator<SavedState>
                CREATOR = ParcelableCompat.newCreator(new ParcelableCompatCreatorCallbacks() {
            public ViewPager.SavedState createFromParcel(Parcel in, ClassLoader loader) {
                return new ViewPager.SavedState(in, loader);
            }

            public ViewPager.SavedState[] newArray(int size) {
                return new ViewPager.SavedState[size];
            }
        });

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.position);
            out.writeParcelable(this.adapterState, flags);
        }

        public String toString() {
            return "FragmentPager.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " position=" + this.position + "}";
        }

        SavedState(Parcel in, ClassLoader loader) {
            super(in);
            if(loader == null) {
                loader = this.getClass().getClassLoader();
            }

            this.position = in.readInt();
            this.adapterState = in.readParcelable(loader);
            this.loader = loader;
        }
    }

    interface Decor {
    }

    interface OnAdapterChangeListener {
        void onAdapterChanged(PagerAdapter var1, PagerAdapter var2);
    }

    public interface PageTransformer {
        void transformPage(View var1, float var2);
    }

    public static class SimpleOnPageChangeListener implements ViewPager.OnPageChangeListener {
        public SimpleOnPageChangeListener() {
        }

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        public void onPageSelected(int position) {
        }

        public void onPageScrollStateChanged(int state) {
        }
    }

    public interface OnPageChangeListener {
        void onPageScrolled(int var1, float var2, int var3);

        void onPageSelected(int var1);

        void onPageScrollStateChanged(int var1);
    }

    static class ItemInfo {
        Object object;
        int position;
        boolean scrolling;
        float widthFactor;
        float offset;

        ItemInfo() {
        }
    }
}
