package com.xplus.commons.thread.junior;

public class ThreadGetStarted {

  public static void main(String[] args) {
    System.out.println(Thread.currentThread().getName());
    PrimeThread p = new PrimeThread(143);
    p.start();
  }

  public static class PrimeThread extends Thread {
    long minPrime;
    PrimeThread(long minPrime) {
      this.minPrime = minPrime;
    }
    public void run() {
      // compute primes larger than minPrime
      // . . .
      System.out.println(Thread.currentThread().getName());
    }
  }

}
