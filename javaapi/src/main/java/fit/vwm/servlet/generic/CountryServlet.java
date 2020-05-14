package fit.vwm.servlet.generic;

import fit.vwm.servlet.master.MasterGenericServlet;
import javax.servlet.annotation.WebServlet;

@WebServlet(name = "CountryServlet",
        urlPatterns = "/country/*")

public class CountryServlet extends MasterGenericServlet {
    public CountryServlet(){
        super("Country");
    }

}

