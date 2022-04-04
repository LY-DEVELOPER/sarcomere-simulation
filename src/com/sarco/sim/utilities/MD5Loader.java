package com.sarco.sim.utilities;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.sarco.sim.Mesh;
import com.sarco.sim.Texture;
import com.sarco.sim.Object;

public class MD5Loader {

	public static Object process(MD5Model md5Model) throws Exception {
		List<MD5Mesh> md5MeshList = md5Model.getMeshes();

		List<Mesh> list = new ArrayList<>();
		for (MD5Mesh md5Mesh : md5Model.getMeshes()) {
			Mesh mesh = generateMesh(md5Model, md5Mesh);
			handleTexture(mesh, md5Mesh);
			list.add(mesh);
		}
		Mesh[] meshes = new Mesh[list.size()];
		meshes = list.toArray(meshes);
		Object object = new Object(meshes);

		return object;
	}

	private static Mesh generateMesh(MD5Model md5Model, MD5Mesh md5Mesh) throws Exception {
		List<VertexInfo> vertexInfoList = new ArrayList<>();
		List<Float> textCoords = new ArrayList<>();
		List<Integer> indices = new ArrayList<>();

		List<MD5Mesh.MD5Vertex> vertices = md5Mesh.getVertices();
		List<MD5Mesh.MD5Weight> weights = md5Mesh.getWeights();
		List<MD5JointInfo.MD5JointData> joints = md5Model.getJointInfo().getJoints();

		for (MD5Mesh.MD5Vertex vertex : vertices) {
			Vector3f vertexPos = new Vector3f();
			Vector2f vertexTextCoords = vertex.getTextCoords();
			textCoords.add(vertexTextCoords.x);
			textCoords.add(vertexTextCoords.y);

			int startWeight = vertex.getStartWeight();
			int numWeights = vertex.getWeightCount();

			for (int i = startWeight; i < startWeight + numWeights; i++) {
				MD5Mesh.MD5Weight weight = weights.get(i);
				MD5JointInfo.MD5JointData joint = joints.get(weight.getJointIndex());
				Vector3f rotatedPos = new Vector3f(weight.getPosition()).rotate(joint.getOrientation());
				Vector3f acumPos = new Vector3f(joint.getPosition()).add(rotatedPos);
				acumPos.mul(weight.getBias());
				vertexPos.add(acumPos);
			}

			vertexInfoList.add(new VertexInfo(vertexPos));
		}

		for (MD5Mesh.MD5Triangle tri : md5Mesh.getTriangles()) {
			indices.add(tri.getVertex0());
			indices.add(tri.getVertex1());
			indices.add(tri.getVertex2());

			// Normals
			VertexInfo v0 = vertexInfoList.get(tri.getVertex0());
			VertexInfo v1 = vertexInfoList.get(tri.getVertex1());
			VertexInfo v2 = vertexInfoList.get(tri.getVertex2());
			Vector3f pos0 = v0.position;
			Vector3f pos1 = v1.position;
			Vector3f pos2 = v2.position;

			Vector3f normal = (new Vector3f(pos2).sub(pos0)).cross(new Vector3f(pos1).sub(pos0));

			v0.normal.add(normal);
			v1.normal.add(normal);
			v2.normal.add(normal);
		}

		// Once the contributions have been added, normalize the result
		for (VertexInfo v : vertexInfoList) {
			v.normal.normalize();
		}
		float[] positionsArr = VertexInfo.toPositionsArr(vertexInfoList);
		float[] textCoordsArr = listToArray(textCoords);
		float[] normalsArr = VertexInfo.toNormalArr(vertexInfoList);
		int[] indicesArr = indices.stream().mapToInt(i -> i).toArray();
		Mesh mesh = new Mesh(positionsArr, textCoordsArr, normalsArr, indicesArr);

		return mesh;
	}
	
	private static void handleTexture(Mesh mesh, MD5Mesh md5Mesh) throws Exception {
	    String texturePath = md5Mesh.getTexture();
	    if (texturePath != null && texturePath.length() > 0) {
	        mesh.setTexture(texturePath);
	    }
	}

	private static class VertexInfo {

		public Vector3f position;

		public Vector3f normal;

		public VertexInfo(Vector3f position) {
			this.position = position;
			normal = new Vector3f(0, 0, 0);
		}

		public VertexInfo() {
			position = new Vector3f();
			normal = new Vector3f();
		}

		public static float[] toPositionsArr(List<VertexInfo> list) {
			int length = list != null ? list.size() * 3 : 0;
			float[] result = new float[length];
			int i = 0;
			for (VertexInfo v : list) {
				result[i] = v.position.x;
				result[i + 1] = v.position.y;
				result[i + 2] = v.position.z;
				i += 3;
			}
			return result;
		}

		public static float[] toNormalArr(List<VertexInfo> list) {
			int length = list != null ? list.size() * 3 : 0;
			float[] result = new float[length];
			int i = 0;
			for (VertexInfo v : list) {
				result[i] = v.normal.x;
				result[i + 1] = v.normal.y;
				result[i + 2] = v.normal.z;
				i += 3;
			}
			return result;
		}
	}
	
    public static float[] listToArray(List<Float> list) {
        int size = list != null ? list.size() : 0;
        float[] floatArr = new float[size];
        for(int i=0; i<size; i++) {
            floatArr[i] = list.get(i);
        }
        return floatArr;
    }
}
