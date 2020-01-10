import Matvec.java;

public class Ray{
    public Matvec direction;
    public double magnitude;

    public Ray(Matvec d, double m){
        this.direction = d;
        this.magnitude = m;
    }
}