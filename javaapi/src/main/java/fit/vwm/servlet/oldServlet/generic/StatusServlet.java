package fit.vwm.servlet.oldServlet.generic;
import fit.vwm.servlet.oldServlet.master.MasterGenericServlet;
import javax.servlet.annotation.WebServlet;

@WebServlet(name = "StatusServlet",
        urlPatterns = "/status/*")

public class StatusServlet extends MasterGenericServlet {

    public StatusServlet(){
        super("Status");
    }

}

