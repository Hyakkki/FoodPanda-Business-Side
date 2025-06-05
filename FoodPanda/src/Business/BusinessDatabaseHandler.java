package Business;

import Database.DatabaseCredentials;
import java.sql.*;
import java.time.Year;

public class BusinessDatabaseHandler {
    private static BusinessDatabaseHandler handler = null;
    private static Statement stmt = null;
    private static PreparedStatement pstatement = null;

    public static String dburl = DatabaseCredentials.ignoreDburl;
    public static String userName = DatabaseCredentials.ignoreUserName;
    public static String password = DatabaseCredentials.ignorePassword;

    public static BusinessDatabaseHandler getInstance() {
        if (handler == null) {
            handler = new BusinessDatabaseHandler();
        }
        return handler;
    }

    public static Connection getDBConnection()
    {
        Connection connection = null;

        try
        {
            connection = DriverManager.getConnection(dburl, userName, password);

        } catch (Exception e){
            e.printStackTrace();
        }

        return connection;
    }

    public ResultSet execQuery(String query) {
        ResultSet result;
        try {
            stmt = getDBConnection().createStatement();
            result = stmt.executeQuery(query);
        }
        catch (SQLException ex) {
            System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
            return null;
        }
        finally {
        }
        return result;
    }

    public int execUpdateQuery(String query) {
        int affectedRows = 0;
        try {
            stmt = getDBConnection().createStatement();
            affectedRows = stmt.executeUpdate(query);
        } catch (SQLException ex) {
            System.out.println("Not working");
        }
        return affectedRows;
    }

        // Login validation of email and password of the business owner
    public static boolean validateBusinessOwnerLogin(String oemail, String opassword) {
        getInstance();

        String query = "SELECT * FROM business_owner WHERE owner_email = ? AND owner_password = ?";
    
        try (Connection conn = getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, oemail);
            pstmt.setString(2, opassword);

            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
             return true;
            }

        } catch (SQLException e) {
            System.out.println("Error validating business owner credentials: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

        // Checks if a business owner email exists in the database
    public static boolean businessOwnerEmailExists(String oemail) {
        getInstance();

        String query = "SELECT * FROM business_owner WHERE owner_email = ?";
    
        try (Connection conn = getDBConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, oemail);

            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
             return true;
            }

        } catch (SQLException e) {
            System.out.println("Error checking business owner email: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Generate a unique ID for location
    public static String generateLocationID() {
    getInstance(); // Assuming this ensures a singleton DB or app instance

    String prefix = "RL";
    String query = "SELECT restaurant_location_ID FROM restaurant_location WHERE restaurant_location_ID LIKE ? ORDER BY restaurant_location_ID DESC LIMIT 1";

    try (Connection conn = getDBConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {

        pstmt.setString(1, prefix + "%");
        ResultSet result = pstmt.executeQuery();

        int nextNumber = 1;

        if (result.next()) {
            String lastID = result.getString("restaurant_location_ID");
            String numberPart = lastID.substring(prefix.length()); // Get the number part
            nextNumber = Integer.parseInt(numberPart) + 1;
        }

        return String.format("%s%03d", prefix, nextNumber); // Format like RL001

    } catch (SQLException e) {
        System.out.println("Error generating location ID: " + e.getMessage());
        e.printStackTrace();
    }

    return null;
}


    // Generate a unique ID for restaurant
    public static String generateRestaurantID() {
    getInstance(); // Ensure DB/app instance

    String prefix = "RS";
    String query = "SELECT restaurant_ID FROM restaurant WHERE restaurant_ID LIKE ? ORDER BY restaurant_ID DESC LIMIT 1";

    try (Connection conn = getDBConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {

        pstmt.setString(1, prefix + "%");
        ResultSet result = pstmt.executeQuery();

        int nextNumber = 1;

        if (result.next()) {
            String lastID = result.getString("restaurant_ID");
            String numberPart = lastID.substring(prefix.length());
            nextNumber = Integer.parseInt(numberPart) + 1;
        }

        return String.format("%s%03d", prefix, nextNumber);

    } catch (SQLException e) {
        System.out.println("Error generating restaurant ID: " + e.getMessage());
        e.printStackTrace();
    }

    return null;
}


    // Generate a unique ID for business owner
    public static String generateBusinessOwnerID() {
    getInstance(); // Ensure DB/app instance

    String prefix = "BO-";
    String query = "SELECT business_owner_ID FROM business_owner WHERE business_owner_ID LIKE ? ORDER BY business_owner_ID DESC LIMIT 1";

    try (Connection conn = getDBConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {

        pstmt.setString(1, prefix + "%");
        ResultSet result = pstmt.executeQuery();

        int nextNumber = 1;

        if (result.next()) {
            String lastID = result.getString("business_owner_ID");
            String numberPart = lastID.substring(prefix.length());
            nextNumber = Integer.parseInt(numberPart) + 1;
        }

        return String.format("%s%03d", prefix, nextNumber);

    } catch (SQLException e) {
        System.out.println("Error generating business owner ID: " + e.getMessage());
        e.printStackTrace();
    }

    return null;
}


    // Check if email exists
    public static boolean businessEmailExists(String email) {
        String query = "SELECT * FROM business_owner WHERE owner_email = ?";
        try (Connection conn = getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return true; // assume taken if DB error
        }
    }

    // Insert location with predefined ID
    public static boolean insertRestaurantLocationWithID(String locationID, String city, String street, String zipCode) {
        String query = "INSERT INTO restaurant_location (restaurant_location_ID, city, street, zip_code) VALUES (?, ?, ?, ?)";

        try (Connection conn = getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, locationID);
            pstmt.setString(2, city);
            pstmt.setString(3, street);
            pstmt.setString(4, zipCode);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Insert restaurant, return its ID
    public static String insertRestaurant(String name, String locationID) {
        String restaurantID = generateRestaurantID();

        String query = "INSERT INTO restaurant (restaurant_ID, restaurant_name, restaurant_location_ID) VALUES (?, ?, ?)";

        try (Connection conn = getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, restaurantID);
            pstmt.setString(2, name);
            pstmt.setString(3, locationID);

            int rows = pstmt.executeUpdate();
            return rows > 0 ? restaurantID : null;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Insert business owner
    public static boolean insertBusinessOwner(String restaurantID, String firstName, String lastName, String email, String password) {
        String ownerID = generateBusinessOwnerID();

        String query = "INSERT INTO business_owner (business_owner_ID, restaurant_ID, owner_first_name, owner_last_name, owner_email, owner_password) " +
                       "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, ownerID);
            pstmt.setString(2, restaurantID);
            pstmt.setString(3, firstName);
            pstmt.setString(4, lastName);
            pstmt.setString(5, email);
            pstmt.setString(6, password);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Insert product into the database
    public static boolean insertProduct(String productID, String restaurantID, String priceRangeID, String productName, int productQuantity, double productPrice, String productDescription, String productImagePath) {
    String query = "INSERT INTO product " + "(product_ID, restaurant_ID, price_range_ID, product_name, product_quantity, product_price, product_description, product_image_path) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    try (Connection conn = getDBConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {

        pstmt.setString(1, productID);
        pstmt.setString(2, restaurantID);
        pstmt.setString(3, priceRangeID);
        pstmt.setString(4, productName);
        pstmt.setInt(5, productQuantity);
        pstmt.setDouble(6, productPrice);
        pstmt.setString(7, productDescription);
        pstmt.setString(8, productImagePath);

        return pstmt.executeUpdate() > 0;

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

    // Generate a unique product ID
    public static String generateProductID() {
    String prefix = "P";
    String query = "SELECT product_ID FROM product WHERE product_ID LIKE ? ORDER BY product_ID DESC LIMIT 1";

    try (Connection conn = getDBConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {

        pstmt.setString(1, prefix + "%");
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            String lastID = rs.getString("product_ID"); // e.g. P00001
            int num = Integer.parseInt(lastID.substring(prefix.length())); // parse number part
            num++; // increment
            return String.format("%s%05d", prefix, num); // e.g. P00002
        } else {
            return prefix + "00001"; // first ID if none exist
        }

    } catch (SQLException e) {
        e.printStackTrace();
        return null;
    }
}
    

}

