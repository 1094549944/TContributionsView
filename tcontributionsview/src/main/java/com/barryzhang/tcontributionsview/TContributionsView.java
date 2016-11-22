package com.barryzhang.tcontributionsview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.barryzhang.tcontributionsview.adapter.BaseContributionsViewAdapter;
import com.barryzhang.tcontributionsview.adapter.PositionContributionsViewAdapter;
import com.barryzhang.tcontributionsview.adapter.TestContributionAdapter;

/**
 * https://github.com/barryhappy
 * Created by Barry on 2016/11/19
 */

public class TContributionsView extends View {

    BaseContributionsViewAdapter mAdapter;

    public static int COLOR_ITEM_EMPTY = Color.rgb(238, 238, 238);

    private int colorEmpty = Color.parseColor("#e0e0e0");
    private int colorL1 = Color.parseColor("#cde372");
    private int colorL2 = Color.parseColor("#7bbd52");
    private int colorL3 = Color.parseColor("#389631");
    private int colorL4 = Color.parseColor("#1a571b");

    protected int itemWidth = 30;
    protected int itemHeight = 30;
    protected int itemSpace = 6;
    Paint paintEmpty = new Paint();
    Paint paintL1 = new Paint();
    Paint paintL2 = new Paint();
    Paint paintL3 = new Paint();
    Paint paintL4 = new Paint();
    RectF rectF;

    public TContributionsView(Context context) {
        this(context, null);
    }

    public TContributionsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TContributionsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TContributionsView);
        try {
            itemWidth = a.getDimensionPixelSize(R.styleable.TContributionsView_contributions_item_width, 20);
            itemHeight = a.getDimensionPixelSize(R.styleable.TContributionsView_contributions_item_height, 20);
            itemSpace = a.getDimensionPixelSize(R.styleable.TContributionsView_contributions_item_space, 2);
            colorEmpty = a.getColor(R.styleable.TContributionsView_contributions_color_0, colorEmpty);
            colorL1 = a.getColor(R.styleable.TContributionsView_contributions_color_1, colorL1);
            colorL2 = a.getColor(R.styleable.TContributionsView_contributions_color_2, colorL2);
            colorL3 = a.getColor(R.styleable.TContributionsView_contributions_color_3, colorL3);
            colorL4 = a.getColor(R.styleable.TContributionsView_contributions_color_4, colorL4);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            a.recycle();
        }

        paintEmpty.setColor(colorEmpty);
        paintL1.setColor(colorL1);
        paintL2.setColor(colorL2);
        paintL3.setColor(colorL3);
        paintL4.setColor(colorL4);


        rectF = new RectF(0, 0, itemWidth, itemHeight);
        if (isInEditMode()) {
            mAdapter = new TestContributionAdapter();
            this.setAdapter(mAdapter);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //        final int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        //        final int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        int heightSize = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
        int row = 5;
        int column = 5;
        if (this.mAdapter != null) {
            row = mAdapter.getRowCount();
            column = mAdapter.getColumnCount();
        }
        int measureWidth = column * (itemWidth + itemSpace) - itemSpace;
        int measureHeight = row * (itemHeight + itemSpace) - itemSpace;


        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? widthMeasureSpec : measureWidth,
                heightMode == MeasureSpec.EXACTLY ? heightMeasureSpec : measureHeight);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mAdapter != null) {
            final int columnCount = mAdapter.getColumnCount();
            final int rowCount = mAdapter.getRowCount();
            for (int week = 0; week < columnCount; week++) {
                for (int day = 0; day < rowCount; day++) {
                    rectF.left = (week == 0 ? 0 : week * (itemWidth + itemSpace));
                    rectF.right = rectF.left + itemWidth;
                    rectF.top = (day == 0 ? 0 : day * (itemHeight + itemSpace));
                    rectF.bottom = rectF.top + itemHeight;
                    final int level = mAdapter.getLevel(day, week);
                    if (level >= 0) {
                        drawItem(rectF, canvas, level);
                    }
                }
            }
        }
    }

    protected void drawItem(RectF rect, Canvas canvas, int level) {
        canvas.drawRect(rect, getPaintByLevel(level));

        //        canvas.drawCircle((rectF.left+rectF.right)/2,
        //                (rectF.top+rectF.bottom)/2,
        //                Math.min(itemWidth,itemHeight)/2,
        //                getPaintByLevel(level));

        //        canvas.drawOval(rect,getPaintByLevel(level));

        //        canvas.drawCircle((rectF.left+rectF.right)/2,
        //                                (rectF.top+rectF.bottom)/2,
        //                                Math.min(itemWidth,itemHeight)/2,
        //                                getPaintByLevel(level));
        //        canvas.drawText(String.valueOf(level),
        //                (rectF.left+rectF.right)/2,
        //                (rectF.top+rectF.bottom)/2,
        //                getPaintByLevel(level));
        //        canvas.drawColor(Color.parseColor());
    }

    private Paint getPaintByLevel(int level) {

        switch (level) {
            case 0:
                return paintEmpty;
            case 1:
                return paintL1;
            case 2:
                return paintL2;
            case 3:
                return paintL3;
            case 4:
                return paintL4;
        }
        return paintEmpty;
    }

    public void setAdapter(BaseContributionsViewAdapter adapter) {
        this.mAdapter = adapter;
        if (this.mAdapter != null) {
            this.mAdapter.setContributionsView(this);
            this.mAdapter.notifyDataSetChanged();
        }
    }
}