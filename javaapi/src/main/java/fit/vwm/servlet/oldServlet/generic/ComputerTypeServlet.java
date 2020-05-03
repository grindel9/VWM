package fit.vwm.servlet.oldServlet.generic;
import fit.vwm.servlet.oldServlet.master.MasterGenericServlet;

import javax.servlet.annotation.WebServlet;

@WebServlet(name = "ComputerTypeServlet",
        urlPatterns = "/computertype/*")

public class ComputerTypeServlet extends MasterGenericServlet {
    public ComputerTypeServlet(){
        super("ComputerType");
    }
}

