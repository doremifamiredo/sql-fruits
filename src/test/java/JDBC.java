import java.sql.*;

public class JDBC {
    private static final String count = "SELECT COUNT(FOOD_NAME) AS NAME FROM FOOD WHERE FOOD_NAME = ?";
    private static final String foodInfo = "SELECT * FROM FOOD WHERE FOOD_NAME = ?";
    private static final String deleteString = "DELETE FROM FOOD WHERE FOOD_NAME = ?";
    private static Connection connection;

    public static int countFoodName(String foodName) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:h2:tcp://localhost:9092/mem:testdb",
                "user", "pass");
        PreparedStatement statement = connection.prepareStatement(count);
        statement.setString(1, foodName);
        ResultSet resultExp = statement.executeQuery();
        resultExp.next();
        int result = resultExp.getInt("NAME");
        connection.close();
        return result;
    }

    public static ProductList.ProductInfo getProductInfoFromDB(String name) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:h2:tcp://localhost:9092/mem:testdb",
                "user", "pass");
        PreparedStatement statement = connection.prepareStatement(foodInfo);
        statement.setString(1, name);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        ProductList.ProductInfo productInfo = new ProductList.ProductInfo(
                resultSet.getString("FOOD_NAME"),
                resultSet.getString("FOOD_TYPE"),
                resultSet.getInt("FOOD_EXOTIC"));
        connection.close();
        return productInfo;
    }

    public static int resetDB(String name) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:h2:tcp://localhost:9092/mem:testdb",
                "user", "pass");
        PreparedStatement statement = connection.prepareStatement(deleteString);
        statement.setString(1, name);
        int count = statement.executeUpdate();
        connection.close();
        return count;
    }
}
