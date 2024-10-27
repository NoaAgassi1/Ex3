import java.util.Arrays;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

    class MapTest {


        @Test
        void testInit() {

            int[][] expected = {{1, 1, 1}, {1, 1, 1}, {1, 1, 1}};
            Map map1 = new Map(3, 3, 1);
            int[][] res1 = map1.getMap();

            assertArrayEquals(expected, res1);

        }

        @Test
        void testInit2() {

            int[][] mapArry = {
                    {1, 2, 3},
                    {4, 5, 6},
                    {7, 8, 9}
            };
            int[][] expectedMap = {
                    {1, 2, 3},
                    {4, 5, 6},
                    {7, 8, 9}
            };
            Map2D map1 = new Map(mapArry);
            assertArrayEquals(expectedMap, map1.getMap());

        }

        @org.junit.jupiter.api.Test
        void getMap() {
            int[][] map = {{1, 2, 3},
                    {4, 5, 6},
                    {7, 8, 9}};
            Map map1 = new Map(map);

            int[][] res = map1.getMap();
            assertArrayEquals(map, res);

        }

        @Test
        void getWidth() {
            int[][] map = {
                    {1, 2, 3},
                    {4, 5, 6},
                    {7, 8, 9}
            };
            Map map2 = new Map(map);
            int res = map2.getWidth();
            assertEquals(3, res);
        }

        @Test
        void getHeight() {
            int[][] map = {
                    {1, 2, 3},
                    {4, 5, 6},
                    {7, 8, 9}
            };
            Map map2 = new Map(map);
            int res = map2.getHeight();
            assertEquals(3, res);
        }

        @Test
        void getPixel() {
            int[][] map1 = {
                    {1, 2, 3},
                    {4, 5, 6},
                    {7, 8, 9}
            };
            Map map = new Map(map1);
            int res = map.getPixel(1, 1);
            assertEquals(5, res);
        }

        @Test
        void testGetPixel() {
            int[][] map1 = {
                    {1, 2, 3},
                    {4, 5, 6},
                    {7, 8, 9}
            };
            Map matrix = new Map(map1);
            Index2D p = new Index2D(1, 1);
            int res = matrix.getPixel(p);
            assertEquals(5, res);
        }

        @Test
        void setPixel() {
            int[][] map2 = {
                    {1, 2, 3},
                    {4, 5, 6},
                    {7, 8, 9}
            };
            Map matrix = new Map(map2);
            matrix.setPixel(2, 0, 10);
            int res = matrix.getPixel(2, 0);
            assertEquals(res, 10);
        }

        @Test
        void testSetPixel() {
            int[][] map2 = {
                    {1, 2, 3},
                    {4, 5, 6},
                    {7, 8, 9}
            };
            Map matrix = new Map(map2);
            Index2D p = new Index2D(2, 2);
            int res = matrix.getPixel(p);
            assertEquals(res, 9);

        }

        @Test
        /**
         * Test that Fill draws correctly,
         *  and also return int number with value amount of the new color in map.
         */
        void fill() {
            int[][] map1 = {
                    {0, 0, 0},
                    {-1, -1, 0},
                    {0, 0, 0}
            };
            int[][] expected1 = {
                    {10, 10, 10},
                    {-1, -1, 10},
                    {10, 10, 10}
            };
            int[][] map2 = {
                    {4, 4, 4},
                    {-1, -1, 4},
                    {4, 4, 4}
            };
            int[][] expected2 = {
                    {10, 10, 10},
                    {-1, -1, 10},
                    {10, 10, 10}
            };

            Map forMapping1 = new Map(map1);
            Map forMapping2 = new Map(map2);
            //forMapping2.setCyclic(false);
            Pixel2D start = new Index2D(0, 0);
            int ans1 = forMapping1.fill(start, 10);
            int ans2 = forMapping2.fill(start, 10);
            assertArrayEquals(expected1,forMapping1.getMap());
            assertArrayEquals(expected2,forMapping2.getMap());
            assertEquals(7,ans1);
            assertEquals(7,ans2);

        }

        @Test
        void shortestPath() {
            int[][] map = {
                    {0, 1, -1},
                    {2, -1, 5},
                    {2, 7, 1}
            };
            Index2D p1 = new Index2D(0,0);
            Index2D p2 = new Index2D(2,1);
            Map map1 = new Map(map);
            map1.setCyclic(false);
            Pixel2D[] ans1 = map1.shortestPath(p1,p2,-1);
            Pixel2D[] excpect1 = {new Index2D(0,0),new Index2D(1,0),new Index2D(2,0),new Index2D(2,1)};

            for (int i = 0; i < ans1.length; i++) {
                assertEquals(excpect1[i],ans1[3-i]);
            }

        }

        @Test
        void isInside() {
            int[][] map = {
                    {8, 2, 3},
                    {4, 5, 6},
                    {0, 8, 9}
            };
            Map i = new Map(map);
            Index2D p = new Index2D(1, 1);
            boolean res = i.isInside(p);
            assertTrue(i.isInside(p));

        }

        @Test
        void isCyclic() {
            Map map = new Map(new int[][]{///
                    {0, 0, 0},
                    {0, 0, 0},
                    {0, 0, 0}
            });
            assertTrue(map.isCyclic());
            map.setCyclic(true);
            assertTrue(map.isCyclic());
        }

        @Test
        void setCyclic () {
            Map map = new Map(new int[][]{
                    {0, 0, 0},
                    {0, 0, 0},
                    {0, 0, 0}
            });
            assertTrue(map.isCyclic());
            map.setCyclic(true);
            assertTrue(map.isCyclic());
            map.setCyclic(false);
            assertFalse(map.isCyclic());
        }
        @Test
        void allDistance () {
            int[][] map = {
                    {9, 1, 2, 3, 4},
                    {1, -1, -1, -1, 12},
                    {9, -1, -5, -1, 6},
                    {3, -1, -1, -1, 7},
                    {4, -2, 5, 4, 8}};

            Map map2D = new Map(map);
            map2D.setCyclic(false);
            Index2D start = new Index2D(0, 0);  // Starting pixel
            int obstacleColor = -1;
            Map result = (Map) map2D.allDistance(start, obstacleColor);
            int[][] expectedDistances = {
                    {0, 1, 2, 3, 4},
                    {1, -1, -1, -1, 5},
                    {2, -1, -1, -1, 6},
                    {3, -1, -1, -1, 7},
                    {4, 5, 6, 7, 8}};
            int[][] res = result.getMap();
            System.out.println(Arrays.deepToString(res));
            assertArrayEquals(expectedDistances, res);
            int [][] res1 =
                    {
                            {0,1,2,1},
                            {1,2,3,2},
                            {2,3,4,3},
                            {1,2,3,2},
                    };
            int [][] map2 =
                    {
                            {-2,1,88,1},
                            {1,2,3,9},
                            {2,5,4,3},
                            {1,2,5,2},
                    };
            Map2D m = new Map(map2);
            m = m.allDistance(new Index2D(0,0),0);
            assertArrayEquals(res1, m.getMap());

        }

    }








