/**
 * ö�����Ͷ������㲿���������
 * delay��ָ���ӳ�����
 * unitNum�����㲿������
 */
public enum DelayAndFunctionalUnit {
    ADD_UNIT(1,1),//�ӷ���ԪĬ��ֵ
    MULTI_UNIT(9,2),//�˷���ԪĬ��ֵ
    DIVIDE_UNIT(39,1),//������ԪĬ��ֵ
    INTEGER_UNIT(0,1);//������ԪĬ��ֵ

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
