package proggraph.openglprojet;

import android.opengl.GLES32;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Model3D {

    private int mVertexID;
    private int mTexID;
    private  int mNormalID;

    private int mProgram;

    private float mVertices[];
    private float mNormals[];
    private float mUV[];
    private int mIndices[];


    private FloatBuffer mVertexBuffer;
    private IntBuffer mIndexBuffer;
    private FloatBuffer mTexBuffer;
    private FloatBuffer mNormalBuffer;


    private static final int COORDS_PER_VERTEX = 3;
    private static final int COORDS_PER_NORMAL = 3;
    private static final int COORDS_PER_TEX = 2;

    private final int VERTEX_STRIDE = COORDS_PER_VERTEX * 4;
    private final int TEX_STRIDE = COORDS_PER_TEX * 4;
    private final int NORMAL_STRIDE = COORDS_PER_NORMAL * 4;
    private int mVertexShaderID;
    private int mFragShaderID;
    private int mIdViewMatrix;
    private int mIdProjMatrix;
    private int mIdModelMatrix;
    private int[] mTextureID;


    Model3D(float[] vertices, float[] normals, float[] uvs, int[] indices)
    {
        mVertices = vertices;
        mNormals = normals;
        mUV = uvs;
        mIndices = indices;

        ByteBuffer byteBuf = ByteBuffer.allocateDirect(mVertices.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        mVertexBuffer = byteBuf.asFloatBuffer();
        mVertexBuffer.put(mVertices);
        mVertexBuffer.position(0);

        byteBuf = ByteBuffer.allocateDirect(mNormals.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        mNormalBuffer = byteBuf.asFloatBuffer();
        mNormalBuffer.put(mNormals);
        mNormalBuffer.position(0);

        byteBuf = ByteBuffer.allocateDirect(mUV.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        mTexBuffer = byteBuf.asFloatBuffer();
        mTexBuffer.put(mUV);
        mTexBuffer.position(0);

        byteBuf = ByteBuffer.allocateDirect(mIndices.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        mIndexBuffer = byteBuf.asIntBuffer();
        mIndexBuffer.put(mIndices);
        mIndexBuffer.position(0);

    }

    void init(String vertexShader, String fragmentShader, String vertexLoc,
              String normalLoc, String texLoc, int texture){


        mProgram = GLES32.glCreateProgram();

        mVertexShaderID = ShaderUtilities.loadShader(GLES32.GL_VERTEX_SHADER, vertexShader);
        mFragShaderID = ShaderUtilities.loadShader(GLES32.GL_FRAGMENT_SHADER, fragmentShader);
        GLES32.glAttachShader(mProgram, mVertexShaderID);
        GLES32.glAttachShader(mProgram, mFragShaderID);
        GLES32.glLinkProgram(mProgram);
        int[] linkStatus = {0};
        GLES32.glGetProgramiv(mProgram, GLES32.GL_LINK_STATUS, linkStatus, 0);

        mVertexID = GLES32.glGetAttribLocation(mProgram, vertexLoc);
        mNormalID = GLES32.glGetAttribLocation(mProgram, normalLoc);
        mTexID = GLES32.glGetAttribLocation(mProgram, texLoc);


        mTextureID = new int[6];
        GLES32.glGenTextures(1, mTextureID, 0);
        GLES32.glGenTextures(1, mTextureID, 0);

        GLES32.glActiveTexture(GLES32.GL_TEXTURE0);
        ShaderUtilities.loadTexture(texture, mTextureID, 0);

        mIdProjMatrix = GLES32.glGetUniformLocation(mProgram, "projection");
        mIdViewMatrix = GLES32.glGetUniformLocation(mProgram, "view");
        mIdModelMatrix = GLES32.glGetUniformLocation(mProgram, "model");

        Log.d("MODEL 3D", "init: mVertexID :" + mVertexID) ;
    }

    void draw(float[] projection, float[] view, float[] model){
        GLES32.glUseProgram(mProgram);


        // Apply the projection and view transformation.
        GLES32.glUniformMatrix4fv(mIdViewMatrix, 1, false, view, 0);
        GLES32.glUniformMatrix4fv(mIdModelMatrix, 1, false, model, 0);
        GLES32.glUniformMatrix4fv(mIdProjMatrix, 1, false, projection, 0);

        GLES32.glEnableVertexAttribArray(mVertexID);
        GLES32.glVertexAttribPointer(
                mVertexID, 3, GLES32.GL_FLOAT, false, VERTEX_STRIDE, mVertexBuffer);

        GLES32.glEnableVertexAttribArray(mTexID);
        GLES32.glVertexAttribPointer(
                mTexID, 2, GLES32.GL_FLOAT, false, TEX_STRIDE, mTexBuffer);

        GLES32.glEnableVertexAttribArray(mNormalID);
        GLES32.glVertexAttribPointer(
                mNormalID, 2, GLES32.GL_FLOAT, false, NORMAL_STRIDE, mNormalBuffer);


        // Bind to the texture in OpenGL
        GLES32.glBindTexture(GLES32.GL_TEXTURE_2D, mTextureID[0]);


        GLES32.glDrawElements(
                GLES32.GL_TRIANGLES, mIndices.length, GLES32.GL_UNSIGNED_INT, mIndexBuffer);

        GLES32.glDisableVertexAttribArray(mVertexID);
        GLES32.glDisableVertexAttribArray(mTexID);
        GLES32.glDisableVertexAttribArray(mNormalID);

    }

    void clean(){
        GLES32.glDetachShader(mProgram, mVertexShaderID);
        GLES32.glDetachShader(mProgram, mFragShaderID);
        GLES32.glDeleteProgram(mProgram);
    }
    public int[] getmIndices() {
        return mIndices;
    }

    public float[] getmNormals() {
        return mNormals;
    }

    public float[] getmUV() {
        return mUV;
    }

    public float[] getmVertices() {
        return mVertices;
    }
}
