import java.io.*;


public class Project {
    static String file = "data.in";    //source file:data.in, data2.in, dataPatterns.in

    public static void main(String[] args) throws Exception {
        int inFileSize = 0;  //use this variable to persist file size once calculated
        try {
            FileInputStream fstream = new FileInputStream(Project.file); //open file

            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;

            //determine the line count of the file, i.e. # of instructions
            while ((strLine = br.readLine()) != null) { //Read File A Line At a Time
                if (!strLine.equals("")) {
                    inFileSize++;
                }
            }

            in.close();//Close the input stream

        } catch (Exception e) {//Catch file exceptions
            System.err.println("Error: " + e.getMessage());
        }

        Instruction[] instructions = new Instruction[inFileSize];//initialize an array of instructions w/ size found above
        int instrIndex = 0;

        try {
            FileInputStream fstream = new FileInputStream(Project.file); //again, open the file
            DataInputStream in = new DataInputStream(fstream); //get respective stream
            BufferedReader br = new BufferedReader(new InputStreamReader(in)); //init buffer w/ stream

            String strLine;
            //≥ı ºªØ÷∏¡Ó
            while ((strLine = br.readLine()) != null) {//Read file a line at a time
                if (!strLine.equals("")) { //only read non-blank lines
                    instructions[instrIndex] = new Instruction(strLine);    //create an Instruciton object from string
                    instrIndex++;   //increment the instruction object counter
                }
            }   //end while, file has been completely read

            in.close();//Close the input stream

        } catch (Exception e) {//Catch exception if any
            System.err.println("Error: " + e.toString() + e.getMessage());
            System.exit(-1);
        }

        //Display all new to be instructions
        for (int ii = 0; ii < instructions.length; ii++) {
            System.out.println(instructions[ii]);
        }

        Scoreboard s = new Scoreboard(instructions);//create Scoreboard with the instructions parsed
        s.display();    //display the scoreboard obj in a Swing window
    }
}
