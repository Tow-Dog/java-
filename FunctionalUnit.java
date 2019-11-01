import java.util.ArrayList;

/**
 * 运算部件基础类
 */
public class FunctionalUnit {
    int releaseTime = 0;
    //定义功能部件状态
    public boolean Busy = false;    //只是功能部件状态
    public String Op = "";          //功能部件正在完成何种操作
    public String Fi = "";          //目的寄存器号
    public String Fj = "",Fk = "";  //源寄存器号
    public String Qj = "",Qk = "";  //产生源寄存器数Fj,Fk的功能部件
    public String Rj = "",Rk = "";  //标识操作数Fj,Fk是否就绪的标志
    public String functionalUnitName = "";
    public int time = -1;           // 运算部件剩余时间占用时间默认为-1
    public int delay = 0;
    public int type = 0;

    public FunctionalUnit(String fuName) { //constructor sets name on creation
		functionalUnitName = fuName; //allows for dynamic extension of this class
    }

    public FunctionalUnit(){}

    public void resetValues(){
        this.Fi = this.Fj = this.Fk = this.Qj = this.Qk = this.Rj = this.Rk = this.Op = "";
        this.Busy = false;
	    this.time = -1;
    }

    @Override
    public String toString(){
        String ret = "";
	if (this.time == -1) {ret += " ";}
	else { ret += Integer.toString(this.time);}
        ret += this.time + " ";
        ret += this.functionalUnitName + this.spaces(8-this.functionalUnitName.length())+"| ";
        ret += this.Op + " ";
        ret += this.Fi + " ";
        ret += this.Fj + " ";
        ret += this.Fk + " ";
        ret += this.Qj + " ";
        ret += this.Qk + " ";
        ret += this.Rj + " ";
        ret += this.Rk + " ";
        return ret;
    }

    public ArrayList toArray(){
        ArrayList a = new ArrayList(11);
	    if (this.time == -1) {
	        a.add(0,"");
	    }
	    else {
	        a.add(0,Integer.toString(this.time));
	    }
	    a.add(1,this.functionalUnitName);
	    if(this.Busy){
	        a.add(2,"Yes");
	    } else {
	        a.add(2,"No");
	    }
	    a.add(3,this.Op);
	    a.add(4,this.Fi);
	    a.add(5,this.Fj);
	    a.add(6,this.Fk);
	    a.add(7,this.Qj);
	    a.add(8,this.Qk);
	    a.add(9,this.Rj);
	    a.add(10,this.Rk);
	    return a;
    }

    public String spaces(int n){
        String s = "";
        for(int i=n; i>0; i--){s+=" ";}
        return s;
    }
}
