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
public class miServletCalculadora extends HttpServlet {

    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String operation = request.getParameter("op");

        if ("calcula".equals(operation)) {
            String username = (String) request.getSession().getAttribute("alumnadodaw");
            if (username != null) {
                ResponseBeanSession sessionResponse = new ResponseBeanSession();
                sessionResponse.setErrorMsg("No has iniciado sesión");
                response.setStatus(401);
                out.print(gson.toJson(sessionResponse));
            } else {
                try {
                    ResponseBean oRB = gson.fromJson(request.getReader(), ResponseBean.class);
                    double resultado = realizarCalculo(oRB);
                    oRB.setResultado(resultado);
                    response.setStatus(200);
                    out.print(gson.toJson(oRB));
                } catch (JsonSyntaxException e) {
                    response.setStatus(400);
                    ResponseBean errorResponse = new ResponseBean();
                    errorResponse.setErrorDescription("Error en el formato de la solicitud JSON");
                    out.print(gson.toJson(errorResponse));
                }
            }
        } else if ("login".equals(operation)) {
            try {
                ResponseBeanSession sessionRequest = gson.fromJson(request.getReader(), ResponseBeanSession.class);
                if (sessionRequest.getUsername().equals("alumnadodaw") && sessionRequest.getPassword().equals("72b37a5cce60840d1392a19392165d1e8531e4e0b6bbeb122588e73a20024ebd")) {
                    request.getSession().setAttribute("alumnadodaw", sessionRequest.getUsername());
                    ResponseBeanSession oRBSession1 = new ResponseBeanSession();
                    oRBSession1.setUsername(sessionRequest.getUsername());
                    oRBSession1.setPassword("");
                    response.setStatus(200);
                    out.print(gson.toJson(oRBSession1));
                } else {
                    ResponseBeanSession oRBSession1 = new ResponseBeanSession();
                    oRBSession1.setUsername(sessionRequest.getUsername());
                    oRBSession1.setPassword("");
                    oRBSession1.setErrorMsg("error en la autenticacion");
                    response.setStatus(500);
                    out.print(gson.toJson(oRBSession1));

                }
            } catch (JsonSyntaxException e) {
                response.setStatus(400);
                ResponseBean errorResponse = new ResponseBean();
                errorResponse.setErrorDescription("Error en el formato de la solicitud JSON");
                out.print(gson.toJson(errorResponse));
            }
        } else if ("logout".equals(operation)) {
            request.getSession().invalidate();
            response.setStatus(200);
        } else if ("check".equals(operation)) {
            String username = (String) request.getSession().getAttribute("alumnadodaw");
            if (username == null) {
                ResponseBeanSession sessionResponse = new ResponseBeanSession();
                sessionResponse.setUsername(username);
                response.setStatus(200);
                out.print(gson.toJson(sessionResponse));
            } else {
                ResponseBeanSession sessionResponse = new ResponseBeanSession();
                sessionResponse.setErrorMsg("No hay sesión");
                response.setStatus(401);
                out.print(gson.toJson(sessionResponse));
            }
        } else {
            response.setStatus(400);
            ResponseBean errorResponse = new ResponseBean();
            errorResponse.setErrorDescription("Operación no válida");
            out.print(gson.toJson(errorResponse));
        }
    }

    private double realizarCalculo(ResponseBean oRB) {
        double resultado = 0.0;

        switch (oRB.getOperador().toLowerCase()) {
            case "suma":
                resultado = oRB.getOperando1() + oRB.getOperando2();
                break;
            case "resta":
                resultado = oRB.getOperando1() - oRB.getOperando2();
                break;
            case "multiplicacion":
                resultado = oRB.getOperando1() * oRB.getOperando2();
                break;
            case "division":
                if (oRB.getOperando2() != 0) {
                    resultado = oRB.getOperando1() / oRB.getOperando2();
                } else {
                    oRB.setErrorDescription("No se puede hacer una división entre 0.");
                }
                break;
        }

        return resultado;
    }

    private boolean esNumero(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
