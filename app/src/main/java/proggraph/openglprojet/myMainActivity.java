package proggraph.openglprojet;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class myMainActivity extends AppCompatActivity {

    private GLSurfaceView mGLView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Create a GLSurfaceView instance and set it
        // as the ContentView for this activity
        mGLView = new MyGLSurfaceView(this);

        setContentView(mGLView);
    }
}
