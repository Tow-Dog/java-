/**
 *
 * @author Matthew Kelly
 */
public class MultiplyFunctionalUnit extends FunctionalUnit{//data structure for multply commands

    public MultiplyFunctionalUnit() {
	super("Mult");//call parent constructor to setup with proper data type
	this.delay = DelayAndFunctionalUnit.MULTI_UNIT.getDelay();
	this.type = 2;
    }

    public MultiplyFunctionalUnit(String unitName){
        super(unitName);//call parent constructor to setup with proper data type
	this.delay = DelayAndFunctionalUnit.MULTI_UNIT.getDelay();
	this.type = 2;
    }

}
