package edu.ucsb.cs.cs185.charlesweng.charleswengMultiTouch;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.widget.ImageView;

import com.example.charleswengtouchgesture.R;

@SuppressLint("NewApi") public class TouchView extends ImageView implements OnGestureListener, OnDoubleTapListener, OnScaleGestureListener, RotationGestureDetector.RotationListener {

	Matrix matrix;
	Matrix inverse;
    float lastFocusX;
    float lastFocusY;
    float focusX;
    float focusY;
    float dx;
    float dy;
    float lastRotateX;
    float lastRotateY;
    float rotateX;
    float rotateY;
    ArrayList<Tuple> drawPoints;
    float scaleFactor;
	float initPoint[];
	float currPoint[];
	private GestureDetector mDetector;
	private ScaleGestureDetector mScale;
	private RotationGestureDetector mRotate;

	class Tuple {
		public float x;
		public float y;
	}
	
	public TouchView(Context context) {
		super(context);
		this.setScaleType(ImageView.ScaleType.MATRIX);
		drawPoints = new ArrayList<Tuple>();
		initPoint = new float[2];
		currPoint = new float[2];
		matrix = new Matrix();
		//1 finger move gesture detector
		mDetector = new GestureDetector(context, this);
		//single tap draw on canvas
		mDetector.setOnDoubleTapListener(this);
		//2 finger scaling/rotating
		mScale = new ScaleGestureDetector(context, this);
		//2 finger rotation
		mRotate = new RotationGestureDetector(this);
		Bitmap picture = BitmapFactory.decodeResource(getResources(), R.drawable.ucsbmap);
		pickImage(picture);
	}
	
	public void pickImage(Bitmap bitmap)
	{
		this.setImageBitmap(bitmap);
		matrix = new Matrix();
		matrix = this.getMatrix();
		this.setImageMatrix(matrix);
		drawPoints.clear();
	}
	
	@Override
	public void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		
		canvas.concat(getImageMatrix());
		
		Paint paint = new Paint();
		paint.setColor(Color.MAGENTA);
		for (int i = 0; i < drawPoints.size(); i++)
		{
			Tuple t = drawPoints.get(i);
			canvas.drawCircle(t.x, t.y, 10, paint);
		}
		
	}
	
	public void setInitPoint(float x, float y)
	{
		initPoint[0] = x;
		initPoint[1] = y;
	}
	
	
	public void setMatrix()
	{
		setImageMatrix(matrix);
		invalidate();
		matrix = getImageMatrix();
	}
	
	@Override 
	public boolean onTouchEvent(MotionEvent event)
	{
		boolean gesture;
		
		mScale.onTouchEvent(event);
		mRotate.onTouch(event);
		gesture = mDetector.onTouchEvent(event);
		

		if (gesture == false)
		switch (event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				setInitPoint(event.getX(),event.getY());
				return true;
			case MotionEvent.ACTION_MOVE:
//				translate(event.getX(), event.getY());
				matrix = getImageMatrix();
				dx = event.getX() - initPoint[0];
				dy = event.getY() - initPoint[1];
				matrix.postTranslate(dx, dy);
				setImageMatrix(matrix);
				invalidate();
				initPoint[0] = event.getX();
				initPoint[1] = event.getY();
				return true;
			default:
				return mDetector.onTouchEvent(event);
		}
		return true;
	}
	

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
//		Log.v("hello", "hello");
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		// TODO Auto-generated method stub
		Tuple point = new Tuple();
		float[] myPoints = {e.getX(), e.getY()};
		matrix = getImageMatrix();
		inverse = new Matrix();
		matrix.invert(inverse);
		inverse.mapPoints(myPoints);
		point.x = myPoints[0];
		point.y = myPoints[1];
		drawPoints.add(point);
		invalidate();
		return false;
	}

	@Override
	public boolean onDoubleTap(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean onScale(ScaleGestureDetector detector) {
		// TODO Auto-generated method stub
		focusX = detector.getFocusX();
		focusY = detector.getFocusY();
		scaleFactor = detector.getScaleFactor();
		
		matrix = getImageMatrix();
		matrix.postScale(scaleFactor, scaleFactor, focusX, focusY);
		this.setImageMatrix(matrix);
		
		
		
		
		invalidate();
		
		
		
		return true;
	}

	@Override
	public boolean onScaleBegin(ScaleGestureDetector detector) {
		// TODO Auto-generated method stub

		return true;
	}

	@Override
	public void onScaleEnd(ScaleGestureDetector detector) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRotate(float deltaAngle, float mid1, float mid2) {
		// TODO Auto-generated method stub
		matrix = getImageMatrix();
		matrix.postRotate(deltaAngle, mid1, mid2);
		invalidate();
	}


}
