package fit.vwm.servlet.basedOnComputer;

import fit.vwm.servlet.master.MasterSpecialComputerServlet;

import javax.servlet.annotation.WebServlet;

@WebServlet(name = "DesktopServlet",
        urlPatterns = "/computer/desktop/*")
public class DesktopServlet extends MasterSpecialComputerServlet {
    public DesktopServlet() {
        super("Computer", "Desktop");
    }
}
