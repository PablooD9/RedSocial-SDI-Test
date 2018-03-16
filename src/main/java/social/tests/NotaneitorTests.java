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
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import social.tests.pageobjects.PO_HomeView;
import social.tests.pageobjects.PO_ListUsers;
import social.tests.pageobjects.PO_LoginView;
import social.tests.pageobjects.PO_NavView;
import social.tests.pageobjects.PO_PrivateView;
import social.tests.pageobjects.PO_Properties;
import social.tests.pageobjects.PO_RegisterView;
import social.tests.pageobjects.PO_View;
import social.tests.utils.SeleniumUtils;

//Ordenamos las pruebas por el nombre del mÈtodo
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class NotaneitorTests 
{
	/* PABLO */
	static String PathFirefox = "C:\\Users\\PabloD\\Desktop\\SDI\\practicas\\p5\\Firefox46.win\\FirefoxPortable.exe";
	
	/* ANTONIO */
	//static String PathFirefox = "C:\\Users\\XXX\\Desktop\\SDI\\practicas\\p5\\Firefox46.win\\FirefoxPortable.exe";
	
	static WebDriver driver = getDriver(PathFirefox);
	static String URL = "http://localhost:9090";
	
	public static WebDriver getDriver(String PathFirefox) 
	{
		System.setProperty("webdriver.firefox.bin",PathFirefox);
		WebDriver driver = new FirefoxDriver();
		return driver;
	}

	//Antes de cada prueba se navega al URL home de la aplicaci√≥nn
	@Before
	public void setUp()
	{
		driver.navigate().to(URL);
	}
	
	//Despu√©s de cada prueba se borran las cookies del navegador
	@After
	public void tearDown()
	{
		driver.manage().deleteAllCookies();
	}
	
	//Antes de la primera prueba
	@BeforeClass
	static public void begin() 
	{
//		// Vamos al formulario de logueo.
//		PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
//		
//		// Rellenamos el formulario
//		PO_LoginView.fillForm(driver, "99999988F", "123456");
//		
//		// Comprobamos que entramos en la pagina privada de un Administrador
//		List<WebElement> element = SeleniumUtils.EsperaCargaPagina(driver, "@href", "restoreDB", 2);
//		element.get(0).click();
//		
//		element = SeleniumUtils.EsperaCargaPagina(driver, "@href", "logout", 2);
//		element.get(0).click();
	}
	
	@AfterClass //Al finalizar la √∫ltima prueba
	static public void end() 
	{
		//Cerramos el navegador al finalizar las pruebas
		driver.quit();
	}

	
	
	/**
	 * 1.1 [RegVal] Registro de Usuario con datos v·lidos
	 */
	@Test
	public void RegVal() 
	{
		// Nos vamos al registro (deberÌa haber un id=registro)
		PO_NavView.clickOption(driver, "registro", "id", "registro");
		
		// Rellenamos el formulario, y nos registramos.
		PO_RegisterView.fillForm(driver, "Prueba", "Josefo@uniovi.es", "77777", "77777");
		
		// Comprobamos que entramos en el panel
		PO_View.checkElement(driver, "text", "Panel");
	}
	
	/**
	 * 1.2 [RegInval] Registro de Usuario con datos inv·lidos (repeticiÛn de contraseÒa invalida).
	 */
	@Test
	public void RegInval() 
	{
		// Nos vamos al registro
		PO_NavView.clickOption(driver, "registro", "id", "registro");
		
		// Rellenamos el formulario, y nos intentamos registrar (las contraseÒas no coinciden).
		PO_RegisterView.fillForm(driver, "Prueba2", "Josefo@uniovi.es", "77777", "66666");
		
		// Comprobamos que aparece el error de contraseÒas no coinciden
		PO_RegisterView.checkKey(driver, "Error.passNoCoincide", PO_Properties.getSPANISH() );
	}
	
	/**
	 * 2.1 [InVal] Inicio de sesiÛn con datos v·lidos.
	 */
	@Test
	public void InVal() 
	{
		// Se intenta ir a la direcciÛn para listar usuarios (no dejar·, puedes debemos loguearnos)
//		driver.navigate().to( "http://localhost:9090/users/lista-usuarios" );
		
		// Rellenamos el formulario de login con datos v·lidos
		PO_LoginView.fillForm(driver, "maria", "123456");
		
		// Comprobamos que aparece el mensaje "Lista de usuarios"
//		PO_RegisterView.checkKey(driver, "Usuarios.lista.listaUsuarios", PO_Properties.getSPANISH() );
		PO_RegisterView.checkKey(driver, "Panel.panel", PO_Properties.getSPANISH() );
	}
	
	/**
	 * 2.2 [InInVal] Inicio de sesiÛn con datos inv·lidos (usuario no existente en la aplicaciÛn).
	 */
	@Test
	public void InInVal() 
	{	
		// Rellenamos el formulario de login con datos INV¡LIDOS
		PO_LoginView.fillForm(driver, "mariaB", "123456");
		
		// Comprobamos que aparece el mensaje de error 
		// "Username o password incorrectos o el usuario no tiene privilegios suficientes"
		PO_RegisterView.checkKey(driver, "Error.falloLogin", PO_Properties.getSPANISH() );
	}
	
	/**
	 * 3.1 [LisUsrVal] Acceso al listado de usuarios desde un usuario en sesiÛn.
	 */
	@Test
	public void LisUsrVal() 
	{	
		// Rellenamos el formulario de login con datos v·lidos
		PO_LoginView.fillForm(driver, "maria", "123456");
		
		PO_NavView.desplegarUsuarios(driver, "Todos los usuarios");
		
		// Comprobamos que aparece el mensaje "Lista de usuarios"
		PO_RegisterView.checkKey(driver, "Usuarios.lista.listaUsuarios", PO_Properties.getSPANISH() );
	}
	
	/**
	 * 3.2 [LisUsrInVal] Intento de acceso con URL desde un usuario no identificado
	 *  al listado de usuarios desde un usuario en sesiÛn. 
	 *  Debe producirse un acceso no permitido a vistas privadas.
	 */
	@Test
	public void LisUsrInVal() 
	{	
		// Se intenta ir, sin estar identificado, a la direcciÛn de lista de usuarios.
		// No se permite acceder a dicha direcciÛn. Se redirecciona al login de usuarios.
		driver.navigate().to( "http://localhost:9090/users/lista-usuarios" );
				
		// Comprobamos que aparece el mensaje "Login"
		PO_RegisterView.checkKey(driver, "Login.titulo", PO_Properties.getSPANISH() );
	}
	
	/**
	 * 4.1 [BusUsrVal] Realizar una b˙squeda valida en el listado de usuarios desde un usuario en sesiÛn
	 */
	@Test
	public void BusUsrVal() 
	{	
		// Rellenamos el formulario de login con datos v·lidos
		PO_LoginView.fillForm(driver, "maria", "123456");
		
		// Se despliega el men˙ de usuarios, y se clica en Todos los usuarios
		PO_NavView.desplegarUsuarios(driver, "Todos los usuarios");
		
		// Buscamos a Pedro
		PO_ListUsers.buscarUsuario(driver, "Pedro");
		
		// Se comprueba (mediante el Email) que solo hay un usuario encontrado (Pedro)
		List<WebElement> usuarios = SeleniumUtils.EsperaCargaPagina(driver, "text", "Email",
				PO_View.getTimeout());
		Assert.assertTrue(usuarios.size() == 1);		
	}
	
	/**
	 * 4.2 [BusUsrInVal] Intento de acceso con URL a la b˙squeda de usuarios desde un usuario no
	 * identificado. Debe producirse un acceso no permitido a vistas privadas.
	 */
	@Test
	public void BusUsrInVal() 
	{	
		// Se intenta ir, sin estar identificado, a la direcciÛn de lista de usuarios, y buscar a Pedro.
		// No se permite acceder a dicha direcciÛn. Se redirecciona al login de usuarios.
		driver.navigate().to( "http://localhost:9090/users/lista-usuarios?searchText=Pedro" );
				
		// Comprobamos que aparece el mensaje "Login"
		PO_RegisterView.checkKey(driver, "Login.titulo", PO_Properties.getSPANISH() );	
	}
	
	/**
	 * 5.1 [InvVal] Enviar una invitaciÛn de amistad a un usuario de forma valida
	 */
	@Test
	public void InvVal() 
	{	
		// Rellenamos el formulario de login con datos v·lidos
		PO_LoginView.fillForm(driver, "maria", "123456");
		
		// Se despliega el men˙ de usuarios, y se clica en Todos los usuarios
		PO_NavView.desplegarUsuarios(driver, "Todos los usuarios");
		
		// Enviamos una invitaciÛn de amistad a Marta (Usuario = marta)
		PO_ListUsers.enviarPeticion(driver, "marta");

		// Se comprueba que se ha enviado la peticiÛn de amistad a Marta (no hay botÛn de enviar)
		SeleniumUtils.EsperaCargaPaginaNoId(driver, "marta", PO_View.getTimeout() );	
	}
	
	/**
	 * 5.2 [InvInVal] Enviar una invitaciÛn de amistad a un usuario al que ya le habÌamos invitado la invitaciÛn
	 * previamente. No deberÌa dejarnos enviar la invitaciÛn, se podrÌa ocultar el botÛn de enviar invitaciÛn o
	 * notificar que ya habÌa sido enviada previamente.
	 */
	@Test
	public void InvInVal() 
	{	
		// Rellenamos el formulario de login con datos v·lidos
		PO_LoginView.fillForm(driver, "maria", "123456");
		
		// Se despliega el men˙ de usuarios, y se clica en Todos los usuarios
		PO_NavView.desplegarUsuarios(driver, "Todos los usuarios");
		
		// Enviamos una invitaciÛn de amistad a Marta (Usuario = marta)
		PO_ListUsers.enviarPeticion(driver, "marta");

		// Se comprueba que se ha enviado la peticiÛn de amistad a Marta (aparece el mensaje "PeticiÛn
		// de amistad enviada", con ID=enviada_marta
		SeleniumUtils.EsperaCargaPaginaConId(driver, "enviada_marta", PO_View.getTimeout() );	
	}
	
	/**
	 * 6.1 [LisInvVal] Listar las invitaciones recibidas por un usuario, realizar la comprobaciÛn con una lista
	 * que al menos tenga una invitaciÛn recibida.
	 */
	@Test
	public void LisInvVal() 
	{	
		// Rellenamos el formulario de login con datos v·lidos
		// Inicio sesiÛn con Lucas, porque tiene 3 peticiones de amistad pendientes
		PO_LoginView.fillForm(driver, "lucas", "123456");
		
		// Se despliega el men˙ de usuarios, y se clica en Ver peticiones de amistad
		PO_NavView.desplegarUsuarios(driver, "Ver peticiones de amistad");
		
		// Se comprueba que Lucas tiene 3 peticiones de amistad
		List<WebElement> usuarios = SeleniumUtils.EsperaCargaPagina(driver, "text", "Aceptar peticiÛn",
				PO_View.getTimeout());
		Assert.assertTrue(usuarios.size() == 3);			
	}
	
	
	// PR02. OPci√≥n de navegaci√≥n. Pinchar en el enlace Registro en la p√°gina home
	@Test
	public void PR02() {
		PO_NavView.clickOption(driver, "signup", "class", "btn btn-primary");
	}
	
	// PR03. OPci√≥n de navegaci√≥n. Pinchar en el enlace Identificate en la p√°gina
	// home
	@Test
	public void PR03() {
		PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
	}
	
	// PR04. OPci√≥n de navegaci√≥n. Cambio de idioma de Espa√±ol a Ingles y vuelta a
	// Espa√±ol
	@Test
	public void PR04() {
		PO_HomeView.checkChangeIdiom(driver, "btnSpanish", "btnEnglish", PO_Properties.getSPANISH(),
				PO_Properties.getENGLISH());
//		SeleniumUtils.esperarSegundos(driver, 2);
	}
	
	// PR05. Prueba del formulario de registro. registro con datos correctos
	@Test
	public void PR05() {
		// Vamos al formulario de registro
		PO_NavView.clickOption(driver, "signup", "class", "btn btn-primary");
		// Rellenamos el formulario.
		PO_RegisterView.fillForm(driver, "Prueba", "Josefo@uniovi.es", "77777", "77777");
		// Comprobamos que entramos en la secci√≥n privada
		PO_View.checkElement(driver, "text", "Notas del usuario");
	}
	
	
	//PR06. Prueba del formulario de registro. DNI repetido en la BD, Nombre corto, .... 
	// pagination pagination-centered, Error.signup.dni.length
	@Test
	public void PR06() 
	{
		//Vamos al formulario de registro
		PO_NavView.clickOption(driver, "signup", "class", "btn btn-primary");
		
		/*	=========== DNI REPETIDO ========== */
		//Rellenamos el formulario.
		PO_RegisterView.fillForm(driver, "Prueba", "Josefo@uniovi.es", "77777", "77777");
		
		//COmprobamos el error de DNI repetido.
		PO_RegisterView.checkKey(driver, "Error.signup.dni.duplicate", PO_Properties.getSPANISH() );
		
		
		/*	=========== NOMBRE CORTO ========== */
		//Rellenamos el formulario.
		PO_RegisterView.fillForm(driver, "Prueba", "Josefo@uniovi.es", "77777", "77777");
		
		//COmprobamos el error de Nombre corto .
		PO_RegisterView.checkKey(driver, "Error.signup.name.length",
		PO_Properties.getSPANISH() );
		
		
		/*	=========== CONTRASE√ëAS NO COINCIDEN (propuesto) ========== */
		
		//Rellenamos el formulario (ERROR -> contrase√±as no coinciden).
		PO_RegisterView.fillForm(driver, "Prueba", "Josefo@uniovi.es", "77777", "77777");
		
		//COmprobamos el error de contrase√±as no coinciden.
		PO_RegisterView.checkKey(driver, "Error.signup.passwordConfirm.coincidence", PO_Properties.getSPANISH() );
	}
	
	
	// PR07. Loguearse con exito desde el Rol de Usuario, 99999990D, 123456
	@Test
	public void PR07() {
		// Vamos al formulario de logueo.
		PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
		
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "99999990A", "123456");
		
		// Comprobamos que entramos en la pagina privada de Alumno
		PO_View.checkElement(driver, "text", "Notas del usuario");
	}
	
	// PR08. Identificaci√≥n v√°lida con usuario de ROL profesor (99999993D/123456).
	@Test
	public void PR08() 
	{
		// Vamos al formulario de logueo.
		PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
		
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "99999993D", "123456");
		
		// Clic en el men√∫ desplegable de gesti√≥n de notas (y uno de sus submen√∫s. En este caso, Agregar Nota)
		PO_NavView.desplegarNotas(driver, "Agregar Nota");
		
		// Comprobamos que entramos en la pagina para a√±adir notas
		PO_View.checkElement(driver, "text", "Alumno");
	}
	
	
	// PR09: Identificaci√≥n v√°lida con usuario de ROL Administrador (99999988F/123456).
	@Test
	public void PR09() 
	{
		// Vamos al formulario de logueo.
		PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
		
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "99999988F", "123456");
		
		// Comprobamos que entramos en la pagina privada de un Administrador
		PO_View.checkElement(driver, "text", "Gesti√≥n de Usuarios");
	}
	
	
	// PR10: Identificaci√≥n inv√°lida con usuario de ROL alumno (99999990A/123456).
	@Test
	public void PR10() 
	{
		// Vamos al formulario de logueo.
		PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
		
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "99999990A", "123456");
		
		// Comprobamos que entramos en la pagina privada de un alumno (no puede tener la gesti√≥n de usuarios disponible
		SeleniumUtils.EsperaCargaPaginaNoTexto(driver, "Gesti√≥n de Usuarios", PO_View.getTimeout() );
	}
	
	
	// PR11: Identificaci√≥n v√°lida y desconexi√≥n con usuario de ROL usuario (99999990A/123456)
	@Test
	public void PR11() 
	{
		// Vamos al formulario de logueo.
		PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
		
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "99999990A", "123456");
		
		// Comprobamos que entramos en la pagina privada de Alumno
		PO_View.checkElement(driver, "text", "Notas del usuario");
		
		// Ahora nos desconectamos
		PO_PrivateView.clickOption(driver, "logout", "text", "Identif√≠cate");
	}
	
	
	//PR12. Loguearse, comprobar que se visualizan 4 filas de notas y desconectarse usando el rol de estudiante.
	@Test
	public void PR12() {
		// Vamos al formulario de logueo.
		PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
		
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "99999990A", "123456");
		
		// COmprobamos que entramos en la pagina privada de Alumno
		PO_View.checkElement(driver, "text", "Notas del usuario");
		
		// Contamos el n√∫mero de filas de notas
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
				PO_View.getTimeout());
		Assert.assertTrue(elementos.size() == 4);
		
		// Ahora nos desconectamos
		PO_PrivateView.clickOption(driver, "logout", "text", "Identif√≠cate");
	}
	
	
	//PR13. Loguearse como estudiante y ver los detalles de la nota con Descripcion = Nota A2.
	//P13. Ver la lista de Notas.
	@Test
	public void PR13() {
		// Vamos al formulario de logueo.
		PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
		
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "99999990A", "123456");
		
		// COmprobamos que entramos en la pagina privada de Alumno
		PO_View.checkElement(driver, "text", "Notas del usuario");
		SeleniumUtils.esperarSegundos(driver, 1);
		
		// Contamos las notas
		By enlace = By.xpath("//td[contains(text(), 'Nota A2')]/following-sibling::*[2]"); // dos "td" despu√©s del de Nota A2
		driver.findElement(enlace).click();
		SeleniumUtils.esperarSegundos(driver, 1);
		
		// Esperamos por la ventana de detalle
		PO_View.checkElement(driver, "text", "Detalles de la nota");
		SeleniumUtils.esperarSegundos(driver, 1);
		
		// Ahora nos desconectamos
		PO_PrivateView.clickOption(driver, "logout", "text", "Identif√≠cate");
	}
	
	
	// P14. Loguearse como profesor y Agregar Nota A2.
	// P14. Esta prueba podr√≠a encapsularse mejor ...
	@Test
	public void PR14() {
		//Vamos al formulario de logueo.
		PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
		
		//Rellenamos el formulario
		PO_LoginView.fillForm(driver, "99999993D" , "123456" );
		
		//Comprobamos que entramos en la pagina privada del Profesor
		PO_View.checkElement(driver, "text", "99999993D");
		
		//Pinchamos en la opci√≥n de menu de Notas: //li[contains(@id, 'marks-menu')]/a
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,"
																			+ "'marks-menu')]/a");
		elementos.get(0).click();
		
		//Esperamos a aparezca la opci√≥n de a√±adir nota: //a[contains(@href, 'mark/add')]
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, 'mark/add')]");
		
		//Pinchamos en agregar Nota.
		elementos.get(0).click();
		
		//Ahora vamos a rellenar la nota. //option[contains(@value, '4')]
		PO_PrivateView.fillFormAddMark(driver, 3, "Nota Nueva 1", "8");
		
		//Esperamos a que se muestren los enlaces de paginaci√≥n la lista de notas
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@class, 'page-link')]");
		
		//Nos vamos a la √∫ltima p√°gina
		elementos.get(3).click();
		
		//Comprobamos que aparece la nota en la pagina
		elementos = PO_View.checkElement(driver, "text", "Nota Nueva 1");
		
		//Ahora nos desconectamos
		PO_PrivateView.clickOption(driver, "logout", "text", "Identif√≠cate");
	}
	
	
	//PRN. Loguearse como profesor, vamos a la ultima p√°gina y Eliminamos la Nota Nueva 1.
	//PRN. Ver la lista de Notas.
	@Test
	public void PR15() {
		//Vamos al formulario de logueo.
		PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
		
		//Rellenamos el formulario
		PO_LoginView.fillForm(driver, "99999993D" , "123456" );
		
		//COmprobamos que entramos en la pagina privada del Profesor
		PO_View.checkElement(driver, "text", "99999993D");
		
		//Pinchamos en la opci√≥n de menu de Notas: //li[contains(@id, 'marks-menu')]/a
		List<WebElement> elementos = PO_View.checkElement(driver, "free",
		"//li[contains(@id, 'marks-menu')]/a");
		elementos.get(0).click();
		
		//Pinchamos en la opci√≥n de lista de notas.
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href,'mark/list')]");
		elementos.get(0).click();
		
		//Esperamos a que se muestren los enlaces de paginacion la lista de notas
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@class, 'page-link')]");
		
		//Nos vamos a la √∫ltima p√°gina
		elementos.get(3).click();
		
		//Esperamos a que aparezca la Nueva nota en la ultima pagina
		//Y Pinchamos en el enlace de borrado de la Nota "Nota D1"
		//td[contains(text(), 'Nota D1')]/following-sibling::*/a[contains(text()'mark/delete')]"
		elementos = PO_View.checkElement(driver, "free", "//td[contains(text(), "
					+ "'Nota D1')]/following-sibling::*/a[contains(@href, 'mark/delete')]");
		elementos.get(0).click();
		
		//Volvemos a la √∫ltima pagina
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@class, 'page-link')]");
		elementos.get(3).click();
		
		//Y esperamos a que NO aparezca la ultima "Nueva Nota 1"
		SeleniumUtils.EsperaCargaPaginaNoTexto(driver, "Nota D1", PO_View.getTimeout() );
		//Ahora nos desconectamos
		PO_PrivateView.clickOption(driver, "logout", "text", "Identif√≠cate");
	}
	
	@Test
	public void PR16()
	{
		// Vamos al formulario de logueo.
		PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
		
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "99999988F", "123456");
		
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,"
																			+ "'users-menu')]/a");
		elementos.get(0).click();
		
		//Esperamos a aparezca la opci√≥n de a√±adir nota: //a[contains(@href, 'mark/add')]
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/user/add')]");
		
		elementos.get(0).click();
		
		PO_PrivateView.fillFormAddUser(driver, 0, "11111111F", "Pablo", "Diaz", "123456");
		
		//Comprobamos que aparece la nota en la pagina
		PO_View.checkElement(driver, "text", "Usuarios");
	}
	
	@Test
	public void PR17()
	{
		// Vamos al formulario de logueo.
		PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
		
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "99999988F", "123456");
		
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,"
																			+ "'users-menu')]/a");
		elementos.get(0).click();
		
		//Esperamos a aparezca la opci√≥n de a√±adir nota: //a[contains(@href, 'mark/add')]
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/user/list')]");
		
		elementos.get(0).click();
		
		//Esperamos a aparezca la opci√≥n de a√±adir nota: //a[contains(@href, 'mark/add')]
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/user/details')]");
		
		elementos.get(0).click();
		
		//Comprobamos que aparece la nota en la pagina
		PO_View.checkElement(driver, "text", "Usuarios");
	}
	
	
	@Test
	public void PR18() // modificar
	{
		// Vamos al formulario de logueo.
		PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
		
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "99999988F", "123456");
		
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,"
																			+ "'users-menu')]/a");
		elementos.get(0).click();
		
		//Esperamos a aparezca la opci√≥n de a√±adir nota: //a[contains(@href, 'mark/add')]
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/user/list')]");
		
		elementos.get(0).click();
		
		//Esperamos a aparezca la opci√≥n de a√±adir nota: //a[contains(@href, 'mark/add')]
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/user/edit')]");
		
		elementos.get(0).click();
		
		//Comprobamos que aparece la nota en la pagina
		PO_View.checkElement(driver, "text", "Editar");
	}
	
	
	@Test
	public void PR19()
	{
		// Vamos al formulario de logueo.
		PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
		
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "99999988F", "123456");
		
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,"
																			+ "'users-menu')]/a");
		elementos.get(0).click();
		
		//Esperamos a aparezca la opci√≥n de listar usuarios
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/user/list')]");
		
		elementos.get(0).click();
		
		elementos = PO_View.checkElement(driver, "text", "eliminar");
		int elem_actuales = elementos.size();
		
		//Se clica en el bot√≥n de borrar
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/user/delete')]");
		
		elementos.get(0).click(); // borra el primer usuario
		
		elementos = PO_View.checkElement(driver, "text", "eliminar");
		Assert.assertFalse( elementos.size() == elem_actuales );
	}
	
	
	@Test
	public void PR20() // test de restaurar BBDD
	{
		// Vamos al formulario de logueo.
		PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
		
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "99999988F", "123456");
		
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,"
																			+ "'users-menu')]/a");
		elementos.get(0).click();
		
		//Esperamos a aparezca la opci√≥n de a√±adir nota: //a[contains(@href, 'mark/add')]
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/restoreDB')]");
		
		elementos.get(0).click();
		
		//Y esperamos a que NO aparezca la ultima "Nueva Nota 1"
		SeleniumUtils.EsperaCargaPaginaNoTexto(driver, "Nota D1", PO_View.getTimeout() );
	}
}
