package fit.vwm.servlet.unquie;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import fit.vwm.connection.Connector;
import fit.vwm.data.Computer;
import fit.vwm.data.ComputerReq;
import fit.vwm.servlet.master.MasterGenericServlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
            String sql = "call selectComputers(null,null,null, null, null,null,null,null,null,null)";

//            provedeni dotazu
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
//            ulozeni vysledku
            while (rs.next())
                dataCollection.add(new Computer(rs.getInt(1), rs.getString(2), rs.getString(3),
                        rs.getString(4), rs.getInt(5), rs.getInt(6), rs.getString(7),
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
        setAccessControlHeaders(response);
        out.print(jsonString);
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        BufferedReader reader =
                new BufferedReader(new InputStreamReader(request.getInputStream()));

//        zapis jsonu co mi prijde
        String json = "";
        if(reader != null){
            json = reader.readLine();
            System.out.println(json);
        }

        ObjectMapper mapper = new ObjectMapper();

        ComputerReq inComp = null;

        try{
            inComp = mapper.readValue(json, ComputerReq.class);
        }catch (IOException e){
            e.printStackTrace();
        }

        //        spojeni
        con = Connector.getConnection();


//      output - defaultne prazdny
        String jsonString = "";
        ArrayList<Computer> dataCollection = new ArrayList<>();
        try {
//            sestaveni selectu
            String sql = "call selectComputers(null,"+inComp.getBrandMysql()+","+inComp.getMadeInMysql()+","+inComp.getModelMysql()+
                    ","+inComp.getPriceMin()+","+inComp.getPriceMax()+","+inComp.getScreenSizeMin()+","+inComp.getScreenSizeMax()+
                    ","+inComp.getType()+","+inComp.getStatus()+")";

//            zalogovani
            System.out.println(sql);
//            provedeni dotazu
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
//            ulozeni vysledku
            while (rs.next())
                dataCollection.add(new Computer(rs.getInt(1), rs.getString(2), rs.getString(3),
                        rs.getString(4), rs.getInt(5), rs.getInt(6), rs.getString(7),
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
        setAccessControlHeaders(response);
        out.print(jsonString);
        out.flush();
    }
}


