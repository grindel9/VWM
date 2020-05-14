package fit.vwm.servlet.unquie;

import com.google.gson.Gson;
import fit.vwm.connection.Connector;
import fit.vwm.data.Computer;
import fit.vwm.servlet.master.MasterGenericServlet;

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

@WebServlet(name = "ModelServlet",
        urlPatterns = "/model/*")

public class ModelServlet extends MasterGenericServlet {

    public ModelServlet() {
        super("Model");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        //        spojeni
        con = Connector.getConnection();

//      output - defaultne prazdny
        String jsonString = "";
        ArrayList<String> dataCollection = new ArrayList<>();
        try {
//            dotaz
            String sql = "CALL `getAllComputerModels`()";

//            provedeni dotazu
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
//            ulozeni vysledku
            while (rs.next())
                dataCollection.add(
                        rs.getString(1));

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
        setAccessControlHeaders(response);
        out.print(jsonString);
        out.flush();
    }
}


