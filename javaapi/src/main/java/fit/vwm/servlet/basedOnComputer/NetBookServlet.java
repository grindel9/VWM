package fit.vwm.servlet.basedOnComputer;

import fit.vwm.servlet.master.MasterSpecialComputerServlet;

import javax.servlet.annotation.WebServlet;

@WebServlet(name = "NetbookServlet",
        urlPatterns = "/computer/netbook/*")
public class NetBookServlet extends MasterSpecialComputerServlet {
    public NetBookServlet() {
        super("Computer", "Netbook");
    }
}
