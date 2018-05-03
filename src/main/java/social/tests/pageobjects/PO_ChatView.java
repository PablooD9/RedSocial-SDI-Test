package social.tests.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PO_ChatView 
{
	
	static public void fillForm(WebDriver driver, String usuariop, String passwordp) 
	{
		WebElement usuario = driver.findElement(By.name("username"));
		usuario.click();
		usuario.clear(); // limpia el campo usuario
		usuario.sendKeys( usuariop ); // copia "usuariop" en el campo usuario
		
		WebElement password = driver.findElement(By.name("password"));
		password.click();
		password.clear();
		password.sendKeys( passwordp );
		
		// Pulsar el boton de Login.
		By boton = By.id("boton-login");
		driver.findElement(boton).click();
	}
	
	static public void buscarUsuario(WebDriver driver, String usuario)
	{
		WebElement campoBusqueda = driver.findElement(By.id("busqueda"));
		campoBusqueda.click();
		campoBusqueda.clear();
		campoBusqueda.sendKeys( usuario );
	}
	
	static public void abrirChat(WebDriver driver, String usuario){
		WebElement abrirChatAmigo = driver.findElement(By.id("chat_" + usuario));
		abrirChatAmigo.click();
	}
	
	static public void escribirMensaje(WebDriver driver, String mensaje){
		WebElement campo_mensaje = driver.findElement(By.id("mensajeAenviar"));
		campo_mensaje.click();
		campo_mensaje.clear();
		campo_mensaje.sendKeys( mensaje );
		
		By boton = By.id("btEnviar");
		driver.findElement(boton).click();
	}
}
