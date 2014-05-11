/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cosmicleap;

import com.imi.cosmic.Environment;
import com.imi.cosmic.terrain.RoadSculptor;
import com.imi.cosmic.terrain.SceneInstanceTerrain;
import com.imi.cosmic.terrain.Terrain;
import com.imi.cosmic.terrain.TerrainFactory;
import com.imi.jist3d.util.GeoUtils;
import com.imi.utils.CosmicDebug;
import com.imi.utils.FastRandom;
import java.util.Calendar;
import java.util.Random;

/**
 * Load an island with water, trees and sky
 * @author Lou Hayt
 */
public class Island {

    static int jday(int y, int m, int d) {
        int M1 = (m - 14) / 12;
        int Y1 = y + 4800;
        int J = 1461 * (Y1 + M1) / 4 + 367 * (m - 2 - 12 * M1) / 12
                - (3 * ((Y1 + M1 + 100) / 100)) / 4 + d - 32075;
        return J;
    }

    public static void loadIsland() {

        String skyTexture = "environment/sky1024-2.jpg";
        
        // Generate environment
        Calendar cal = Calendar.getInstance();
        int value = jday(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        value = value * new Random().nextInt();
        System.out.println("DATE as INT " + value);
        value = 1912216833;
        FastRandom fRandom = new FastRandom(value);
        float yScale = 80;// + (20.0f * (float)Math.random());
        int density = 128;
        float worldSize = 1000.0f;
        TerrainFactory.width = 1024;
        TerrainFactory.height = 1024;
        SceneInstanceTerrain scene = new SceneInstanceTerrain("Environment", fRandom.nextLong(), density, density, worldSize, worldSize, yScale);

        // TODO random values in range?
        scene.waterLine = 0.15f;// + (0.4f * fRandom.nextFloat());
        scene.meadowB = 0.5f;
        scene.meadowT = 0.72f;
        if (yScale > 30.0)
            scene.snowCap = 0.95f;
        else
            scene.snowCap = 1.0f;

//        scene.hillPointCount = (int) (5.0f + (30.0f * fRandom.nextFloat()));
        scene.hillPointCount = (int) (20.0f + (20.0f * Math.random()));
        scene.blurPassCount = 3;//1 + (int) (4.0f * fRandom.nextFloat());
        scene.erodePassCount = 10;//20;// 1 + (int) (5.0f * fRandom.nextFloat());
        scene.mazeBlend = 0.6f;//0.5f;//(3.0f * fRandom.nextFloat());
        scene.erodeVoume = 0.01f;//0.1f * fRandom.nextFloat();
        scene.heightFieldNoise = 0.1f;//0.2f;//0.0f + (0.5f * fRandom.nextFloat());
        scene.useErosionField = true;
        scene.ambient = 0.5f;
        scene.moisureWeight = 0.75f;//(float) fRandom.nextFloat() - 0.2f;

        RoadSculptor roads = new RoadSculptor();
        scene.roadScultor = roads;

        Environment env = new Environment(scene);
        float waterLevel = yScale * scene.waterLine;
        env.setWaterLevel(waterLevel);
        Environment.set(env);
        env.filterAndSortBlobs();
        int totalBlobCount = scene.getTerrain().meadowsBlobList.size() + scene.getTerrain().shoreBlobList.size() + scene.getTerrain().waterBlobList.size();
//        System.out.println("totalBlobCount " + totalBlobCount);
        float max = scene.getTerrain().getHeightField().findMaxHeight();
//        System.out.println("(scene.hillPointCount/50.0f)          = " + (scene.hillPointCount/50.0f));
//        System.out.println("max                                   = " + (max/2.0f) );
//        System.out.println("(scene.heightFieldNoise/0.5f)         = " + ((scene.heightFieldNoise/0.5f)));
//        System.out.println("(( 7.0f - scene.erodePassCount)/6.0f) = " + ((( 7.0f - scene.erodePassCount)/6.0f)));
//        System.out.println("(( 6.0f - scene.blurPassCount)/5.0f)  = " + ((( 6.0f - scene.blurPassCount)/5.0f)));
        float interestScore = ((scene.hillPointCount / 50.0f) * 0.2f
                + (max / 2.0f) * 0.4f + // amount of REAL elevation, including waterline rise and mazeBlend
                (scene.heightFieldNoise / 0.5f) * 0.1f
                + ((7.0f - scene.erodePassCount) / 6.0f) * 0.1f
                + ((6.0f - scene.blurPassCount) / 5.0f) * 0.1f
                + //scene.moisureWeight *
                0.0f)
                * (totalBlobCount * 0.1f)
                * 1000.0f;
        System.out.println("###############################################");
        System.out.println("INTEREST/DIFFICULTY RATING " + interestScore);
        System.out.println("###############################################");


        int count = scene.roadScultor.path.length;
        for (int i = 0;i<count;i++)
        {
            CosmicDebug.debugRoot.addChild(scene.roadScultor.path[i]);
        }
        
        // Sky, put it in the debug entity
        CosmicDebug.debugRoot.addChild(GeoUtils.makeSkySphereShapeNode(skyTexture));
    }
}
