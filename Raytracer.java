import java.lang.*;
import java.util.*;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.*;


public class Raytracer{

    public static Ray shootRay(double x, double y, int width, int height, double depth, Matvec camposition){
        double vecx = 1 - (x * 2 / (double) width);
        double vecy = -1 + (y * 2 / (double) height);
        double vecz = depth;

        double[] rayvec = {vecx, vecy, vecz};
        Matvec raymat = new Matvec(rayvec);
        Ray ray = new Ray(raymat, camposition);
        ray.direction.sub(camposition);
        ray.direction.normalize();
        return ray;
    }

    //diffuse shading logic

    public static double diffuseColor(Ray surfacenormal, Ray lightray){
        double result = 0;

        result = surfacenormal.direction.dot(lightray.direction);
        if(result < 0.2){
            return 0.2;
        }
        return result;
    }

    public static Ray getLightRay(Matvec lightposition, Ray ray, double t){
        Matvec startposition = ray.direction.multreturn(t);
        Matvec lightdirection = lightposition.subreturn(startposition);

        lightdirection.normalize();
        

        Ray lightray = new Ray(lightdirection, startposition);
        return lightray;
    }

    public static Ray getSurfaceNormal(Sphere sphere, Ray ray, double t){
        Matvec hitposition = ray.direction.multreturn(t);
        Matvec surfaceNormal = hitposition.subreturn(sphere.position);
        Ray result = new Ray(surfaceNormal, hitposition);
        surfaceNormal.normalize();
        return result;
    }

    // specular shading logic

    public static Ray getCamRay(Matvec camposition, Ray ray, double t){
        Matvec startposition = ray.direction.multreturn(t);
        Matvec camdirection = camposition.subreturn(startposition);

        camdirection.normalize();
        

        Ray camray = new Ray(camdirection, startposition);
        return camray;
    }

    public static double limit(double value) {
        return Math.max(0, Math.min(value, 1));
    }

    public static double specularValue(Ray lightray, Ray camray, Ray surfacenormal, double power){
        Matvec specularvec = lightray.direction.addreturn(camray.direction);
        specularvec.normalize();
        double specular = specularvec.dot(surfacenormal.direction);
        specular = Math.pow(specular, power);
        specular = limit(specular);
        return specular;
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

    public static int[] raySphereIntersection(Ray ray, ArrayList<Sphere> spheres, Matvec lightposition, Matvec camposition){
        int[] result = {0,0,0};
        double tmin = 99999999999.0;
        Sphere minSphere = null;
        for(int i = 0; i < spheres.size(); i++){

            double t = intersect(ray, spheres.get(i).position, spheres.get(i).radius);
            if( t <= tmin && t >= 0){
                tmin = t;
                minSphere = spheres.get(i);
                result[0] = minSphere.color[0];
                result[1] = minSphere.color[1];
                result[2] = minSphere.color[2];
                //steps for diffuse color
                Ray lightray = getLightRay(lightposition, ray, tmin);
                Ray surfacenormal = getSurfaceNormal(minSphere, ray, tmin);
                double diffusevalue = diffuseColor(surfacenormal, lightray);
                Ray camray = getCamRay(camposition, ray, t);
                double specularvalue = specularValue(lightray, camray, surfacenormal, 15.0);
                //the diffuse value determines the alpha value
                //result[3] = (int) (255 * diffusevalue);

                for(int k = 0; k < 3; k++){
                    result[k] = (int) (result[k] * limit(diffusevalue + specularvalue));
                    //result[k] = (int) (result[k] * specularvalue);
                }
            }

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

    public static void trace(Matvec camposition, ArrayList<Sphere> spheres, int width, int height, double depth, Matvec lightposition, String name){
        //create buffered image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        //file object
        File f = null;

        //write each pixel indevidually
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                Ray ray = shootRay(x, y, width, height, depth, camposition);
                int[] color = raySphereIntersection(ray, spheres, lightposition, camposition);
                //the color int array adds the alpha value to the last place
                //int a = color[3];
                int r = color[0];
                int g = color[1];
                int b = color[2];
                
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

        double[] lamp = {5,-5, -1};
        Matvec lampposition = new Matvec(lamp);
        double[] capos = {0,0,1};
        double[] lisapos = {3,0,-10};
        int[] gunthercolor = {20,250,0};
        double[] guntherpos = {0,0,-15};
        int[] lisacolor = {100, 0, 100};
        Matvec camposition = new Matvec(capos);
        Sphere Lisa = new Sphere(lisapos, 2, lisacolor);

        Sphere Gunther = new Sphere(guntherpos, 4, gunthercolor);
        ArrayList<Sphere> spheres = new ArrayList<>();
        spheres.add(Lisa);
        spheres.add(Gunther);

        trace(camposition, spheres, 512, 512, -2, lampposition, "raytrace");

    }
}