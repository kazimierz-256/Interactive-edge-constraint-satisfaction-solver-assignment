/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gk1.textures;

import gk1.LightSource;
import gk1.Vector;
import static java.lang.Math.floor;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author kazimierz
 */
public class Texture {

    private CachedImage texture;
    private CachedImage normals;
    private CachedImage heights;

    public Texture() {
// pizza https://upload.wikimedia.org/wikipedia/commons/thumb/d/d1/Pepperoni_pizza.jpg/320px-Pepperoni_pizza.jpg
//https://upload.wikimedia.org/wikipedia/commons/4/43/Radiosity-yes.jpg
//https://upload.wikimedia.org/wikipedia/commons/thumb/7/78/Painters_problem.svg/340px-Painters_problem.svg.png
        // pizza
        texture = new CachedImage(
                "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d1/Pepperoni_pizza.jpg/320px-Pepperoni_pizza.jpg"
        );
        //https://upload.wikimedia.org/wikipedia/commons/thumb/3/3b/Normal_map_example_-_Map.png/600px-Normal_map_example_-_Map.png
        // abstract shapes
        normals = new CachedImage(
                "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3b/Normal_map_example_-_Map.png/600px-Normal_map_example_-_Map.png"
        );
        // earth
        heights = new CachedImage(
                "https://upload.wikimedia.org/wikipedia/commons/thumb/1/15/Srtm_ramp2.world.21600x10800.jpg/800px-Srtm_ramp2.world.21600x10800.jpg"
        );
    }

    public Texture(CachedImage texture, CachedImage normals, CachedImage heights) {
        this.texture = texture;
        this.normals = normals;
        this.heights = heights;
    }

    public int getPixel(double leftmost, double bottommost, double z, int x, int y, ArrayList<LightSource> lights) {
        // lambert reflectance
        int texturePixel = texture.getPixel(x, y);
        int normalPixel = normals.getPixel(x, y);
        int heightPixel = heights.getPixel(x, y);

        Vector N = new Vector((ArgbHelper.getRed(normalPixel) - 127) / 127,
                (ArgbHelper.getGreen(normalPixel) - 127) / 127,
                ArgbHelper.getBlue(normalPixel) / 255); // from normal map
        N.normalizeZ();

//        Vector D = new Vector(0, 0, 0);// from height map?
        // update normal vector
//        N.add(D);
        int resultingRed = 0;
        int resultingGreen = 0;
        int resultingBlue = 0;

        for (Iterator<LightSource> iterator = lights.iterator(); iterator.hasNext();) {
            LightSource light = iterator.next();
            int lightColour = light.getLightColour();

            Vector L = Vector.fromVertex(light.getPosition());
            L.minus(new Vector(leftmost + x, bottommost + y, z));
            double dotProduct = 1;//N.dotProductNormalized(L);

            if (dotProduct <= 0) {
                continue;
            }

            resultingRed = normalizeTo255((dotProduct * ArgbHelper.getRed(lightColour))
                    * ArgbHelper.getRed(texturePixel));
            resultingGreen = normalizeTo255((dotProduct * ArgbHelper.getGreen(lightColour))
                    * ArgbHelper.getGreen(texturePixel));
            resultingBlue = normalizeTo255((dotProduct * ArgbHelper.getBlue(lightColour))
                    * ArgbHelper.getBlue(texturePixel));

        }

        int resultingPixel = 0xdd000000;
        resultingPixel = ArgbHelper.setRed(resultingPixel, resultingRed);
//        resultingPixel = ArgbHelper.setGreen(resultingPixel, resultingGreen);
//        resultingPixel = ArgbHelper.setBlue(resultingPixel, resultingBlue);
        return resultingPixel;

    }

    public CachedImage getTexture() {
        return texture;
    }

    public void setTexture(CachedImage texture) {
        this.texture = texture;
    }

    public CachedImage getNormals() {
        return normals;
    }

    public void setNormals(CachedImage normals) {
        this.normals = normals;
    }

    public CachedImage getHeights() {
        return heights;
    }

    public void setHeights(CachedImage heights) {
        this.heights = heights;
    }

    private int normalizeTo255(double color) {
        color /= 255;
        return color < 255 ? (int) floor(color) : 255;
    }
}
