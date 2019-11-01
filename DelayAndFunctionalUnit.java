/**
 * 枚举类型定义运算部件相关内容
 * delay：指令延迟周期
 * unitNum：运算部件数量
 */
public enum DelayAndFunctionalUnit {
    ADD_UNIT(1,1),//加法单元默认值
    MULTI_UNIT(9,2),//乘法单元默认值
    DIVIDE_UNIT(39,1),//除法单元默认值
    INTEGER_UNIT(0,1);//整数单元默认值

    private int delay;
    private int unitNum;
    DelayAndFunctionalUnit(int delay,int unitNum){
        this.delay = delay;
        this.unitNum = unitNum;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getUnitNum() {
        return unitNum;
    }

    public void setUnitNum(int unitNum) {
        this.unitNum = unitNum;
    }
}
