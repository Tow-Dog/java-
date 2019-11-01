import java.util.ArrayList;
/**
 *
 * @author Matthew Kelly
 */
public class Register implements Comparable<Register> {

    public String name; //the name of the register
    public int value;   //the value currently held within the regiwster
    public int type; // Type 0 = destination, type 1 = source
    public String regFU; //the name of the functional unit that the register is using

    public Register(String n , int v, int t) {//contruct the register with name,value and type parameters
	name = n;
	value =v;
	type = t;
	regFU = "";
    } 

    public Register(String n) {//construct the register with just a name parameter
	name = n;
	value = 0;
	type = -1;
	regFU = "";
    } 

    public Register() { //default constructor for register, no parameters required
	name = "";
	value = 0;
	type = -1;
	regFU = "";
    } 

    @Override
    public String toString(){   //debugging method to see contents of register
        String ret = "name: " + name + " value: " + value + " type: " + type + " regFU: " + regFU;
	return ret;
    }


    @Override
   public int compareTo(Register that) {
	final int BEFORE = -1;
	final int EQUAL = 0;
        final int AFTER = 1;

	if(this.name.substring(0,1).compareTo(that.name.substring(0,1)) < 0 ) {
            return BEFORE;
	}else if (this.name.substring(0,1).compareTo(that.name.substring(0,1)) > 0) {
            return AFTER;
	}else {
            if(Integer.parseInt(this.name.substring(1)) < Integer.parseInt(that.name.substring(1)) ) {
                    return BEFORE;
            }else if (Integer.parseInt(this.name.substring(1)) > Integer.parseInt(that.name.substring(1))) {
                    return AFTER;
            }else {
                    return EQUAL;
            }
	}
  }
}
