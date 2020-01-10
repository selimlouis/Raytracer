import java.lang.*;
import java.util.*;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.*;


public class Raytracer{

    public static void gradient(int width, int height, String name){

        //create buffered image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        //file object
        File f = null;

        //write each pixel indevidually
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                int r = (int) ((int) 255 * (x /(float) width));
                int g = 100;
                int b = (int) ((int) 255 * (y/(float)height));
                
                int rgb = r*(65536) + g*(256) + b;
                image.setRGB(x, y, rgb);
            }
        }
        //write image
        try{
            f = new File(name + ".png");
            ImageIO.write(image, "png", f);
        }catch(IOException e){
            System.out.println("Error: " + e);
        }

    }

    public void writePng(File png, ArrayList<List<Double>> colors){

    }
    public static void main(String[] args){
        System.out.print("Trace the Rays\n");

        gradient(512, 512, "test");
    }
}