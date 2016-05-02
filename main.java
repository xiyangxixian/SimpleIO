/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.io.Serializable;

/**
 *
 * @author yangming
 */
public class main {
    public static void main(String[] args) {
        SimpleIO sio=new SimpleIO("test.txt");
        sio.write("hello,world");
        String str=sio.readAsString();
        System.err.println(str);
    }
}
