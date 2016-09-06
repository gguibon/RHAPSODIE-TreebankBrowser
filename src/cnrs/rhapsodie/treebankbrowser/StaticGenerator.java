package cnrs.rhapsodie.treebankbrowser;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;

import cnrs.rhapsodie.treebankbrowser.util.OsValidator;
import cnrs.rhapsodie.treebankbrowser.utils.Corpus;
import cnrs.rhapsodie.treebankbrowser.utils.JarResourcesExtractor;
import cnrs.rhapsodie.treebankbrowser.utils.ResourcesFromJar;
import cnrs.rhapsodie.treebankbrowser.utils.Tools;

/**
 * Class which generate the static web interface and launch it on the default browser
 * (Chrome is recommanded for easy animations)
 * @author Gaël Guibon
 *
 */
public class StaticGenerator {
	static String baseDir = "/home/gael/workspacefx/RHAPSODIE/resources/interface-statique";
	static String emptySample = "";
	static String title = "Super corpus";
	static String subtitle = "ceci est un sous-titre";
	static String baseDirCrea = "generatedUI";
	static String currentDir = System.getProperty("user.dir");
	static String rawDir = "";//"/home/gael/workspacefx/RHAPSODIE/resources/samplesraw";
	static String licenceTitle = "A very confined licence !";
	static String licenceHtml = "<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.</p>"
			+ "<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.</p>";
	static String projectTitle = "My superb project !";
	static String projectHtml = "<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.</p>"
			+ "<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.</p>";
	
	final static Pattern pattern = Pattern.compile("<h2 id=\"samplename\">(.+?)</h2>");
//	public static void main(String[] args) throws Exception{
//		creaHtmlTrees(rawDir);
//		insertMetaInfo();
//		adaptSampleView();
//		File index = new File(baseDirCrea+ File.separatorChar +"index.html");
//		openIt(index.getAbsolutePath());
//	}
//	
	public void generateStaticInterface(String title, String subtitle, 
			String projectTitle, String projectHtml, String licenceTitle, String licenceHtml
			, String rawDir) throws Exception{
		if(title.length()!=0)this.title = title;
		if(subtitle.length()!=0)this.subtitle = subtitle;
		if(projectTitle.length()!=0)this.projectTitle = projectTitle;
		if(projectHtml.length()!=0)this.projectHtml = projectHtml;
		if(licenceTitle.length()!=0)this.licenceTitle = licenceTitle;
		if(licenceHtml.length()!=0)this.licenceHtml = licenceHtml;
		
		if(rawDir.length()!=0)this.rawDir = rawDir;
		
		insertMetaInfo();
		creaHtmlTrees(this.rawDir);
		adaptSampleView();
		File index = new File(baseDirCrea+ File.separatorChar +"index.html");
		openIt(index.getAbsolutePath());
//		((JavascriptExecutor)this.webDriver).executeScript("alert('Test')");
//		this.webDriver.switchTo().alert().accept();
	}

	
	private  void creaHtmlTrees(String dirPath) throws Exception {
		List<String> listDirs = Tools.listDirs(dirPath);
		int sampleIndex = 1;
		for(String dir : listDirs){
			//initiate each raw dir
			File dirOld = new File(String.format("%s%s%s%s%s", dirPath, File.separatorChar, dir, File.separatorChar, "old" ));
			File dirNew = new File(String.format("%s%s%s%s%s", dirPath, File.separatorChar, dir, File.separatorChar, "new" ));
			// get sample informations sample.info
			HashMap<String, String> sampleInfos = getSampleInfos(String.format("%s%s%s%s%s", dirPath, File.separatorChar, dir
					, File.separatorChar, "sample.info"));
			// get content of files in each dir
			List<String> oldPaths = Tools.dir2listepaths(dirOld.getAbsolutePath());
			List<String> newPaths = Tools.dir2listepaths(dirNew.getAbsolutePath());
			StringBuilder sbOld = new StringBuilder();
			StringBuilder sbNew = new StringBuilder();
			for(String path : oldPaths) sbOld.append( Tools.readFile(path) + "\n" );
			for(String path : newPaths) sbNew.append( Tools.readFile(path) + "\n");
			// create the paths dirs if none
			File dirFile = new File ( String.format("%s%s%s%s%s", baseDirCrea, File.separatorChar
					, "samples", File.separatorChar, "sample"+sampleIndex ) );
			dirFile.mkdirs();
			
			// merge the html contents for each tree html
//			String treesModel = Tools.readFile( String.format("%s%s%s%s%s", 
//					StaticGenerator.class.getResource("/resources/interface-statique").toExternalForm()
////					baseDir
//					, File.separatorChar, "samples", File.separatorChar , "treesModel.html" ) );
//			String treesModel = StaticGenerator.class.getResourceAsStream("/resources/interface-statique/samples/treesModel.html");
			Tools tool = new Tools();
			String treesModel = tool.accessRessourceFile("/resources/interface-statique/samples/treesModel.html");
			List<String> sentOld = Corpus.getSentences(Tools.tempFile("old", ".txt", sbOld.toString()));
			StringBuilder sbOldMerged = new StringBuilder();
			for (String sent : sentOld){
				sbOldMerged.append(String.format("%s%s%s\n", "<conll>", 
					Tools.replaceLastOccurrence(sent, "\n", "") , "</conll>") );
			}
			List<String> sentNew = Corpus.getSentences(Tools.tempFile("new", ".txt", sbNew.toString()));
			StringBuilder sbNewMerged = new StringBuilder();
			for (String sent : sentNew){
				sbNewMerged.append(String.format("%s%s%s\n", "<conll>",
						Tools.replaceLastOccurrence(sent, "\n", "") , "</conll>") );
			}
			// write the conlls inside the treesModel and create the files
			Tools.ecrire( String.format("%s%s%s", dirFile.getAbsolutePath(), File.separatorChar
					, "oldTrees.html") , treesModel.replace("{conlls}", sbOldMerged)
								.replace("{samplename}", sampleInfos.get("sample name")));
			Tools.ecrire( String.format("%s%s%s", dirFile.getAbsolutePath(), File.separatorChar
					, "trees.html") , treesModel.replace("{conlls}", sbNewMerged)
								.replace("{samplename}", sampleInfos.get("sample name")));
			
			sampleIndex++;
		}
	}
	
	/**
	 * parse the sample.info and returns the values in a hashmap
	 * @param path
	 * @return
	 * @throws Exception
	 */
	private  HashMap<String, String> getSampleInfos(String path) throws Exception{
		List<String> lines = Tools.path2liste(path);
		HashMap<String, String> values = new HashMap<String, String>();
		for(String line : lines){
			String[] cols = line.split("\t");
			if(cols[0].equals("sample name")) values.put("sample name", cols[1]);
		}
		return values;
	}
	
	/**
	 * adapt the sample view with the reality of the trees.html 
	 * @throws IOException
	 */
	private  void adaptSampleView() throws IOException {
		List<String> listDir = Tools.listDirs(baseDirCrea+File.separatorChar +"samples");
		StringBuilder samplesView = new StringBuilder();
		for(String dir : listDir){
			String dirPath = String.format("%s%s%s%s%s", baseDirCrea , File.separatorChar, "samples" 
				, File.separatorChar, dir );
			if(Tools.dirFileExists(String.format("%s%s%s", dirPath, File.separatorChar, "trees.html" ) ) 
					|| Tools.dirFileExists(String.format("%s%s%s", dirPath, File.separatorChar, "oldTrees.html")) ){
				// retrieve the numbers of trees for both old and new
				int n = Tools.countOccurrences( Tools.readFile(dirPath+ File.separatorChar +"trees.html"),"<conll>");
				int o = Tools.countOccurrences( Tools.readFile(dirPath+ File.separatorChar +"oldTrees.html"),"<conll>");
				// get the href content for both old and new
				String relPath = String.format("%s%s"
						, Tools.relativisePath(dirPath, currentDir).replace(baseDirCrea+File.separatorChar, "")
						, "trees.html");
				String relOldPath = String.format("%s%s"
						, Tools.relativisePath(dirPath, currentDir).replace(baseDirCrea+File.separatorChar, "")
						, "oldTrees.html");
				// get the sample name
				
				final Matcher matcherOld = pattern.matcher(Tools.readFile(
						String.format("%s%s"
								, dirPath + File.separatorChar
								, "trees.html")));
				final Matcher matcherNew = pattern.matcher(Tools.readFile(
						String.format("%s%s"
								, dirPath + File.separatorChar
								, "trees.html")));
				if(matcherOld.find()){
					//generate the sample row
					samplesView.append(	sampleRow(o, n, relOldPath, relPath, matcherOld.group(1)) );
				}else if(matcherNew.find()){
					samplesView.append(	sampleRow(o, n, relOldPath, relPath, matcherNew.group(1)) );
				}else{
					samplesView.append( sampleRow(o, n, relOldPath, relPath, "Nonamed Sample") );
				}
			}
		}
		
		replaceInFile(baseDirCrea+File.separatorChar +"samples.html", "{sampleslist}", samplesView.toString() );
	}
	
	/**
	 * generate the html for one row given the informations.
	 * @param o - oldSample number of trees
	 * @param n - newSample number of trees
	 * @param oUrl - href url for the sample
	 * @param nUrl - href url for the sample (usually trees.html)
	 * @param sampleName
	 * @return the html
	 */
	private  String sampleRow(int o, int n, String oUrl, String nUrl, String sampleName ){
		StringBuilder sb = new StringBuilder();
		sb.append("<div class=\"row no-margin\">");
		sb.append("<li class=\"list-group-item col-xs-8 text-color-black\">"+sampleName+"</li>");
		if(o == 0){
			sb.append("<li class=\"list-group-item col-xs-2 opacity-hover\"><a class=\"btn-list btn btn-default btn-empty\"><span class=\"badge\">0</span>Old</a></li>");
		}else{
			sb.append("<li class=\"list-group-item col-xs-2 opacity-hover\"><a  href=\""+oUrl+"\" class=\"btn-list animsition-link btn btn-primary\"><span class=\"badge\">"+o+"</span>Old</a></li>");
		}		
		if(n == 0){
			sb.append("<li class=\"list-group-item col-xs-2 opacity-hover\"><a class=\"btn-list btn btn-default btn-empty\"><span class=\"badge\">0</span>Old</a></li>");
		}else{
			sb.append("<li class=\"list-group-item col-xs-2 opacity-hover\"><a  href=\""+nUrl+"\" class=\"btn-list animsition-link btn btn-primary\"><span class=\"badge\">"+n+"</span>New</a></li>");
		}
		sb.append("</div>");
		return sb.toString();
	}
	
	/**
	 * replace a string content by another in a file (overwrite the current file)
	 * @param filePath
	 * @param toReplace
	 * @param toInsert
	 */
	private  void replaceInFile(String filePath, String toReplace, String toInsert){
		String content;
		try {
			content = Tools.readFile(filePath);
			content = content.replace(toReplace, toInsert);
			Tools.ecrire(filePath, content);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	private  void insertMetaInfo() throws Exception{
//		Tools tool = new Tools();
//		String html = tool.accessRessourceFile("/home/gael/workspacefx/RHAPSODIE/resources/interface-statique/index.html");
//		Tools.copyDirToAnotherDir(baseDir, baseDirCrea);
//		
		
		// version needed outside a jar
//		Tools.copyDirToAnotherDir(StaticGenerator.class.getResource("/resources/interface-statique").toExternalForm()
//				.replace("file:", "").replace("jar:","")
//				, baseDirCrea);
		ResourcesFromJar rfj = new ResourcesFromJar();
		rfj.get();

		
		
		List<String> listAllFilePaths = new ArrayList<String>();
		Tools.listFilesAndSubfiles(baseDirCrea, listAllFilePaths);
		for(String path : listAllFilePaths){
			if(FilenameUtils.getExtension(path).equals("html")){
				replaceInFile(path, "{title}", title);
				replaceInFile(path, "{subtitle}", subtitle);
				replaceInFile(path, "{projecttitle}", projectTitle);
				replaceInFile(path, "{projecthtml}", projectHtml);
				replaceInFile(path, "{licencetitle}", licenceTitle);
				replaceInFile(path, "{licencehtml}", licenceHtml);
			}
		}
	}
	
	
	private  void openIt(String path) {
		if (OsValidator.isWindows()) {
			System.out.println("This is Windows");

			try {
				Runtime rt = Runtime.getRuntime();
				String url = "file:///" + path.replaceAll("/", "\\");
				rt.exec("rundll32 url.dll,FileProtocolHandler " + url);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (OsValidator.isMac()) {
			System.out.println("This is Mac");
			if (Desktop.isDesktopSupported()) {
				Desktop desktop = Desktop.getDesktop();
				Thread t = new Thread("New Thread") {
					public void run() {
						try {
							desktop.browse(new URI("file://" + path));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				};
				t.start();
			}
		} else if (OsValidator.isUnix()) {
			System.out.println("This is Unix or Linux");
			if (Desktop.isDesktopSupported()) {
				Desktop desktop = Desktop.getDesktop();
				Thread t = new Thread("New Thread") {
					public void run() {
						try {
							desktop.browse(new URI("file://" + path));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				};
				t.start();
			}
		} else {
			System.out.println("Your OS is not supported!!");
		}
	}
}
