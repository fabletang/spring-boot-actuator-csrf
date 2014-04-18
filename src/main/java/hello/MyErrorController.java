package hello;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;

import org.springframework.boot.actuate.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestAttributes;

@Controller
public class MyErrorController implements ErrorController {
	
    @RequestMapping(value = "/error", produces = "text/html")
    public String error() {
        return "error";
    }

    @Override
    public String getErrorPath() {
        return "error";
    }

    @Override
    public Map<String, Object> extract(RequestAttributes attributes, boolean trace, boolean log) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("timestamp", new Date());
        try {
            Throwable error = (Throwable) attributes.getAttribute(
                    "javax.servlet.error.exception", RequestAttributes.SCOPE_REQUEST);
            Object obj = attributes.getAttribute("javax.servlet.error.status_code",
                    RequestAttributes.SCOPE_REQUEST);
            int status = 999;
            if (obj != null) {
                status = (Integer) obj;
                map.put("error", HttpStatus.valueOf(status).getReasonPhrase());
            }
            else {
                map.put("error", "None");
            }
            map.put("status", status);
            if (error != null) {
                while (error instanceof ServletException && error.getCause() != null) {
                    error = ((ServletException) error).getCause();
                }
                map.put("exception", error.getClass().getName());
                map.put("message", error.getMessage());
                if (trace) {
                    StringWriter stackTrace = new StringWriter();
                    error.printStackTrace(new PrintWriter(stackTrace));
                    stackTrace.flush();
                    map.put("trace", stackTrace.toString());
                }
                if (log) {
                    System.out.println(error.getMessage());
                }
            }
            else {
                Object message = attributes.getAttribute("javax.servlet.error.message",
                        RequestAttributes.SCOPE_REQUEST);
                map.put("message", message == null ? "No message available" : message);
            }
            return map;
        }
        catch (Exception ex) {
            map.put("error", ex.getClass().getName());
            map.put("message", ex.getMessage());
            if (log) {
            	System.out.println(ex.getMessage());
            }
            return map;
        }
    }
}
