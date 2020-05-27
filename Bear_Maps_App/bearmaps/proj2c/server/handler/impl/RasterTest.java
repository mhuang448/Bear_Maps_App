package bearmaps.proj2c.server.handler.impl;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class RasterTest {

    @Test
    public void testRaster() {
        Map<String, Double> params = new HashMap<>();
        params.put("ullon", -122.241632);
        params.put("lrlon", -122.24053);
        params.put("w", 892.0);
        params.put("h", 875.0);
        params.put("ullat", 37.87655);
        params.put("lrlat", 37.87548);

        RasterAPIHandler rasterer = new RasterAPIHandler();



        double expectedRasterUlLon = -122.24212646484375;
        int expectedDepth = 7;
        double expectedRasterLrLon = -122.24006652832031;
        double expectedRasterLrLat = 37.87538940251607;
        double expectedRasterUlLat = 37.87701580361881;
        boolean expectedQuerySuccess = true;
        String[][] expectedRenderGrid = new String[][] {
                {"d7_x84_y28.png", "d7_x85_y28.png", "d7_x86_y28.png"},
                {"d7_x84_y29.png", "d7_x85_y29.png", "d7_x86_y29.png"},
                {"d7_x84_y30.png", "d7_x85_y30.png", "d7_x86_y30.png"}
        };

        Map<String, Object> actual = rasterer.processRequest(params, null);

        assertEquals(expectedDepth, actual.get("depth"));
        assertEquals(expectedQuerySuccess, actual.get("query_success"));
        assertEquals(expectedRasterUlLon, (Double) actual.get("raster_ul_lon"), 0.00001);
        assertEquals(expectedRasterLrLon, (Double) actual.get("raster_lr_lon"), 0.00001);
        assertEquals(expectedRasterLrLat, (Double) actual.get("raster_lr_lat"), 0.00001);
        assertEquals(expectedRasterUlLat, (Double) actual.get("raster_ul_lat"), 0.00001);
        String[][] actualRenderGrid = (String[][]) actual.get("render_grid");
//        System.out.println("row length: " + actualRenderGrid.length);
//        System.out.println("col length: " + actualRenderGrid[0].length);
//        System.out.println(actualRenderGrid[0][0]);
//        System.out.println(actualRenderGrid[0][1]);
//        System.out.println(actualRenderGrid[0][2]);
//        System.out.println(actualRenderGrid[0][3]);
//
//        assertEquals(expectedRenderGrid, actual.get("render_grid"));

        assertTrue(Arrays.deepEquals(expectedRenderGrid, actualRenderGrid));


    }

}
