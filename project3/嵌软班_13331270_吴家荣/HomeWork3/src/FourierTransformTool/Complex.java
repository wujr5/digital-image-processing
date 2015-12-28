package FourierTransformTool;

// complex inner class
public class Complex {
  private final double r;
  private final double i;
  
  public Complex (double r, double i) {
    this.r = r;
    this.i = i;
  }
  
  public double abs() { // return sqrt(r^2 +i^2)
    return Math.hypot(r, i);
  }
  
  public double phase() {
    return Math.atan2(i, r);
  }
  
  public Complex plus (Complex c) {
    return new Complex (this.r + c.r, this.i + c.i);
  }
  
  public Complex minus (Complex c) {
    return new Complex (this.r - c.r, this.i - c.i);
  }
  
  public Complex times (Complex c) {
    return new Complex (this.r * c.r - this.i * c.i,
        this.r * c.i + this.i * c.r);
  }
  
  public Complex times (double d) {
    return new Complex (this.r * d, this.i * d);
  }
  
  public Complex conjugate() {
    return new Complex (r, -i);
  }
  
  public double getR () {
    return r;
  }
  
  public double getI () {
    return i;
  }
  
  public Complex exp() {
    return new Complex(Math.exp(r) * Math.cos(i),
        Math.exp(r) * Math.sin(i));
  }
}