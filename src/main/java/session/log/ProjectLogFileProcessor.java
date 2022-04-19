package session.log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

import basic.SimpleName;

public class ProjectLogFileProcessor {
	private static final String LOG_FILE_NAME = "project.log";
	private static final String NAME_PATH_SEPARATOR_REGEX = "\\|";
	private static final String NAME_PATH_SEPARATOR_PLAIN = "|";
	
	
	///////////////
	private Map<SimpleName,Path> existingProjectNamePathMap;
	
	public ProjectLogFileProcessor(){
		this.existingProjectNamePathMap = new LinkedHashMap<>();
		this.readAll();
	}
	
	private Path getProjectLogFilePath() {
		return  Paths.get(System.getProperty("user.dir"), LOG_FILE_NAME);
	}
	
	
	private void readAll() {
		if(!Files.exists(this.getProjectLogFilePath())) {
			return;
		}
		
		
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(getProjectLogFilePath().toString()));
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
				
				line = line.trim();
				
				if(line.isEmpty()) {
					continue;
				}
				
				String[] splits = line.split(NAME_PATH_SEPARATOR_REGEX);
				
				this.existingProjectNamePathMap.put(new SimpleName(splits[0].trim()), Paths.get(splits[1]));
				
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	/**
	 * @throws IOException 
	 * 
	 */
	public void writeAll() throws IOException {
		if(Files.exists(getProjectLogFilePath())) {
			Files.delete(getProjectLogFilePath());
		}
		
		FileWriter writer = new FileWriter(this.getProjectLogFilePath().toString());
		
		for(SimpleName projectName:this.existingProjectNamePathMap.keySet()) {
//			System.out.println(projectName);
			writer.write(projectName.getStringValue().concat(NAME_PATH_SEPARATOR_PLAIN).concat(this.existingProjectNamePathMap.get(projectName).toString()));
			writer.write(System.lineSeparator());
		}
		
		writer.flush();
		
		writer.close();
		
		System.out.println("Successfully wrote to the file.");
	}
	
	public Map<SimpleName,Path> getAllExistingProjectNamePathMap(){
		return this.existingProjectNamePathMap;
	}
	
	public void deleteProject(SimpleName projectName) {
		this.existingProjectNamePathMap.remove(projectName);
	}
	
	
	public void addNewProject(SimpleName projectName, Path projectPath) {
		this.existingProjectNamePathMap.put(projectName, projectPath);
	}
	
}
