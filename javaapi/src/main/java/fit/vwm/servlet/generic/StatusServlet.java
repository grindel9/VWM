package fit.vwm.servlet.generic;
import fit.vwm.servlet.master.MasterGenericServlet;
import javax.servlet.annotation.WebServlet;

@WebServlet(name = "StatusServlet",
        urlPatterns = "/status/*")

public class StatusServlet extends MasterGenericServlet {

    public StatusServlet(){
        super("Status");
    }

}

