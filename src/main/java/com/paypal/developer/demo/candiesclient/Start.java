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
        
        if (args.length == 1)  {
            if (args[0] != null)    {
                cc  = new CandiesClient(args[0]);
            }   
            
        }   else if (args.length == 2)  {
            if (args[0] != null && args[1] != null)    {
                cc  = new CandiesClient(args[0], args[1]);
            }   
            
        }   else if (args.length == 4)  {
            if (args[0] != null && args[1] != null && args[2] != null && args[3] != null)    {
                cc  = new CandiesClient(args[0], args[1], args[2], args[3]);
            }   
            
        }   else    {
            cc = new CandiesClient();
        }
    }
}
