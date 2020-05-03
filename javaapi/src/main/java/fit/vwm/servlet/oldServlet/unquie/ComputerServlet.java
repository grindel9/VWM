package fit.vwm.servlet.oldServlet.unquie;

import com.google.gson.Gson;
import fit.vwm.connection.Connector;
import fit.vwm.data.oldData.Computer;
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

@WebServlet(name = "ComputerServlet",
        urlPatterns = "/computer/*")

public class ComputerServlet extends MasterGenericServlet {

    public ComputerServlet() {
        super("Computer");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        //        spojeni
        con = Connector.getConnection();

//      output - defaultne prazdny
        String jsonString = "";
        ArrayList<Computer> dataCollection = new ArrayList<>();
        try {
//            dotaz
            String sql = "select * from " + tableName;

//            provedeni dotazu
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
//            ulozeni vysledku
            while (rs.next())
                dataCollection.add(new Computer(rs.getInt(1), rs.getString(4), rs.getInt(2),
                        rs.getInt(3), rs.getInt(5), rs.getInt(6), rs.getInt(7),
                        rs.getInt(8)));

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
        //        spojeni
        con = Connector.getConnection();

//      output - defaultne prazdny
        String jsonString = "";

        String id = request.getPathInfo().replace("/", "");
        try {
//            dotaz
            String sql = "select * from " + tableName + " where " + tableName + "Id = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, id);
//            provedeni dotazu
            ResultSet rs = stmt.executeQuery();
//            ulozeni vysledku
            rs.next();
            Computer c = new Computer(rs.getInt(1), rs.getString(4), rs.getInt(2),
                    rs.getInt(3), rs.getInt(5), rs.getInt(6), rs.getInt(7),
                    rs.getInt(8));

//            zavreni pripojeni
            Connector.closeConnection();

//            namapovani vystupu do .json
            jsonString = new Gson().toJson(c);

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
}


