/**
 * 定义加法运算部件
 */
public class AddFunctionalUnit extends FunctionalUnit{
    public AddFunctionalUnit(){
	super("Add");
        this.delay=DelayAndFunctionalUnit.ADD_UNIT.getDelay();
	this.type = 1;
    }

    public AddFunctionalUnit(String unitName){
        super(unitName);
        this.delay=DelayAndFunctionalUnit.ADD_UNIT.getDelay();
	this.type =1;
    }
}
