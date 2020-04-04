package fit.vwm.servlet.generic;

import fit.vwm.servlet.master.MasterServlet;
import javax.servlet.annotation.WebServlet;

@WebServlet(name = "CountryServlet",
        urlPatterns = "/country/*")

public class CountryServlet extends MasterServlet {
    public CountryServlet(){
        super("Country");
    }

}

