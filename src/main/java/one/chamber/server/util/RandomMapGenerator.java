package one.chamber.server.util;

import lombok.SneakyThrows;
import lombok.val;
import one.chamber.server.containers.Map;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.security.SecureRandom;

public class RandomMapGenerator {

    @SneakyThrows
    public static Map generateRandomMap(int obstacleMin, int obstacleMax) {
        val map = new Map(1000, 1000);
        val random = SecureRandom.getInstanceStrong();
        for (int i = 0; i < random.nextInt(obstacleMin, obstacleMax); i++) {
            if (random.nextBoolean()) {
                val rectWidth = random.nextInt(10, 100);
                val rectHeight = random.nextInt(10, 100);
                map.addObstacle(new Rectangle2D.Float(
                        random.nextInt(rectWidth / 2, map.getMapWidth() - rectWidth / 2),
                        random.nextInt(rectHeight / 2, map.getMapHeight() - rectHeight / 2),
                        rectWidth,
                        rectHeight)
                );
            } else {
                val circleDiameter = random.nextInt(10, 100);
                map.addObstacle(new Ellipse2D.Float(
                        random.nextInt(circleDiameter / 2, map.getMapWidth() - circleDiameter / 2),
                        random.nextInt(circleDiameter / 2, map.getMapHeight() - circleDiameter / 2),
                        circleDiameter,
                        circleDiameter)
                );
            }
        }
        return map;
    }

}
