package fit.vwm.servlet.generic;

import fit.vwm.servlet.master.MasterServlet;
import javax.servlet.annotation.WebServlet;

@WebServlet(name = "BrandServlet",
        urlPatterns = "/brand/*")

public class BrandServlet extends MasterServlet {
    public BrandServlet(){
        super("Brand");
    }
}

