# Small PPM Photo Editor

A small desktop photo editor written in Java (Swing) that opens, edits, and saves images in the plain-text **PPM (P3)** format.

## About this project

This was a small project built by a group of three students for a class. It was a learning exercise in Java, Swing GUIs, and working with raw pixel data. We started from an instructor-provided template and filled in the image reading/writing and the image transformations.

## Running it

You need a JDK (Java 8 or newer). From the project folder:

```bash
javac *.java
java ImageEditorRunner
```

The editor opens in a 512x512 window titled "Image Editor".

## What the program does

### File menu
- **Open** (`Ctrl+O`): Choose a `.ppm` file. The program reads the header (width, height, max color value) and then every pixel's red, green, and blue values into an image. Opening an image clears the undo/redo history and enables the Tools menu.
- **Save** (`Ctrl+S`): Writes the current image back out as a P3 PPM file (header, dimensions, `255`, then one row of RGB values per line).

### Edit menu
- **Undo** (`Ctrl+Z`): Steps back to the previous version of the image.
- **Redo** (`Ctrl+Y`): Re-applies an undone change. Making a new edit clears the redo history.

### Tools menu
The Tools menu is disabled until an image has been opened.

| Tool | What it does |
| --- | --- |
| **Zero Red** | Sets the red channel of every pixel to 0, leaving only green and blue. |
| **Grayscale** | Replaces each pixel with the average of its red, green, and blue values. |
| **Invert** | Replaces each channel value `v` with `255 - v`, producing a color negative. |
| **Mirror -> Horizontal / Vertical** | Reflects one half of the image onto the other half. Horizontal mirrors the top half onto the bottom; vertical mirrors the left half onto the right. |
| **Rotate -> Clockwise / Counter-clockwise** | Rotates the image 90 degrees in the chosen direction (width and height swap). |
| **Repeat -> Horizontal / Vertical** | Asks for a number `n` and tiles the image `n` times side by side or top to bottom. |

### Zoom
Hold the **right mouse button** on the image and scroll the **mouse wheel** to zoom in (up) or out (down) in 10% steps. Scrolling without the right button held scrolls the image normally. Transformations should only be applied to a non-zoomed image, so apply your edits first and zoom afterward.

### Help menu
- **Getting Started**: Shows a dialog summarizing how to open, edit, zoom, and save an image.

## How the code is organized

| File | Role |
| --- | --- |
| `ImageEditorRunner.java` | Entry point; creates the window. |
| `ImageEditor.java` | Main panel. Reads/writes PPM files and manages the undo/redo stacks. |
| `ImageOperations.java` | The pixel-level image transformations (zero red, grayscale, invert, mirror, rotate, repeat, zoom). |
| `ColorOperations.java` | Helpers for pulling color channels out of pixels. |
| `ImagePanel.java` | Draws the current image. |
| `ShortcutKeyMap.java` | Keyboard shortcuts and the open/save file dialogs. |
| `ZoomMouseEventListener.java` | Right-click + mouse-wheel zoom handling. |
| `MenuBar.java`, `FileMenu.java`, `EditMenu.java`, `ToolsMenu.java`, `HelpMenu.java` | Menu structure. |
| `*MenuItem.java` | One class per menu action (Open, Save, Undo, Redo, Grayscale, Invert, Mirror, Rotate, Repeat, Zero Red, Help). |

## Known limitations

Because this was a small class project, it has some rough edges:
- Only the P3 (plain-text) PPM format is supported, and PPM comments are not handled.
- Some tools (zero red, grayscale, invert, mirror) edit the image in place, so undo may not fully restore the earlier look after using them.
- Invalid input, such as a non-numeric value in the Repeat dialog or a malformed PPM file, is not handled gracefully.
