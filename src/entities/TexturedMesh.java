package entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import engine.Art;
import engine.Calculator;
import engine.Camera;
import engine.Coordinate;
import engine.MeshLoader;
import engine.RenderHandler;

public class TexturedMesh {
	
	private static final Texture TEXTURE = new Texture(Art.SPRITES[1][0]);
	
	private static int MeshCount = 0;
	//middle coordinate defaults to 0,0,0
	
	//Use arrays instead?
	private List<VertexTex> texVertices;
	private List<Integer> indices;

	private String name;
	
	private Texture theTexture;
	
	public TexturedMesh(List<VertexTex> texVertices, List<Integer> indices, Texture theTexture) {
		this.texVertices = texVertices;
		this.indices = indices;
		this.theTexture = theTexture;
	}
	
	//Use more verices
	public void render(RenderHandler renderer, Camera cam) {
		for (int i = 0; i < indices.size(); i+=3) {
			VertexTex c1 = texVertices.get(indices.get(i)).clone();
			VertexTex c2 = texVertices.get(indices.get(i + 1)).clone();
			VertexTex c3 = texVertices.get(indices.get(i + 2)).clone();
			
			c1 = (VertexTex) Calculator.rotateAroundCamera(c1, cam);
			c2 = (VertexTex) Calculator.rotateAroundCamera(c2, cam);
			c3 = (VertexTex) Calculator.rotateAroundCamera(c3, cam);
			
			renderer.drawTriangleScanLineTex(c1, c2, c3, theTexture);
		}
	}
	
	
	public static TexturedMesh createTextureCube(double x, double y, double z,
			double width, double depth, double height, Texture theTexture) {
		
		List<VertexTex> coords = new ArrayList<>();
		List<Integer> indices = new ArrayList<>();
				
		coords.add(new VertexTex(x, y, z,
								 0,0, theTexture));
		coords.add(new VertexTex(x + width, y, z, 
								 1, 0, theTexture));
		coords.add(new VertexTex(x + width, y + depth, z, 
								 1, 1,theTexture));
		coords.add(new VertexTex(x, y + depth, z,
								 0, 1, theTexture));
		
		coords.add(new VertexTex(x, y, z + height,
								 0, 0,theTexture));
		coords.add(new VertexTex(x + width, y, z + height,
								 1, 0,theTexture));
		coords.add(new VertexTex(x + width, y + depth, z + height,
								 1, 1, theTexture));
		coords.add(new VertexTex(x, y + depth, z+ height,
								 0, 1, theTexture));
		
		Collections.addAll(indices, new Integer[]{0,1,2});
		Collections.addAll(indices, new Integer[]{0,3,2});
		Collections.addAll(indices, new Integer[]{0,4,1});
		Collections.addAll(indices, new Integer[]{1,5,4});
		Collections.addAll(indices, new Integer[]{0,3,7});
		Collections.addAll(indices, new Integer[]{0,4,7});
		Collections.addAll(indices, new Integer[]{2,1,5});
		Collections.addAll(indices, new Integer[]{2,5,6});
		Collections.addAll(indices, new Integer[]{2,3,7});
		Collections.addAll(indices, new Integer[]{2,6,7});
		Collections.addAll(indices, new Integer[]{4,7,6});
		Collections.addAll(indices, new Integer[]{4,6,5});
		
		return new TexturedMesh(coords, indices, theTexture);
	}
	
	public static TexturedMesh createTexTeapot() {
		Mesh teaPotMesh = MeshLoader.loadMesh();
		
		List<VertexTex> texVerts = new ArrayList<>();
		
		int temp = 0;
		
		for (int i = 0; i < teaPotMesh.getVertices().size(); i++) {
			Coordinate c1 = teaPotMesh.getVertices().get(i);
			if (temp == 0) {
				texVerts.add(new VertexTex(c1.getX(), c1.getY(), c1.getZ(), 0, 0, TEXTURE));
			} else if (temp == 1) {
				texVerts.add(new VertexTex(c1.getX(), c1.getY(), c1.getZ(), 1, 1, TEXTURE));
			} else if (temp == 2) {
				texVerts.add(new VertexTex(c1.getX(), c1.getY(), c1.getZ(), 0, 1, TEXTURE));
			}
			temp = (temp + 1) % 3;
		}
		
		return new TexturedMesh(texVerts, teaPotMesh.getIndices(), TEXTURE);
	}

}
