package fit.vwm.servlet.generic;
import fit.vwm.servlet.master.MasterServlet;

import javax.servlet.annotation.WebServlet;

@WebServlet(name = "ComputerTypeServlet",
        urlPatterns = "/computertype/*")

public class ComputerTypeServlet extends MasterServlet {
    public ComputerTypeServlet(){
        super("ComputerType");
    }
}

