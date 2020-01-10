

public class Matvec{
    public double[] xyz;

    public Matvec(double[] values){
        this.xyz = values;
    }

    public void normalize(){
        double length = Math.sqrt(this.xyz[0] * this.xyz[0] + this.xyz[1] * this.xyz[1] + this.xyz[2] * this.xyz[2]);
        for(int i = 0; i <= 3; i++){
            this.xyz[i] /= length;
        }
    }

    public double dot(double[] b){
        double product = 0; 
      
        // Loop for calculate cot product 
        for (int i = 0; i < 3; i++) 
            product = product + this.xyz[i] * b[i]; 
        return product;
    }

    public double[] cross(double[] b){
        double[] cross_P = {0,0,0};
        cross_P[0] = this.xyz[1] * b[2]  - this.xyz[2] * b[1]; 
        cross_P[1] = this.xyz[0] * b[2]  - this.xyz[2] * b[0]; 
        cross_P[2] = this.xyz[0] * b[1]  - this.xyz[1] * b[0]; 
        return cross_P;
    }

    
}