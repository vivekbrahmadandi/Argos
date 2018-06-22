package integrationTests.cucumber;

import net.masterthought.cucumber.ReportBuilder;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.poi.util.SystemOutLogger;

public class Cucumber_report_generate {

	public static String  GenerateMasterthoughtReport() {

		String rootDir = System.getProperty("user.dir");

		try{

			File reportOutputDirectory = new File("target\\Masterthought");
			List<String> list = new ArrayList<String>();
			list.addAll(getListOfJsonReports(rootDir , "target"));
			//list.addAll(getListOfJsonReports(rootDir , "target\\parallel-tests"));
			String pluginUrlPath = "";
			String buildNumber = "1";
			String buildProject = "cucumber-jvm";
			boolean skippedFails = false;
			boolean pendingFails = false;
			boolean undefinedFails = true;
			boolean missingFails = true;
			boolean flashCharts = true;
			boolean runWithJenkins = false;
			boolean highCharts = true;
			boolean parallelTesting = true;
			boolean artifactsEnabled = false;
			String artifactConfig = "";

			ReportBuilder reportBuilder = new ReportBuilder(list, reportOutputDirectory, pluginUrlPath, buildNumber,
					buildProject, skippedFails, pendingFails, undefinedFails, missingFails, flashCharts, runWithJenkins,
					highCharts, parallelTesting);

			reportBuilder.generateReports();
		}catch(Exception e){
			e.printStackTrace();
		}

		
		
		//return location of report
		return System.getProperty("user.dir") + "\\target\\Masterthought\\feature-overview.html";

	}

	public static List<String> getListOfJsonReports(String rootDir, String dir){

		List<String> list = new ArrayList<String>();

		File folder = new File(rootDir + "\\" + dir);

		if (folder.listFiles() != null){

			File[] listOfFiles = folder.listFiles();

			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile() && listOfFiles[i].length()> 0) {
					if (listOfFiles[i].getName().contains(".json")){

						String filepath = dir + "\\" + listOfFiles[i].getName();

						System.out.println(filepath);

						list.add(filepath);

					}
				} 
			}
		}
		return list;

	}

	//Enable unique Masterthought reporting for each environment setup. 
	public static String moveReports() throws IOException{

		File newDir = null;

		File dir = new File(System.getProperty("user.dir") + "\\target\\Masterthought");
		if (!dir.isDirectory()) {
			System.err.println("There is no directory @ given path");
		} else {

			newDir = new File(dir.getParent() + "\\" + "Masterthought-"  + Cucumber_runner.operating_system.get() + "-" + Cucumber_runner.browser.get());
			dir.renameTo(newDir);
		}

		return newDir.getPath();
	}
}
