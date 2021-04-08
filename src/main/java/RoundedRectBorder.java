import javax.swing.border.AbstractBorder; // import libraries
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;

class RoundedRectBorder extends AbstractBorder {

    private Color color; // declare variables
    private int thickness;
    private int radii;
    private Insets insets;
    private BasicStroke stroke;
    private int strokePad;
    private boolean left = true;
    RenderingHints hints;

    RoundedRectBorder(Color color, int thickness, int radii) { // create a constructor to take in the preferences and set the values
        this.thickness = thickness;
        this.radii = radii;
        this.color = color;

        stroke = new BasicStroke(thickness);
        strokePad = thickness / 2;

        hints = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int pad = radii + strokePad;
        int bottomPad = pad + strokePad;
        insets = new Insets(pad, pad, bottomPad, pad);
    }

    @Override
    public Insets getBorderInsets(Component c) { // return insets
        return insets;
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) { // return the components insets
        return getBorderInsets(c);
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        // draw the border around the button using the given perameters

        Graphics2D g2 = (Graphics2D) g;

        int bottomLineY = height - thickness;

        RoundRectangle2D.Double bubble = new RoundRectangle2D.Double( // create a rounded rectangle object
                strokePad,
                strokePad,
                width - thickness,
                bottomLineY,
                radii,
                radii);

        Area area = new Area(bubble); // add the rectangle to an area

        g2.setRenderingHints(hints);

        // set the color of the parent, everywhere outside the rectangle
        Component parent  = c.getParent();
        if (parent!=null) {
            Color bg = parent.getBackground();
            Rectangle rect = new Rectangle( 0, 0, width, height);
            Area borderRegion = new Area(rect);
            borderRegion.subtract(area); // fill the rectangle with the provided color
            g2.setClip(borderRegion);
            g2.setColor(bg);
            g2.fillRect(0, 0, width, height);
            g2.setClip(null);
        }

        g2.setColor(color); // draw the graphic
        g2.setStroke(stroke);
        g2.draw(area);
    }
}
