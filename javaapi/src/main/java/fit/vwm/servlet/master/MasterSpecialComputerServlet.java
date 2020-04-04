package fit.vwm.servlet.master;


import com.google.gson.Gson;
import fit.vwm.connection.Connector;
import fit.vwm.data.Computer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public abstract class MasterSpecialComputerServlet extends MasterGenericServlet {
    String typeId;

    public MasterSpecialComputerServlet(String tableName, String requestor) {
        super(tableName);
//        je to prasacky reseni, ale sestavuje to pak za me spravne ten dotaz
        switch (requestor){
            case "Laptop":
                typeId = "1";
                break;
            case "Desktop":
                typeId = "2";
                break;
            case "Netbook":
                typeId = "3";
                break;
        }
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
            String sql = "select * from " + tableName + " where Type = ?";
//            provedeni dotazu
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, typeId);
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
            String sql = "select * from " + tableName + " where " + tableName + "Id = ? AND Type = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, id);
            stmt.setString(2, typeId);
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
