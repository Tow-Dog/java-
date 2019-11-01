import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;
import java.util.List;

/**
 *
 * @author Matthew Kelly
 */
public class Scoreboard implements ActionListener, Cloneable {
   FunctionalUnit [] fus;   //the functional units available to the system
   Instruction [] instructions; //the instructions in the text file stored as an array
   ArrayList<Register> registers = new ArrayList<Register>();
   ArrayList<Integer> jReads = new ArrayList<Integer>();
   ArrayList<Integer> kReads = new ArrayList<Integer>();
   static final int INSTRUCTIONS_VISIBLE_AT_A_TIME = 7; //determines when the UI should scroll, this value is in the specs
   static final String LABEL_TABLE1 = "Instruction status:";     //labels for the GUI
   static final String LABEL_TABLE2 = "Functional unit status:";
   static final String LABEL_TABLE3 = "Register result status:";

   JFrame frame;  //the UI element on which the other elements will be attached
   int clock;     //the current clock cycle of the system

   public Scoreboard() throws Exception {
       throw new Exception("A scoreboard must be implemented with Instruction[] parameters");
   }

   public Scoreboard(Instruction [] iIn){
        clock = 0; //initialize the clock
        int unitNum= DelayAndFunctionalUnit.ADD_UNIT.getUnitNum()
                +DelayAndFunctionalUnit.DIVIDE_UNIT.getUnitNum()
                +DelayAndFunctionalUnit.MULTI_UNIT.getUnitNum()
                +DelayAndFunctionalUnit.INTEGER_UNIT.getUnitNum();
        fus = new FunctionalUnit[unitNum]; //setup the function unit array for proceeding fu types

        //per the project specification, the below set of functional units is available
       int fusNum = 0;
        for (int i = 0; i<DelayAndFunctionalUnit.INTEGER_UNIT.getUnitNum();i++){
            IntegerFunctionalUnit integer = new IntegerFunctionalUnit("Integer "+(i+1));
            fus[fusNum++] = integer;
        }
       for (int i = 0; i<DelayAndFunctionalUnit.ADD_UNIT.getUnitNum();i++){
           AddFunctionalUnit adder = new AddFunctionalUnit("Add "+(i+1));
           fus[fusNum++] = adder;
       }
       for (int i = 0; i<DelayAndFunctionalUnit.MULTI_UNIT.getUnitNum();i++){
           MultiplyFunctionalUnit multi = new MultiplyFunctionalUnit("Multi "+(i+1));
           fus[fusNum++] = multi;
       }
       for (int i = 0; i<DelayAndFunctionalUnit.DIVIDE_UNIT.getUnitNum();i++){
           DivideFunctionalUnit divide = new DivideFunctionalUnit("Divide "+(i+1));
           fus[fusNum++] = divide;
       }
        instructions = iIn; //associate the instructions passed in with the instance

        determineRegistersUsedInInstructions();
   }

   public void display(){

       frame = new JFrame();    
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       JTable table1 = this.displayInstructionsStatus(this.instructions);//instruction statuses
       JTable table2 = this.displayFunctionalUnitStatus(); //functional unit statuses
       JTable table3 = this.displayRegisterResultStatus(); //register result status


      //multi-line headers, allows Java Swing to display multiple lines in a header
        MultiLineHeaderRenderer renderer = new MultiLineHeaderRenderer();
        Enumeration enume = table1.getColumnModel().getColumns();
        while (enume.hasMoreElements()) {
          ((TableColumn)enume.nextElement()).setHeaderRenderer(renderer);
        }
        Enumeration enume2 = table2.getColumnModel().getColumns();
        while (enume2.hasMoreElements()) {            
          ((TableColumn)enume2.nextElement()).setHeaderRenderer(renderer);
        }
      // *************

        Button incClock = new Button("> Increment Clock");  //init UI element to advance clock
        incClock.addActionListener(this);                   //attach event to this UI element
        Container c = this.frame.getContentPane();          //get UI pane to which UI elements will be added
        c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));    //allow everything to be automatically aligned

        // ADD TABLE1 to UI
        c.add(new Label(Scoreboard.LABEL_TABLE1));            //top instructions table
        c.add(table1.getTableHeader());

        JScrollPane scrollableInstructions = new JScrollPane(table1);

        //show 7 instructions at a time. The header is 2.5 rows, 16px is height of a row
        scrollableInstructions.setPreferredSize(new Dimension(scrollableInstructions.getPreferredSize().width,(int)(16.0*9.5)));

        //Assure that the most recently modified instruction is visible
        int scrollIncrementsNeeded=0;   //increment this with each instruction that is not visible
        for(int instruction=Scoreboard.INSTRUCTIONS_VISIBLE_AT_A_TIME; instruction<this.instructions.length; instruction++){
            if(this.instructions[instruction-1].issue > 0){ //count the number of instructions already issued to use as a
                scrollIncrementsNeeded++;                   // basis for how far down to scroll
            }
        }

        //dynamically show the instruction with the most recent activity
        JViewport jvp = scrollableInstructions.getViewport();   //get window contents
        Point p = jvp.getViewPosition();                        //get current display position of scrolling pane
        p.setLocation(p.x, p.y+table1.getRowHeight()*scrollIncrementsNeeded);   //revise the vertical position of the scrolling pane point element
        jvp.setViewPosition(p);                                 //apply the modified view point to the window contents
        scrollableInstructions.setViewport(jvp);                //set the modified viewport to the scrollable pane
   //End of visibility assurance code

        c.add(scrollableInstructions);//add the instructions table with modified viewport coords to the view

        // ADD TABLE2 to UI
        c.add(new Label(Scoreboard.LABEL_TABLE2)); 
        c.add(new Label(Scoreboard.LABEL_TABLE2));  //for some reason this isn't showing up unless added twice
        c.add(table2.getTableHeader());             //add the table's label as a component
        c.add(table2);                              //add table's contents to GUI

        // ADD TABLE3 to UI
        c.add(new Label(Scoreboard.LABEL_TABLE3));
        c.add(table3.getTableHeader()); //add the table's header as a component
        c.add(table3);                  //add table's contents to GUI
        c.add(incClock);                //add button to GUI
        this.frame.pack();              //condense everything for alignment

        this.frame.setVisible(true);
   }

   public void hide(){
     this.frame.setVisible(false); //hides the GUI from the user
   }

   public void show(){
     this.frame.setVisible(true); //shows the GUI to the user
   }

   public JTable displayInstructionsStatus(Instruction [] instr){
       String [] columnHeaders = {"Instructions","Issue","Read\nOper","Exec\nComp","Write\nResult"};

       Object [][] data = new Object[instr.length][8];

       //set the values in the GUI for each instruction's stage
       for(int ii=0; ii<instr.length; ii++){
           data[ii][0] = instr[ii].rawCommand;
           if(instr[ii].issue == 0)     {data[ii][1] = "";} else{data[ii][1] = instr[ii].issue;}
           if(instr[ii].readOper == 0)  {data[ii][2] = "";} else{data[ii][2] = instr[ii].readOper;}
           if(instr[ii].execComp == 0)  {data[ii][3] = "";} else{data[ii][3] = instr[ii].execComp;}
           if(instr[ii].writeResult == 0){data[ii][4] = "";}else{data[ii][4] = instr[ii].writeResult;}
       }
       JTable table = new JTable(data,columnHeaders);
       return table;
   }

   public JTable displayFunctionalUnitStatus(){
       
        String [] columnHeaders = {"Time","Name","Busy","Op","dest\nFi","S1\nFj",
              "S2\nFk","FU\nQj","FU\nQk","Fj\nRj","Fk\nRk"
        };

        Object[][]data = new Object[this.fus.length][11];
        //for each functional unit
        for(int i = 0; i < this.fus.length; i++){
            //fetch arraylist version of function unit's data
            Object [] d = fus[i].toArray().toArray();
            for(int di=0; di<d.length; di++){
                data[i][di] = d[di];
            }
        }

        JTable table = new JTable(data,columnHeaders);
        return table;
   }

   public JTable displayRegisterResultStatus(){
        //Dynamically create table headers based on register usage
        String [] columnHeaders = new String[registers.size()+2];
        columnHeaders[0] = "Clock"; 
	columnHeaders[1] = "";
        for(int ii=0, chi=2; chi<columnHeaders.length; chi++, ii++){
            columnHeaders[chi] = registers.get(ii).name;
        }

        Object[][]data = new Object[1][columnHeaders.length];

        //reset all values in table
        data[0][0] = Integer.toString(this.clock);
        data[0][1] = "FU";  //functional unit title for register result table
        for(int ii=2; ii<columnHeaders.length; ii++){
            data[0][ii] = registers.get(ii-2).regFU; //add the registers' contents to the GUI
        }     
        
       JTable table = new JTable(data,columnHeaders); //create a SWING table object to pass to the GUI
       return table;
   }

   public void processInstructions() {  //instruction logic
	//First, clear the Operands freed by exiting instructions, so other instructions can get into a Read Stage
	for(int ji=0; ji<jReads.size(); ji++){
		fus[jReads.get(ji)].Rj="Yes";
	}
	jReads.clear();
	
	for(int ki=0; ki<kReads.size(); ki++){
		fus[kReads.get(ki)].Rk="Yes";
	}
	kReads.clear();

        //cycle through the instructions determining at what stage of execution they're in
	for(int instruction=0; instruction < instructions.length; instruction++) {
            if(instructions[instruction].issue == 0){ //the instruction has not been issued
                if ( checkIssue(instructions[instruction]) == true ){ //instruction is able to be issued
                    bookKeepIssue(instructions[instruction]);
                    break;  //found an issuable instruction, get out of instr FOR loop
		} else {
                    break; //found instruction that has not yet been issued but cannot currently be, escape out of FOR loop
		}
            }else if(instructions[instruction].readOper == 0){          //the instruction has not been read
                if ( checkRead(instructions[instruction]) == true ){    //instruction can be read
                    bookKeepRead(instructions[instruction]);
                    instructions[instruction].fuUsed.time = instructions[instruction].delay+1; //set the delay value of the functional unit based on the instruction that has secured it
		}
            }else if ( (instructions[instruction].delay > 0) && (instructions[instruction].execComp == 0 ) ){//the instruction either has a latency and/or has not been executed
                // Run an exec cycle, decrement the delay of typeOfFUUsed
                instructions[instruction].delay--; //decrement instruction's execution counter
                instructions[instruction].fuUsed.time--; //decrement the functional unit's counter to correspond to the instruction
	    }else if ((instructions[instruction].delay == 0) && (instructions[instruction].execComp == 0)){//instruction has no latency remaining and has not yet been executed
                    // This is the last one => just set the execComp to the time in bookKeepExec
                    bookKeepExec(instructions[instruction]);
                    instructions[instruction].fuUsed.time--; //decrement
	    }else if (instructions[instruction].writeResult == 0){ //instruction has not yet written the result
                    if ( checkWrite(instructions[instruction]) == true) { //check for memory aliasing
                        bookKeepWrite(instructions[instruction]);
                    }
		}
	}   //end for loop
   }

   boolean checkIssue(Instruction instr){
	boolean issueable = false;  //can the instruction be issued?
	boolean freefu = false; //is a functional unit of the type needed free?
	boolean wawhaz = false; //boolean for write-after-write hazard
	int possibleFUindex = 0;

	//See if there is a free register
	for (int i = 0; i < fus.length ; i++) {
            //determine if a functional unit of the right type is free and is not set to release in this clock cycle
            if ( (fus[i].type == instr.typeOfFUUsed) && ( fus[i].Busy == false) && (fus[i].releaseTime != this.clock) ) {
                freefu = true;          //specify that a functional unit of the right type has been found
                possibleFUindex = i;    //specify the index of the functional unit to be used in respect to the array of all FUs
                break;                  //we found a functional unit, so can break from the FOR loop
            }
	}

	//See if noone wants to write to the same place
	for (int j=0; j < instructions.length ; j++) {
            if ( (instructions[j].issue != 0 ) &&                           //if instruction has been issued
                 (instructions[j].dest().name.equals(instr.dest().name)) && // and destinations are the same
                 (instructions[j].writeResult == 0)) {                      // and result has not yet been written
                    wawhaz = true;                                          //there's a write after write hazard
            }
	}

	issueable = (freefu && !wawhaz); //determine whether the instruction should be issued
	
	if(issueable){
            instr.fuUsed = fus[possibleFUindex]; //secure a functional unit
	}

	return issueable;
   }

   boolean checkRead(Instruction instr){
	boolean readable = true;    //default to the reigsters being ready. Check below and set if not ready

	if (!instr.sourcej().name.equals("")) { //assure that the source register's name is not blank
            if (instr.fuUsed.Rj.equals("No")) {
                    readable = false;           //instruction cannot procede, as registers are not ready
            }
	}

	if (!instr.sourcek().name.equals("")) { //assure that the second source register's name is not blank
		if (instr.fuUsed.Rk.equals("No")) {
			readable = false;       //instruction cannot procede, as registers are not ready
		}
	}

	return readable;
   }

   boolean checkWrite(Instruction instr){//checks for memory aliasing
	boolean writable = true;
	for (int i = 0; i < instructions.length; i++) {
            // only check issued before the current
            if  ( (instructions[i].issue > 0 ) && (instructions[i].issue < instr.issue) && ( (instructions[i].readOper == 0) ||(instructions[i].readOper == this.clock ) )) {
                if (instructions[i].sourcej().name.equals(instr.dest().name)){writable = false;}
                if (instructions[i].sourcek().name.equals(instr.dest().name)){writable = false;}
                //TODO - check for those that had direct addressing of a STORE -> no name for the register
            }
	}
	return writable;
   }

   void bookKeepIssue(Instruction instr){
	instr.issue = this.clock; //set the instruction's issue value to the current clock
	instr.fuUsed.Busy = true; //assign the functional unit to prevent other instructions from using
	instr.fuUsed.Op = new String(instr.opcodeString); //tell the FU the operand name
	instr.fuUsed.Fi = new String(instr.dest().name); //tell the operand the first input register name
	instr.fuUsed.Fj = new String(instr.sourcej().name);//tell the operand the second input register name
	instr.fuUsed.Fk = new String(instr.sourcek().name);//tell the operand the third input register name

	if (!instr.sourcej().name.equals("")) {
		instr.fuUsed.Qj = findRegFU(instr.sourcej().name);
		// if no unit is responsible for this operand, then set ready
		if (instr.fuUsed.Qj.equals("") ) {
			instr.fuUsed.Rj = "Yes";
		} else {
			instr.fuUsed.Rj = "No";
		}
	} else {
		instr.fuUsed.Qj = "";
		instr.fuUsed.Rj = "";
	}

	if (!instr.sourcek().name.equals("")) {
		instr.fuUsed.Qk = findRegFU(instr.sourcek().name);
		// if no unit is responsible for this operand, then set ready
		if (instr.fuUsed.Qk.equals("") ) {
			instr.fuUsed.Rk = "Yes";
		} else {
			instr.fuUsed.Rk = "No";
		}
	} else {
		instr.fuUsed.Qk = "";
		instr.fuUsed.Rk = "";
	}

	setRegFU(instr.dest().name, instr.fuUsed.functionalUnitName);
   }

   void bookKeepRead(Instruction instr){
	instr.readOper = this.clock; //set the instruction's read value to the current clock value
	if (!instr.sourcej().name.equals("")) {
		instr.fuUsed.Rj="No";
	}
	if (!instr.sourcek().name.equals("")) {
		instr.fuUsed.Rk="No";
	}
	instr.fuUsed.Qj="";
	instr.fuUsed.Qk="";
   }

   void bookKeepExec(Instruction instr){		
	instr.execComp = this.clock; //set the instruction's exec value to the current clock value
   }

   void bookKeepWrite(Instruction instr){
	instr.writeResult = this.clock; //set the instruction's write value to the current clock value

	for(int fu=0; fu< fus.length; fu++ ) { //poll through all of the functional units
            if (fus[fu].Qj.equals(instr.fuUsed.functionalUnitName)) {
                    //fus[i].Rj="Yes";
                    jReads.add(fu);
            }
            if (fus[fu].Qk.equals(instr.fuUsed.functionalUnitName)) {
                    //fus[i].Rk="Yes";
                    kReads.add(fu);
            }
	}
	instr.fuUsed.releaseTime = this.clock;
	instr.fuUsed.resetValues();
	clearRegFU(instr.dest().name);
   }

   void clearRegFU(String regName) {
       Iterator<Register> itr = registers.iterator(); //create iterator to allow polling through registers

       while (itr.hasNext()) {  //as long as there are more registers,
             Register tReg = itr.next();    //get the next register
            if (tReg.name.equals(regName)) { //if the functional unit is equivalent to the fu's name,
                      tReg.regFU="";        // then clear the register's functional unit value
            }
       }
   }

   String findRegFU(String regName) {
        String regFUResult = "";
        Iterator<Register> itr = registers.iterator(); //create a means to poll through registers

        while (itr.hasNext()) {
             Register tReg = itr.next();
            if (tReg.name.equals(regName)) {
                    // TODO, make sure this is the right way
                      regFUResult = tReg.regFU;
            }
        }

	return regFUResult;
   }

   void setRegFU(String regName, String fuName) {
           Iterator<Register> itr = registers.iterator();

           while (itr.hasNext()) {
                 Register tReg = itr.next();
                if (tReg.name.equals(regName)) {
			tReg.regFU = fuName;
                }
           }
   }

   public void incrementClock() {
       this.clock++;        // increment clock       
       this.processInstructions();//call to logic, perform any actions appropriate for active instructions
       this.hide(); //destroy/hide the current GUI
       this.display();	  //show the current state of the scoreboard
   }

    @Override
   public String toString(){
       String ret = "";             
       for(int fs = 0; fs < this.fus.length; fs++){
           ret += fus[fs] + "\r\n";
       }    
       return ret;
   }

    @Override
   public void actionPerformed(ActionEvent e) { //performed when UI button is pused
        if(!checkDone()) {
             this.incrementClock();
        }
    }

    public void determineRegistersUsedInInstructions() {
        //scans through all of the instructions and adds each register op to scoreboard's register array
        for(int instruction=0; instruction<this.instructions.length; instruction++){
            if (!this.instructions[instruction].dest().name.equals("")) {
                Register tempReg = new Register(this.instructions[instruction].dest().name);
                if (registers.size() == 0) {
                    registers.add(tempReg);
	        } else {
                    boolean in = false;
                    Iterator<Register> itr = registers.iterator();

                    while (itr.hasNext()) {
                        Register tReg = itr.next();
                        if (tReg.name.equals(tempReg.name)) {in = true;}
                    }

                    if (!in) {registers.add(tempReg);}
               }    //end else
            }//end if
	}   //end for
	Collections.sort(registers);
    }


    public boolean checkDone () {
        //scans through all of the instructions and reads the writeResult attribute to determine whether
        // all of the instruction are completed
	int totalDone = 0;      //number of instructions completed, increments each time on is found
	boolean done = true;    //set to false if all instructions have not completed

        for (int instruction=0; instruction < instructions.length; instruction++) {//scan through all instructions
            if ( instructions[instruction].writeResult != 0 ) {totalDone++;}       //increment if the instruction is done
        }
	if (totalDone != instructions.length) {done = false;}   //check to see if all instructions are done, set flag if so
		
	return done;
    }
}
