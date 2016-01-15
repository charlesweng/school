Approach
--------
1. I first instantiated a drawable using pictures stored in the resources folder for drawable
2. Then I created an intent when the action button PICTURE on the top right corner is picked. It allows you to choose an image file.
3. I created a seperate class called TouchView, inside, I implemented OnRotationListener, OnScaleGestureListener, OnDoubleTapListener, and OnGestureListener.
4. Then I created 4 detector variables that each is responsible for certain actions (scale, rotate, singletap)
5. For scaling, rotation, and translating, I used matrix postscale, postrotate, and posttranslate.
6. I then set the image with the matrix. and concatenate the matrix to the canvas (canvas.concat())
7. For the dots, I created an ArrayList of Tuples and stored coordinate points of where the person clicked. Also, I used the matrix.inverse to find the actual points tapped on the picture.

Bugs
----
- The first picture is extremely big for some reason and lags.
- Sometimes the screen is too sensitive so when u lift 1 finger up while keeping the other finger down, it translates

Sources
-------
http://developer.android.com/training/animation/zoom.html
http://stackoverflow.com/questions/10682019/android-two-finger-rotation?rq=1
https://code.google.com/p/osmdroid/source/browse/trunk/OpenStreetMapViewer/src/org/osmdroid/RotationGestureDetector.java?r=1186
http://developer.android.com/reference/android/view/ScaleGestureDetector.html
http://developer.android.com/reference/android/view/GestureDetector.html
http://developer.android.com/reference/android/graphics/Matrix.html
http://developer.android.com/reference/android/graphics/Canvas.html
http://stackoverflow.com/questions/2169649/get-pick-an-image-from-androids-built-in-gallery-app-programmatically

