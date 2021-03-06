/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gk1;

import gk1.textures.ArgbHelper;
import java.awt.image.BufferedImage;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 *
 * @author Kazimierz
 */
public class Viewer {

    private Model lastDrawnModel;
    private Canvas drawing;
    private GraphicsContext graphicsContext;
    private PixelWriter pixelWriter;
    private double width;
    private double height;

    public Viewer(Canvas drawing, double width, double height) {
        this.drawing = drawing;
        this.graphicsContext = drawing.getGraphicsContext2D();
        this.pixelWriter = graphicsContext.getPixelWriter();
        this.width = width;
        this.height = height;
    }

    public void draw(Segment segment, boolean visualAid) {
        graphicsContext.strokeLine(
                segment.getBeginning().getX(),
                segment.getBeginning().getY(),
                segment.getEnd().getX(),
                segment.getEnd().getY());

        // I DISLIKE SLOW SETPIXELS
//        Vertex b = segment.getBeginning();
//        Vertex e = segment.getEnd();
//        boolean steep = abs(e.getY() - b.getY()) > abs(e.getX() - b.getX());
//        if (b.getX() > e.getX()) {
//            if (b.getY() > e.getY()) {
//                if (steep) {
//                    BresenhamSteepUp(e, b);
//                } else {
//                    BresenhamUp(e, b);
//                }
//            } else if (steep) {
//                BresenhamSteepDown(e, b);
//            } else {
//                BresenhamDown(e, b);
//            }
//        } else if (e.getY() > b.getY()) {
//            if (steep) {
//                BresenhamSteepUp(b, e);
//            } else {
//                BresenhamUp(b, e);
//            }
//        } else if (steep) {
//            BresenhamSteepDown(b, e);
//        } else {
//            BresenhamDown(b, e);
//        }
        double x, y;

        switch (segment.getConstraint()) {
            case vertical:
                x = segment.getCenterX() + 4;
                y = segment.getCenterY() - 10;
                graphicsContext.strokeRect(x, y, 4, 20);
                break;
            case horizontal:
                x = segment.getCenterX() - 10;
                y = segment.getCenterY() - 8;
                graphicsContext.strokeRect(x, y, 20, 4);
                break;
            case fixedLength:
                x = segment.getCenterX() + 4;
                y = segment.getCenterY() + 4;
//                graphicsContext.strokeRect(x, y, 10, 10);
                graphicsContext.strokeText(
                        String.format("[%2.1f]", segment.getConstraintLength()), x, y);
                break;
            case free:
                if (visualAid) {
                    graphicsContext.setStroke(Color.BLUEVIOLET);
                    if (segment.isAlmostHorizontal() && segment.isSafeToRestrictHorizontal()) {

                        x = segment.getCenterX() - 10;
                        y = segment.getCenterY() - 8;
                        graphicsContext.strokeRect(x, y, 20, 4);
                    } else if (segment.isAlmostVertical() && segment.isSafeToRestrictVertical()) {
                        x = segment.getCenterX() + 4;
                        y = segment.getCenterY() - 10;
                        graphicsContext.strokeRect(x, y, 4, 20);
                    }
                    graphicsContext.setStroke(Color.BLACK);
                }
                break;
        }
    }

    private void BresenhamDown(Vertex left, Vertex right) {
        Color color = Color.BLACK;

        int x1 = (int) left.getX();
        int x2 = (int) right.getX();
        int y1 = (int) left.getY();
        int y2 = (int) right.getY();

        int dx = x2 - x1;
        int dy = y2 - y1;
        int d = dy * 2 - dx; //initial value of d
        int incrE = dy * 2; //increment used for move to E
        int incrSE = (dy + dx) * 2; //increment used for move to NE
        int twiceDy = dy * 2;
        int twiceNegativeDx = (-dx) * 2;
        pixelWriter.setColor(x1, y1, color);
        int x = x1;
        int y = y1;
        while (x < x2) {

            if (d > 0) {
                d += incrE;
                x++;
            } else {
                d += incrSE;
                x++;
                y--;
            }
            pixelWriter.setColor(x, y, color);
        }
    }

    private void BresenhamSteepDown(Vertex left, Vertex right) {
        Color color = Color.BLACK;

        int x1 = (int) left.getX();
        int x2 = (int) right.getX();
        int y1 = (int) left.getY();
        int y2 = (int) right.getY();

        int dx = x2 - x1;
        int dy = y2 - y1;
        int d = dy * 2 - dx; //initial value of d
        int incrS = dx * 2; //increment used for move to E
        int incrSE = (dy + dx) * 2; //increment used for move to NE
        int twiceDy = dy * 2;
        int twiceNegativeDx = (-dx) * 2;
        pixelWriter.setColor(x1, y1, color);
        int x = x1;
        int y = y1;
        while (y > y2) {

            if (d > 0) {
                d += incrSE;
                x++;
                y--;
            } else {
                d += incrS;
                y--;
            }
            pixelWriter.setColor(x, y, color);
        }
    }

    private void BresenhamUp(Vertex left, Vertex right) {
        Color color = Color.BLACK;

        int x1 = (int) left.getX();
        int x2 = (int) right.getX();
        int y1 = (int) left.getY();
        int y2 = (int) right.getY();

        int dx = x2 - x1;
        int dy = y2 - y1;
        int d = dy * 2 - dx; //initial value of d
        int incrE = dy * 2; //increment used for move to E
        int incrNE = (dy - dx) * 2; //increment used for move to NE
        int twiceDy = dy * 2;
        int twiceNegativeDx = (-dx) * 2;
        pixelWriter.setColor(x1, y1, color);
        int x = x1;
        int y = y1;
        while (x < x2) {

            if (d < 0) {
                d += incrE;
                x++;
            } else {
                d += incrNE;
                x++;
                y++;
            }
            pixelWriter.setColor(x, y, color);
        }
    }

    private void BresenhamSteepUp(Vertex left, Vertex right) {
        Color color = Color.BLACK;

        int x1 = (int) left.getX();
        int x2 = (int) right.getX();
        int y1 = (int) left.getY();
        int y2 = (int) right.getY();

        int dx = x2 - x1;
        int dy = y2 - y1;
        int d = dy * 2 - dx; //initial value of d
        int incrN = -dx * 2; //increment used for move to E
        int incrNE = (dy - dx) * 2; //increment used for move to NE
        int twiceDy = dy * 2;
        int twiceNegativeDx = (-dx) * 2;
        pixelWriter.setColor(x1, y1, color);
        int x = x1;
        int y = y1;
        while (y < y2) {

            if (d < 0) {
                d += incrNE;
                x++;
                y++;
            } else {
                d += incrN;
                y++;
            }
            pixelWriter.setColor(x, y, color);
        }
    }

    public void draw(LightSource light) {
        double x = light.getPosition().getX();
        double y = light.getPosition().getY();
        double radius = light.getPosition().getZ() / 2;
        Paint backupPaint = graphicsContext.getStroke();
        double backupWidth = graphicsContext.getLineWidth();
        int color = light.getLightColor();
        graphicsContext.setLineWidth(2);
        graphicsContext.setStroke(Color.rgb(
                ArgbHelper.getRed(color),
                ArgbHelper.getGreen(color),
                ArgbHelper.getBlue(color)
        ));
        graphicsContext.strokeOval(x - radius / 2, y - radius / 2, radius, radius);
        graphicsContext.setStroke(backupPaint);
        graphicsContext.setLineWidth(backupWidth);
    }

    public void draw(Vertex vertex) {
        double x = vertex.getX();
        double y = vertex.getY();
        double w, h;
        w = h = 6;
        graphicsContext.fillOval(x - w / 2, y - h / 2, w, h);

        if (vertex.isFixed()) {
            w = h = 10;
            graphicsContext.strokeOval(x - w / 2, y - h / 2, w, h);
        }

//        graphicsContext.strokeText(vertex.toString(), x, y - 10);
    }

//    public static BufferedImage getImageFromArray(int[] pixels, int width, int height) {
//        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
//        WritableRaster raster = (WritableRaster) image.getData();
//        raster.setPixels(0, 0, width, height, pixels);
//        return image;
//    }
    public void drawImage(BufferedImage image, double x, double y, double width, double height) {
        graphicsContext.drawImage(SwingFXUtils.toFXImage(image, null), x, y, width, height);
    }

//    private void drawImageEXPERIMENTAL() {
//        // performance: https://stackoverflow.com/questions/6524196/java-get-pixel-array-from-image
//        //more: https://gamedev.stackexchange.com/questions/82909/how-do-i-rotate-and-flip-2d-sprites-stored-in-a-1d-array-of-pixels
//        //
////            canvasImage = new BufferedImage(
////                getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
////            canvasPixels = PixelImages.getPixels(canvasImage);
//    }
    public void clear() {
        graphicsContext.clearRect(0, 0, width, height);
    }

    public void setWidth(double width) {
        this.width = width;
        drawing.setWidth(width);
    }

    public void setHeight(double height) {
        this.height = height;
        drawing.setHeight(height);
    }

    void drawLastModel() {
        drawModel(lastDrawnModel);
    }

    private boolean isDrawing = false;
    private final Object lock = new Object();

    void drawModel(Model model) {
        if (isCurrentlyDrawing()) {
            return;
        } else {
            synchronized (lock) {
                if (isCurrentlyDrawing()) {
                    return;
                } else {
                    isDrawing = true;
                }
            }
        }

        clear();
        lastDrawnModel = model;
        long startTime = System.currentTimeMillis();
        GK1.model.draw(GK1.viewer);

        double elapsedTimeSeconds
                = (double) ((System.currentTimeMillis() - startTime)) / 1000d;
//        double fps = 1 / elapsedTimeSeconds;
//        graphicsContext.strokeText(String.format("%3.2f fps", fps), 10, 50);

        isDrawing = false;
    }

    public boolean isCurrentlyDrawing() {
        return isDrawing;
    }

}
