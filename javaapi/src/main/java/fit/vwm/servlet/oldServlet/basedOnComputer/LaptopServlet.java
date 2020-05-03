package fit.vwm.servlet.oldServlet.basedOnComputer;

import fit.vwm.servlet.oldServlet.master.MasterSpecialComputerServlet;

import javax.servlet.annotation.WebServlet;

@WebServlet(name = "LaptopServlet",
        urlPatterns = "/computer/laptop/*")
public class LaptopServlet extends MasterSpecialComputerServlet {
    public LaptopServlet() {
        super("Computer", "Laptop");
    }
}
