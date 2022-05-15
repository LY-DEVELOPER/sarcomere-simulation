package com.sarco.sim;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transform {

    public static Matrix4f getSceneMatrix(Window window) {     
    	Matrix4f sceneMatrix = new Matrix4f();
        sceneMatrix.identity();
        sceneMatrix.perspective((float) Math.toRadians(65f), (float) window.getWidth() / window.getHeight(), 0.01f, 1000f);
        return sceneMatrix;
    }
    
    public static Matrix4f getCameraMatrix(Camera camera) {
    	Matrix4f cameraMatrix = new Matrix4f();
        cameraMatrix.identity();
        cameraMatrix.translate(0, 0, -camera.getPosition().z);
        cameraMatrix.rotate((float)Math.toRadians(camera.getRotation().x), new Vector3f(1, 0, 0)).rotate((float)Math.toRadians(camera.getRotation().y), new Vector3f(0, 1, 0));
        cameraMatrix.translate(-camera.getPosition().x, -camera.getPosition().y, 0);
        cameraMatrix.scale(camera.getScale());
        return cameraMatrix;
    }
    
    public static Matrix4f getObjectCameraMatrix(Object object, Matrix4f matrix) {
        Vector3f rotation = object.getRotation();
        Matrix4f objectMatrix = new Matrix4f();
        objectMatrix.identity().translate(object.getPosition()).                
                rotateX((float)Math.toRadians(-rotation.x)).
                rotateY((float)Math.toRadians(-rotation.y)).
                rotateZ((float)Math.toRadians(-rotation.z)).
                scale(object.getScale());
        Matrix4f objectCameraMatrix = new Matrix4f();
        objectCameraMatrix.set(matrix);
        return objectCameraMatrix.mul(objectMatrix);
    }
    
    public static final Matrix4f getHudProjectionMatrix(Window window) {
    	Matrix4f hudMatrix = new Matrix4f();
        hudMatrix.identity();
        hudMatrix.setOrtho2D(0, window.getWidth(), window.getHeight(), 0);
        return hudMatrix;
    }
    
    public static Matrix4f getHudProjTextMatrix(Object object, Matrix4f hudMatrix) {
        Matrix4f textMatrix = new Matrix4f();
        textMatrix.identity().translate(object.getPosition()).scale(object.getScale());
        Matrix4f hudMatrixCurr = new Matrix4f(hudMatrix);
        hudMatrixCurr.mul(textMatrix);
        return hudMatrixCurr;
    }

}
