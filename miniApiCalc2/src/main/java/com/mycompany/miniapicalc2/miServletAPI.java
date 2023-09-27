/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.miniapicalc2;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author USER
 */
public class miServletAPI extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        try {
            ResponseBean oRB = gson.fromJson(request.getReader(), ResponseBean.class);

            // Realizar operaciones
            double resultado = 0;
            switch (oRB.getOperador().toLowerCase()) {
                case "suma":
                    resultado = oRB.getNumero1() + oRB.getNumero2();
                    break;
                case "resta":
                    resultado = oRB.getNumero1() - oRB.getNumero2();
                    break;
                case "multiplicacion":
                    resultado = oRB.getNumero1() * oRB.getNumero2();
                    break;
                case "division":

                    resultado = oRB.getNumero1() / oRB.getNumero2();

                    break;

            }

            // Establecer el resultado en el objeto de respuesta
            oRB.setResultado(resultado);
            response.setStatus(200); // Código de estado 200 para éxito
            out.print(gson.toJson(oRB));
        } catch (JsonIOException e) {
            response.setStatus(500); // Código de estado 500 para error interno del servidor
            ResponseBean oRB = new ResponseBean();
            oRB.setErrorDescription("Error en JSON 1");
            out.print(gson.toJson(oRB));
        } catch (IOException e) {
            response.setStatus(500); // Código de estado 500 para error interno del servidor
            ResponseBean oRB = new ResponseBean();
            oRB.setErrorDescription("Error en JSON 2");
            out.print(gson.toJson(oRB));
        } catch (JsonSyntaxException e) {
            response.setStatus(500); // Código de estado 500 para error interno del servidor
            ResponseBean oRB = new ResponseBean();
            oRB.setErrorDescription("Error en JSON 3");
            out.print(gson.toJson(oRB));
        }
    }

  
}
