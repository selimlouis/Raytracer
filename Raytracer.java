import java.lang.*;
import java.util.*;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.*;


public class Raytracer{

    public static Ray shootRay(double x, double y, int width, int height, double depth, Matvec camposition){
        double vecx = 1 - (x / (double) width);
        double vecy = 1 - (y / (double) height);
        double vecz = depth;

        double[] rayvec = {vecx, vecy, vecz};
        Matvec raymat = new Matvec(rayvec);
        Ray ray = new Ray(raymat, camposition);
        ray.direction.sub(camposition);
        ray.direction.normalize();
        return ray;
    }

    public static double intersect(Ray ray, Matvec pos, double radius){
        double t;

        Matvec L = ray.position.subreturn(pos);

        double a = ray.direction.dot(ray.direction);
        double b = 2*(ray.direction.dot(L));
        double c = (L.dot(L)) - (radius*radius);

        double disc = b*b - 4*a*c;

        if (disc < 0)
            return -1;

        double distSqrt = Math.sqrt(disc);
        double q;

        if (b < 0)
            q = (-b - distSqrt)/2;
        else
            q = (-b + distSqrt)/2;

        double t0 = q/a;
        double t1 = c/q;

        if (t0 > t1)
        {
            double temp = t0;
            t0 = t1;
            t1 = temp;
        }

        if (t1 < 0)
            return -1;

        if (t0 < 0)
            t = t1;
        else
            t = t0;

        return t;
    }

    public static int[] raySphereIntersection(Ray ray, ArrayList<Sphere> spheres){
        int[] result = {0,0,0};
        double tmin = 99999999999.0;
        Sphere minSphere = null;
        for(int i = 0; i < spheres.size(); i++){

            double t = intersect(ray, spheres.get(i).position, spheres.get(i).radius);
            if( t <= tmin && t >= 0){
                tmin = t;
                minSphere = spheres.get(i);
            }

        }
        
        
        if(minSphere != null){
            result = minSphere.color;
        }
        return result;
    }

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

    public static void trace(Matvec camposition, ArrayList<Sphere> spheres, int width, int height, double depth, String name){
        //create buffered image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        //file object
        File f = null;

        //write each pixel indevidually
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                Ray ray = shootRay(x, y, width, height, depth, camposition);
                int[] color = raySphereIntersection(ray, spheres);
                int r = (int) ((int) color[0] * (x /(float) width));
                int g = color[1];
                int b = (int) ((int) color[2] * (y/(float)height));
                
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

    
    public static void main(String[] args){
        System.out.print("Trace the Rays\n");

        //gradient(512, 512, "test");

        double[] capos = {0,0,1};
        double[] lipos = {0,0,-2};
        int[] licolor = {100, 0, 200};
        Matvec camposition = new Matvec(capos);
        Sphere Lisa = new Sphere(lipos, 2, licolor);
        ArrayList<Sphere> spheres = new ArrayList<>();
        spheres.add(Lisa);

        trace(camposition, spheres, 512, 512, -2, "raytrace");

    }
}