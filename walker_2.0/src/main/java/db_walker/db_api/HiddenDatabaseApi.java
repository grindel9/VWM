package db_walker.db_api;

import db_walker.utils.*;

import java.sql.*;
import java.util.Set;
import java.util.TreeSet;

/**
 * A class implementing a database accessor
 */
public final class HiddenDatabaseApi extends DatabaseAccessor{
    /**
     * A constructor for the hidden db api.
     * @param dbURL is the database url
     * @param dbName is the  name of the database
     * @param username is the username user to login
     * @param password is the password for user to login
     * @param requestLimit is the request limit to be returned
     */
    public HiddenDatabaseApi(String dbURL, String dbName, String username, String password, int requestLimit) throws SQLException {
        super(requestLimit);
        this.connection = null;

        String loginParams = "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        String driver = "jdbc:mysql://" + dbURL + '/' + dbName + loginParams;
        establishConnection(driver, username, password);
        // Load valid types
        loadAllValidFields();
    }

    /**
     * Establish a connection to a database.
     */
    private void establishConnection(String driver, String username, String password) throws SQLException {
        if (connection == null || connection.isClosed())
            connection = DriverManager.getConnection(driver, username, password);
    }

    /**
     * A method that loads all valid fields.
     */
    private void loadAllValidFields() {
        this.validBrands = loadValidField("Name", "Brand");
        this.validCountries = loadValidField("Name", "Country");
        this.validTypes = loadValidField("Value", "ComputerType");
        this.validStatuses = loadValidField("Value", "Status");
    }

    /**
     * A method representing a sql query that returns entire column.
     * @param column is the column to be matched
     * @param table is the table against which we are querying
     * @return set of values from the request
     */
    private Set<String> loadValidField(String column, String table) {
        String query = "select " + column + " from " + table;
        try {
            PreparedStatement statement;
            ResultSet rs;
            // Brands
            statement = this.connection.prepareStatement(query);
            rs = statement.executeQuery();

            Set<String> result = new TreeSet<>();
            while (rs.next())
                result.add(rs.getString(1));
            return result;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return new TreeSet<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMinProductPrice() {
        return getBorderValue("Price", true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMaxProductPrice() {
        return getBorderValue("Price", false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMinProductScreenSize() {
        return getBorderValue("ScreenSize", true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMaxProductScreenSize() {
        return getBorderValue("ScreenSize", false);
    }

    /**
     * Getter for limit values of a specific column.
     * Returns min if param min is set to false, else returns max.
     * @param column is the column to be checked
     * @param min is set to true if you want to get min instead of max
     * @return a value corresponding to the query
     */
    public int getBorderValue(String column, boolean min) {
        String query;
        if (min)
            query = "select min(" + column + ") from Computer";
        else
            query = "select max(" + column + ") from Computer";
        try {
            PreparedStatement statement = this.connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();

            if (rs.next())
                return rs.getInt(1);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Product> get() throws SQLException {
        return getQueryOfLimit(this.requestLimit);
    }

    /**
     * Get query of specified limit.
     * @param limit is the limit specified.
     * @return set of products returned by the query
     * @throws SQLException if request to db was unsuccessful
     */
    private Set<Product> getQueryOfLimit(int limit) throws SQLException {
        String query = buildQueryFromArguments() + " limit " + limit;
        PreparedStatement statement = this.connection.prepareStatement(query);
        ResultSet rs = statement.executeQuery();

        Set<Product> returnVal = new TreeSet<>();
        while (rs.next()) {
            returnVal.add(new Product(
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getInt(5),
                    rs.getDouble(6),
                    rs.getString(7),
                    rs.getString(8)));
        }
        return returnVal;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean doesCurrentQueryOverflow() throws SQLException {
        return getQueryOfLimit(this.requestLimit+1).size() > this.requestLimit;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean doesCurrentQueryUnderflow() throws SQLException {
        return getQueryOfLimit(1).size() == 0;
    }

    /**
     * A builder method that builds SQL query from arguments.
     * @return string representing a sql-syntax of the given query based on arguments specified earlier
     */
    private String buildQueryFromArguments() {
        this.query = new StringBuilder();
        this.query.append("select distinct " +
                "Computer.ComputerID as ID, " +
                "Brand.Name as Brand," +
                "Country.Name as Country," +
                "Computer.Model as Model," +
                "Computer.Price as Price," +
                "Computer.ScreenSize as ScreenSize," +
                "ComputerType.Value as Type," +
                "Status.Value as Status" +
                " from " +
                " Computer " +
                " join Brand on Brand.BrandID = Computer.BrandID " +
                " join Country on Country.CountryID = Computer.MadeIn " +
                " join ComputerType on ComputerType.TypeID = Computer.Type" +
                " join Status on Status.StatusID = Computer.Status "
                );

        boolean wasWhereSet = false;
        wasWhereSet = addSetToQuery("Brand.Name", this.wantedBrands, wasWhereSet);
        wasWhereSet = addSetToQuery("Country.Name", this.wantedCountries, wasWhereSet);
        wasWhereSet = addLikeToQuery("Computer.Model", this.wantedModel, wasWhereSet);
        wasWhereSet = addMatchToQuery("ComputerType.Value", this.wantedType, wasWhereSet);
        wasWhereSet = addMatchToQuery("Status.Value", this.wantedStatus, wasWhereSet);
        wasWhereSet = addRangeToQuery("Computer.Price", this.wantedPriceRange, wasWhereSet);
        wasWhereSet = addRangeToQuery("Computer.ScreenSize", this.wantedScreenSizeRange, wasWhereSet);

        return query.toString();
    }

    /**
     * Add a set to match a against in the query
     * @param column is the column specifier
     * @param values is the set of values against which we are matching
     * @param wasWhereSet is checker whether we append "where" or "and"
     * @return true if we appended something
     */
    boolean addSetToQuery(String column, Set<String> values, boolean wasWhereSet) {
        boolean returnValue = wasWhereSet;
        if (values.size() != 0) {
            if (wasWhereSet)
                this.query.append(" and ");
            else {
                this.query.append(" where ");
                returnValue = true;
            }
            this.query.append(column).append(" in (");
            int i = 0;
            for (String value : values) {
                if (i != 0)
                    this.query.append(',');
                this.query.append('"').append(value).append('"');
                i += 1;
            }
            this.query.append(')');
        }

        return returnValue;
    }

    /**
     * Add a string to match against
     * @param column is the column specifier
     * @param toMatch is the string against which we are matching with 'like "%toMatch%'"
     * @param wasWhereSet is checker whether we append "where" or "and"
     * @return true if we appended something
     */
    boolean addLikeToQuery(String column, String toMatch, boolean wasWhereSet) {
        boolean returnValue = wasWhereSet;
        if (toMatch != null) {
            if (wasWhereSet)
                this.query.append(" and ");
            else {
                this.query.append(" where ");
                returnValue = true;
            }
            this.query.append(column).append(" like '%").append(toMatch).append("%' ");
        }

        return returnValue;
    }

    /**
     * Add a string to match against
     * @param column is the column specifier
     * @param toMatch is the string against which we are matching exactly
     * @param wasWhereSet is checker whether we append "where" or "and"
     * @return true if we appended something
     */
    boolean addMatchToQuery(String column, String toMatch, boolean wasWhereSet) {
        boolean returnValue = wasWhereSet;
        if (toMatch != null) {
            if (wasWhereSet)
                this.query.append(" and ");
            else {
                this.query.append(" where ");
                returnValue = true;
            }
            this.query.append(column).append(" = ").append('"').append(toMatch).append('"').append(' ');
        }

        return returnValue;
    }

    /**
     * Add a range to match against using 'between x and y'.
     * @param column is the column specifier
     * @param range is the range against which we are matching
     * @param wasWhereSet is checker whether we append "where" or "and"
     * @return true if we appended something
     */
    boolean addRangeToQuery(String column, String range, boolean wasWhereSet) {
        boolean returnValue = wasWhereSet;
        if (range != null) {
            if (wasWhereSet)
                this.query.append(" and ");
            else {
                this.query.append(" where ");
                returnValue = true;
            }
            this.query.append(column).append(" between ");
            this.query.append(range).append(' ');
        }

        return returnValue;
    }

    private Connection connection;
    private StringBuilder query;
}
