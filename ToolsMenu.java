import javax.swing.*;

class ToolsMenu extends JMenu {

    ToolsMenu(ImageEditor mainPanel) {
        super("Tools");
        // Create the menu items and disable them.
        JMenuItem zeroRed = new ZeroRedMenuItem(mainPanel);
        zeroRed.setEnabled(true);
        JMenuItem grayscale = new GrayscaleMenuItem(mainPanel);
        grayscale.setEnabled(true);
        JMenuItem invert = new InvertMenuItem(mainPanel);
        invert.setEnabled(false);
        JMenuItem mirror = new MirrorMenuItem(mainPanel);
        mirror.setEnabled(false);
        JMenuItem repeat = new RepeatMenuItem(mainPanel);
        repeat.setEnabled(false);
        JMenuItem rotate = new RotateMenuItem(mainPanel);
        rotate.setEnabled(false);

        // Add the menu items to the menu.
        this.add(zeroRed);
        this.add(grayscale);
        this.add(invert);
        this.add(mirror);
        this.add(repeat);
        this.add(rotate);
    }
}
