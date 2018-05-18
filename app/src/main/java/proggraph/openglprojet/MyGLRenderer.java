package proggraph.openglprojet;

import android.content.Context;
import android.opengl.GLES32;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.provider.Settings;
import android.util.Log;
import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyGLRenderer implements GLSurfaceView.Renderer {

    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mModelMatrix = new float[16];

    private final float[] mModelMatrixVaisseau = new float[16];

    private final float[] mModelMatrixAsteroid  = new float[16];
    private final float[] mModelMatrixAsteroid2 = new float[16];
    private final float[] mModelMatrixAsteroid3 = new float[16];
    private final float[] mModelMatrixAsteroid4 = new float[16];

    private Context mContext;
    public MyGLRenderer(Context context) {
        super();
        mContext = context;
        Log.d("Debug : ", "MyGLRenderer");
    }
    Model3D mModel;
    Model3D mModelVaisseau;
    Model3D mModelAsteroid;
    Model3D mModelAsteroid2;
    Model3D mModelAsteroid3;
    Model3D mModelAsteroid4;


    public void translate(float dx, float dy, float dz){
        Matrix.translateM(mModelMatrixVaisseau, 0, dx, dy, dz);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        Log.d("Debug : ", "onSurfaceCreated");
        GLES32.glClearColor(0.0f, 0.0f, 0.5f, 1.0f);
        GLES32.glClearDepthf(1.0f);
        GLES32.glEnable(GLES32.GL_DEPTH_TEST);
        GLES32.glDepthFunc(GLES32.GL_LEQUAL);

        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 1.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        Matrix.perspectiveM(mProjectionMatrix, 0, 70.0f, 9.0f / 16.0f, 0.1f, 100.0f);
        Matrix.setIdentityM(mModelMatrix,0);

        Matrix.setIdentityM(mModelMatrixVaisseau,0);
        Matrix.translateM(mModelMatrixVaisseau,0,0,0,-5.0f);

        mModelAsteroid = initialisation(mModelMatrixAsteroid);
        mModelAsteroid2 = initialisation(mModelMatrixAsteroid2);
        mModelAsteroid3 = initialisation(mModelMatrixAsteroid3);
        mModelAsteroid4 = initialisation(mModelMatrixAsteroid4);

        mModel = ModelLoader.readOBJFile(mContext, "cubeBis.obj");
        mModelVaisseau = ModelLoader.readOBJFile(mContext, "vaisseau.obj");

        mModel.init("vertexshader.vert", "fragmentshader.frag","vPosition", "vNormal", "vTexcoord", R.drawable.skyboxsun25degtest_tn);
        mModelVaisseau.init("vertexshader.vert", "fragmentshader.frag","vPosition", "vNormal", "vTexcoord", R.drawable.i);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Log.d("Debug", "onSurfaceChanged");
        GLES32.glViewport(0, 0, width, height);
        Matrix.perspectiveM(mProjectionMatrix, 0, 70.0f, (float) width / (float) height, 0.1f, 100.0f);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        Log.d("Debug", "onDrawFrame");

        GLES32.glClear(GLES32.GL_COLOR_BUFFER_BIT | GLES32.GL_DEPTH_BUFFER_BIT);

        float[] temp  = new float[16];
        float[] temp2 = new float[16];
        float[] temp3 = new float[16];
        float[] temp4 = new float[16];

        Matrix.scaleM(temp,0,mModelMatrix,0,100,100,100);
        Matrix.rotateM(temp2,0,temp,0,180,1,0,0);
        Matrix.rotateM(temp2,0,180,0,1,0);

        Matrix.rotateM(temp4,0,mModelMatrixVaisseau,0,180,0,1,0);
        Matrix.scaleM(temp3,0,temp4,0,0.005f,0.005f,0.005f);

        respawn(mModelAsteroid,mModelMatrixAsteroid);
        respawn(mModelAsteroid2,mModelMatrixAsteroid2);
        respawn(mModelAsteroid3,mModelMatrixAsteroid3);
        respawn(mModelAsteroid4,mModelMatrixAsteroid4);

        mModel.draw(mProjectionMatrix,mViewMatrix,temp2);
        mModelVaisseau.draw(mProjectionMatrix,mViewMatrix,temp3);
    }

    private void respawn(Model3D model,float[] matrix){
        Random random = new Random();
        int i = 1;
        int randX = random.nextInt(i-(-i)+1)+(-i);
        int randY = random.nextInt(i-(-i)+1)+(-i);
        if( matrix[14]> 5f){
            Matrix.setIdentityM(matrix,0);
            Matrix.translateM(matrix,0,(float)randX,(float)randY, (float) (-110 + Math.random()*(-100-(-110))));
        }
        Matrix.translateM(matrix, 0, 0, 0, 0.3f);
        model.draw(mProjectionMatrix,mViewMatrix,matrix);
    }

    private Model3D initialisation(float[] matrix ){
        Matrix.setIdentityM(matrix,0);
        Matrix.translateM(matrix,0,1,1,-101.0f);
        Model3D modelinit = ModelLoader.readOBJFile(mContext, "cube.obj");
        modelinit.init("vertexshader.vert", "fragmentshader.frag","vPosition", "vNormal", "vTexcoord", R.drawable.brick);
        return modelinit;
    }
}