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

//Ordenamos las pruebas por el nombre del m�todo
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RedSocialTests {
	/* PABLO */
	// static String PathFirefox =
	// "C:\\Users\\PabloD\\Desktop\\SDI\\practicas\\p5\\Firefox46.win\\FirefoxPortable.exe";

	/* ANTONIO */
	static String PathFirefox = "D:\\UNIOVI\\Tercero\\Segundo Semestre\\SDI\\Practicas\\recursos\\Firefox46.win\\FirefoxPortable.exe";

	static WebDriver driver = getDriver(PathFirefox);
	static String URL = "http://localhost:9090";

	public static WebDriver getDriver(String PathFirefox) {
		System.setProperty("webdriver.firefox.bin", PathFirefox);
		WebDriver driver = new FirefoxDriver();
		return driver;
	}

	// Antes de cada prueba se navega al URL home de la aplicaciónn
	@Before
	public void setUp() {
		driver.navigate().to(URL);
	}

	// Después de cada prueba se borran las cookies del navegador
	@After
	public void tearDown() {
		driver.manage().deleteAllCookies();
	}

	// Antes de la primera prueba
	@BeforeClass
	static public void begin() {
		// // Vamos al formulario de logueo.
		// PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
		//
		// // Rellenamos el formulario
		// PO_LoginView.fillForm(driver, "99999988F", "123456");
		//
		// // Comprobamos que entramos en la pagina privada de un Administrador
		// List<WebElement> element = SeleniumUtils.EsperaCargaPagina(driver, "@href",
		// "restoreDB", 2);
		// element.get(0).click();
		//
		// element = SeleniumUtils.EsperaCargaPagina(driver, "@href", "logout", 2);
		// element.get(0).click();
	}

	@AfterClass // Al finalizar la última prueba
	static public void end() {
		// Cerramos el navegador al finalizar las pruebas
		driver.quit();
	}

	/**
	 * 1.1 [RegVal] Registro de Usuario con datos v�lidos
	 */
	@Test
	public void RegVal() {
		// Nos vamos al registro (deber�a haber un id=registro)
		PO_NavView.clickOption(driver, "registro", "id", "registro");

		// Rellenamos el formulario, y nos registramos.
		PO_RegisterView.fillForm(driver, "Prueba", "Josefo@uniovi.es", "77777", "77777");

		// Comprobamos que entramos en el panel
		PO_View.checkElement(driver, "text", "Panel");
	}

	/**
	 * 1.2 [RegInval] Registro de Usuario con datos inv�lidos (repetici�n de
	 * contrase�a invalida).
	 */
	@Test
	public void RegInval() {
		// Nos vamos al registro
		PO_NavView.clickOption(driver, "registro", "id", "registro");

		// Rellenamos el formulario, y nos intentamos registrar (las contrase�as no
		// coinciden).
		PO_RegisterView.fillForm(driver, "Prueba2", "Josefo@uniovi.es", "77777", "66666");

		// Comprobamos que aparece el error de contrase�as no coinciden
		PO_RegisterView.checkKey(driver, "Error.passNoCoincide", PO_Properties.getSPANISH());
	}

	/**
	 * 2.1 [InVal] Inicio de sesi�n con datos v�lidos.
	 */
	@Test
	public void InVal() {
		// Se intenta ir a la direcci�n para listar usuarios (no dejar�, puedes debemos
		// loguearnos)
		// driver.navigate().to( "http://localhost:9090/users/lista-usuarios" );

		// Rellenamos el formulario de login con datos v�lidos
		PO_LoginView.fillForm(driver, "maria", "123456");

		// Comprobamos que aparece el mensaje "Lista de usuarios"
		// PO_RegisterView.checkKey(driver, "Usuarios.lista.listaUsuarios",
		// PO_Properties.getSPANISH() );
		PO_RegisterView.checkKey(driver, "Panel.panel", PO_Properties.getSPANISH());
	}

	/**
	 * 2.2 [InInVal] Inicio de sesi�n con datos inv�lidos (usuario no existente en
	 * la aplicaci�n).
	 */
	@Test
	public void InInVal() {
		// Rellenamos el formulario de login con datos INV�LIDOS
		PO_LoginView.fillForm(driver, "mariaB", "123456");

		// Comprobamos que aparece el mensaje de error
		// "Username o password incorrectos o el usuario no tiene privilegios
		// suficientes"
		PO_RegisterView.checkKey(driver, "Error.falloLogin", PO_Properties.getSPANISH());
	}

	/**
	 * 3.1 [LisUsrVal] Acceso al listado de usuarios desde un usuario en sesi�n.
	 */
	@Test
	public void LisUsrVal() {
		// Rellenamos el formulario de login con datos v�lidos
		PO_LoginView.fillForm(driver, "maria", "123456");

		PO_NavView.desplegarUsuarios(driver, "Todos los usuarios");

		// Comprobamos que aparece el mensaje "Lista de usuarios"
		PO_RegisterView.checkKey(driver, "Usuarios.lista.listaUsuarios", PO_Properties.getSPANISH());
	}

	/**
	 * 3.2 [LisUsrInVal] Intento de acceso con URL desde un usuario no identificado
	 * al listado de usuarios desde un usuario en sesi�n. Debe producirse un acceso
	 * no permitido a vistas privadas.
	 */
	@Test
	public void LisUsrInVal() {
		// Se intenta ir, sin estar identificado, a la direcci�n de lista de usuarios.
		// No se permite acceder a dicha direcci�n. Se redirecciona al login de
		// usuarios.
		driver.navigate().to("http://localhost:9090/users/lista-usuarios");

		// Comprobamos que aparece el mensaje "Login"
		PO_RegisterView.checkKey(driver, "Login.titulo", PO_Properties.getSPANISH());
	}

	/**
	 * 4.1 [BusUsrVal] Realizar una b�squeda valida en el listado de usuarios desde
	 * un usuario en sesi�n
	 */
	@Test
	public void BusUsrVal() {
		// Rellenamos el formulario de login con datos v�lidos
		PO_LoginView.fillForm(driver, "maria", "123456");

		// Se despliega el men� de usuarios, y se clica en Todos los usuarios
		PO_NavView.desplegarUsuarios(driver, "Todos los usuarios");

		// Buscamos a Pedro
		PO_ListUsers.buscarUsuario(driver, "Pedro");

		// Se comprueba (mediante el Email) que solo hay un usuario encontrado (Pedro)
		List<WebElement> usuarios = SeleniumUtils.EsperaCargaPagina(driver, "text", "Email", PO_View.getTimeout());
		Assert.assertTrue(usuarios.size() == 1);
	}

	/**
	 * 4.2 [BusUsrInVal] Intento de acceso con URL a la b�squeda de usuarios desde
	 * un usuario no identificado. Debe producirse un acceso no permitido a vistas
	 * privadas.
	 */
	@Test
	public void BusUsrInVal() {
		// Se intenta ir, sin estar identificado, a la direcci�n de lista de usuarios, y
		// buscar a Pedro.
		// No se permite acceder a dicha direcci�n. Se redirecciona al login de
		// usuarios.
		driver.navigate().to("http://localhost:9090/users/lista-usuarios?searchText=Pedro");

		// Comprobamos que aparece el mensaje "Login"
		PO_RegisterView.checkKey(driver, "Login.titulo", PO_Properties.getSPANISH());
	}

	/**
	 * 5.1 [InvVal] Enviar una invitaci�n de amistad a un usuario de forma valida
	 */
	@Test
	public void InvVal() {
		// Rellenamos el formulario de login con datos v�lidos
		PO_LoginView.fillForm(driver, "maria", "123456");

		// Se despliega el men� de usuarios, y se clica en Todos los usuarios
		PO_NavView.desplegarUsuarios(driver, "Todos los usuarios");

		// Enviamos una invitaci�n de amistad a Marta (Usuario = marta)
		PO_ListUsers.enviarPeticion(driver, "marta");

		// Se comprueba que se ha enviado la petici�n de amistad a Marta (no hay bot�n
		// de enviar)
		SeleniumUtils.EsperaCargaPaginaNoId(driver, "marta", PO_View.getTimeout());
	}

	/**
	 * 5.2 [InvInVal] Enviar una invitaci�n de amistad a un usuario al que ya le
	 * hab�amos invitado la invitaci�n previamente. No deber�a dejarnos enviar la
	 * invitaci�n, se podr�a ocultar el bot�n de enviar invitaci�n o notificar que
	 * ya hab�a sido enviada previamente.
	 */
	@Test
	public void InvInVal() {
		// Rellenamos el formulario de login con datos v�lidos
		PO_LoginView.fillForm(driver, "maria", "123456");

		// Se despliega el men� de usuarios, y se clica en Todos los usuarios
		PO_NavView.desplegarUsuarios(driver, "Todos los usuarios");

		// Enviamos una invitaci�n de amistad a Marta (Usuario = marta)
		PO_ListUsers.enviarPeticion(driver, "marta");

		// Se comprueba que se ha enviado la petici�n de amistad a Marta (aparece el
		// mensaje "Petici�n
		// de amistad enviada", con ID=enviada_marta
		SeleniumUtils.EsperaCargaPaginaConId(driver, "enviada_marta", PO_View.getTimeout());
	}

	/**
	 * 6.1 [LisInvVal] Listar las invitaciones recibidas por un usuario, realizar la
	 * comprobaci�n con una lista que al menos tenga una invitaci�n recibida.
	 */
	@Test
	public void LisInvVal() {
		// Rellenamos el formulario de login con datos v�lidos
		// Inicio sesi�n con Lucas, porque tiene 3 peticiones de amistad pendientes
		PO_LoginView.fillForm(driver, "lucas", "123456");

		// Se despliega el men� de usuarios, y se clica en Ver peticiones de amistad
		PO_NavView.desplegarUsuarios(driver, "Ver peticiones de amistad");

		// Se comprueba que Lucas tiene 3 peticiones de amistad
		List<WebElement> usuarios = SeleniumUtils.EsperaCargaPagina(driver, "text", "Aceptar petici�n",
				PO_View.getTimeout());
		Assert.assertTrue(usuarios.size() == 3);
	}

	/**
	 * 13.1 [AdInVal] Inicio de sesi�n como administrador con datos v�lidos.
	 */
	@Test
	public void AdInVal() {
		// Navegamos al login de administrador
		driver.navigate().to("http://localhost:9090/admin/login");

		// Rellenamos el formulario de login con datos v�lidos
		PO_LoginView.fillForm(driver, "edward", "123456");

		// Comprobamos que aparece el mensaje "Lista de usuarios"
		PO_RegisterView.checkKey(driver, "Usuarios.lista.listaUsuarios", PO_Properties.getSPANISH());
	}

	/**
	 * 13.2 [AdInInVal] Inicio de sesi�n como administrador con datos inv�lidos
	 * (usar los datos de un usuario que no tenga perfil administrador).
	 */
	@Test
	public void AdInInVal() {
		// Navegamos al login de administrador
		driver.navigate().to("http://localhost:9090/admin/login");

		// Rellenamos el formulario de login con datos INV�LIDOS, usuario existente
		// pero sin privilegios
		PO_LoginView.fillForm(driver, "pedro", "123456");

		// Comprobamos que aparece el mensaje de error
		// "Username o password incorrectos o el usuario no tiene privilegios
		// suficientes"
		PO_RegisterView.checkKey(driver, "Error.falloLogin", PO_Properties.getSPANISH());
	}

	/**
	 * 13.3 [AdInInVal2] Inicio de sesi�n como administrador con datos inv�lidos
	 * (usuario no existente).
	 */
	@Test
	public void AdInInVal2() {
		// Navegamos al login de administrador
		driver.navigate().to("http://localhost:9090/admin/login");

		// Rellenamos el formulario de login con datos INV�LIDOS, usuario no existente
		PO_LoginView.fillForm(driver, "pedro3", "123456");

		// Comprobamos que aparece el mensaje de error
		// "Username o password incorrectos o el usuario no tiene privilegios
		// suficientes"
		PO_RegisterView.checkKey(driver, "Error.falloLogin", PO_Properties.getSPANISH());
	}

	/**
	 * 14.1 [AdLisUsrVal] Desde un usuario identificado en sesi�n como administrador
	 * listar a todos los usuarios de la aplicaci�n.
	 */
	@Test
	public void AdLisUsrVal() {
		// Navegamos al login de administrador
		driver.navigate().to("http://localhost:9090/admin/login");

		// Rellenamos el formulario de login con datos v�lidos
		PO_LoginView.fillForm(driver, "edward", "123456");

		// Comprobamos que aparece el mensaje "Lista de usuarios"
		PO_RegisterView.checkKey(driver, "Usuarios.lista.listaUsuarios", PO_Properties.getSPANISH());
	}

	/**
	 * 15.1 [AdBorUsrVal] Desde un usuario identificado en sesi�n como administrador
	 * eliminar un usuario existente en la aplicaci�n.
	 */
	@Test
	public void AdBorUsrVal() {
		// Navegamos al login de administrador
		driver.navigate().to("http://localhost:9090/admin/login");

		// Rellenamos el formulario de login con datos v�lidos
		PO_LoginView.fillForm(driver, "edward", "123456");

		List<WebElement> usuarios = SeleniumUtils.EsperaCargaPagina(driver, "text", "Email", PO_View.getTimeout());
		int elementosAnteriores = usuarios.size();
		// Borramos a Pedro
		PO_ListUsers.borrarUsuario(driver, "pedro");

		// Comprobamos que hay menos usuarios
		usuarios = SeleniumUtils.EsperaCargaPagina(driver, "text", "Email", PO_View.getTimeout());
		Assert.assertTrue(usuarios.size() < elementosAnteriores);
	}

	/**
	 * 15.2 [AdBorUsrInVal] Intento de acceso v�a URL al borrado de un usuario
	 * existente en la aplicaci�n. Debe utilizarse un usuario identificado en sesi�n
	 * pero que no tenga perfil de administrador.
	 */
	@Test
	public void AdBorUsrInVal() {

		// Rellenamos el formulario de login con datos v�lidos
		PO_LoginView.fillForm(driver, "lucas", "123456");
		
		//Intentamos borrar un usuario sin ser administradores
		driver.navigate().to("http://localhost:9090/admin/eliminarUsuario/1");
		
		// Comprobamos que aparece el mensaje de error
		PO_RegisterView.checkKey(driver, "Error.priv", PO_Properties.getSPANISH());
		
	}

}
