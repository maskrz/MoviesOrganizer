/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package servicesImpl;

import java.io.FileInputStream;
import org.dbunit.DBTestCase;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import services.MoviesService;

/**
 *
 * @author Skrzypek
 */
public class DbUnitTestCase extends DBTestCase {

    protected MoviesService moviesService;
    public DbUnitTestCase(String name)
    {
        super( name );
        System.setProperty( PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS, "org.postgresql.Driver" );
        System.setProperty( PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL, "jdbc:postgresql://localhost/movies" );
        System.setProperty( PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME, "postgres" );
        System.setProperty( PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD, "root" );
//	 System.setProperty( PropertiesBasedJdbcDatabaseTester.DBUNIT_SCHEMA, "" );
        this.moviesService = new MoviesServiceImpl();
    }

    @Override
    protected IDataSet getDataSet() throws Exception
    {
//        FileInputStream fis = new FileInputStream("test\\resources\\movies.xml");
//        System.out.println(fis.toString());

        return new FlatXmlDataSetBuilder().build(new FileInputStream("test\\resources\\movies.xml"));
    }
}
