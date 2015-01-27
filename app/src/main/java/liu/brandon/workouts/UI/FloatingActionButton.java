package liu.brandon.workouts.UI;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageButton;

import liu.brandon.workouts.R;

/**
 * Created by Brandon on 12/3/14.
 */
public class FloatingActionButton extends ImageButton {

    private final static int currentapiVersion = android.os.Build.VERSION.SDK_INT;

    private int TYPE_NORMAL = 0;
    private int TYPE_MINI = 1;
    private int mType;
    private int mElevation;
    private int mShadowSize;

    private int mColorNormal;
    private int mColorPressed;
    private int mColorRipple;

    private boolean mClicked = false;
    private boolean mRotatable;
    private boolean mMarginsSet;
    private boolean mExpanded = false;

    private static final int ANIMATION_DURATION = 300;
    private static final float COLLAPSED_PLUS_ROTATION = 0f;
    private static final float EXPANDED_PLUS_ROTATION = 90f + 45f;

    private AnimatorSet mExpandAnimation = new AnimatorSet().setDuration(ANIMATION_DURATION);
    private AnimatorSet mCollapseAnimation = new AnimatorSet().setDuration(ANIMATION_DURATION);

    private StateListDrawable mDrawable;

    private RippleDrawable mRippleDrawable;

    public FloatingActionButton(Context context) {
        super(context);
    }

    public FloatingActionButton(Context context, AttributeSet attrs){
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FloatingActionButton);
        mShadowSize = getResources().getDimensionPixelSize(R.dimen.fab_shadow_size);
        try{
            mColorNormal = a.getColor(R.styleable.FloatingActionButton_colorNormal, R.color.grey_white_1000);
            mColorPressed = a.getColor(R.styleable.FloatingActionButton_colorPressed, R.color.grey_white_1000);
            mColorRipple = a.getColor(R.styleable.FloatingActionButton_colorRipple, R.color.grey_white_1000);
            mType = a.getInt(R.styleable.FloatingActionButton_type, TYPE_NORMAL);
            mRotatable = a.getBoolean(R.styleable.FloatingActionButton_rotatable, false);
        } finally {
            a.recycle();
        }

        initBackground();

        if(mRotatable)
            animateView();

    }

    @SuppressWarnings("deprecation")
    @TargetApi(21)
    private void initBackground() {
        mDrawable = new StateListDrawable();
        mDrawable.addState(new int[]{android.R.attr.state_pressed}, createDrawable(mColorPressed));
        mDrawable.addState(new int[]{}, createDrawable(mColorNormal));
        if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP){
            setElevation(getResources().getDimension(R.dimen.fab_elevation_lollipop));
            setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    int size = getResources().getDimensionPixelSize(mType == TYPE_NORMAL ? R.dimen.fab_type_normal : R.dimen.fab_type_mini);
                    outline.setOval(0, 0, size, size);
                }
            });

            mRippleDrawable = new RippleDrawable(new ColorStateList(new int[][]{{}},
                    new int[]{mColorRipple}), mDrawable, null);
            setClipToOutline(true);
            setBackground(mRippleDrawable);
        }
        else if (currentapiVersion >= Build.VERSION_CODES.JELLY_BEAN){
            setBackground(mDrawable);
        }
        else{
            setBackgroundDrawable(mDrawable);
        }
    }

    @SuppressWarnings("deprecation")
    @TargetApi(21)
    public Drawable getRoundedDrawable(int colorNormal, int colorPressed){
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{android.R.attr.state_pressed}, createDrawable(colorPressed));
        drawable.addState(new int[]{}, createDrawable(colorNormal));
        if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP){
            setElevation(getResources().getDimension(R.dimen.fab_elevation_lollipop));
            setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    int size = getResources().getDimensionPixelSize(mType == TYPE_NORMAL ? R.dimen.fab_type_normal : R.dimen.fab_type_mini);
                    outline.setOval(0, 0, size, size);
                }
            });

            mRippleDrawable = new RippleDrawable(new ColorStateList(new int[][]{{}},
                    new int[]{mColorRipple}), drawable, null);
            setClipToOutline(true);
            return mRippleDrawable;
        }
        else{
            return drawable;
        }
    }

    private Drawable createDrawable(int color) {
        OvalShape ovalShape = new OvalShape();
        ShapeDrawable shapeDrawable = new ShapeDrawable(ovalShape);
        shapeDrawable.getPaint().setColor(color);

        if (currentapiVersion<Build.VERSION_CODES.LOLLIPOP) {
            Drawable shadowDrawable = getResources().getDrawable(mType == TYPE_NORMAL ? R.drawable.shadow : R.drawable.shadow_mini);
            LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{shadowDrawable, shapeDrawable});
            layerDrawable.setLayerInset(1, mShadowSize, mShadowSize, mShadowSize, mShadowSize);
            return layerDrawable;
        } else {
            return shapeDrawable;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = getResources().getDimensionPixelSize(mType == TYPE_NORMAL ? R.dimen.fab_type_normal : R.dimen.fab_type_mini);
        if(currentapiVersion < Build.VERSION_CODES.LOLLIPOP){
            size += mShadowSize * 2;
            setMarginsWithoutShadow();
        }
        setMeasuredDimension(size, size);
    }

    private void animateView(){
        final OvershootInterpolator interpolator = new OvershootInterpolator();

        final ObjectAnimator collapseAnimator = ObjectAnimator.ofFloat(this, "rotation", EXPANDED_PLUS_ROTATION, COLLAPSED_PLUS_ROTATION);
        final ObjectAnimator expandAnimator = ObjectAnimator.ofFloat(this, "rotation", COLLAPSED_PLUS_ROTATION, EXPANDED_PLUS_ROTATION);

        collapseAnimator.setInterpolator(interpolator);
        expandAnimator.setInterpolator(interpolator);

        mExpandAnimation.play(expandAnimator);
        mCollapseAnimation.play(collapseAnimator);

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClicked == true){
                    mClicked = false;
                    mExpandAnimation.cancel();
                    mCollapseAnimation.start();
                }
                else {
                    mClicked = true;
                    mCollapseAnimation.cancel();
                    mExpandAnimation.start();
                }
            }
        });
    }

    public void runAnimation(boolean expanded){
        final OvershootInterpolator interpolator = new OvershootInterpolator();

        final ObjectAnimator collapseAnimator = ObjectAnimator.ofFloat(this, "rotation", EXPANDED_PLUS_ROTATION, COLLAPSED_PLUS_ROTATION);
        final ObjectAnimator expandAnimator = ObjectAnimator.ofFloat(this, "rotation", COLLAPSED_PLUS_ROTATION, EXPANDED_PLUS_ROTATION);

        collapseAnimator.setInterpolator(interpolator);
        expandAnimator.setInterpolator(interpolator);

        mExpandAnimation.play(expandAnimator);
        mCollapseAnimation.play(collapseAnimator);


        if(expanded){
            mExpandAnimation.cancel();
            mCollapseAnimation.start();
        }
        else{
            mCollapseAnimation.cancel();
            mExpandAnimation.start();

        }

    }

    private void setMarginsWithoutShadow() {
        if (!mMarginsSet) {
            if (getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
                int leftMargin = layoutParams.leftMargin - mShadowSize;
                int topMargin = layoutParams.topMargin - mShadowSize;
                int rightMargin = layoutParams.rightMargin - mShadowSize;
                int bottomMargin = layoutParams.bottomMargin - mShadowSize;
                layoutParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);

                requestLayout();
                mMarginsSet = true;
            }
        }
    }

}
