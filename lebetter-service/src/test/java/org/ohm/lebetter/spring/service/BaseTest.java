package org.ohm.lebetter.spring.service;

import liquibase.ChangeSet;
import liquibase.DatabaseChangeLog;
import liquibase.FileOpener;
import liquibase.change.Change;
import liquibase.change.ColumnConfig;
import liquibase.change.DeleteDataChange;
import liquibase.change.InsertDataChange;
import liquibase.change.RawSQLChange;
import liquibase.parser.ChangeLogParser;
import net.sf.ehcache.CacheManager;
import org.dbunit.DataSourceDatabaseTester;
import org.dbunit.database.DatabaseDataSet;
import org.dbunit.database.IDatabaseConnection;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.room13.mallcore.log.RMLogger;
import org.room13.mallcore.spring.dao.UserDao;
import org.room13.mallcore.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: eugene
 * Date: 11.09.11
 * Time: 15:15
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings("unchecked")
@ContextConfiguration(locations = {"/lebetter.context.test.xml"})
public abstract class BaseTest extends AbstractTestNGSpringContextTests {

    protected final RMLogger log = new RMLogger("test." + this.getClass().getName());

    protected static final String DEF_DATASET_NAME = "schema/changelog.xml";

    private static DataSourceDatabaseTester tester;
    protected UserEntity admin = null;
    private String[] allTables = null;
    private Map<String, PreparedStatement> statements = new HashMap<String, PreparedStatement>();
    private DatabaseChangeLog databaseChangeLog = null;

    @Autowired
    protected UserDao userDao;

    @Autowired
    @Qualifier("serviceManager")
    protected ServiceManager serviceManager;

    @Autowired
    @Qualifier("properties")
    protected Properties properties;

    @Autowired
    @Qualifier("dataSource")
    protected DataSource dataSource;

    private DataSourceDatabaseTester getTester() throws Exception {
        if (tester == null) {
            tester = new DataSourceDatabaseTester((DataSource) applicationContext.getBean("dataSource"));
        }
        return tester;
    }

    private String[] getAllTables(IDatabaseConnection connection) throws Exception {
        if (allTables == null) {
            DatabaseDataSet dataSet = new DatabaseDataSet(connection, false);
            allTables = dataSet.getTableNames();
        }
        return allTables;
    }

    private PreparedStatement getStatement(String sql, Connection connection) throws Exception {
        PreparedStatement res = statements.get(sql);
        if (res == null) {
            res = connection.prepareStatement(sql);
            statements.put(sql, res);
        }
        return res;
    }

    private DatabaseChangeLog getDatabaseChangeLog(String dataSetName) throws Exception {
        if (databaseChangeLog == null) {
            ChangeLogParser changeLogParser = new ChangeLogParser();
            databaseChangeLog = changeLogParser.parse(dataSetName, new FileOpener() {
                @Override
                public InputStream getResourceAsStream(String s) throws IOException {
                    return getClass().getClassLoader().getResourceAsStream(s);
                }

                @Override
                public Enumeration<URL> getResources(String s) throws IOException {
                    return null;
                }

                @Override
                public ClassLoader toClassLoader() {
                    return getClass().getClassLoader();
                }
            });
        }
        return databaseChangeLog;
    }

    //Parses liquibase changelog and inserts data manually
    private void allLoad(IDatabaseConnection connection, String dataSetName) throws Exception {
        log.debug("Importing changesets");
        DatabaseChangeLog databaseChangeLog = getDatabaseChangeLog(dataSetName);

        List<ChangeSet> changeSets = databaseChangeLog.getChangeSets();
        for (ChangeSet changeSet : changeSets) {
            log.trace("\tchangeset " + changeSet.getId());
            List<Change> changes = changeSet.getChanges();
            for (Change change : changes) {
                try {
                    if (change instanceof InsertDataChange) {
                        InsertDataChange idc = (InsertDataChange) change;
                        List<ColumnConfig> columns = idc.getColumns();
                        String tableName = idc.getTableName();
                        StringBuffer str = new StringBuffer("insert into " + tableName);
                        StringBuffer cols = new StringBuffer();
                        StringBuffer vals = new StringBuffer();
                        Iterator it = columns.iterator();
                        while (it.hasNext()) {
                            ColumnConfig column = (ColumnConfig) it.next();
                            cols.append(column.getName());
                            vals.append("?");
                            if (it.hasNext()) {
                                cols.append(",");
                                vals.append(",");
                            }
                        }
                        str.append("(").append(cols).append(") values (").append(vals).append(")");
                        PreparedStatement stmt = getStatement(str.toString(), connection.getConnection());
                        it = columns.iterator();
                        int i = 1;
                        while (it.hasNext()) {
                            ColumnConfig column = (ColumnConfig) it.next();
                            String val = column.getValue();
                            try {
                                Long lval = Long.parseLong(val);
                                stmt.setLong(i, lval);
                            } catch (Exception ex) {
                                try {
                                    Float fval = Float.parseFloat(val);
                                    stmt.setFloat(i, fval);
                                } catch (Exception ex3) {
                                    if ("true".equals(val) || "false".equals(val)) {
                                        stmt.setBoolean(i, Boolean.parseBoolean(val));
                                    } else {
                                        stmt.setString(i, val);
                                    }
                                }
                            }
                            i++;
                        }

                        stmt.executeUpdate();
                    } else if (change instanceof DeleteDataChange) {
                        DeleteDataChange ddc = (DeleteDataChange) change;
                        StringBuffer sql = new StringBuffer("delete from ").append(ddc.getTableName());
                        if (!StringUtil.isEmpty(ddc.getWhereClause())) {
                            sql.append(" where ").append(ddc.getWhereClause());
                        }
                        PreparedStatement stmt = getStatement(sql.toString(), connection.getConnection());
                        stmt.executeUpdate();
                    } else if (change instanceof RawSQLChange &&
                               changeSet.getId().indexOf("data-") != -1) {
                        RawSQLChange sql = (RawSQLChange) change;
                        if (sql.getSql().toLowerCase().indexOf("insert") != -1 ||
                            sql.getSql().toLowerCase().indexOf("delete") != -1 ||
                            sql.getSql().toLowerCase().indexOf("update") != -1) {
                            PreparedStatement stmt = getStatement(sql.getSql(), connection.getConnection());
                            stmt.executeUpdate();
                        }
                    }
                } catch (Exception ex) {
                    log.error("Error procesing change set " + change.getChangeSet().getId(), ex);
                    throw ex;
                }
            }
        }
    }

    private void allCleanup(IDatabaseConnection connection) throws Exception {

        log.debug("Truncate all data");
        String allTables[] = getAllTables(connection);

        String url = connection.getConnection().getMetaData().getURL();
        Connection rootDatabaseConnection = DriverManager.getConnection(url, "postgres", "postgres");

        for (String name : allTables) {
            log.trace("\tdisable " + name);
            String disable_permissions_query = "ALTER TABLE " + name + " DISABLE TRIGGER ALL";
            PreparedStatement stmt = getStatement(disable_permissions_query, rootDatabaseConnection);
            stmt.executeUpdate();
            stmt.close();

            log.trace("\ttruncate " + name);
            String del_query = "delete from " + name;
            stmt = getStatement(del_query, connection.getConnection());
            stmt.executeUpdate();
            stmt.close();

            log.trace("\tenable " + name);
            String enable_permissions_query = "ALTER TABLE " + name + " ENABLE TRIGGER ALL";
            stmt = getStatement(enable_permissions_query, rootDatabaseConnection);
            stmt.executeUpdate();
            stmt.close();

        }

        rootDatabaseConnection.close();
    }

    protected final void setUp(String dataSetName) throws Exception {
        IDatabaseConnection connection = getTester().getConnection();
        statements = new HashMap<String, PreparedStatement>();
        try {

            long t = System.currentTimeMillis();

            //Clean data
            allCleanup(connection);

            log.debug("del - " + (System.currentTimeMillis() - t));
            t = System.currentTimeMillis();

            //Load data
            allLoad(connection, dataSetName);

            log.debug("load - " + (System.currentTimeMillis() - t));

            closeTransaction();
            openTransaction();

            admin = serviceManager.getUserManager().get(1L);
            log.trace("admin: " + admin);
            log.trace("setting Locale to ru");
            LocaleContextHolder.setLocale(new Locale("ru"));

            //Clean cache
            CacheManager.getInstance().clearAll();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        admin = serviceManager.getUserManager().get(1L);
        log.trace("admin: " + admin);
        log.trace("setting Locale to ru");
        LocaleContextHolder.setLocale(new Locale("ru"));

        //Clean cache
        CacheManager.getInstance().clearAll();

    }

    @BeforeMethod
    public void beforeTest(Method method) {
        log.debug("Executing " + this.getClass().getName() +
                  "#" + method.getName());
        openTransaction();
    }

    @AfterMethod
    public void afterTest() {
        closeTransaction();
    }

    protected final void openTransaction() {
        SessionFactory sessionFactory = (SessionFactory) applicationContext.getBean("sessionFactory");
        Session session = sessionFactory.openSession();
        TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
    }

    protected final void closeTransaction() {
        log.trace("Close transaction");
        SessionFactory sessionFactory = (SessionFactory) applicationContext.getBean("sessionFactory");
        ((SessionHolder) TransactionSynchronizationManager.getResource(sessionFactory)).getSession().close();
        TransactionSynchronizationManager.unbindResource(sessionFactory);
    }

}
