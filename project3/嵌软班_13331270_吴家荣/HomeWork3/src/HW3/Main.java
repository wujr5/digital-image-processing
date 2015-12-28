package HW3;

import java.io.IOException;

public class Main {
  public static void main(String[] args) throws IOException {
    System.out.println("Fourier Transform Action!");
    
    Answer answer = new Answer();
    
    answer.HW3_2_2_DFT();
    answer.HW3_2_3_FFT();
    answer.HW3_2_4_FrequencyDomainFilter();
    
    System.out.println("Fourier Transform Succeed!");
  }
}
