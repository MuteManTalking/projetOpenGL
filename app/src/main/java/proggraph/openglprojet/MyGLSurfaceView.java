package proggraph.openglprojet;

import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

class MyGLSurfaceView extends GLSurfaceView {
    private final MyGLRenderer myRenderer;
    private final float TOUCH_SCALE_FACTOR = 0.00001f;
    private float mPreviousX;
    private float mPreviousY;

    public boolean onTouchEvent(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:

                float dx = x - mPreviousX;
                float dy = y - mPreviousY;
                if(dx > -getWidth()/2 && dx < getWidth()/2){
                    dx = dx * -10;
                }
                if(dy > -getHeight()/2 && dy < getHeight()/2){
                    dy = dy * -10;
                }
                myRenderer.translate(dx/1000.0f, dy/1000.0f, 0);
                System.out.println(dx + " " + dy);
                requestRender();

        }

        mPreviousX = x;
        mPreviousY = y;

        return true;
    }

    public MyGLSurfaceView(OpenGLActivity myMainActivity) {
        super(myMainActivity);

        Log.d("Debug : ", "MyGLSurfaceView");
        setEGLContextClientVersion(2);
        Log.d("Debug : ", "Setting OpenGLES version");

        myRenderer = new MyGLRenderer(myMainActivity);

        Log.d("Debug : ", "Initialize renderer");
        setRenderer(myRenderer);
        Log.d("Debug : ", "Set renderer");
        //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        Log.d("Debug : ", "Set render mode");
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }
}
