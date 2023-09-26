/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.miniapicalc;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class miServletCalculadora extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        
        try (PrintWriter out = response.getWriter()) {
         
           
            Gson gson = new Gson();
            Operacion op = gson.fromJson(request.getReader(), Operacion.class);
          
             switch (op.getOperador()) {
                    case "suma":
                        op.setResultado( op.getNumero1() + op.getNumero2());
                        break;
                    case "resta":
                       op.setResultado( op.getNumero1() - op.getNumero2());
                        break;
                    case "multiplicacion":
                       op.setResultado(  op.getNumero1() * op.getNumero2());
                        break;
                    case "division":
                        if (op.getNumero2() != 0) {
                            op.setResultado( op.getNumero1() / op.getNumero2());
                        } else {
                            out.println("Error: Division por cero");
                            return; 
                        }
                        break;
                        default:
                        out.println("Error: Operacio no valida");
                        return; 
             
            }
            out.println(gson.toJson(op));
            
            
        }
    }

 
}
