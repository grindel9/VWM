package fit.vwm.servlet.generic;
import fit.vwm.servlet.master.MasterGenericServlet;

import javax.servlet.annotation.WebServlet;

@WebServlet(name = "ComputerTypeServlet",
        urlPatterns = "/computertype/*")

public class ComputerTypeServlet extends MasterGenericServlet {
    public ComputerTypeServlet(){
        super("ComputerType");
    }
}

