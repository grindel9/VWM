package fit.vwm.servlet.oldServlet.basedOnComputer;

import fit.vwm.servlet.oldServlet.master.MasterSpecialComputerServlet;

import javax.servlet.annotation.WebServlet;

@WebServlet(name = "DesktopServlet",
        urlPatterns = "/computer/desktop/*")
public class DesktopServlet extends MasterSpecialComputerServlet {
    public DesktopServlet() {
        super("Computer", "Desktop");
    }
}
