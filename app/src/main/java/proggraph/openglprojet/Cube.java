package proggraph.openglprojet;

import android.opengl.GLES20;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Cube {


    private final String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    // the matrix must be included as a modifier of gl_Position
                    // Note that the uMVPMatrix factor *must be first* in order
                    // for the matrix multiplication product to be correct.
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    private final String fragmentShaderCode=
            "precision mediump float;"+
                    "uniform vec4 vColor;"+
                    "void main(){"+
                    "gl_FragColor = vColor;"+
                    "}";


    // Use to access and set the view transformation
    private int mMVPMatrixHandle;
    private int mPositionHandle;
    private int mColorHandle;

    private final int vertexCount = cubeCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride =COORDS_PER_VERTEX * 4; //4  bytes per vertex


    private FloatBuffer vertexBuffer;
    private ByteBuffer mIndexBuffer;

    private FloatBuffer mTexBuffer;

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;

    static float cubeCoords[] = {

            //face avant
            -1, -1, 1, //0      0
             1, -1, 1, //1      1
             1,  1, 1, //2      2
            -1,  1, 1, //3      3

            //face droite
             1, -1,  1, //1     4
             1,  1,  1, //2     5
             1,  1, -1, //6     6
             1, -1, -1, //5     7

            //face arrière
            -1, -1, -1, //4     8
             1, -1, -1, //5     9
             1,  1, -1, //6     10
            -1,  1, -1, //7     11

            //face gauche
            -1, -1,  1, //0     12
            -1, -1, -1, //4     13
            -1,  1, -1, //7     14
            -1,  1,  1, //3     15

            //face dessus
            -1, 1,  1, //3      16
            -1, 1, -1, //7      17
             1, 1, -1, //6      18
             1, 1,  1, //2      19

            //face dessous
            -1, -1,  1, //0     20
            -1, -1, -1, //4     21
             1, -1, -1, //5     22
             1, -1,  1  //1     23

    };
//

    private byte indices[] = {
            0, 1, 2,
            2, 3, 0,

            4, 5, 6,
            6, 7, 4,

            8, 9, 10,
            10, 11, 8,

            12, 13, 14,
            14, 15, 12,

            16, 17, 18,
            18, 19, 16,

            20, 21, 22,
            22, 23, 20
    };
    /*
    private float tex_coords[] = {
            0.0f, 0.0f, 1.0f, 0.0f,
            1.0f, 1.0f, 0.0f, 1.0f,

            0.0f, 0.0f, 1.0f, 0.0f,
            1.0f, 1.0f, 0.0f, 1.0f,

            0.0f, 0.0f, 1.0f, 0.0f,
            1.0f, 1.0f, 0.0f, 1.0f,

            0.0f, 0.0f, 1.0f, 0.0f,
            1.0f, 1.0f, 0.0f, 1.0f,

            0.0f, 0.0f, 1.0f, 0.0f,
            1.0f, 1.0f, 0.0f, 1.0f,

            0.0f, 0.0f, 1.0f, 0.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
    };*/



    //set color with red, green blue and alpha (opacity) values
    float color []= {0.63f,0.76f,0.22f,1.0f};


    private final int mProgram;


    public Cube (){

        //initialize vertex byte buffer for shape coordinates
        //number of coordinate values * 4 bytes per float
        ByteBuffer bb = ByteBuffer.allocateDirect(cubeCoords.length*4);

        //use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        //create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(cubeCoords);
        vertexBuffer.position(0);

        /*
        bb = ByteBuffer.allocateDirect(tex_coords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        mTexBuffer = bb.asFloatBuffer();
        mTexBuffer.put(tex_coords);
        mTexBuffer.position(0);
        */
        mIndexBuffer = ByteBuffer.allocateDirect(indices.length);
        mIndexBuffer.put(indices);
        mIndexBuffer.position(0);



        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,fragmentShaderCode);

        //create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram();

        //add the vertex shader to program
        GLES20.glAttachShader(mProgram,vertexShader);

        //add the fragment shader to program
        GLES20.glAttachShader(mProgram,fragmentShader);

        //create OpenGL ES program executables
        GLES20.glLinkProgram(mProgram);

    }

    public void draw(float[] mvpMatrix){

        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the cube vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the cube coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);



        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // Set color for drawing the cube
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);


        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

        // Draw the cube
        GLES20.glDrawElements(GLES20.GL_TRIANGLES,vertexCount,GLES20.GL_UNSIGNED_BYTE,mIndexBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }


}
