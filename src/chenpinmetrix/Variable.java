/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chenpinmetrix;

/**
 *
 * @author Evgeny
 */
public class Variable {
    public String name;
    public boolean input;
    public boolean modifiable;
    public boolean managing;
    public boolean parasitic;
    
    public int depthLevel;
    
    public boolean active;  
    
    public Variable(String name, int depthLevel) {
        this.name = name;
        this.depthLevel = depthLevel;
        input = false;
        modifiable = false;
        managing = false;
        parasitic = true;
        active = true;
    }
}
