package fit.vwm.servlet.generic;
import fit.vwm.servlet.master.MasterServlet;
import javax.servlet.annotation.WebServlet;

@WebServlet(name = "StatusServlet",
        urlPatterns = "/status/*")

public class StatusServlet extends MasterServlet {

    public StatusServlet(){
        super("Status");
    }

}

