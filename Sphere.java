public class Sphere{
    public Matvec position;
    public double radius;
    public int[] color;

    public Sphere(double[] pos, double rad, int[] col){
        this.position = new Matvec(pos);
        this.radius = rad;
        this.color = col;
    }
}