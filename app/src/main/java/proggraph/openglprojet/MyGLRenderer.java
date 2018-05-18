package proggraph.openglprojet;

import android.content.Context;
import android.opengl.GLES32;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyGLRenderer implements GLSurfaceView.Renderer {


    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mModelMatrix = new float[16];

    private Context mContext;
    public MyGLRenderer(Context context) {
        super();
        mContext = context;
        Log.d("Debug : ", "MyGLRenderer");
    }
    Cube mCube;
    Cube mCube2;
    Model3D mModel;
    public void translate(float dx, float dy, float dz){
        Matrix.translateM(mModelMatrix, 0, dx, dy, dz);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        Log.d("Debug : ", "onSurfaceCreated");
        GLES32.glClearColor(0.0f, 0.0f, 0.5f, 1.0f);
        GLES32.glClearDepthf(1.0f);
        GLES32.glEnable(GLES32.GL_DEPTH_TEST);
        GLES32.glDepthFunc(GLES32.GL_LEQUAL);

        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 0.5f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        Matrix.perspectiveM(mProjectionMatrix, 0, 70.0f, 9.0f / 16.0f, 0.1f, 100.0f);
        Matrix.setIdentityM(mModelMatrix,0);

        mModel = ModelLoader.readOBJFile(mContext, "cube.obj");
        mModel.init("vertexshader.vert", "fragmentshader.frag","vPosition", "vNormal", "vTexcoord", R.drawable.miramar_bk);

        /*mCube = new Cube();
        mCube.addLight(new Light(0, 2, 0));
        mCube2 = new Cube();
        mCube2.addLight(new Light(0, 2, 3));*/
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Log.d("Debug", "onSurfaceChanged");
        GLES32.glViewport(0, 0, width, height);
        //float ratio = (float) width / (float) height;
        //Matrix.perspectiveM(mProjectionMatrix, 0, -ratio, ratio , -1, 1);
        Matrix.perspectiveM(mProjectionMatrix, 0, 70.0f, (float) width / (float) height, 0.1f, 100.0f);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        Log.d("Debug", "onDrawFrame");

        GLES32.glClear(GLES32.GL_COLOR_BUFFER_BIT | GLES32.GL_DEPTH_BUFFER_BIT);

        float[] temp = new float[16];
        Matrix.scaleM(temp,0,mModelMatrix,0,150,150,150);

        mModel.draw(mProjectionMatrix,mViewMatrix,temp);

        //mCube.draw(mProjectionMatrix, mViewMatrix, mModelMatrix);

        /*float[] temp = new float[16];
        Matrix.translateM(temp,0,mModelMatrix,0,5,0,0);

        mCube2.draw(mProjectionMatrix, mViewMatrix, temp);
        */
    }


}