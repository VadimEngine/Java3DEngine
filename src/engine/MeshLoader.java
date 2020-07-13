package engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import entities.Mesh;

/**
 * TODO: Add method to load all meshes in a folder
 * @author Vadim
 *
 */
public class MeshLoader {
	
	public static Mesh loadMesh(String fileName) {
		URL resource = MeshLoader.class.getClassLoader().getResource(fileName);

		File theFile = new File(resource.getFile());
		
		List<Coordinate> vetex = new ArrayList<>();
		List<Integer> index = new ArrayList<>();
		
		try {
			Scanner sc = new Scanner(theFile);
			while (sc.hasNext()) {
				String mark = sc.next();
				
				if (mark.equals("v")) {
					double x = sc.nextDouble();
					double y = sc.nextDouble();
					double z = sc.nextDouble();
					
					vetex.add(new Coordinate(x, y, z));
				} 
				
				if (mark.equals("f")) {
					int i1 = sc.nextInt();
					int i2 = sc.nextInt();
					int i3 = sc.nextInt();
									
					index.add(i1 - 1);
					index.add(i2 - 1);
					index.add(i3 - 1);
				}
			}
			
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return new Mesh(vetex, index);
	}
	
	
	public static Mesh loadMesh() {	
		String filename = "sphere.txt";
		URL resource = MeshLoader.class.getClassLoader().getResource(filename);

		File theFile = new File(resource.getFile());
		
		List<Coordinate> vetex = new ArrayList<>();
		List<Integer> index = new ArrayList<>();
		
		try {
			Scanner sc = new Scanner(theFile);
			while (sc.hasNext()) {
				String mark = sc.next();
				
				if (mark.equals("v")) {
					double x = sc.nextDouble();
					double y = sc.nextDouble();
					double z = sc.nextDouble();
					
					vetex.add(new Coordinate(x * 100, y * 100, z * 100));
				} 
				
				if (mark.equals("f")) {
					int i1 = sc.nextInt();
					int i2 = sc.nextInt();
					int i3 = sc.nextInt();
					
					index.add(i1 - 1);
					index.add(i2 - 1);
					index.add(i3 - 1);
				}
			}
			
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return new Mesh(vetex, index);
	}
	

}
