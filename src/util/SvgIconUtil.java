package util;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class SvgIconUtil {

    public static ImageIcon createEyeOpenIcon(int size) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        g2d.setColor(Color.BLACK);
        float strokeWidth = size / 12.0f;
        g2d.setStroke(new BasicStroke(strokeWidth));

        double scale = size / 24.0;

        Path2D eyeShape = new Path2D.Double();
        eyeShape.moveTo(2 * scale, 12 * scale);
        eyeShape.curveTo(6 * scale, 6 * scale, 8 * scale, 6 * scale, 12 * scale, 6 * scale);
        eyeShape.curveTo(16 * scale, 6 * scale, 18 * scale, 6 * scale, 22 * scale, 12 * scale);
        eyeShape.curveTo(18 * scale, 18 * scale, 16 * scale, 18 * scale, 12 * scale, 18 * scale);
        eyeShape.curveTo(8 * scale, 18 * scale, 6 * scale, 18 * scale, 2 * scale, 12 * scale);
        g2d.draw(eyeShape);

        g2d.fill(new Ellipse2D.Double(9 * scale, 9 * scale, 6 * scale, 6 * scale));

        g2d.dispose();
        return new ImageIcon(img);
    }

    public static ImageIcon createEyeClosedIcon(int size) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        g2d.setColor(Color.BLACK);
        float strokeWidth = size / 12.0f;
        g2d.setStroke(new BasicStroke(strokeWidth));

        double scale = size / 24.0;

        Path2D upperCurve = new Path2D.Double();
        upperCurve.moveTo(17.94 * scale, 17.94 * scale);
        upperCurve.curveTo(16.13 * scale, 19.22 * scale, 14.13 * scale, 20 * scale, 12 * scale, 20 * scale);
        upperCurve.curveTo(6 * scale, 20 * scale, 2 * scale, 12 * scale, 2 * scale, 12 * scale);
        upperCurve.curveTo(7.06 * scale, 6.06 * scale, 8.06 * scale, 5.94 * scale, 12 * scale, 5.94 * scale);
        g2d.draw(upperCurve);
        g2d.draw(new Line2D.Double(1 * scale, 1 * scale, 23 * scale, 23 * scale));

        Path2D lowerHint = new Path2D.Double();
        lowerHint.moveTo(9.53 * scale, 9.53 * scale);
        lowerHint.curveTo(9.53 * scale, 12.5 * scale, 10.5 * scale, 15 * scale, 12 * scale, 15 * scale);
        g2d.draw(lowerHint);

        Path2D upperHint = new Path2D.Double();
        upperHint.moveTo(14.47 * scale, 14.47 * scale);
        upperHint.curveTo(14.47 * scale, 11.5 * scale, 13.5 * scale, 9 * scale, 12 * scale, 9 * scale);
        g2d.draw(upperHint);

        Path2D bottomCurve = new Path2D.Double();
        bottomCurve.moveTo(10.59 * scale, 5.51 * scale);
        bottomCurve.curveTo(11.3 * scale, 5.17 * scale, 11.65 * scale, 5 * scale, 12 * scale, 5 * scale);
        bottomCurve.curveTo(18 * scale, 5 * scale, 22 * scale, 12 * scale, 22 * scale, 12 * scale);
        bottomCurve.curveTo(18.94 * scale, 16.5 * scale, 18 * scale, 16.5 * scale, 18.94 * scale, 16.5 * scale);
        g2d.draw(bottomCurve);

        g2d.dispose();
        return new ImageIcon(img);
    }
}
