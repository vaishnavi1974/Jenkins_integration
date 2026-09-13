package Orange_HRM;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Properties;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class Login {
	WebDriver driver;
	DesiredCapabilities dc=new DesiredCapabilities();
	@FindBy (xpath="//input[@placeholder='Username']") WebElement Username;
	@FindBy (xpath="//input[@placeholder='Password']") WebElement Password;
	@FindBy(xpath="//button[normalize-space()='Login']") WebElement Submit;
	
	/**
	 * Loads Selenium Grid URL from config.properties
	 */
	private String getGridURL() throws IOException {
		Properties props = new Properties();
		InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties");
		if (input == null) {
			System.out.println("config.properties not found, using default URL");
			return "http://localhost:4444/wd/hub";
		}
		props.load(input);
		String env = System.getProperty("selenium.grid.env", props.getProperty("selenium.grid.env", "docker"));
		String gridUrl = props.getProperty("selenium.grid.url." + env, "http://localhost:4444/wd/hub");
		System.out.println("Connecting to Selenium Grid at: " + gridUrl);
		return gridUrl;
	}
	
	@BeforeMethod
	@Parameters({"br","os"})
	public void driver_initialization(String br,String os) throws MalformedURLException, IOException
	{
		
		switch(os)
		{
		case "linux":dc.setPlatform(Platform.LINUX);break;
		case "windows":dc.setPlatform(Platform.WIN11);break;
		case "mac":dc.setPlatform(Platform.MAC);break;
		default:System.out.println("Invalid os");return;
		}
		switch(br)
		{
		case "chrome": dc.setBrowserName("chrome");;break;
		case "firefox":dc.setBrowserName("firefox");break;
		case "Edge":dc.setBrowserName("Edge");break;
		default:System.out.println("Invalid driver");return;
		}
		
		driver=new RemoteWebDriver(new URL(getGridURL()),dc);
		PageFactory.initElements(driver, this);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		driver.manage().window().maximize();
		
	}
	@Test(dataProvider="login_Data")
	public void login(String user,String Pass) throws InterruptedException
	{
		driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
		Username.sendKeys(user);
		Password.sendKeys(Pass);
		Submit.click();
		
		Thread.sleep(3000);
		if(driver.getCurrentUrl().equals("https://opensource-demo.orangehrmlive.com/web/index.php/dashboard/index"))
		{
			Assert.assertTrue(true);
		}
		else
		{
			Assert.assertTrue(false);
		}
		
	}
	
	@DataProvider(name="login_Data",indices= {0,1})
	public Object[][] login_data()
	{
		return new Object[][] {
			{"Admin","admin123"},
			{"Vaish","vaish123"},
			{"Admin","admin123"},
			{"Manoj","manu123"}
		};
	}
	
	@AfterMethod
	public void browser_teardown()
	{
		driver.quit();
	}

}
