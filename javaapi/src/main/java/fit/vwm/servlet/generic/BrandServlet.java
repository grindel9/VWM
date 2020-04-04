package fit.vwm.servlet.generic;

import fit.vwm.servlet.master.MasterGenericServlet;
import javax.servlet.annotation.WebServlet;

@WebServlet(name = "BrandServlet",
        urlPatterns = "/brand/*")

public class BrandServlet extends MasterGenericServlet {
    public BrandServlet(){
        super("Brand");
    }
}

