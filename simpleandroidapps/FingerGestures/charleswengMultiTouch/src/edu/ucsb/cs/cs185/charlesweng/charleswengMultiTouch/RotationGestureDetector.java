package edu.ucsb.cs.cs185.charlesweng.charleswengMultiTouch;

import android.view.MotionEvent;

public class RotationGestureDetector {

        public interface RotationListener {
                public void onRotate(float deltaAngle, float mid1, float mid2);
        }

        protected float mRotation;
        private RotationListener mListener;

        public RotationGestureDetector(RotationListener listener) {
                mListener = listener;
        }

        private float rotation(MotionEvent event) {
                double delta_x = (event.getX(0) - event.getX(1));
                double delta_y = (event.getY(0) - event.getY(1));
                double radians = Math.atan2(delta_y, delta_x);
                return (float) Math.toDegrees(radians);
        }

        public void onTouch(MotionEvent e) {
                if (e.getPointerCount() != 2)
                        return;

                if (e.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN) {
                        mRotation = rotation(e);
                }
                
                float rotation = rotation(e);
                float delta = rotation - mRotation;
                float midpointX = (e.getX(1)+e.getX(0))/2;
                float midpointY = (e.getY(1)+e.getY(0))/2;
                mRotation += delta;
                mListener.onRotate(delta, midpointX, midpointY);
        }

}