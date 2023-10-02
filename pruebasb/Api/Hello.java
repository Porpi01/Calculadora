package net.ausiasmarch.pruebasb.Api;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import net.ausiasmarch.pruebasb.bean.Calculadora;
import net.ausiasmarch.pruebasb.bean.CalculadoraResponse;
import net.ausiasmarch.pruebasb.bean.Operaciones;
import net.ausiasmarch.pruebasb.bean.ResponseBean;
import net.ausiasmarch.pruebasb.bean.UserBean;

@RestController
@RequestMapping("/hello")

public class Hello {

    @Autowired
    private HttpServletRequest oRequest;

    @GetMapping(value = "/", produces = "application/json")
    public ResponseBean hello() {
        ResponseBean oResponseBean = new ResponseBean();
        oResponseBean.setMessage("Hello World");
        oResponseBean.setCode(10);
        return oResponseBean;
    }

    @PostMapping(value = "/login", produces = "application/json")
    public ResponseBean helloPost(@RequestBody UserBean oUserBean) {
        if (oUserBean.getUsername().equals("monica") && oUserBean.getPassword().equals("1234")) {
            oRequest.getSession().setAttribute("daw", oUserBean);
            ResponseBean oResponseBean = new ResponseBean();
            oResponseBean.setMessage("Bienvenido");
            oResponseBean.setCode(10);
            return oResponseBean;
        } else {
            ResponseBean oResponseBean = new ResponseBean();
            oResponseBean.setMessage("Error, no se pudo autenticar");
            oResponseBean.setCode(20);
            return oResponseBean;
        }
    }

    @GetMapping(value = "/check", produces = "application/json")
    public ResponseBean check() {
        UserBean oUserBean = (UserBean) oRequest.getSession().getAttribute("daw");

        if (oUserBean != null) {
            ResponseBean oResponseBean = new ResponseBean();
            oResponseBean.setMessage("Usuario autenticado: " + oUserBean.getUsername());
            oResponseBean.setCode(10);
            return oResponseBean;
        } else {
            ResponseBean oResponseBean = new ResponseBean();
            oResponseBean.setMessage("Usuario no autenticado");
            oResponseBean.setCode(20);
            return oResponseBean;
        }
    }

    @DeleteMapping(value = "/logout", produces = "application/json")
    public ResponseBean logout() {
        oRequest.getSession().removeAttribute("daw");

        ResponseBean oResponseBean = new ResponseBean();
        oResponseBean.setMessage("Usuario desconectado");
        oResponseBean.setCode(10);
        return oResponseBean;
    }

    private Operaciones operar = new Operaciones();
    @PostMapping(value = "/calcular", produces = "application/json", consumes = "application/json")
    public CalculadoraResponse calcular(@RequestBody Calculadora request) {
        String operacion = request.getOperador();
        double resultado = 0.0;
        String mensaje = "";
        int codigo = 0;
    
        // Verificar si el usuario está autenticado
        UserBean oUserBean = (UserBean) oRequest.getSession().getAttribute("daw");
        if (oUserBean == null) {
            mensaje = "Acceso denegado. Usuario no autenticado";
            codigo = 20;
            return new CalculadoraResponse(resultado, mensaje, codigo);
        }
    
        switch (operacion.toLowerCase()) {
            case "sumar":
                resultado = operar.sumar(request.getOperando1(), request.getOperando2());
                mensaje = "Suma realizada con éxito";
                codigo = 10;
                break;
            case "restar":
                resultado = operar.restar(request.getOperando1(), request.getOperando2());
                mensaje = "Resta realizada con éxito";
                codigo = 10;
                break;
            case "multiplicar":
                resultado = operar.multiplicar(request.getOperando1(), request.getOperando2());
                mensaje = "Multiplicación realizada con éxito";
                codigo = 10;
                break;
            case "dividir":
                try {
                    resultado = operar.dividir(request.getOperando1(), request.getOperando2());
                    mensaje = "División realizada con éxito";
                    codigo = 10;
                } catch (ArithmeticException e) {
                    mensaje = "No se puede dividir por 0";
                    codigo = 20;
                }
                break;
            default:
                mensaje = "Operación no válida";
                codigo = 20;
                break;
        }
    
        return new CalculadoraResponse(resultado, mensaje, codigo);
    }
}