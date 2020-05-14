package fit.vwm.servlet.master;
import com.google.gson.Gson;
import fit.vwm.connection.Connector;
import fit.vwm.data.GenericData;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;

public abstract class MasterGenericServlet extends HttpServlet {
    protected Connection con;
    protected String tableName = "";

    public MasterGenericServlet(String tableName){
        this.tableName = tableName;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
//        spojeni
        con = Connector.getConnection();

//      output - defaultne prazdny
        String jsonString = "";
        ArrayList<GenericData> dataCollection = new ArrayList<>();
        try {
//            dotaz
            String sql = "select * from "+tableName;
            PreparedStatement stmt = con.prepareStatement(sql);
//            provedeni dotazu
            ResultSet rs = stmt.executeQuery();
//            ulozeni vysledku
            while (rs.next())
                dataCollection.add(new GenericData(rs.getInt(1), rs.getString(2)));

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

//    getById, jednoduse je to POST protoze me zajima ze neco ctu na vstupu z requestu
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
//        spojeni
        con = Connector.getConnection();

//      output - defaultne prazdny
        String jsonString = "";

        String id = request.getPathInfo().replace("/", "");
        try {
//            dotaz
            String sql = "select * from "+tableName+" where "+tableName+"Id = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, id);
//            provedeni dotazu
            ResultSet rs = stmt.executeQuery();
//            ulozeni vysledku
            rs.next();
            GenericData c = new GenericData(rs.getInt(1), rs.getString(2));

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

//    cross over prlatforms
protected void setAccessControlHeaders(HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
    }
}

