package lwjglgamedev.modelLoaders;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class AnimVertex {
	
	// this class is from https://github.com/lwjglgamedev/lwjglbook

    public Vector3f position;

    public Vector2f textCoords;

    public Vector3f normal;

    public float[] weights;

    public int[] jointIndices;

    public AnimVertex() {
        super();
        normal = new Vector3f();
    }
}
