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

import social.tests.pageobjects.PO_ListUsers;
import social.tests.pageobjects.PO_LoginView;
import social.tests.pageobjects.PO_NavView;
import social.tests.pageobjects.PO_PostView;
import social.tests.pageobjects.PO_Properties;
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
	static String PathFirefox = "D:\\UNIOVI\\Tercero\\Segundo Semestre\\SDI\\Practicas\\recursos\\Firefox46.win\\FirefoxPortable.exe";

	static WebDriver driver = getDriver(PathFirefox);
	static String URL = "http://localhost:9090";

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

	// Antes de la primera prueba
	@BeforeClass
	static public void begin() {

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
	public void RegVal() {
		// Nos vamos al registro (debería haber un id=registro)
		PO_NavView.clickOption(driver, "registro", "id", "registro");

		// Rellenamos el formulario, y nos registramos.
		PO_RegisterView.fillForm(driver, "Prueba", "Josefo@uniovi.es", "77777", "77777");

		// Comprobamos que entramos en el panel
		PO_View.checkElement(driver, "text", "Panel");
	}

	/**
	 * 1.2 [RegInval] Registro de Usuario con datos inválidos (repetición de
	 * contraseña invalida).
	 */
	@Test
	public void RegInval() {
		// Nos vamos al registro
		PO_NavView.clickOption(driver, "registro", "id", "registro");

		// Rellenamos el formulario, y nos intentamos registrar (las contraseñas no
		// coinciden).
		PO_RegisterView.fillForm(driver, "Prueba2", "Josefo@uniovi.es", "77777", "66666");

		// Comprobamos que aparece el error de contraseñas no coinciden
		PO_RegisterView.checkKey(driver, "Error.passNoCoincide", PO_Properties.getSPANISH());
	}

	/**
	 * 2.1 [InVal] Inicio de sesión con datos válidos.
	 */
	@Test
	public void InVal() {
		// Se intenta ir a la dirección para listar usuarios (no dejará, puedes debemos
		// loguearnos)
		// driver.navigate().to( "http://localhost:9090/users/lista-usuarios" );

		// Rellenamos el formulario de login con datos válidos
		PO_LoginView.fillForm(driver, "maria", "123456");

		// Comprobamos que aparece el mensaje "Lista de usuarios"
		// PO_RegisterView.checkKey(driver, "Usuarios.lista.listaUsuarios",
		// PO_Properties.getSPANISH() );
		PO_RegisterView.checkKey(driver, "Panel.panel", PO_Properties.getSPANISH());
	}

	/**
	 * 2.2 [InInVal] Inicio de sesión con datos inválidos (usuario no existente en
	 * la aplicación).
	 */
	@Test
	public void InInVal() {
		// Rellenamos el formulario de login con datos INVÁLIDOS
		PO_LoginView.fillForm(driver, "mariaB", "123456");

		// Comprobamos que aparece el mensaje de error
		// "Username o password incorrectos o el usuario no tiene privilegios
		// suficientes"
		PO_RegisterView.checkKey(driver, "Error.falloLogin", PO_Properties.getSPANISH());
	}

	/**
	 * 3.1 [LisUsrVal] Acceso al listado de usuarios desde un usuario en sesión.
	 */
	@Test
	public void LisUsrVal() {
		// Rellenamos el formulario de login con datos válidos
		PO_LoginView.fillForm(driver, "maria", "123456");

		PO_NavView.desplegarUsuarios(driver, "Todos los usuarios");

		// Comprobamos que aparece el mensaje "Lista de usuarios"
		PO_RegisterView.checkKey(driver, "Usuarios.lista.listaUsuarios", PO_Properties.getSPANISH());
	}

	/**
	 * 3.2 [LisUsrInVal] Intento de acceso con URL desde un usuario no identificado
	 * al listado de usuarios desde un usuario en sesión. Debe producirse un acceso
	 * no permitido a vistas privadas.
	 */
	@Test
	public void LisUsrInVal() {
		// Se intenta ir, sin estar identificado, a la dirección de lista de usuarios.
		// No se permite acceder a dicha dirección. Se redirecciona al login de
		// usuarios.
		driver.navigate().to("http://localhost:9090/users/lista-usuarios");

		// Comprobamos que aparece el mensaje "Login"
		PO_RegisterView.checkKey(driver, "Login.titulo", PO_Properties.getSPANISH());
	}

	/**
	 * 4.1 [BusUsrVal] Realizar una búsqueda valida en el listado de usuarios desde
	 * un usuario en sesión
	 */
	@Test
	public void BusUsrVal() {
		// Rellenamos el formulario de login con datos válidos
		PO_LoginView.fillForm(driver, "maria", "123456");

		// Se despliega el menú de usuarios, y se clica en Todos los usuarios
		PO_NavView.desplegarUsuarios(driver, "Todos los usuarios");

		// Buscamos a Pedro
		PO_ListUsers.buscarUsuario(driver, "Edward");

		// Se comprueba (mediante el Email) que solo hay un usuario encontrado (Pedro)
		List<WebElement> usuarios = SeleniumUtils.EsperaCargaPagina(driver, "text", "Email", PO_View.getTimeout());
		Assert.assertTrue(usuarios.size() == 1);
	}

	/**
	 * 4.2 [BusUsrInVal] Intento de acceso con URL a la búsqueda de usuarios desde
	 * un usuario no identificado. Debe producirse un acceso no permitido a vistas
	 * privadas.
	 */
	@Test
	public void BusUsrInVal() {
		// Se intenta ir, sin estar identificado, a la dirección de lista de usuarios, y
		// buscar a Pedro.
		// No se permite acceder a dicha dirección. Se redirecciona al login de
		// usuarios.
		driver.navigate().to("http://localhost:9090/users/lista-usuarios?searchText=Pedro");

		// Comprobamos que aparece el mensaje "Login"
		PO_RegisterView.checkKey(driver, "Login.titulo", PO_Properties.getSPANISH());
	}

	/**
	 * 5.1 [InvVal] Enviar una invitación de amistad a un usuario de forma valida
	 */
	@Test
	public void InvVal() {
		// Rellenamos el formulario de login con datos válidos
		PO_LoginView.fillForm(driver, "maria", "123456");

		// Se despliega el menú de usuarios, y se clica en Todos los usuarios
		PO_NavView.desplegarUsuarios(driver, "Todos los usuarios");
		
		// Se puede enviar petición de amistad a Pelayo
		SeleniumUtils.EsperaCargaPaginaConId(driver, "pelayo", PO_View.getTimeout());

		// Enviamos una invitación de amistad a Pelayo (Usuario = pelayo)
		PO_ListUsers.enviarAceptarPeticion(driver, "pelayo");
	}

	/**
	 * 5.2 [InvInVal] Enviar una invitación de amistad a un usuario al que ya le
	 * habíamos invitado la invitación previamente. No debería dejarnos enviar la
	 * invitación, se podría ocultar el botón de enviar invitación o notificar que
	 * ya había sido enviada previamente.
	 */
	@Test
	public void InvInVal() {
		// Rellenamos el formulario de login con datos válidos
		PO_LoginView.fillForm(driver, "edward", "123456");

		// Se despliega el menú de usuarios, y se clica en Todos los usuarios
		PO_NavView.desplegarUsuarios(driver, "Todos los usuarios");

		// Enviamos una invitación de amistad a Marta (Usuario = marta)
		PO_ListUsers.enviarAceptarPeticion(driver, "marta");

		// Se comprueba que se ha enviado la petición de amistad a Marta (aparece el
		// mensaje "Petición
		// de amistad enviada", con ID=enviada_marta
		SeleniumUtils.EsperaCargaPaginaConId(driver, "enviada_marta", PO_View.getTimeout());
	}

	/**
	 * 6.1 [LisInvVal] Listar las invitaciones recibidas por un usuario, realizar la
	 * comprobación con una lista que al menos tenga una invitación recibida.
	 */
	@Test
	public void LisInvVal() {
		// Rellenamos el formulario de login con datos válidos
		// Inicio sesión con Marta, porque tiene 2 peticiones de amistad pendientes
		PO_LoginView.fillForm(driver, "marta", "123456");

		// Se despliega el menú de usuarios, y se clica en Ver peticiones de amistad
		PO_NavView.desplegarUsuarios(driver, "Ver peticiones de amistad");

		// Se comprueba que Marta tiene 2 peticiones de amistad
		List<WebElement> usuarios = SeleniumUtils.EsperaCargaPagina(driver, "text", "Aceptar petición",
				PO_View.getTimeout());
		Assert.assertTrue(usuarios.size() == 2);
	}
	
	/**
	 * 7.1 [AcepInvVal] Aceptar una invitación recibida.
	 */
	@Test
	public void AcepInvVal() 
	{
		// Rellenamos el formulario de login con datos válidos
		// Inicio sesión con Lucas, porque tiene 3 peticiones de amistad pendientes
		PO_LoginView.fillForm(driver, "lucas", "123456");
		
		// Se despliega el menú de usuarios, y se clica en Ver peticiones de amistad
		PO_NavView.desplegarUsuarios(driver, "Ver peticiones de amistad");
		
		// Se comprueba que Lucas tiene 3 peticiones de amistad
		List<WebElement> usuarios = SeleniumUtils.EsperaCargaPagina(driver, "text", "Aceptar petición",
				PO_View.getTimeout());
		Assert.assertTrue(usuarios.size() == 3);			
		
		// Se acepta la petición del usuario "edward"
		PO_ListUsers.enviarAceptarPeticion(driver, "edward");
		
		// Se comprueba que ahora que se ha aceptado una petición, hay dos pendientes
		usuarios = SeleniumUtils.EsperaCargaPagina(driver, "text", "Aceptar petición",
				PO_View.getTimeout());
		Assert.assertTrue(usuarios.size() == 2);
	}

	/**
	 * 8.1 [ListAmiVal] Listar los amigos de un usuario, realizar la comprobación con una lista que al menos tenga un amigo.
	 */
	@Test
	public void ListAmiVal() 
	{
		// Rellenamos el formulario de login con datos válidos
		// Inicio sesión con Lucas, que tiene varios amigos
		PO_LoginView.fillForm(driver, "lucas", "123456");
		
		// Se despliega el menú de usuarios, y se clica en Mis amigos
		PO_NavView.desplegarUsuarios(driver, "Mis amigos");
		
		// Se comprueba que Lucas tiene 2 amigos
		List<WebElement> usuarios = SeleniumUtils.EsperaCargaPagina(driver, "text", "Email",
				PO_View.getTimeout());
		Assert.assertTrue(usuarios.size() == 2);
	}
	
	/**
	 * 9.1 [PubVal] Crear una publicación con datos válidos. 
	 */
	@Test
	public void PubVal() 
	{
		// Rellenamos el formulario de login con datos válidos
		PO_LoginView.fillForm(driver, "maria", "123456");
		
		// Se despliega el menú de post, y pulsamos en "Crear post"
		PO_NavView.desplegarPost(driver, "Crear post");
		
		// Rellenamos la creación del post, y lo creamos
		PO_PostView.crearPost(driver, "Prueba de nuevo post", "Descripción de nuevo post");
		
		// Se comprueba que se ha creado el post anterior
		SeleniumUtils.EsperaCargaPagina(driver, "text", "Prueba de nuevo post", PO_View.getTimeout());
	}
	
	/**
	 * 10.1 [LisPubVal] Acceso al listado de publicaciones desde un usuario en sesión
	 */
	@Test
	public void LisPubVal() 
	{
		// Rellenamos el formulario de login con datos válidos
		// Inicio sesión con María
		PO_LoginView.fillForm(driver, "maria", "123456");
		
		// Se despliega el menú de post, y pulsamos en "Listar mis post"
		PO_NavView.desplegarPost(driver, "Listar mis post");
		
		// Se comprueba que se visualiza correctamente el post de María (solo tiene uno), buscándolo
		// por su título (Publicacion de prueba)
		SeleniumUtils.EsperaCargaPagina(driver, "text", "Publicacion de prueba", PO_View.getTimeout());
	}
	
	/**
	 * 11.1 [LisPubAmiVal] Listar las publicaciones de un usuario amigo
	 */
	@Test
	public void LisPubAmiVal() 
	{
		// Rellenamos el formulario de login con datos válidos
		// Inicio sesión con maría
		PO_LoginView.fillForm(driver, "maria", "123456");
		
		// Se despliega el menú de Usuarios -> Mis amigos
		PO_NavView.desplegarUsuarios(driver, "Mis amigos");
		
		// Vamos al perfil de Lucas
		PO_NavView.clickOption(driver, "perfil/lucas", "text", "Publicaciones");
		
		// Listamos sus publicaciones (tiene 3)
		List<WebElement> publicaciones = SeleniumUtils.EsperaCargaPagina(driver, "class", "post-media",
				PO_View.getTimeout());
		Assert.assertTrue(publicaciones.size() == 3);
	}
	
	/**
	 * 11.2 [LisPubAmiInVal] Utilizando un acceso vía URL tratar de listar las publicaciones de un usuario que
	 * no sea amigo del usuario identificado en sesión.
	 */
	@Test
	public void LisPubAmiInVal() 
	{
		// Rellenamos el formulario de login con datos válidos
		// Inicio sesión con maría
		PO_LoginView.fillForm(driver, "maria", "123456");
		
		// Vamos al perfil de Marta (NO ES AMIGA DE MARÍA, por lo tanto no se verán sus publicaciones).
		// Marta tiene creada una publicación (título = Drop tables)
		driver.navigate().to( "http://localhost:9090/users/perfil/marta" );

		// Vemos que no tiene ninguna publicación
		SeleniumUtils.EsperaCargaPaginaNoTexto(driver, "Drop tables", PO_View.getTimeout());
	}
	
	/**
	 * 12.1 [PubFot1Val] Crear una publicación con datos válidos y una foto adjunta. 
	 */
	@Test
	public void PubFot1Val() 
	{
		// Rellenamos el formulario de login con datos válidos
		// Inicio sesión con Lucas, que tiene varios amigos
		PO_LoginView.fillForm(driver, "lucas", "123456");
		
		// Se despliega el menú de post, y pulsamos en "Crear post"
		PO_NavView.desplegarPost(driver, "Crear post");
		
		/** DESCOMENTAR Y EJECUTAR
		//Cambiar ruta por la de una imagen propia
		String rutaImg = "D:\\prueba12.png";
		
		// Rellenamos la creación del post, y lo creamos
		PO_PostView.crearPostConFoto(driver, "Prueba de nuevo post", "Descripción de nuevo post",rutaImg);
		
		// Se comprueba que se ha creado el post anterior con una imagen con id img_
		SeleniumUtils.EsperaCargaPaginaConId(driver, "img_", PO_View.getTimeout());
		*/
		
	}
	
	/**
	 * 12.2 [PubFot2Val] Crear una publicación con datos válidos y sin una foto adjunta.
	 */
	@Test
	public void PubFot2Val() 
	{
		// Rellenamos el formulario de login con datos válidos 
		PO_LoginView.fillForm(driver, "marta", "123456");
		
		// Se despliega el menú de post, y pulsamos en "Crear post"
		PO_NavView.desplegarPost(driver, "Crear post");
		
		// Rellenamos la creación del post, y lo creamos
		PO_PostView.crearPost(driver, "Prueba de nuevo post", "Descripción de nuevo post");
		
		// Se comprueba que se ha creado el post anterior
		SeleniumUtils.EsperaCargaPagina(driver, "text", "Prueba de nuevo post", PO_View.getTimeout());
		
	}

	/**
	 * 13.1 [AdInVal] Inicio de sesión como administrador con datos válidos.
	 */
	@Test
	public void AdInVal() {
		// Navegamos al login de administrador
		driver.navigate().to("http://localhost:9090/admin/login");

		// Rellenamos el formulario de login con datos válidos
		PO_LoginView.fillForm(driver, "edward", "123456");

		// Comprobamos que aparece el mensaje "Lista de usuarios"
		PO_RegisterView.checkKey(driver, "Usuarios.lista.listaUsuarios", PO_Properties.getSPANISH());
	}

	/**
	 * 13.2 [AdInInVal] Inicio de sesión como administrador con datos inválidos
	 * (usar los datos de un usuario que no tenga perfil administrador).
	 */
	@Test
	public void AdInInVal() {
		// Navegamos al login de administrador
		driver.navigate().to("http://localhost:9090/admin/login");

		// Rellenamos el formulario de login con datos INVÁLIDOS, usuario existente
		// pero sin privilegios
		PO_LoginView.fillForm(driver, "pedro", "123456");

		// Comprobamos que aparece el mensaje de error
		// "Username o password incorrectos o el usuario no tiene privilegios
		// suficientes"
		PO_RegisterView.checkKey(driver, "Error.falloLogin", PO_Properties.getSPANISH());
	}

	/**
	 * 14.1 [AdLisUsrVal] Desde un usuario identificado en sesión como administrador
	 * listar a todos los usuarios de la aplicación.
	 */
	@Test
	public void AdLisUsrVal() {
		// Navegamos al login de administrador
		driver.navigate().to("http://localhost:9090/admin/login");

		// Rellenamos el formulario de login con datos válidos
		PO_LoginView.fillForm(driver, "edward", "123456");

		// Comprobamos que aparece el mensaje "Lista de usuarios"
		PO_RegisterView.checkKey(driver, "Usuarios.lista.listaUsuarios", PO_Properties.getSPANISH());
	}

	/**
	 * 15.1 [AdBorUsrVal] Desde un usuario identificado en sesión como administrador
	 * eliminar un usuario existente en la aplicación.
	 */
	@Test
	public void AdBorUsrVal() {
		// Navegamos al login de administrador
		driver.navigate().to("http://localhost:9090/admin/login");

		// Rellenamos el formulario de login con datos válidos
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
	 * 15.2 [AdBorUsrInVal] Intento de acceso vía URL al borrado de un usuario
	 * existente en la aplicación. Debe utilizarse un usuario identificado en sesión
	 * pero que no tenga perfil de administrador.
	 */
	@Test
	public void AdBorUsrInVal() {

		// Rellenamos el formulario de login con datos válidos
		PO_LoginView.fillForm(driver, "lucas", "123456");
		
		//Intentamos borrar un usuario sin ser administradores
		driver.navigate().to("http://localhost:9090/admin/eliminarUsuario/1");
		
		// Comprobamos que aparece el mensaje de error
		PO_RegisterView.checkKey(driver, "Error.priv", PO_Properties.getSPANISH());
		
	}

	
	/**
	 * 13.3 [AdInInVal2] Inicio de sesión como administrador con datos inválidos
	 * (usuario no existente).
	 */
	@Test
	public void AdInInVal2() {
		// Navegamos al login de administrador
		driver.navigate().to("http://localhost:9090/admin/login");

		// Rellenamos el formulario de login con datos INVÁLIDOS, usuario no existente
		PO_LoginView.fillForm(driver, "pedro3", "123456");

		// Comprobamos que aparece el mensaje de error
		// "Username o password incorrectos o el usuario no tiene privilegios
		// suficientes"
		PO_RegisterView.checkKey(driver, "Error.falloLogin", PO_Properties.getSPANISH());
	}
}
