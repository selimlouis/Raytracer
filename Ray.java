public class Ray{
    public Matvec direction;
    public Matvec position;

    public Ray(Matvec d, Matvec p){
        this.direction = d;
        this.position = p;
    }
}