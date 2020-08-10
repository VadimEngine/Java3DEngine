package entities;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import engine.Calculator;
import engine.Camera;
import engine.Coordinate;
import engine.RenderHandler;

public class TexturedCube {
    // make selectable

    private List<VertexTex> texVertices = new ArrayList<>();
    private List<Integer> indices = new ArrayList<>();

    private Texture theTexture;

    private Coordinate position;

    private double xScale = 1;
    private double yScale = 1;
    private double zScale = 1;

    private double zAngle = 0;

    // rotates around y axis (zyAngle)
    private double yAngle = 0;

    // rotates around x axis (xzAngle)
    private double xAngle = 0;

    public TexturedCube(double x, double y, double z, double width, double height, double depth, Texture theTexure) {
        this.theTexture = theTexure;
        double size = 100;
        position = new Coordinate(x, y, z);

        /*
         * ef [] acijm [][][][] bdkln [] gh
         */
        // abc cbd efc fci cid ikd dkg hkg ijk ljk jml mln
        // 0,1,2 2,1,3 | 4,5,2 5,2,8 | 2,8,3 8,10,3 | 3,10,6 7,10,6 | 8,9,10 11,9,10 |
        // 9,12,11 12,11,13
        // a 0 f 5 k 10
        // b 1 g 6 l 11
        // c 2 h 7 m 12
        // d 3 i 8 n 13
        // e 4 j 9
        VertexTex ca = new VertexTex(position.getX() + 0, position.getY() + 0, position.getZ() + size, 0, 0);
        // b
        VertexTex cb = new VertexTex(position.getX() + 0, position.getY() + size, position.getZ() + size, 0, 1);
        // c
        VertexTex cc = new VertexTex(position.getX() + 0, position.getY() + 0, position.getZ() + 0, 1, 0);
        // d
        VertexTex cd = new VertexTex(position.getX() + 0, position.getY() + size, position.getZ() + 0, 1, 1);
        // e
        VertexTex ce = new VertexTex(position.getX() + 0, position.getY() + 0, position.getZ() + size, 1, -1);
        // f
        VertexTex cf = new VertexTex(position.getX() + size, position.getY() + 0, position.getZ() + size, 2, -1);
        // g
        VertexTex cg = new VertexTex(position.getX() + 0, position.getY() + size, position.getZ() + size, 1, 2);
        // h
        VertexTex ch = new VertexTex(position.getX() + size, position.getY() + size, position.getZ() + size, 2, 2);
        // i
        VertexTex ci = new VertexTex(position.getX() + size, position.getY() + 0, position.getZ() + 0, 2, 0);
        // j
        VertexTex cj = new VertexTex(position.getX() + size, position.getY() + 0, position.getZ() + size, 3, 0);
        // k
        VertexTex ck = new VertexTex(position.getX() + size, position.getY() + size, position.getZ() + 0, 2, 1);
        // l
        VertexTex cl = new VertexTex(position.getX() + size, position.getY() + size, position.getZ() + size, 3, 1);
        // m
        VertexTex cm = new VertexTex(position.getX() + 0, position.getY() + 0, position.getZ() + size, 4, 0);
        // n
        VertexTex cn = new VertexTex(position.getX() + 0, position.getY() + size, position.getZ() + size, 4, 1);

        texVertices.add(ca);
        texVertices.add(cb);
        texVertices.add(cc);
        texVertices.add(cd);
        texVertices.add(ce);
        texVertices.add(cf);
        texVertices.add(cg);
        texVertices.add(ch);
        texVertices.add(ci);
        texVertices.add(cj);
        texVertices.add(ck);
        texVertices.add(cl);
        texVertices.add(cm);
        texVertices.add(cn);

        Collections.addAll(indices, new Integer[] { 0, 1, 2 });
        Collections.addAll(indices, new Integer[] { 2, 1, 3 });

        Collections.addAll(indices, new Integer[] { 4, 5, 2 });
        Collections.addAll(indices, new Integer[] { 5, 2, 8 });

        Collections.addAll(indices, new Integer[] { 2, 8, 3 });
        Collections.addAll(indices, new Integer[] { 8, 10, 3 });

        Collections.addAll(indices, new Integer[] { 3, 10, 6 });
        Collections.addAll(indices, new Integer[] { 7, 10, 6 });

        Collections.addAll(indices, new Integer[] { 8, 9, 10 });
        Collections.addAll(indices, new Integer[] { 11, 9, 10 });

        Collections.addAll(indices, new Integer[] { 9, 12, 11 });
        Collections.addAll(indices, new Integer[] { 12, 11, 13 });
    }

    public void render(RenderHandler renderer, Camera cam) {
        for (int i = 0; i < indices.size(); i += 3) {
            VertexTex c1 = texVertices.get(indices.get(i)).clone();
            VertexTex c2 = texVertices.get(indices.get(i + 1)).clone();
            VertexTex c3 = texVertices.get(indices.get(i + 2)).clone();

            c1 = (VertexTex) Calculator.rotateAroundCamera(c1, cam);
            c2 = (VertexTex) Calculator.rotateAroundCamera(c2, cam);
            c3 = (VertexTex) Calculator.rotateAroundCamera(c3, cam);
            // System.out.println(theTexture);
            renderer.drawTriangleScanLineTex(c1, c2, c3, theTexture);
        }
    }

}
