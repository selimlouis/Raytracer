

public class Matvec{
    public double[] xyz;

    public Matvec(double[] values){
        this.xyz = values;
    }

    public void normalize(){
        double length = Math.sqrt(this.xyz[0] * this.xyz[0] + this.xyz[1] * this.xyz[1] + this.xyz[2] * this.xyz[2]);
        for(int i = 0; i < 3; i++){
            this.xyz[i] /= length;
        }
    }

    public double dot(Matvec b){
        double product = 0; 
      
        // Loop for calculate cot product 
        for (int i = 0; i < 3; i++) 
            product = product + this.xyz[i] * b.xyz[i]; 
        return product;
    }

    public Matvec cross(Matvec b){
        double[] cross_P = {0,0,0};
        cross_P[0] = this.xyz[1] * b.xyz[2]  - this.xyz[2] * b.xyz[1]; 
        cross_P[1] = this.xyz[0] * b.xyz[2]  - this.xyz[2] * b.xyz[0]; 
        cross_P[2] = this.xyz[0] * b.xyz[1]  - this.xyz[1] * b.xyz[0]; 
        return new Matvec(cross_P);
    }

    public void add(Matvec b){
        for(int i = 0; i < 3; i++){
            this.xyz[i] += b.xyz[i];
        }
    }

    public void sub(Matvec b){
        for(int i = 0; i < 3; i++){
            this.xyz[i] -= b.xyz[i];
        }
    }

    public void mult(double factor){
        for(int i = 0; i < 3; i++){
            this.xyz[i] *= factor;
        }
    }

    public Matvec addreturn(Matvec b){
        double[] res = {0,0,0};
        Matvec result = new Matvec(res);

        for(int i = 0; i < 3; i++){
            result.xyz[i] = this.xyz[i] + b.xyz[i];
        }

        return result;
    }

    public Matvec subreturn(Matvec b){
        double[] res = {0,0,0};
        Matvec result = new Matvec(res);

        for(int i = 0; i < 3; i++){
            result.xyz[i] = this.xyz[i] - b.xyz[i];
        }

        return result;
    }

    public Matvec multreturn(double factor){
        double[] res = {0,0,0};
        Matvec result = new Matvec(res);

        for(int i = 0; i < 3; i++){
            result.xyz[i] = this.xyz[i] * factor;
        }

        return result;
    }

    
}