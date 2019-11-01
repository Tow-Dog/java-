/**
 * 除法运算部件定义
 */
public class DivideFunctionalUnit extends FunctionalUnit{
    public DivideFunctionalUnit() {
        super("Divide");
        this.delay = DelayAndFunctionalUnit.DIVIDE_UNIT.getDelay();
        this.type = 3;
    }

    public DivideFunctionalUnit(String unitName) {
        super(unitName);
        this.delay = DelayAndFunctionalUnit.DIVIDE_UNIT.getDelay();
        this.type = 3;

    }
}
