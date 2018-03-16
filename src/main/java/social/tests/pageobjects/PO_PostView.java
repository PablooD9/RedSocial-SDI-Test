package social.tests.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PO_PostView extends PO_View 
{
	static public void crearPost(WebDriver driver, String titulop, String descripcion)
	{
		WebElement titulo = driver.findElement(By.id("titulo"));
		titulo.click();
		titulo.clear();
		titulo.sendKeys( titulop );
		
		WebElement desc = driver.findElement(By.id("content"));
		desc.click();
		desc.clear();
		desc.sendKeys( descripcion );
		
		// Pulsar el boton de Crear
		By boton = By.id("crear");
		driver.findElement(boton).click();
	}
}
