package fit.vwm.servlet.oldServlet.generic;

import fit.vwm.servlet.oldServlet.master.MasterGenericServlet;
import javax.servlet.annotation.WebServlet;

@WebServlet(name = "CountryServlet",
        urlPatterns = "/country/*")

public class CountryServlet extends MasterGenericServlet {
    public CountryServlet(){
        super("Country");
    }

}

