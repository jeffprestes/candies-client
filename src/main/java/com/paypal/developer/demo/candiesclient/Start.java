/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.paypal.developer.demo.candiesclient;

/**
 *
 * @author jeffprestes
 */
public class Start {
    
    public static void main(String[] args)  {
        
        CandiesClient cc;
        
        if (args.length>0)  {
            if (args[0] != null)    {
                cc  = new CandiesClient(args[0]);
            }   
        }   else    {
            cc = new CandiesClient();
        }
                
    }
    
}
