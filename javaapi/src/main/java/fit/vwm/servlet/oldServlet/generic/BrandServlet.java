package fit.vwm.servlet.oldServlet.generic;

import fit.vwm.servlet.oldServlet.master.MasterGenericServlet;
import javax.servlet.annotation.WebServlet;

@WebServlet(name = "BrandServlet",
        urlPatterns = "/brand/*")

public class BrandServlet extends MasterGenericServlet {
    public BrandServlet(){
        super("Brand");
    }
}

