package liu.brandon.workouts.UI;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Brandon on 2/4/15.
 */
public class FloatingActionMenu extends ViewGroup{

    private final static float mViewSeparation = 10f;
    private final static float mLabelSeparation = 0.5f;

    private final static int STATE_COLLAPSED = 0;
    private final static int STATE_EXPANDED = 1;

    private int mLeftWidth;
    private int mRightWidth;

    private Context mContext;

    private View mMainView;

    public FloatingActionMenu(Context context){
        super(context);
        init(context, null, 0);
    }

    public FloatingActionMenu(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        init(context, attributeSet, 0);
    }

    public FloatingActionMenu(Context context, AttributeSet attributeSet, int defStyle){
        super(context, attributeSet, defStyle);
        init(context, attributeSet, defStyle);

    }

    private void init(Context context, AttributeSet attributeSet, int defStyle){
        mContext = context;

    }

    @Override
    public boolean shouldDelayChildPressedState(){
        return false;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int count = getChildCount();

        int leftPos = getPaddingLeft();
        int rightPos = right - left - getPaddingRight();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    }

    public void expand(){

    }

    public void collapse(){

    }


    private float getCenterX(View view){
        return view.getX() + view.getWidth()/2;
    }

    private float getCenterY(View view){
        return view.getY() + view.getHeight()/2;
    }



}
