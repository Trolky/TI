import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class DrawingPanel extends JPanel {

    private BufferedImage img;

    public DrawingPanel(BufferedImage img) {
        this.img = img;
        this.setPreferredSize(new Dimension(1400, 650));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D)g;
        Image resImage = img.getScaledInstance(950,650,Image.SCALE_SMOOTH);
        BufferedImage scaledImage = new BufferedImage(950,650,img.getType());
        scaledImage.getGraphics().drawImage(resImage,0,0,null);
        g2.drawImage(scaledImage,0,0,null);
    }

    public BufferedImage getImg() {
        return img;
    }

    public void setImg(BufferedImage img) {
        this.img =img;
    }


}
