package fit.vwm.servlet.finalOnelyThatIsUsedInApi;

import com.google.gson.Gson;
import fit.vwm.connection.Connector;
import fit.vwm.data.finalOnelyThatIsUsedInApi.MarketComputer;
import fit.vwm.servlet.oldServlet.master.MasterGenericServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "ComputerMarketServlet",
        urlPatterns = "/computermarket/*")

public class ComputerMarketServlet extends MasterGenericServlet {

    public ComputerMarketServlet() {
        super("Computer");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        //        spojeni
        con = Connector.getConnection();

//      output - defaultne prazdny
        String jsonString = "";
        ArrayList<MarketComputer> dataCollection = new ArrayList<>();
        try {
//            dotaz
            String sql = "CALL `getAllComputers`()";

//            provedeni dotazu
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
//            ulozeni vysledku
            while (rs.next())
                dataCollection.add(new MarketComputer(rs.getInt(1), rs.getString(2),rs.getString(3),
                        rs.getString(4), rs.getInt(5),rs.getInt(6),rs.getString(7),
                        rs.getString(8)));

//            zavreni pripojeni
            Connector.closeConnection();

//            namapovani vystupu do .json
            jsonString = new Gson().toJson(dataCollection);

        } catch (SQLException e) {
            e.printStackTrace();
        }

//        vystupni parametr

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(jsonString);
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    }
}


