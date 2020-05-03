package fit.vwm.servlet.oldServlet.basedOnComputer;

import fit.vwm.servlet.oldServlet.master.MasterSpecialComputerServlet;

import javax.servlet.annotation.WebServlet;

@WebServlet(name = "NetbookServlet",
        urlPatterns = "/computer/netbook/*")
public class NetBookServlet extends MasterSpecialComputerServlet {
    public NetBookServlet() {
        super("Computer", "Netbook");
    }
}
