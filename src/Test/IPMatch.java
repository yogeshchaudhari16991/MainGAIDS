/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

/**
 *
 * @author admin
 */
public class IPMatch {
    public static int popLimit=4;
    
    public IPMatch()
    {
        String[][] parent={{"C0A800B0" , "C0A80064" , "5959" , "4DFB"},{"C0A8003E" , "C0A8000B" , "CA0F" , "A530"},
        {"C0A800F5" , "C0A80089" , "49F3" , "A135"},{"C0A80063" , "C0A8009A" , "279D" , "3878"}};
    
        String[][] a=newChild(parent);
    }
    
    
    //Starting Half of the population in i  
    //and next half in j
    
    //if population is 100 then i={0,49} and j={50,99}
    
    
    
    
    
    private static String[][] newChild(String[][] parents) {
        String[][] child = new String[popLimit][4];
        int i, j;
        for (i = 0, j = popLimit / 2; i < (popLimit / 2) || j < (popLimit); i++, j++) {

            System.out.println("i : "+i);
            System.out.println("j : "+j);
            child[i][0] = parents[j][1];
            child[i][1] = parents[j][0];
            child[i][2] = parents[i][2];
            child[i][3] = parents[i][3];
            System.out.println(i + " : {" + child[i][0] + "," + child[i][1] + "," + child[i][2] + "," + child[i][3] + "}");
            child[j][0] = parents[i][1];
            child[j][1] = parents[i][0];
            child[j][2] = parents[j][2];
            child[j][3] = parents[j][3];
            System.out.println(j + " : {" + child[j][0] + "," + child[j][1] + "," + child[j][2] + "," + child[j][3] + "}");
        }
        return child;
    }
    
    public static void main(String[] args)
    {
        new IPMatch();
    }
    }
