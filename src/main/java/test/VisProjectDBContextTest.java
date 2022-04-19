package test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

import basic.SimpleName;
import context.project.VisProjectDBContext;

public class VisProjectDBContextTest {
	static Path projectParentDir = Paths.get("C:\\visframeUI-test");
	
	//
	public static VisProjectDBContext TEST_PROJECT_1;
	static SimpleName project1Name = new SimpleName("project1");
	//
	public static VisProjectDBContext TEST_PROJECT_2;
	static SimpleName project2Name = new SimpleName("project2");
	
	public static VisProjectDBContext TEST_PROJECT_3;
	static SimpleName project3Name = new SimpleName("project3");
	
	
	public static VisProjectDBContext getConnectedProject1() throws SQLException {
		TEST_PROJECT_1 = new VisProjectDBContext(project1Name,projectParentDir);
        TEST_PROJECT_1.connect();
        
        return TEST_PROJECT_1;
	}
	
	
	public static VisProjectDBContext getConnectedProject2() throws SQLException {
		TEST_PROJECT_2 = new VisProjectDBContext(project2Name,projectParentDir);
        TEST_PROJECT_2.connect();
        
        return TEST_PROJECT_2;
	}
	
	public static VisProjectDBContext getConnectedProject11() throws SQLException {
		TEST_PROJECT_2 = new VisProjectDBContext(new SimpleName("project11"),projectParentDir);
        TEST_PROJECT_2.connect();
        
        return TEST_PROJECT_2;
	}
	
	public static VisProjectDBContext getConnectedProject3() throws SQLException {
		TEST_PROJECT_3 = new VisProjectDBContext(project3Name,projectParentDir);
		TEST_PROJECT_3.connect();
        
        return TEST_PROJECT_3;
	}
}
