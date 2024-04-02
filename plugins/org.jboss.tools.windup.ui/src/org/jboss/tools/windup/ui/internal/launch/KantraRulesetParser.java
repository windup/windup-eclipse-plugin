package org.jboss.tools.windup.ui.internal.launch;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.jboss.tools.windup.model.domain.KantraConfiguration;
import org.jboss.tools.windup.model.domain.KantraConfiguration.Link;
import org.jboss.tools.windup.model.domain.KantraConfiguration.Ruleset;
import org.jboss.tools.windup.model.domain.ModelService;
import org.jboss.tools.windup.windup.WindupFactory;
import org.jboss.tools.windup.windup.WindupResult;

//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import static org.jboss.tools.windup.model.domain.KantraConfiguration.*;

public class KantraRulesetParser {

//	 public static List<KantraConfiguration.Ruleset> parseRuleset(String resultFilePath) {
//		 	Thread currentThread = Thread.currentThread();
//	        ClassLoader originalClassLoader = currentThread.getContextClassLoader();
//	        ClassLoader jacksonClassLoader = KantraRulesetParser.class.getClassLoader();
//	        try {
//	        	currentThread.setContextClassLoader(jacksonClassLoader);
//	            ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
//	            File yamlFile = new File(resultFilePath);
//	            objectMapper.findAndRegisterModules();
//	            List<Ruleset> ruleSets = objectMapper.readValue(yamlFile, new TypeReference<List<Ruleset>>(){});
//	            if (ruleSets != null) {
//	                ruleSets.removeIf(ruleset -> { 
//	                	System.out.println("deleting ruleset without violations");
//	                	if (ruleset.getViolations() == null || ruleset.getViolations().isEmpty()) {
//	                		return true;
//	                	}
//	                	return false;
//                	});
//	                return ruleSets;
//	            } else {
//	                System.out.println("YAML file is empty or invalid.");
//	            }
//	        } catch (Exception e) {
//	            System.err.println("Error reading results: " + resultFilePath);
//	            e.printStackTrace();
//	        }
//	        finally {
//	            currentThread.setContextClassLoader(originalClassLoader);
//	        }
//	        return null;
//	    }
//	 
//    public static void parseRulesetForKantraConfig (KantraConfiguration configuration){
//        String outputLocation = configuration.getRulesetResultLocation();
//        File outFile = new File(outputLocation);
//        if (outFile.exists()) {
//        	List<Ruleset> rulesets = parseRuleset(outputLocation);
//        	if (!rulesets.isEmpty()) {
//        		AnalysisResultsSummary summary = new AnalysisResultsSummary(rulesets);
//				configuration.setSummary(summary);
//		        processIncidents(rulesets, configuration);
//        	}
//        }
//    }
//	    
//    public static void processIncidents(List<Ruleset> rulesets, KantraConfiguration configuration) {
//        if (rulesets != null) {
//        	System.out.println("processIncidents");
//        	WindupResult result = WindupFactory.eINSTANCE.createWindupResult();
//            configuration.getWindupConfiguration().setWindupResult(result);
//            configuration.getWindupConfiguration().setTimestamp(ModelService.createTimestamp());
//            for (Ruleset ruleset: rulesets) {
//            	System.out.println("Ruleset");
//                Map<String, Violation> violations = ruleset.getViolations();
//                System.out.println("Violations");
//                System.out.println(violations);
//                if (violations != null ){
//                    for (Map.Entry<String, Violation> entry : violations.entrySet()) {
//                    	System.out.println("entry");
//                    	System.out.println(entry);
//                        toHints(configuration, entry.getKey(), entry.getValue(), result);
//                    }
//                }
//            }
//        }
//    }
//    
//    private static void toHints(KantraConfiguration configuration, String ruleId, Violation violation, WindupResult result) {
//    	System.out.println("toHints");
//    	if (configuration.getWindupConfiguration().getInputs().isEmpty()) return;
//    	String input = configuration.getWindupConfiguration().getInputs().get(0).getLocation();
//    	String title = violation.getDescription().split("\n", 2)[0];
//    	for (Incident incident : violation.getIncidents()) {
//    		org.jboss.tools.windup.windup.Hint hint = WindupFactory.eINSTANCE.createHint();
//    		result.getIssues().add(hint);    		
//    		String filePath = incident.getUri();
//    		System.out.println("Incident");
//    		System.out.println("FilePath: ");
//    		System.out.println(filePath);
//    		String absolutePath = filePath.substring(filePath.indexOf("/source-code") + "/source-code".length());
//    		hint.setFileAbsolutePath(input + absolutePath);
//    		System.out.println("Absolute Path");
//    		System.out.println(hint.getFileAbsolutePath());
//    		
//    		// System.out.println("Incident File: " + hint.getFileAbsolutePath());
//    		hint.setRuleId(ruleId);
//    		hint.setTitle(title);
//    		hint.setLineNumber(incident.getLineNumber());
//    		hint.setOriginalLineSource(incident.getCodeSnip());
//    		hint.setEffort(violation.getEffort()); 
//    		hint.setMessageOrDescription(incident.getMessage());
//    		for (Link link : violation.getLinks()) {
//    			org.jboss.tools.windup.windup.Link windupLink = WindupFactory.eINSTANCE.createLink();
//    			hint.getLinks().add(windupLink);
//    			windupLink.setDescription(link.getTitle());
//    			windupLink.setUrl(link.getUrl());
//    		}
//    	}
//    }
}
