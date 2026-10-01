import java.awt.*;
import java.awt.image.BufferedImage;

class ImageOperations {

    /**
     * Takes a Buffered image img and removes the red channel from the image
     * @param img image to be edited
     * @return image img but without the red channel
     */
    static BufferedImage zeroRed(BufferedImage img) {
        int width=img.getWidth();
        int height=img.getHeight();
        BufferedImage newImg = img;
        for(int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color rgbValue=ColorOperations.getColorAtPos(img,x,y);
                int greenValue= rgbValue.getGreen();
                int blueValue=rgbValue.getBlue();
                rgbValue=new Color(0,greenValue,blueValue);
                newImg.setRGB(x,y,rgbValue.getRGB());
            }
        }
        return newImg;
    }

    /**
     * Takes a buffered image img and coverts it into a greyscale image
     * @param img image to be edited 
     * @return image img but greyscaled
     */
    static BufferedImage grayscale(BufferedImage img) {
    
        int width=img.getWidth();
        int height=img.getHeight();
        BufferedImage newImg = img;
        for(int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color rgbValue=ColorOperations.getColorAtPos(img,x,y);
                int redValue= (int) (rgbValue.getRed());
                int greenValue= (int) (rgbValue.getGreen());
                int blueValue= (int) (rgbValue.getBlue());
                int avg=(redValue+greenValue+blueValue)/3;
                rgbValue=new Color(avg,avg,avg);
                newImg.setRGB(x,y,rgbValue.getRGB());
            }
        }
        return newImg;
    }

    /**
     * Takes a buffered image img and inverts it 
     * @param img image to be edited 
     * @return image img but inverted 
     */
    static BufferedImage invert(BufferedImage img) {
        int width=img.getWidth();
        int height=img.getHeight();
        BufferedImage newImg = img;
        for(int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color rgbValue=ColorOperations.getColorAtPos(img,x,y);
                int redValue= (int) 255-(rgbValue.getRed());
                int greenValue= 255-rgbValue.getGreen();
                int blueValue=255-rgbValue.getBlue();
                rgbValue=new Color(redValue,greenValue,blueValue);
                newImg.setRGB(x,y,rgbValue.getRGB());
            }
        }
        return newImg;
    }

    /**
     * TODO.
     *
     * @param img TODO.
     * @param dir TODO.
     * @return TODO.
     */
    static BufferedImage mirror(BufferedImage img, MirrorMenuItem.MirrorDirection dir) {
        // TODO instantiate newImg with the *correct* dimensions.
        int width=img.getWidth();
        int height=img.getHeight();
        int xMidPoint=width/2;
        int yMidPoint=height/2;
        BufferedImage newImg = img;
        if (dir == MirrorMenuItem.MirrorDirection.VERTICAL) {

            for(int y = 0; y < height; y++) {
                int currentXIndex =xMidPoint-1;
                for (int x = 0; x < width; x++) {
                    if (x>xMidPoint){
                        Color rgbValue=ColorOperations.getColorAtPos(img, currentXIndex,y);
                        newImg.setRGB(x,y,rgbValue.getRGB());
                        currentXIndex--;
                    }
                }
            }
        } else {
            int currentYIndex=yMidPoint-1;
            for(int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (y>yMidPoint){
                        Color rgbValue=ColorOperations.getColorAtPos(img, x,currentYIndex);
                        newImg.setRGB(x,y,rgbValue.getRGB());
                    }
                }if (y>yMidPoint){
                    currentYIndex--;}
            }
        }
        return newImg;
    }

    /**
     * TODO.
     *
     * @param img TODO.
     * @param dir TODO.
     * @return TODO.
     */

    static BufferedImage rotate(BufferedImage img, RotateMenuItem.RotateDirection dir) {
        // TODO instantiate newImg with the *correct* dimensions.
        int width=img.getWidth();
        int height=img.getHeight();

        BufferedImage newImg =new BufferedImage(height, width, BufferedImage.TYPE_INT_RGB);
        if (dir == RotateMenuItem.RotateDirection.CLOCKWISE) {
            for(int y = 0; y < width; y++) {
                int ReverseXHelper = newImg.getWidth()-1;
                for (int x = 0; x < height; x++) {
                    int rgbVal=img.getRGB(y,x);
                    newImg.setRGB(ReverseXHelper -x,y,rgbVal);
                }
            }
        } else {
            for (int i = 0; i < 3; i++) {
                img = rotate(img, RotateMenuItem.RotateDirection.CLOCKWISE);
            }
            newImg=img;
        }
        return newImg;
    }

    /**
     * TODO.
     *
     * @param img TODO.
     * @param n   TODO.
     * @param dir TODO.
     * @return TODO.
     */
    static BufferedImage repeat(BufferedImage img, int n, RepeatMenuItem.RepeatDirection dir) {
        BufferedImage newImg = null;
        int width=img.getWidth();
        int height=img.getHeight();
        // newImg must be instantiated in both branches with the correct dimensions.
        if (dir == RepeatMenuItem.RepeatDirection.HORIZONTAL) {
            newImg=new BufferedImage(width*n,height, BufferedImage.TYPE_INT_RGB);
            int currentXindex = 0;
            for (int i = 0; i <n; i++) {
                for(int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        int rgbValue=img.getRGB(x,y);
                        newImg.setRGB(x+currentXindex,y,rgbValue);
                    }
                    }
                currentXindex+=width-1;
            }

        } else {
            newImg=new BufferedImage(width,height*n, BufferedImage.TYPE_INT_RGB);
            int currentYindex = 0;
            for (int i = 0; i <n; i++) {
                for(int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        int rgbValue=img.getRGB(x,y);
                        newImg.setRGB(x,y+currentYindex,rgbValue);
                    }
                }
                currentYindex+=height-1;
            }
        }
        return newImg;
    }

    /**
     * Zooms in on the image. The zoom factor increases in multiplicatives of 10% and
     * decreases in multiplicatives of 10%.
     *
     * @param img        the original image to zoom in on. The image cannot be already zoomed in
     *                   or out because then the image will be distorted.
     * @param zoomFactor The factor to zoom in by.
     * @return the zoomed in image.
     */
    static BufferedImage zoom(BufferedImage img, double zoomFactor) {
        int newImageWidth = (int) (img.getWidth() * zoomFactor);
        int newImageHeight = (int) (img.getHeight() * zoomFactor);
        BufferedImage newImg = new BufferedImage(newImageWidth, newImageHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = newImg.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(img, 0, 0, newImageWidth, newImageHeight, null);
        g2d.dispose();
        return newImg;
    }
}
