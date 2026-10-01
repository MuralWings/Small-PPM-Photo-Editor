import org.w3c.dom.css.RGBColor;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;
import java.util.Stack;
import java.io.*;

class ImageEditor extends JPanel {

    private final Stack<BufferedImage> UNDO_STACK;
    private final Stack<BufferedImage> REDO_STACK;
    private final JPanel IMAGE_PANEL;
    private final JMenuBar MENU_BAR;
    private final JScrollPane SCROLL_PANE;
    private final ShortcutKeyMap SHORTCUT_KEY_MAP;
    private final ZoomMouseEventListener ZOOM_LISTENER;
    private int zoomImageIndex;

    ImageEditor() {
        this.UNDO_STACK = new Stack<>();
        this.REDO_STACK = new Stack<>();
        this.SHORTCUT_KEY_MAP = new ShortcutKeyMap(this);
        this.IMAGE_PANEL = new ImagePanel(this);
        this.SCROLL_PANE = new JScrollPane(this.IMAGE_PANEL, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.MENU_BAR = new MenuBar(this);
        this.ZOOM_LISTENER = new ZoomMouseEventListener(this, this.IMAGE_PANEL);
        this.zoomImageIndex = 0;
        this.setLayout(new BorderLayout());
        this.add(this.MENU_BAR, BorderLayout.NORTH);
        this.add(this.SCROLL_PANE, BorderLayout.CENTER);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.IMAGE_PANEL.repaint();
    }

    @Override
    public void revalidate() {
        super.revalidate();
        if (this.IMAGE_PANEL != null) {
            this.IMAGE_PANEL.revalidate();
        }
    }

    /**
     *This method reads a given ppm file and makes it a buffered image object
     *
     * @param in the name of  the file that you want to read the data of!
     */
    void readPpmImage(String in){

        try(Scanner sc = new Scanner(new File(in))) {
            // TODO read the PPM image file into the "img" variable.
            // Read the width, height into the "width" and "height" variables.


            sc.nextLine();
            int width = sc.nextInt();
            int height = sc.nextInt();
            sc.nextLine();
            sc.nextLine();
            BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            // TODO Read the pixel data.
            for(int y=0;y<=width-1;y++){
                for(int x=0;x<=height-1;x++){
                    int redValue=sc.nextInt();
                    int greenValue=sc.nextInt();
                    int blueValue=sc.nextInt();
                    Color currentColor=new Color(redValue,greenValue,blueValue);
                    img.setRGB(x,y,currentColor.getRGB());
                }
            }
            // Do not modify the lines below.
            this.UNDO_STACK.clear();
            this.REDO_STACK.clear();
            this.zoomImageIndex = 0;
            this.addImage(img);


        } catch (IOException e) { // <- this will need to be a different exception!
            throw new RuntimeException(e);
        }
    }

    /**
     * This method opens a file, wirtes out the ppm header data, then the the image pixel data
     *
     * @param out the new file name that you are writing to.
     */
    void writePpmImage(String out) {
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(out));
            BufferedImage img = this.getImage();
            int width=img.getWidth();
            int height=img.getHeight();
            pw.println("P3");
            pw.println(width+" "+height);
            pw.println("255");

            for(int y = 0; y < height; y++) {
                for(int x = 0; x < width; x++) {
                    Color rgbValue=ColorOperations.getColorAtPos(img,x,y);
                    int redValue=rgbValue.getRed();
                    int greenValue=rgbValue.getGreen();;
                    int blueValue= rgbValue.getBlue();
                    pw.print(redValue+" "+greenValue+" "+blueValue+" ");
                }
                pw.println();
                } pw.close();

            // TODO write the image to the PPM file.

        } catch (IOException e) { // <- this will need to be a different exception!
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds a new image to the editor and the undo stack. It is assumed that the image
     * being passed is not zoomed. If so, use the other addImage method.
     *
     * @param img image to add.
     */
    void addImage(BufferedImage img) {
        this.UNDO_STACK.push(img);
        this.REDO_STACK.clear();
        this.revalidate();
        this.repaint();
        this.zoomImageIndex++;
    }

    /**
     * Adds a new zoomed image to the editor. Because we only want to apply transformations
     * to non-zoomed images, we need to keep track of where the last non-zoomed image is in
     * the undo stack.
     *
     * @param img    image to add.
     * @param zoomed flag indicating whether the image is zoomed. This is always true.
     */
    void addImage(BufferedImage img, boolean zoomed) {
        this.UNDO_STACK.push(img);
        this.REDO_STACK.clear();
        this.revalidate();
        this.repaint();
        if (!zoomed) {
            this.zoomImageIndex++;
        }
    }

    /**
     * Removes the current image from the editor and the undo stack.
     * The undone image is pushed to the redo stack. If there are no images
     * to undo, this method does nothing.
     */
    void undoImage() {
        if (!this.UNDO_STACK.isEmpty()) {
            this.REDO_STACK.push(this.UNDO_STACK.pop());
            this.revalidate();
            this.repaint();
        }
    }

    /**
     * Redoes the last undone image. The redone image is pushed to the undo stack.
     * If there are no images to redo, this method does nothing.
     */
    void redoImage() {
        if (!this.REDO_STACK.isEmpty()) {
            this.UNDO_STACK.push(this.REDO_STACK.pop());
            this.revalidate();
            this.repaint();
        }
    }

    Stack<BufferedImage> getUndoStack() {
        return this.UNDO_STACK;
    }

    Stack<BufferedImage> getRedoStack() {
        return this.REDO_STACK;
    }

    BufferedImage getImage() {
        return this.UNDO_STACK.isEmpty() ? null : this.UNDO_STACK.peek();
    }

    BufferedImage getOriginalImage() {
        if (this.zoomImageIndex < 1 || this.zoomImageIndex >= this.UNDO_STACK.size()) {
            return null;
        } else {
            return this.UNDO_STACK.elementAt(this.zoomImageIndex - 1);
        }
    }

    MenuBar getMenuBar() {
        return (MenuBar) MENU_BAR;
    }

    JScrollPane getScrollPane() {
        return this.SCROLL_PANE;
    }

    ZoomMouseEventListener getZoomListener() {
        return this.ZOOM_LISTENER;
    }
}
