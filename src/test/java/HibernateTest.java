import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HibernateTest {

    private Equipo equipo1;
    private Equipo equipo2;
    private Equipo equipo3;
    private TestService testService;

    @Before
    public void prepare(){
        System.out.println("Empezando el Before");

        SessionFactoryProvider.getInstance().setSessionFactoryTest();


        this.testService = new TestService();
        Torneo torneo = Torneo.getTorneo();
        if(this.testService.recuperarEntidad(Torneo.class, 1) == null ){
            this.testService.crearEntidad(torneo);
        }

        this.equipo1 = new Equipo();
        this.equipo2 = new Equipo();
        this.equipo3 = new Equipo();
        equipo1.setZona("D");
        equipo1.setNombre("Argentina");
        equipo2.setZona("D");
        equipo2.setNombre("Islandia");
        equipo3.setZona("D");
        equipo3.setNombre("Croacia");

        this.testService.crearEntidad(equipo1);
        this.testService.crearEntidad(equipo2);
        this.testService.crearEntidad(equipo3);
        System.out.println("Terminando el Before");
    }

    @After
    public void cleanup() {
        SessionFactoryProvider.destroy();
        System.out.println("Terminando el After");
    }

    @Test
    public void test_recuperarEquipo() {
        Runner.runInSession(() -> {

            Equipo equipo = this.testService.recuperarEntidad(Equipo.class, "Islandia");
            assertEquals("Islandia", equipo.getNombre());
            assertEquals("D", equipo.getZona());
            System.out.println("Terminando test_recuperarEquipo");
            return null;
        });

    }

    @Test
    public void  test_recuperarEquipoPorNombre() {
        Runner.runInSession(() -> {
            Equipo equipo = this.testService.recuperarPorNombre("Islandia");
            assertEquals("Islandia", equipo.getNombre());
            assertEquals("D", equipo.getZona());
            System.out.println("Terminando test_recuperarEquipoPorNombre");
            return null;
        });
    }

    @Test
    public void test_RecuperarEquipoInexistente() {
        Runner.runInSession(() -> {
            Equipo equipo = this.testService.recuperarPorNombre("Chipre");
            assertEquals(null, equipo);
            System.out.println("Terminando test_recuperarEquipoInexistente");
            return null;
        });
    }
}