package edu.ucsb.cs.cs185.charlesweng.charleswengMultiTouch;

public class RosMath {

	  private RosMath() {
	    // Utility class.
	  }

	  public static double clamp(double value, double minmum, double maximum) {
	    if (value < minmum) {
	      return minmum;
	    }
	    if (value > maximum) {
	      return maximum;
	    }
	    return value;
	  }

	  public static float clamp(float value, float minmum, float maximum) {
	    if (value < minmum) {
	      return minmum;
	    }
	    if (value > maximum) {
	      return maximum;
	    }
	    return value;
	  }

	  public static int clamp(int value, int minmum, int maximum) {
	    if (value < minmum) {
	      return minmum;
	    }
	    if (value > maximum) {
	      return maximum;
	    }
	    return value;
	  }

	  public static long clamp(long value, long minmum, long maximum) {
	    if (value < minmum) {
	      return minmum;
	    }
	    if (value > maximum) {
	      return maximum;
	    }
	    return value;
	  }
	}