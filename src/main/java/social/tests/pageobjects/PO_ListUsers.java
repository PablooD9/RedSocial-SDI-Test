package social.tests.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PO_ListUsers extends PO_View
{
	static public void buscarUsuario(WebDriver driver, String usuario)
	{
		WebElement campoBusqueda = driver.findElement(By.id("buscar"));
		campoBusqueda.click();
		campoBusqueda.clear();
		campoBusqueda.sendKeys( usuario );
		
		By boton = By.id("btn-buscar");
		driver.findElement(boton).click();
	}
	
	static public void enviarPeticion(WebDriver driver, String usuario)
	{
		By boton = By.id( usuario );
		driver.findElement(boton).click();
	}
}
