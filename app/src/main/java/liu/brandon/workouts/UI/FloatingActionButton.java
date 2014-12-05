package liu.brandon.workouts.UI;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
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

    private int mColorNormal;
    private int mColorPressed;
    private int mColorRipple;


    public FloatingActionButton(Context context) {
        super(context);
    }

    public FloatingActionButton(Context context, AttributeSet attrs){
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.FloatingActionButton, 0, 0);
        try{
            mColorNormal = a.getColor(R.styleable.FloatingActionButton_colorNormal, R.color.grey_white_1000);
            mColorPressed = a.getColor(R.styleable.FloatingActionButton_colorPressed, R.color.grey_white_1000);
            mColorRipple = a.getColor(R.styleable.FloatingActionButton_colorRipple, R.color.grey_white_1000);
            mType = a.getInt(R.styleable.FloatingActionButton_type, TYPE_NORMAL);
        } finally {
            a.recycle();
        }

        initBackground();
    }

    private void initBackground() {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{android.R.attr.state_pressed}, createDrawable(mColorPressed));
        drawable.addState(new int[]{}, createDrawable(mColorNormal));
        if (currentapiVersion == android.os.Build.)
    }

    private Drawable createDrawable(int color) {
        OvalShape ovalShape = new OvalShape();
        ShapeDrawable shapeDrawable = new ShapeDrawable(ovalShape);
        shapeDrawable.getPaint().setColor(color);
        return shapeDrawable;
    }


}
