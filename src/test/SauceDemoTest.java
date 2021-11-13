package test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import test.DBConnection;

public class SauceDemoTest {
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		
		//Database
		String URL = "jdbc:mysql://localhost:3306/ecommerce";
		String Username = "root";
		String Password = "root";
		
		DBConnection dbObj = new DBConnection(URL,Username,Password);
		Statement statement = dbObj.getConnection().createStatement();
		
		//Chrome driver
		System.setProperty("webdriver.chrome.driver", "chromedriver");
		WebDriver driver = new ChromeDriver();
		driver.get("https://www.saucedemo.com/");
		
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(4000, TimeUnit.MILLISECONDS);
		
		//username and password
		ResultSet result = statement.executeQuery("select * from login_details");
		
		String username="";
		String pass="";
		
		while(result.next()) {
			username = result.getString("username");
			pass = result.getString("password");
		}
		
		//login
		WebElement email = driver.findElement(By.xpath("//input[@id='user-name']"));
		email.sendKeys(username);
		
		WebElement password = driver.findElement(By.xpath("//input[@id='password']"));
		password.sendKeys(pass);
		
		WebElement btn_sign = driver.findElement(By.xpath("//input[@id='login-button']"));
		btn_sign.click();
		
		//product name and price
		ResultSet result1 = statement.executeQuery("select * from eproduct");
		String PName="";
		Double PPrice=0.0;
		
		while(result1.next()) {
			PName = result1.getString("name");
			PPrice = result1.getDouble("price");
		}
		
		//add product
		WebElement addProduct = driver.findElement(By.xpath("//div[@class='inventory_item_name' and text()='" + PName + "']/following::button[1]"));
		addProduct.click();
		
		//cart
		WebElement cartbtn = driver.findElement(By.cssSelector("a[class=shopping_cart_link]"));
		cartbtn.click();
		
		WebElement cartProduct  = driver.findElement(By.xpath("//div[@class = 'inventory_item_name']"));
		WebElement cartPrice = driver.findElement(By.xpath("//div[@class='inventory_item_price']"));
		
		
		if(cartProduct.equals(PName) && cartPrice.equals(PPrice)) {
			System.out.println("Adding product failed" +PPrice);
		}else {
			System.out.println("Product added successfully "+ PPrice);
		}
		
	}
}
