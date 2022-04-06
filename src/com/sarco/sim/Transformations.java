package com.sarco.sim;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transformations {

    private final Matrix4f projectionMatrix;

    private final Matrix4f modelViewMatrix;
    
    private final Matrix4f viewMatrix;
    private final Matrix4f modelMatrix;

    public Transformations() {
        modelViewMatrix = new Matrix4f();
        projectionMatrix = new Matrix4f();
        viewMatrix = new Matrix4f();
        modelMatrix = new Matrix4f();
    }

    public final Matrix4f getProjectionMatrix(float fov, float width, float height, float zNear, float zFar) {
        float aspectRatio = width / height;        
        projectionMatrix.identity();
        projectionMatrix.perspective(fov, aspectRatio, zNear, zFar);
        return projectionMatrix;
    }

    public Matrix4f buildModelViewMatrix(Object object, Matrix4f matrix) {
        Vector3f rotation = object.getRotation();
        modelMatrix.identity().translate(object.getPosition()).                
                rotateX((float)Math.toRadians(-rotation.x)).
                rotateY((float)Math.toRadians(-rotation.y)).
                rotateZ((float)Math.toRadians(-rotation.z)).
                scale(object.getScale());
        modelViewMatrix.set(matrix);
        return modelViewMatrix.mul(modelMatrix);
    }
    
    public Matrix4f getViewMatrix(Camera camera) {
        Vector3f cameraPos = camera.getPosition();
        Vector3f rotation = camera.getRotation();

        viewMatrix.identity();
        viewMatrix.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        viewMatrix.scale(camera.getScale());
        // First do the rotation so camera rotates over its position
        viewMatrix.rotate((float)Math.toRadians(rotation.x), new Vector3f(1, 0, 0))
            .rotate((float)Math.toRadians(rotation.y), new Vector3f(0, 1, 0));
        // Then do the translation
        return viewMatrix;
    }
}
