package social.tests.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PO_RegisterView extends PO_View
{
	static public void fillForm(WebDriver driver, String usuariop, String emailp, String passp, 
			String passwordconfp) 
	{
		WebElement usuario = driver.findElement(By.id("usuario"));
		usuario.click();
		usuario.clear(); // limpia el campo usuario
		usuario.sendKeys( usuariop ); // copia "usuariop" en el campo usuario
		
		WebElement email = driver.findElement(By.id("email"));
		email.click();
		email.clear();
		email.sendKeys( emailp );
		
		WebElement pass = driver.findElement(By.id("pass"));
		pass.click();
		pass.clear();
		pass.sendKeys( passp );
		
		WebElement passconf = driver.findElement(By.id("rep_pass"));
		passconf.click();
		passconf.clear();
		passconf.sendKeys( passwordconfp );
		
		// Pulsar el boton de Registro
		By boton = By.id("registro");
		driver.findElement(boton).click();
	}
}
