package org.ohm.lebetter.spring.service;

import liquibase.ant.DropAllTask;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;

/**
 * Created by IntelliJ IDEA.
 * User: eugene
 * Date: 11.09.11
 * Time: 15:14
 * To change this template use File | Settings | File Templates.
 */
public class DropAllObjects extends DropAllTask {

    @Override
    public void execute() throws BuildException {
        Project prj = new Project();
        super.setProject(prj);
        super.createClasspath();
        super.execute();
    }
}