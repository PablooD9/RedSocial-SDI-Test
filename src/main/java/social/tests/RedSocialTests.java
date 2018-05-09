package social.tests;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import social.tests.pageobjects.PO_ChatView;
import social.tests.pageobjects.PO_ListUsers;
import social.tests.pageobjects.PO_LoginView;
import social.tests.pageobjects.PO_NavView;
import social.tests.pageobjects.PO_RegisterView;
import social.tests.pageobjects.PO_View;
import social.tests.utils.SeleniumUtils;

//Ordenamos las pruebas por el nombre del método
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RedSocialTests {
	/* PABLO */
//	 static String PathFirefox =
//	 "C:\\Users\\PabloD\\Desktop\\SDI\\practicas\\p5\\Firefox46.win\\FirefoxPortable.exe";

	/* ANTONIO */
	static String PathFirefox = 
			"D:\\UNIOVI\\Tercero\\Segundo Semestre\\SDI\\Practicas\\recursos\\Firefox46.win\\FirefoxPortable.exe";

	static WebDriver driver = getDriver(PathFirefox);
	static String URL = "http://localhost:8081";

	public static WebDriver getDriver(String PathFirefox) {
		System.setProperty("webdriver.firefox.bin", PathFirefox);
		WebDriver driver = new FirefoxDriver();
		return driver;
	}

	// Antes de cada prueba se navega al URL home de la aplicación
	@Before
	public void setUp() {
		driver.navigate().to(URL);
	}

	// Después de cada prueba se borran las cookies del navegador
	@After
	public void tearDown() {
		driver.manage().deleteAllCookies();
	}

	// Antes de la primera prueba reseteamos la base de datos
	// con los datos que queremos
	@BeforeClass
	static public void begin() {
		driver.navigate().to("http://localhost:8081/api/vaciarBBDD");
		driver.navigate().to("http://localhost:8081/api/cargarBBDD");
	}

	@AfterClass // Al finalizar la última prueba
	static public void end() {
		// Cerramos el navegador al finalizar las pruebas
		driver.quit();
	}

	/**
	 * 1.1 [RegVal] Registro de Usuario con datos válidos
	 */
	@Test
	public void t1_11_RegVal() {
		// Nos vamos al registro (debería haber un id=registro)
		PO_NavView.clickOption(driver, "registro", "id", "registro");

		// Rellenamos el formulario, y nos registramos.
		PO_RegisterView.fillForm(driver, "Mongo", "mongo@db.es", "123456", "123456");

		// Comprobamos que ya podemos iniciar sesión con nuestro nuevo usuario
		PO_View.checkElement(driver, "text", " Login");
	}

	/**
	 * 1.2 [RegInval] Registro de Usuario con datos inválidos (repetición de
	 * contraseña invalida).
	 */
	@Test
	public void t1_12_RegInval() {
		// Nos vamos al registro
		PO_NavView.clickOption(driver, "registro", "id", "registro");

		// Rellenamos el formulario, y nos intentamos registrar (las contraseñas no
		// coinciden).
		PO_RegisterView.fillForm(driver, "Prueba3", "Josefo2@uniovi.es", "77777", "66666");

		// Comprobamos que aparece el error de contraseñas no coinciden
		PO_RegisterView.checkElement(driver, "text", "Las contraseñas no coinciden");
	}

	/**
	 * 2.1 [InVal] Inicio de sesión con datos válidos.
	 */
	@Test
	public void t1_2_1InVal() {
		// Rellenamos el formulario de login con datos válidos
		PO_LoginView.fillForm(driver, "Pablo", "123456");

		// Comprobamos que aparece el mensaje "Perfil"
		PO_RegisterView.checkKey(driver, "Perfil");
	}

	/**
	 * 2.2 [InInVal] Inicio de sesión con datos inválidos (usuario no existente en
	 * la aplicación).
	 */
	@Test
	public void t1_22_InInVal() {
		// Rellenamos el formulario de login con datos INVÁLIDOS
		PO_LoginView.fillForm(driver, "mongoDB", "123456");

		// Comprobamos que aparece el mensaje de error
		// "Usuario o password incorrecto"
		PO_RegisterView.checkKey(driver, "Usuario o password incorrecto");
	}

	/**
	 * 3.1 [LisUsrVal] Acceso al listado de usuarios desde un usuario en sesión.
	 */
	@Test
	public void t1_31_LisUsrVal() {
		// Rellenamos el formulario de login con datos válidos
		PO_LoginView.fillForm(driver, "Pablo", "123456");

		PO_NavView.desplegarUsuarios(driver, "Todos los usuarios");

		// Comprobamos que aparece el mensaje "Lista de usuarios" y "Buscar usuarios"
		PO_RegisterView.checkKey(driver, "Lista de usuarios");
		PO_RegisterView.checkKey(driver, "Buscar usuarios");
	}

	/**
	 * 3.2 [LisUsrInVal] Intento de acceso con URL desde un usuario no identificado
	 * al listado de usuarios desde un usuario en sesión. Debe producirse un acceso
	 * no permitido a vistas privadas.
	 */
	@Test
	public void t1_32_LisUsrInVal() {
		// Se intenta ir, sin estar identificado, a la dirección de lista de usuarios.
		// No se permite acceder a dicha dirección. Se redirecciona al login de
		// usuarios.
		driver.navigate().to("http://localhost:8081/users/lista-usuarios");

		// Comprobamos que aparece el mensaje "Login"
		PO_RegisterView.checkKey(driver, " Login");
	}

	/**
	 * 4.1 [BusUsrVal] Realizar una búsqueda valida en el listado de usuarios desde
	 * un usuario en sesión
	 */
	@Test
	public void t1_41_BusUsrVal() {
		// Rellenamos el formulario de login con datos válidos
		PO_LoginView.fillForm(driver, "Pablo", "123456");

		// Se despliega el menú de usuarios, y se clica en Todos los usuarios
		PO_NavView.desplegarUsuarios(driver, "Todos los usuarios");

		// Buscamos a Antonio
		PO_ListUsers.buscarUsuario(driver, "Antonio");

		// Se comprueba (mediante el Email) que solo hay un usuario encontrado (Antonio)
		List<WebElement> usuarios = SeleniumUtils.EsperaCargaPagina(driver, "text", "Email", PO_View.getTimeout());
		Assert.assertTrue(usuarios.size() == 1);
	}

	/**
	 * 4.2 [BusUsrInVal] Intento de acceso con URL a la búsqueda de usuarios desde
	 * un usuario no identificado. Debe producirse un acceso no permitido a vistas
	 * privadas.
	 */
	@Test
	public void t1_42_BusUsrInVal() {
		// Se intenta ir, sin estar identificado, a la dirección de lista de usuarios, y
		// buscar a Antonio.
		// No se permite acceder a dicha dirección. Se redirecciona al login de
		// usuarios.
		driver.navigate().to("http://localhost:8081/users/lista-usuarios?searchText=Antonio");

		// Comprobamos que aparece el mensaje "Login"
		PO_RegisterView.checkKey(driver, " Login");
	}

	/**
	 * 5.1 [InvVal] Enviar una invitación de amistad a un usuario de forma valida
	 */
	@Test
	public void t1_51_InvVal() {
		// Rellenamos el formulario de login con datos válidos
		PO_LoginView.fillForm(driver, "Mongo", "123456");

		// Se despliega el menú de usuarios, y se clica en Todos los usuarios
		PO_NavView.desplegarUsuarios(driver, "Todos los usuarios");
		
		// Se puede enviar petición de amistad a Pablo (uo251017@uniovi.es)
		SeleniumUtils.EsperaCargaPagina(driver, "text", "uo251017@uniovi.es", PO_View.getTimeout());

		// Enviamos una invitación de amistad a Pablo (Usuario = Pablo)
		PO_ListUsers.enviarAceptarPeticion(driver, "Pablo");
		
		// Comprobamos que aparece el mensaje "Petición de amistad enviada satisfactoriamente"
		PO_RegisterView.checkKey(driver, "Petición de amistad enviada satisfactoriamente");
		
	}

	/**
	 * 5.2 [InvInVal] Enviar una invitación de amistad a un usuario al que ya le
	 * habíamos invitado la invitación previamente. No debería dejarnos enviar la
	 * invitación, se podría ocultar el botón de enviar invitación o notificar que
	 * ya había sido enviada previamente.
	 */
	@Test
	public void t1_52_InvInVal() {
		// Rellenamos el formulario de login con datos válidos
		PO_LoginView.fillForm(driver, "Mongo", "123456");

		// Se despliega el menú de usuarios, y se clica en Todos los usuarios
		PO_NavView.desplegarUsuarios(driver, "Todos los usuarios");

		// Se comprueba que se ha enviado la petición de amistad a Pablo (aparece el
		// mensaje "Petición de amistad enviada", con ID=enviada_Pablo
		SeleniumUtils.EsperaCargaPaginaConId(driver, "enviada_Pablo", PO_View.getTimeout());
	}

	/**
	 * 6.1 [LisInvVal] Listar las invitaciones recibidas por un usuario, realizar la
	 * comprobación con una lista que al menos tenga una invitación recibida.
	 */
	@Test
	public void t1_61_LisInvVal() {
		// Rellenamos el formulario de login con datos válidos
		PO_LoginView.fillForm(driver, "Pablo", "123456");

		// Se despliega el menú de usuarios, y se clica en Peticiones de amistad
		PO_NavView.desplegarUsuarios(driver, "Peticiones de amistad");

		// Se comprueba que Pablo tiene 1 peticiones de amistad
		List<WebElement> usuarios = SeleniumUtils.EsperaCargaPagina(driver, "text", "Aceptar petición",
				PO_View.getTimeout());
		Assert.assertTrue(usuarios.size() == 1);
	}
	
	/**
	 * 7.1 [AcepInvVal] Aceptar una invitación recibida.
	 */
	@Test
	public void t1_71_AcepInvVal() 
	{
		// Rellenamos el formulario de login con datos válidos
		PO_LoginView.fillForm(driver, "Pablo", "123456");
		
		// Se despliega el menú de usuarios, y se clica en Ver peticiones de amistad
		PO_NavView.desplegarUsuarios(driver, "Peticiones de amistad");
		
		// Se comprueba que Pablo tiene 2 peticiones de amistad
		List<WebElement> usuarios = SeleniumUtils.EsperaCargaPagina(driver, "text", "Aceptar petición",
				PO_View.getTimeout());
		Assert.assertTrue(usuarios.size() == 1);			
		
		// Se acepta la petición del usuario "Mongo"
		PO_ListUsers.enviarAceptarPeticion(driver, "Mongo");
		
		// Se comprueba que salta un mensaje al aceptar la petición
		// El mensaje es "¡Petición aceptada con éxito!"
		PO_RegisterView.checkKey(driver, "¡Petición aceptada con éxito!");
		
		// Se comprueba que ahora que ya se ha aceptado la petición por tanto no hay peticiones pendientes
		PO_RegisterView.checkKey(driver, "¡Vaya! No tienes ninguna petición de amistad pendiente.");
		
	}

	/**
	 * 8.1 [ListAmiVal] Listar los amigos de un usuario, realizar la comprobación con una lista que al menos tenga un amigo.
	 */
	@Test
	public void t1_81_ListAmiVal() 
	{
		// Rellenamos el formulario de login con datos válidos
		// Inicio sesión con Pablo, que tiene varios amigos
		PO_LoginView.fillForm(driver, "Pablo", "123456");
		
		// Se despliega el menú de usuarios, y se clica en Mis amigos
		PO_NavView.desplegarUsuarios(driver, "Mis amigos");
		
		// Se comprueba que Pablo tiene 4 amigos
		List<WebElement> usuarios = SeleniumUtils.EsperaCargaPagina(driver, "text", "Email",
				PO_View.getTimeout());
		Assert.assertTrue(usuarios.size() == 4);
	}
	
	/**
	 * C1.1[[CInVal] Inicio de sesión con datos válidos.
	 */
	@Test
	public void t2_11_CInVal() 
	{		
		// Vamos a la URL para el chat
		driver.navigate().to("http://localhost:8081/chat-redsocial.html");
		
		// Rellenamos el formulario de login con datos válidos
		SeleniumUtils.EsperaCargaPagina(driver, "text", "Iniciar sesión", PO_View.getTimeout());
		PO_ChatView.fillForm(driver, "Antonio", "123456");
		
		// Comprobamos que entramos al chat (aparece un mensaje)
		PO_RegisterView.checkKey(driver, "Autores: Antonio y Pablo");
	}
	
	/**
	 * C1.2 [CInInVal] Inicio de sesión con datos inválidos (usuario no existente en la aplicación).
	 */
	@Test
	public void t2_12_CInInVal() 
	{		
		// Vamos a la URL para el chat
		driver.navigate().to("http://localhost:8081/chat-redsocial.html");
		
		// Rellenamos el formulario de login con datos válidos
		PO_ChatView.fillForm(driver, "Ana Botella", "123456");
		
		// Comprobamos que seguimos en la ventana de login
		PO_RegisterView.checkKey(driver, "Iniciar sesión");
	}
	
	/**
	 *  C.2.1 [CListAmiVal] Acceder a la lista de amigos de un usuario, que al menos 
	 *  tenga tres amigos.
	 */
	@Test
	public void t2_21_CListAmiVal() 
	{		
		// Vamos a la URL para el chat
		driver.navigate().to("http://localhost:8081/chat-redsocial.html");
		
		// Rellenamos el formulario de login con datos válidos
		PO_ChatView.fillForm(driver, "Pablo", "123456");
		
		//SeleniumUtils.esperarSegundos(driver, 2);
		
		// Se comprueba que Pablo tiene mas de 3 amigos( 3 en caso de ejecutar esta prueba
		// unicamente y 4 si se han ejecutado el resto de pruebas en conjunto )
		List<WebElement> usuarios = SeleniumUtils.EsperaCargaPagina(driver, "class", "name_amigo",
				PO_View.getTimeout());
		Assert.assertTrue(usuarios.size() >= 3);
	}
	
	/**
	 * C.2.2 [CListAmiFil] Acceder a la lista de amigos de un usuario, y realizar un 
	 * filtrado para encontrar a un amigo concreto, el nombre a buscar debe 
	 * coincidir con el de un amigo.
	 */
	@Test
	public void t2_22_CListAmiFil() 
	{		
		// Vamos a la URL para el chat
		driver.navigate().to("http://localhost:8081/chat-redsocial.html");
		
		// Rellenamos el formulario de login con datos válidos
		PO_ChatView.fillForm(driver, "Pablo", "123456");
		
		//SeleniumUtils.esperarSegundos(driver, 2);
		
		// Se comprueba que Pablo tiene mas de 3 amigos( 3 en caso de ejecutar esta prueba
		// unicamente y 4 si se han ejecutado el resto de pruebas en conjunto )
		List<WebElement> usuarios = SeleniumUtils.EsperaCargaPagina(driver, "class", "name_amigo",
				PO_View.getTimeout());
		Assert.assertTrue(usuarios.size() >= 3);
		
		// Buscamos a Antonio
		PO_ChatView.buscarUsuario(driver, "Antonio");
		
		// Comprobamos que solo aparece un amigo
		usuarios = SeleniumUtils.EsperaCargaPagina(driver, "class", "name_amigo",
				PO_View.getTimeout());
		Assert.assertTrue(usuarios.size() == 1);
	}
	
	/**
	 *  C3.1 [CListMenVal] Acceder a la lista de mensajes de un amigo “chat”, la lista debe contener al menos tres mensajes.
	 */
	@Test
	public void t2_31_CListMenVal() 
	{		
		// Vamos a la URL para el chat
		driver.navigate().to("http://localhost:8081/chat-redsocial.html");
		
		// Rellenamos el formulario de login con datos válidos
		PO_ChatView.fillForm(driver, "Pablo", "123456");
		
		// Esperamos a que se carguen los amigos
		SeleniumUtils.EsperaCargaPagina(driver, "class", "name_amigo",
				PO_View.getTimeout());
		
		// Damos clic al amigo "Antonio" para abrir el chat con él
		PO_ChatView.abrirChat(driver, "Antonio");
		
		// Comprobamos que tenemos al menos 3 mensajes con Antonio
		List<WebElement> mensajes = SeleniumUtils.EsperaCargaPagina(driver, "class", "message-data-name",
				PO_View.getTimeout());
		Assert.assertTrue(mensajes.size() >= 3);
	}
	
	/**
	 *  C4.1 [CCrearMenVal] Acceder a la lista de mensajes de un amigo “chat” y crear un nuevo mensaje, 
	 *  validar que el mensaje aparece en la lista de mensajes.
	 */
	@Test
	public void t2_41_CCrearMenVal() 
	{		
		// Vamos a la URL para el chat
		driver.navigate().to("http://localhost:8081/chat-redsocial.html");
		
		// Rellenamos el formulario de login con datos válidos
		PO_ChatView.fillForm(driver, "Pablo", "123456");
		
		// Esperamos a que se carguen los amigos
		SeleniumUtils.EsperaCargaPagina(driver, "class", "name_amigo",
				PO_View.getTimeout());
		
		// Damos clic al amigo "Antonio" para abrir el chat con él
		PO_ChatView.abrirChat(driver, "Antonio");
		
		// Escribimos un nuevo mensaje para "Antonio"
		PO_ChatView.escribirMensaje(driver, "Prueba para el test C4.1 de Selenium");
		
		// Comprobamos que aparece el mensaje enviado
		List<WebElement> mensajes = SeleniumUtils.EsperaCargaPagina(driver, "text", "Prueba para el test C4.1 de Selenium",
				PO_View.getTimeout());
		Assert.assertTrue(mensajes.size() > 0);
	}
	
	/**
	 *  C5.1 [CMenLeidoVal] Identificarse en la aplicación y enviar un mensaje a un amigo, validar que 
	 *  el mensaje enviado aparece en el chat. Identificarse después con el usuario que recibido el mensaje y 
	 *  validar que tiene un mensaje sin leer, entrar en el chat y comprobar que el mensaje pasa a tener el estado leído.
	 */
	@Test
	public void t2_51_CMenLeidoVal() 
	{		
		// Vamos a la URL para el chat
		driver.navigate().to("http://localhost:8081/chat-redsocial.html");
		
		// Rellenamos el formulario de login con datos válidos
		PO_ChatView.fillForm(driver, "Pablo", "123456");
		
		// Esperamos a que se carguen los amigos
		SeleniumUtils.EsperaCargaPagina(driver, "class", "name_amigo",
				PO_View.getTimeout());
		
		// Damos clic al amigo "Antonio" para abrir el chat con él
		PO_ChatView.abrirChat(driver, "Antonio");
		
		// Escribimos un nuevo mensaje para "Antonio"
		PO_ChatView.escribirMensaje(driver, "Prueba para el test C5.1 de Selenium");
		
		// Comprobamos que aparece el mensaje enviado
		List<WebElement> mensajes = SeleniumUtils.EsperaCargaPagina(driver, "text", "Prueba para el test C5.1 de Selenium",
				PO_View.getTimeout());
		Assert.assertTrue(mensajes.size() > 0);
		
		// Volvemos a la ventana de Login para entrar con el usuario Antonio
		driver.navigate().to("http://localhost:8081/chat-redsocial.html");
		
		// Rellenamos el formulario de login con datos válidos
		PO_ChatView.fillForm(driver, "Antonio", "123456");
		
		// Comprobamos que tenemos mensajes sin leer
		SeleniumUtils.EsperaCargaPagina(driver, "text", "mensajes sin leer",
							PO_View.getTimeout());
		
		// Damos clic al amigo "Pablo" para abrir el chat con él
		PO_ChatView.abrirChat(driver, "Pablo");
		
		// Comprobamos que ya no hay mensajes sin leer
		SeleniumUtils.EsperaCargaPaginaNoTexto(driver, "mensajes sin leer",
							PO_View.getTimeout());
	}
	
	/**
	 *  C6.1 [CListaMenNoLeidoVal] Identificarse en la aplicación y enviar tres mensajes a un amigo, validar que 
	 *  los mensajes enviados aparecen en el chat. 
	 *  Identificarse después con el usuario que recibido el mensaje y validar que el número de mensajes sin leer aparece en la propia lista de amigos.
	 */
	@Test
	public void t2_61_CListaMenNoLeidoVal() 
	{		
		// Vamos a la URL para el chat
		driver.navigate().to("http://localhost:8081/chat-redsocial.html");
		
		// Rellenamos el formulario de login con datos válidos
		PO_ChatView.fillForm(driver, "Pablo", "123456");
		
		// Esperamos a que se carguen los amigos
		SeleniumUtils.EsperaCargaPagina(driver, "class", "name_amigo",
				PO_View.getTimeout());
		
		// Damos clic al amigo "Antonio" para abrir el chat con él
		PO_ChatView.abrirChat(driver, "Antonio");
		
		// Escribimos 3 MENSAJES para ANTONIO:
		// =======================================
		// Escribimos un nuevo mensaje para "Antonio"
		PO_ChatView.escribirMensaje(driver, "Prueba 1 para el test C6.1 de Selenium");
		
		// Comprobamos que aparece el mensaje enviado
		List<WebElement> mensajes = SeleniumUtils.EsperaCargaPagina(driver, "text", "Prueba 1 para el test C6.1 de Selenium",
				PO_View.getTimeout());
		Assert.assertTrue(mensajes.size() > 0);
		
		// Escribimos un nuevo mensaje para "Antonio"
		PO_ChatView.escribirMensaje(driver, "Prueba 2 para el test C6.1 de Selenium");
		
		// Comprobamos que aparece el mensaje enviado
		mensajes = SeleniumUtils.EsperaCargaPagina(driver, "text", "Prueba 2 para el test C6.1 de Selenium",
				PO_View.getTimeout());
		Assert.assertTrue(mensajes.size() > 0);
		
		// Escribimos un nuevo mensaje para "Antonio"
		PO_ChatView.escribirMensaje(driver, "Prueba 3 para el test C6.1 de Selenium");
		
		// Comprobamos que aparece el mensaje enviado
		mensajes = SeleniumUtils.EsperaCargaPagina(driver, "text", "Prueba 3 para el test C6.1 de Selenium",
				PO_View.getTimeout());
		Assert.assertTrue(mensajes.size() > 0);
		// =======================================
		
		// Volvemos a la ventana de Login para entrar con el usuario Antonio
		driver.navigate().to("http://localhost:8081/chat-redsocial.html");
		
		// Rellenamos el formulario de login con datos válidos
		PO_ChatView.fillForm(driver, "Antonio", "123456");
		
		// Comprobamos que tenemos tres mensajes sin leer
		SeleniumUtils.EsperaCargaPagina(driver, "text", "3 mensajes sin leer",
							PO_View.getTimeout());
	}
	
	/**
	 *  C7.1 [COrdenMenVal] Identificarse con un usuario A que al menos tenga 3 amigos, ir al 
	 *  chat del ultimo amigo de la lista y enviarle un mensaje, volver a la lista de amigos y comprobar 
	 *  que el usuario al que se le ha enviado el mensaje esta en primera posición. 
	 *  Identificarse con el usuario B y enviarle un mensaje al usuario A. 
	 *  Volver a identificarse con el usuario A y ver que el usuario que acaba de mandarle el mensaje es el primero en su lista de amigos.
	 */
	@Test
	public void t2_71_COrdenMenVal() 
	{		
		// Vamos a la URL para el chat
		driver.navigate().to("http://localhost:8081/chat-redsocial.html");
		
		// Rellenamos el formulario de login con datos válidos
		PO_ChatView.fillForm(driver, "Pablo", "123456");
		
		// Esperamos a que se carguen los amigos
		SeleniumUtils.EsperaCargaPagina(driver, "class", "name_amigo",
				PO_View.getTimeout());
		
		// Damos clic al amigo "Antonio" para abrir el chat con él
		PO_ChatView.abrirChat(driver, "Antonio");
		
		// Escribimos un nuevo mensaje para "Antonio"
		PO_ChatView.escribirMensaje(driver, "Prueba 1 para el test C7.1 de Selenium");
		
		// Comprobamos que aparece el mensaje enviado
		List<WebElement> mensajes = SeleniumUtils.EsperaCargaPagina(driver, "text", "Prueba 1 para el test C7.1 de Selenium",
				PO_View.getTimeout());
		Assert.assertTrue(mensajes.size() > 0);
		
		// Clicamos en el primer elemento de la lista de usuarios
		List<WebElement> listaUsuarios = SeleniumUtils.EsperaCargaPagina(driver, "class", "amigo",
				PO_View.getTimeout());
		listaUsuarios.get(0).click();
		
		// Comprobamos que estamos en el chat con Antonio
		mensajes = SeleniumUtils.EsperaCargaPagina(driver, "text", "Prueba 1 para el test C7.1 de Selenium",
				PO_View.getTimeout());
		Assert.assertTrue(mensajes.size() > 0);
		
		// Volvemos a la ventana de Login para entrar con el usuario Antonio
		driver.navigate().to("http://localhost:8081/chat-redsocial.html");
		
		// Rellenamos el formulario de login con datos válidos
		PO_ChatView.fillForm(driver, "Antonio", "123456");
		
		// Damos clic al amigo "Antonio" para abrir el chat con él
		PO_ChatView.abrirChat(driver, "Pablo");
		
		// Escribimos un nuevo mensaje para "Pablo"
		PO_ChatView.escribirMensaje(driver, "Prueba 2 para el test C7.1 de Selenium");
		
		// Vamos a la URL para el chat
		driver.navigate().to("http://localhost:8081/chat-redsocial.html");
		
		// Rellenamos el formulario de login con datos válidos
		PO_ChatView.fillForm(driver, "Pablo", "123456");
		
		// Clicamos en el primer elemento de la lista de usuarios
		listaUsuarios = SeleniumUtils.EsperaCargaPagina(driver, "class", "amigo",
				PO_View.getTimeout());
		listaUsuarios.get(0).click();
		
		// Comprobamos que estamos en el chat con Antonio
		mensajes = SeleniumUtils.EsperaCargaPagina(driver, "text", "Prueba 1 para el test C7.1 de Selenium",
				PO_View.getTimeout());
		Assert.assertTrue(mensajes.size() > 0);
				
	}
	
	
}
