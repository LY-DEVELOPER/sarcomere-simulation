package com.sarco.sim;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL20.*;
import org.lwjgl.system.MemoryStack;

public class ShaderProgram {

    private final int program;

    private final Map<String, Integer> uniforms;

    public ShaderProgram(String vertexShader, String fragShader) throws Exception {
        program = glCreateProgram();
        uniforms = new HashMap<>();
        
        int vShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vShader, vertexShader);
        glCompileShader(vShader);
        glAttachShader(program, vShader);

        int fShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fShader, fragShader);
        glCompileShader(fShader);
        glAttachShader(program, fShader);
        
        glLinkProgram(program);
        glDetachShader(program, vShader);
        glDetachShader(program, fShader);
        glValidateProgram(program);
    }
    
    public void createUniform(String uniform) throws Exception {
        int location = glGetUniformLocation(program, uniform);
        uniforms.put(uniform, location);
    }
    
    public void setUniform(String uniform, Matrix4f[] matrices) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            int length = matrices != null ? matrices.length : 0;
            FloatBuffer fb = stack.mallocFloat(16 * length); 
            for (int i = 0; i < length; i++) {
                matrices[i].get(16 * i, fb);
            }
            glUniformMatrix4fv(uniforms.get(uniform), false, fb);
        }
    }

    public void setUniform(String uniform, Matrix4f value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            glUniformMatrix4fv(uniforms.get(uniform), false,
                               value.get(stack.mallocFloat(16)));
        }
    }
    
    public void setUniform(String uniform, Vector4f value) {
        glUniform4f(uniforms.get(uniform), value.x, value.y, value.z, value.w);
    }
    
    public void setUniform(String uniform, int value) {
        glUniform1i(uniforms.get(uniform), value);
    }
    
    public void open() {
        glUseProgram(program);
    }

    public void close() {
        glUseProgram(0);
    }

    public void delete() {
        close();
        if (program != 0) {
            glDeleteProgram(program);
        }
    }
}